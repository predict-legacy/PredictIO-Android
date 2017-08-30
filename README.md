# PredictIO-Android
predict.io offers mobile developers a battery-optimized SDK to get normalized sensor results. Available for iOS and Android. It gives you real-time updates when a user starts or ends a journey. With this trigger come contextual details for the mode of transportation (car vs. non-car).

[![Download](https://api.bintray.com/packages/predict-io/maven/PredictIO/images/download.svg)](https://bintray.com/predict-io/maven/PredictIO/_latestVersion)

## Requirements
* [Sign up](http://www.predict.io/sign-up/) for API key
* Android 4.0.3 (API 15) or above
* Google Play services 9.4.0 or above

## Installation
### Using Gradle 
#### Integration Steps
Please follow these steps in order to integrate predict.io in your project.

In order to use the library (Gradle dependency)
- Add the following to your project level build.gradle:
```gradle
allprojects {
    repositories {
        maven { url 'https://dl.bintray.com/predict-io/maven/' }
    }
}
```
- Add this to your app build.gradle:
```gradle
dependencies {
    compile 'com.google.android.gms:play-services-location:11.2.0'
    compile 'com.google.android.gms:play-services-base:11.2.0'
    compile 'io.predict:predict-io:4.9.0'
}
```
>  NOTE: If you are using some other libraries of play-service other than the mentioned above, must use of same version i-e 11.2.0, otherwise there is a high possibility of getting errors.

- Add following tags in **AndroidManifest.xml**  

**YOUR_API_KEY** provided with meta-data tag  
```xml
<meta-data
            android:name="io.predict.sdk.API_KEY"
            android:value="YOUR_API_KEY" />
```
Google Play services version under Application tag in manifest i.e.
```xml
<meta-data
            android:name="com.google.android.gms.version"
            android:value="@integer/google_play_services_version" />
```
Permission tags
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="com.google.android.gms.permission.ACTIVITY_RECOGNITION" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```
### If not using Gradle
#### Manual Integration Steps
Please follow these steps in order to integrate predict.io in your project.
- Copy provided [predict.io SDK JAR](https://github.com/predict-io/PredictIO-Android/tree/master/SDK) to your app's libs folder

- Add this to your app build.gradle:
```gradle
dependencies {
    compile 'com.google.android.gms:play-services-location:11.2.0'
    compile 'com.google.android.gms:play-services-base:11.2.0'
}
```
>  NOTE: If you are using some other libraries of play-service other than the mentioned above, must use of same version i-e 11.2.0, otherwise there is a high possibility of getting errors.


- Add following tags in **AndroidManifest.xml**  

**YOUR_API_KEY** provided with meta-data tag  
```xml
<meta-data
            android:name="io.predict.sdk.API_KEY"
            android:value="YOUR_API_KEY" />
```
Google Play services version under Application tag in manifest i.e.
```xml
<meta-data
            android:name="com.google.android.gms.version"
            android:value="@integer/google_play_services_version" />
```
Permission tags
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="com.google.android.gms.permission.ACTIVITY_RECOGNITION" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```
### Example
```
PredictIo.start( object : PredictIo.Companion.PredictIoCallback{
                override fun success() {
                    print("Successfully started PredictIO SDK!")
                }

                override fun error(error: PredictIo.PredictIOError) {
                    when(error){
                        PredictIo.PredictIOError.invalidKey -> {
                            // Your API key is invalid (incorrect or deactivated)
                        }
                        PredictIo.PredictIOError.killSwitch -> {
                            // Kill switch has been enabled to stop the SDK
                        }
                    }
                }
            })
```

## Communication 
If you need help, visit our [Help Center] (https://support.predict.io)

## Author
predict.io, support@predict.io

## Credits
### About predict.io
Our mobile SDK gives you battery friendly background location so you always know where and when a user arrives or departs. No fiddling with geofences. No beacons or NFC needed. It uses the sensors embedded in any modern smartphone. You can embed it in minutes. Rather than spending months fiddling with the Activity APIs yourself, our SDK powers many industry leading Android and iOS apps in mobility, retail, hospitality, lifestyle and banking.
### License
#### Terms of Service 
Terms of service can be found [here](http://www.predict.io/terms-of-service/).
#### Privacy Policy 
Privacy Policy can be found [here](http://www.predict.io/privacy-policy/).
#### License
predict.io is available under the MIT license. See the LICENSE file for more info.
