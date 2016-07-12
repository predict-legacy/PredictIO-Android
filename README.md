# Android PredictIO SDK Documentation
## SDK Version (3.0.0)
## Introduction
The predict.io API (patent pending) allows you to retrieve real-time updates on the departure and arrival status of a user. You can run the API in apps designed for Android and iOS devices. No additional in-car hardware is required. http://www.predict.io 

Main features include detecting when a user..  
...is searching for parking (BETA),   
...has arrived,  
...has departed a parking spot,  
...is about to depart a parking spot (Under-Development),  
...is using a specific mode of transportation (BETA).

You may use the departure updates to trigger events or notifications specific to your use case. The departure detection service stack leverages all available phone sensors. Sensor usage is extremely battery optimized. Incremental battery consumption does not exceed 10% in typical cases. Also we strongly suggest you ensure inclusion of LBS in your T&C and Privacy Policies. By using this API you explicitly agree to our license agreement, terms and conditions and privacy policy. If you are unsure about any item, please contact us at [Support Center](http://www.predict.io/support-center/)

We wish you great enjoyment with this API and welcome any feedback you might have for us. 
Your PredictIO Dev Team,

## Sample Project:
Add your sdk api key in manifest file of project and run the project for quick demo.

## Integration Steps
Please follow these steps in order to integrate PredictIO SDK in your project.

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
    compile 'com.google.android.gms:play-services-location:9.0.1'
    compile 'com.google.android.gms:play-services-base:9.0.1'
    compile 'io.predict:predict-io:3.0.0'
}
```
>  NOTE: If you are using some other libraries of play-service other than the mentioned above, must use of same version i-e 9.0.1, otherwise there is a high possibility of getting errors.

- Now create a class that implements the interface io.predict.PredictIOInterface (Preferably Application class), this interface includes following methods:  
```java 
public void departedLocation(Location departureLocation, long departureTime, TransportMode transportMode) 
```
This method is invoked when PredictIO detects that the user has just departed a parking spot and have started a new trip.

```java 
public void arrivedAtLocation(Location arrivalLocation, long departureTime, long arrivalTime, TransportMode transportMode)
```
This method is invoked when PredictIO detects that the user has just arrived at destination.
```java 
public void arrivalSuspectedFromLocation(Location arrivalLocation, long departureTime, long arrivalTime, TransportMode transportMode)
```
This method is invoked when PredictIO suspects that user has arrived at destination. If you need only confirmed arrival events, use arrivedAtLocation method instead
```java 
public void searchingInPerimeter(Location location)
```
This method is invoked when PredictIO detects that the user is looking for parking. This method is in Beta state.
```java 
public void didUpdateLocation(Location location)
```
This method is invoked when PredictIO receives new location from its location provider
```java 
public void activationFailed(int errorCode)
```
This method is called when there is any failure in activation of PredictIO
- Once this is done Override the onCreate() Method of Application class and pass the implementation of io.predict.PredictIOInterface interface to PredictIO Sdk using following method:  
```java 
 public class YOUR_APPLICATION_CLASS extends Application {

        @Override
        public void onCreate() {
        super.onCreate();
        //PredictIO SDK code
        PredictIO predictIO = PredictIO.getInstance(getApplicationContext());
        // This notifies sdk that app is initialised, this is important for the performance of the detection.
        predictIO.setAppOnCreate(this);
        // set this to get event callbacks
        predictIO.setListener(predictIOListenerInstance);	        		
        
        //Your app code..
        }
  }
```
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
Service tags
```xml 
 		<service
            android:name="io.predict.sdk.detection.services.PIOMainService"
            android:exported="false" />
        <service
            android:name="io.predict.sdk.detection.services.PIOUtilService"
            android:exported="false" />
        <service
            android:name="io.predict.sdk.detection.services.PIOLocationService"
            android:exported="false" />
        <service
            android:name="io.predict.sdk.detection.services.PIOComService"
            android:exported="false" />
```
Receiver tags
```xml
<receiver android:name="io.predict.sdk.detection.receivers.PIOReceiver">
    <intent-filter>
        <action android:name="android.intent.action.BOOT_COMPLETED" />
        <action android:name="android.intent.action.AIRPLANE_MODE" />
        <action android:name="android.location.PROVIDERS_CHANGED" />
        <action android:name="android.net.wifi.STATE_CHANGE" />
        <action android:name="android.intent.action.ACTION_POWER_CONNECTED" />
        <action android:name="android.intent.action.ACTION_POWER_DISCONNECTED" />
        <action android:name="com.sonymobile.SUPER_STAMINA_SETTING_CHANGED" />
    </intent-filter>
    <intent-filter>
        <action android:name="android.intent.action.PACKAGE_REPLACED" />
        <data android:scheme="package" />
    </intent-filter>
