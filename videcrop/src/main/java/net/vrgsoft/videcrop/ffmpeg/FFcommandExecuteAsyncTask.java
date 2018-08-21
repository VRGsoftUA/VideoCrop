package net.vrgsoft.videcrop.ffmpeg;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.TimeoutException;

class FFcommandExecuteAsyncTask extends AsyncTask<Void, String, CommandResult> implements FFtask {

    private final String[] cmd;
    private Map<String, String> environment;
    private final FFcommandExecuteResponseHandler ffmpegExecuteResponseHandler;
    private final ShellCommand shellCommand;
    private final long timeout;
    private long startTime;
    private Process process;
    private String output = "";
    private float duration;
    private float outputFps;
    private boolean quitPending;

    FFcommandExecuteAsyncTask(String[] cmd, Map<String, String> environment, long timeout, FFcommandExecuteResponseHandler ffmpegExecuteResponseHandler, float duration) {
        this.cmd = cmd;
        this.timeout = timeout;
        this.environment = environment;
        this.ffmpegExecuteResponseHandler = ffmpegExecuteResponseHandler;
        this.shellCommand = new ShellCommand();
        this.duration = duration;
    }

    @Override
    protected void onPreExecute() {
        startTime = System.currentTimeMillis();
        if (ffmpegExecuteResponseHandler != null) {
            ffmpegExecuteResponseHandler.onStart();
        }
    }

    @Override
    protected CommandResult doInBackground(Void... params) {
        try {
            process = shellCommand.run(cmd, environment);
            if (process == null) {
                return CommandResult.getDummyFailureResponse();
            }
            Log.d("Running publishing updates method");
            checkAndUpdateProcess();
            return CommandResult.getOutputFromProcess(process);
        } catch (TimeoutException e) {
            Log.e("FFmpeg binary timed out", e);
            return new CommandResult(false, e.getMessage());
        } catch (Exception e) {
            Log.e("Error running FFmpeg binary", e);
        } finally {
            Util.destroyProcess(process);
        }
        return CommandResult.getDummyFailureResponse();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        if (values != null && values[0] != null && ffmpegExecuteResponseHandler != null) {
            ffmpegExecuteResponseHandler.onProgress(values[0]);
            if (values[0].contains("Stream #") && values[0].contains("Video")) {
                outputFps = Util.getFpsFromCommandMessage(values[0]);
            }
//            if (values[0].contains("Duration:")) {
//                duration = Util.getDurationFromCommandMessage(values[0]);
//            }
            if (values[0].contains("frame=")) {
                float frameNumber = Util.getFramePositionFromCommandMessage(values[0]);
                ffmpegExecuteResponseHandler.onProgressPercent((frameNumber / (outputFps * duration)) * 100);
            }
        }
    }

    @Override
    protected void onPostExecute(CommandResult commandResult) {
        if (ffmpegExecuteResponseHandler != null) {
            output += commandResult.output;
            if (commandResult.success) {
                ffmpegExecuteResponseHandler.onSuccess(output);
            } else {
                ffmpegExecuteResponseHandler.onFailure(output);
            }
            ffmpegExecuteResponseHandler.onFinish();
        }
    }

    private void checkAndUpdateProcess() throws TimeoutException, InterruptedException {
        while (!Util.isProcessCompleted(process)) {

            // checking if process is completed
            if (Util.isProcessCompleted(process)) {
                return;
            }

            // Handling timeout
            if (timeout != Long.MAX_VALUE && System.currentTimeMillis() > startTime + timeout) {
                throw new TimeoutException("FFmpeg binary timed out");
            }

            try {
                String line;
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                while ((line = reader.readLine()) != null) {
                    if (isCancelled()) {
                        process.destroy();
                        process.waitFor();
                        return;
                    }

                    if (quitPending) {
                        sendQ();
                        process = null;
                        return;
                    }

                    output += line + "\n";
                    publishProgress(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isProcessCompleted() {
        return Util.isProcessCompleted(process);
    }

    @Override
    public boolean killRunningProcess() {
        return Util.killAsync(this);
    }

    @Override
    public void sendQuitSignal() {
        quitPending = true;
    }

    private void sendQ() {
        OutputStream outputStream = process.getOutputStream();
        try {
            outputStream.write("q\n".getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
