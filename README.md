#### [HIRE US](http://vrgsoft.net/)
# VideoCrop
Video cropping library with trimming and opportunity to choose different aspect ratio types</br></br>
<img src="https://github.com/VRGsoftUA/VideoCrop/blob/master/video.gif" width="270" height="480" />
# Usage
*For a working implementation, Have a look at the Sample Project - app*
1. Include the library as local library project.
```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
dependencies {
    implementation 'com.github.VRGsoftUA:VideoCrop:1.0'
}
```
2. In code you need to start Activityfor result like so:
```
startActivityForResult(VideoCropActivity.createIntent(this, inputPath, outputPath), CROP_REQUEST);
```
3. Then catch result in onActivityResult callback
```@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CROP_REQUEST && resultCode == RESULT_OK){
            //crop successful
        }
    }
```
#### Contributing
* Contributions are always welcome
* If you want a feature and can code, feel free to fork and add the change yourself and make a pull request