</receiver>
```
- Incase you are using ProGuard, add following line in your ProGuard config file in order to prevent ProGuard from stripping away required classes.
```
##### For PredictIO SDK ######
-keep public class io.predict.**{*;}
```
That's it! Congratulations, you have successfully integrated PredictIO Sdk with your app.
Now you just need to start and stop PredictIO tracker by using following methods accordingly

##### Start:
```java
PredictIO instance = PredictIO.getInstance(context);  
// To enable in full precision mode  
instance.enableFullPrecision(true);  
// To Finally start Algo call start i.e.  
instance.start();
```
##### Stop:
```java
PredictIO.getInstance(context).stop();  
```
#### Marshmallow's Compatibility:

- As Marshmallow (API 23) has been rolled out, it is recommended to set your app target level to 23 or above and prompt users to grant permissions at runtime.
- For apps having target api level 23 or above.  
  - PredictIO tracker needs Locations access to get started. It is developer's responsibility to prompt the user to grant "Location" permission before starting the Tracker.
  - Since users can revoke "Location" permission at any time from the app Settings screen, note that this will also disable Tracker as well.
  - Please note, if user re-grants the same permission again from the app Settings, this will not automatically enable the Tracker.
  - **Note:** PredictIO Sdk is using 'Appache HTTP Client' and as marshmallow removes support for it, if you are having trouble with it, you need to add it's library declaration (with relevant proguarding rules) in your apps's gradle file. Ref: [Apache HTTP Client Removal](https://developer.android.com/about/versions/marshmallow/android-6.0-changes.html?hl=es#behavior-apache-http-client)
```gradle
android {
   	useLibrary  'org.apache.http.legacy'
}
```
- For apps having target api level less than 23.  
  - Tracker will run normally for all devices having OS less than 23.
  - For marshmallow and above devices, all permissions will be granted by default and tracker will run normally too.
  - But if a marshmallow or above user revokes "Location" permission from app settings, tracker will not work properly because app will not be able to get user's location.
- See [Permissions](https://developer.android.com/training/permissions/index.html?hl=es) documentation for more details.  

## Release Notes (skip them if you are integrating first time) :
- 15.44 version and onwards there is no need to restart tracker in your app BOOT_COMPLETED receiver.
- Marhsmallow(Permissions) compatibility has been added from version 16.10. Its recommended to go through the whole document for it.
- From version 16.10 add following line within onCreate() method of you Application class.
```jave
predictIO.setAppOnCreate(this);
```
- In version 2.1.0 we added new sdk event method i-e "arrivalSuspectedFromLocation", introduced multiclass vehicle detection (transport mode) and added wifi state change filter ("android.net.wifi.STATE_CHANGE") in PIOReceiver receiver.  

## Methods
```java
public class PredictIO {

    /**
     * @param context
     * @return instance of PredictIO Tracker
     */
    static PredictIO getInstance(Context context);

    /**
     * Starts the PredictIO Tracker.
     * This method will validate the API KEY (Asynchronously), incase of failure
     * activationFailed method (of PredictIOInterface) will be called with appropriate error code
     *
     * @throws Exception with message "Google Play Services Error" if Google Play services are not installed
     *                   with message "Permission is not granted by user." if required dangerous permission is not granted.
     *                   Ref: <a href="http://developer.android.com/intl/es/guide/topics/security/permissions.html#normal-dangerous">Permissions</a>
     */
    @RequiresPermission(allOf = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION})
    void start();

    /**
     * Stops PredictIO tracker and reset tracker state
     */
    void stop();

    /**
     * This method determines whether tracker is enabled or not
     * This method has been deprecated since 2.1, to check running state refer to isTrackerRunning();
     *
     * @return true: if PredictIO is enabled else false
     */
    boolean isEnabled();

    /**
     * Use this method to verify if Tracker is active or not
     *
     * @return true, if PredictIO tracker is in running state otherwise false
     */
    boolean isTrackerRunning();

    /**
     * Enables/Disables full precision mode, Full precision mode gives much better results but uses little bit more battery.
     * It can enabled any time, but disabling will also reset tracker state.
     *
     * @param enabled
     */
    void enableFullPrecision(boolean enabled);

    /**
     * Use this method to verify if Tracker is active in full precision mode or not
     *
     * @return true if PredictIO is in full precision otherwise false
     */
    boolean isFullPrecisionEnabled();

    /**
     * Instance of implemented PredictIOInterface must be set to receive PredictIO events
     *
     * @param listener Object which implements PredictIOInterface
     */
    void setListener(PredictIOInterface listener);

    /**
     * Use this method to get unique id(Identifier) assigned to each device by PredictIO SDK
     *
     * @param Context
     * @return UUID
     */
    String getDeviceIdentifier();

    /**
     * Dangerous Permissions Ref: Permissions
     *
     * @return array of dangerous permissions required to start PredictIO tracker
     */
    static String[] getAllRequiredDangerousPermissions();

    /**
     * Call this method within onCreate() of Application class.
     * This notifies sdk that app is initialised.
     */
    void setAppOnCreate(Application application);
} 
```

## Interface Methods
```java
import android.location.Location;

