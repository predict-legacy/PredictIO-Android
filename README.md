# PredictIO-Android
A battery-optimized SDK for android to get real-time updates with context information when a user starts or ends a journey.

[![Download](https://api.bintray.com/packages/predict-io/maven/PredictIO/images/download.svg)](https://bintray.com/predict-io/maven/PredictIO/_latestVersion)

## Requirements
* [Sign up](http://www.predict.io/sign-up/) for API key
* Android 4.0.3 (API 15) or above
* Google Play services 9.4.0 or above
* `targetSdkVersion 25` and `compileSdkVersion 26`

## Installation
### Using Gradle 
#### Integration Steps
If the application is written in Java, you need to include the following in your *dependencies* list in your App *build.gradle* file
```
// Note: Jdk version should be same to the one you are using in Android Studio.
compile "org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.2.21"
```

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
    // 11.8.0 or above
    compile 'com.google.android.gms:play-services-location:11.8.0'
    compile 'com.google.android.gms:play-services-base:11.8.0'
    compile 'io.predict:predict-io:5.1.3'
}
```

- Add following tags in **AndroidManifest.xml** under **Application** tag 

**YOUR_API_KEY** provided with meta-data tag  
```xml
<meta-data
            android:name="io.predict.sdk.API_KEY"
            android:value="YOUR_API_KEY" />
```
Google Play services version
```xml
<meta-data
            android:name="com.google.android.gms.version"
            android:value="@integer/google_play_services_version" />
```
Permission tags
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="com.google.android.gms.permission.ACTIVITY_RECOGNITION" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

### Intialize the SDK
Add the following code in your *Application* class inside *OnCreate()* method

**Kotlin**
```kotlin
PredictIo.init(context = this)
```

**Java**
```java
PredictIo.Companion.init(this);
```

### Start SDK 
After getting location permission, start the SDK using following code. Optional, you can also provide 
`API_KEY`

**Kotlin**
```kotlin
PredictIo.start(object : PredictIoCallback {
        override fun error(error: PredictIOError) {
            when(error){
                PredictIOError.invalidKey -> {
                    // Your API key is invalid (incorrect or deactivated)
                }
                PredictIOError.killSwitch -> {
                    // Kill switch has been enabled to stop the SDK
                }
                PredictIOError.wifiDisabled -> {
                    // Wifi is disabled
                }
                PredictIOError.locationPermission -> {
                    // Location permission is not granted
                }
                else -> {
                    // SDK started without any error
                }
            }
        }
    })
```

```kotlin
PredictIo.start(object : PredictIoCallback {
    override fun error(error: PredictIOError?) {
    // error handled as aboove
    }
}, apiKey = apiKey)
```

**Java**
```java
PredictIo.Companion.start(new PredictIoCallback() {
    @Override
    public void error(PredictIOError predictIOError) {
        if (predictIOError == null) {
            // SDK started successfully
        }
        else {
            switch (predictIOError) {
                case invalidKey:
                    // Your API key is invalid (incorrect or deactivated)
                break;
                
                case killSwitch:
                    // Kill switch has been enabled to stop the SDK
                break;

                case wifiDisabled:
                    // Wifi is disabled
                break;

                case locationPermission:
                    // Location permission is not granted
                break;
            }
        }
    }
});
```

## Events

The predict.io SDK can give you callbacks for the events which are detected for you to integrate with your own app's functionality.

```kotlin
PredictIo.notify(PredictIOTripEventType.ANY){
    event: PredictIOTripEvent ->
    // Do something with event 
}

```

```java
PredictIo.Companion.notify(PredictIOTripEventType.ARRIVAL, new Function1<PredictIOTripEvent, Unit>() {
     @Override
     public Unit invoke(PredictIOTripEvent predictIOTripEvent) {
     // Do something when user has arrived to a location
         return null;
     }
});
```
## High Power & Low Power

The predict.io SDK comes in two power levels which cater to different requirements of battery consumption and latency of events being detected.

> **NOTE**: Power level won't take effect properly until a fresh app relaunch, it's not a setting which should be toggled at runtime.

**High Power**

* 5% typical battery usage in 24 hour period
* Events detected within a few minutes

```Kotlin
PredictIo.start(object : PredictIoCallback {
    override fun error(error: PredictIOError?) {
    // error handled as aboove
    }
}, powerMode = PowerMode.HIGH_POWER)
```

**Low Power**

* Around 1% typical battery usage in 24 hour period
* Events detected with up to 30 min delay

> **NOTE**: Low power is the default if no value is set for the `powerLevel` parameter.

```java
PredictIo.Companion.start(new PredictIoCallback() {
    @Override
    public void error(@Nullable PredictIOError error) {
    // Handled as above
    }
}, "apiKey", PowerMode.LOW_POWER);
```

## Communication 
If you need help, visit our [Help Center] (https://support.predict.io)

## Author
predict.io, support@predict.io

## Credits
### License
#### Terms of Service 
Terms of service can be found [here](http://www.predict.io/terms-of-service/).
#### Privacy Policy 
Privacy Policy can be found [here](http://www.predict.io/privacy-policy/).
#### License
predict.io is available under the MIT license. See the LICENSE file for more info.