import mobi.parktag.sdk.TransportMode;

/**
 * This is an interface which needs to be implemented
 */
public interface PredictIOInterface {

    /* This method is invoked when predict.io detects that the user has just departed
    * from his location and have started a new trip
    *
    * @param departureLocation: The Location from where the user departed
    * @param departureTime: Start time of the trip
    * @param transportMode: Mode of transport
    *                       UNDETERMINED: if sdk was unable to determine any transport mode
    *                       CAR: if transport mode detected by sdk is car
    *                       OTHER: if transport mode detected by sdk is non-car (other than car)
    */
    void departedLocation(Location departureLocation, long departureTime, TransportMode transportMode);

    /**
     * This method is invoked when predict.io detects that the user is searching for a
     * parking space at a specific location
     *
     * @param location: The Location where predict.io identifies that user is searching for a parking space
     */
    void searchingInPerimeter(Location location);

    /**
     * This method is invoked when predict.io suspects that the user has just arrived
     * at his location and have ended a trip
     * Most of the time it is followed by a confirmed arrivedAtLocation event
     * If you need only confirmed arrival events, use arrivedAtLocation method (below) instead
     *
     * @param arrivalLocation: The Location where the user arrived and ended the trip
     * @param departureTime:   Start time of trip
     * @param arrivalTime:     Stop time of trip
     * @param transportMode:   Mode of transport
     *                         UNDETERMINED: if sdk was unable to determine any transport mode
     *                         CAR: if transport mode detected by sdk is car
     *                         OTHER: if transport mode detected by sdk is non-car (other than car)
     */
    void arrivalSuspectedFromLocation(Location arrivalLocation, long departureTime, long arrivalTime, TransportMode transportMode);

    /**
     * This method is invoked when predict.io detects that the user has just arrived at destination
     *
     * @param arrivalLocation: The Location where the user arrived and ended a trip
     * @param departureTime:   Start time of trip
     * @param arrivalTime:     Stop time of trip
     * @param transportMode:   Mode of transport
     *                         UNDETERMINED: if sdk was unable to determine any transport mode
     *                         CAR: if transport mode detected by sdk is car
     *                         OTHER: if transport mode detected by sdk is non-car (other than car)
     */
    void arrivedAtLocation(Location arrivalLocation, long departureTime, long arrivalTime, TransportMode transportMode);

    /**
     * This method is invoked when predict.io is unable to validate the last departure event.
     * This can be due to invalid data received from sensors or the trip amplitude.
     * i.e. If the trip takes less than 5 minutes or the distance travelled is less than 1km
     */
    void departureCanceled();

    /**
     * This is invoked when new location information is received from location services
     * Implemented this method if you need raw GPS data, instead of creating new location manager
     * Since, it is not recommended to use multiple location managers in a single app
     *
     * @param location: New location
     */
    void didUpdateLocation(Location location);

    /**
     * @param errorCode <br>
     *                  -> Error codes for GoogleApiClient, Ref: <a href="https://developers.google.com/android/reference/com/google/android/gms/common/ConnectionResult">GoogleApiClient</a>
     *                  SERVICE_MISSING = 1;<br>
     *                  SERVICE_VERSION_UPDATE_REQUIRED = 2;<br>
     *                  SERVICE_DISABLED = 3;<br>
     *                  SIGN_IN_REQUIRED = 4;<br>
     *                  INVALID_ACCOUNT = 5;<br>
     *                  RESOLUTION_REQUIRED = 6;<br>
     *                  NETWORK_ERROR = 7;<br>
     *                  INTERNAL_ERROR = 8;<br>
     *                  SERVICE_INVALID = 9; DEVELOPER_ERROR = 10;<br>
     *                  LICENSE_CHECK_FAILED = 11;<br>
     *                  DATE_INVALID = 12;<br>
     *                  CANCELED = 13;<br>
     *                  TIMEOUT = 14;<br>
     *                  INTERRUPTED = 15;<br>
     *                  DRIVE_EXTERNAL_STORAGE_REQUIRED = 1500;<br>
     *                  -> Other Error Codes
     *                  API_KEY_AUTHENTICATION_FAILED = 401;<br>
     */
    void activationFailed(int errorCode);
}
```