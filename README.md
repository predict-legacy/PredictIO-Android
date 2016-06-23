# Android ParkTAG SDK Documentation
## SDK Version (2.1.0)
## Introduction
The parking detection API (patent pending) allows you to retrieve real-time updates on the parking status of a user. You can run the API in apps designed for Android and iOS devices. No additional in-car hardware is required. 

###### Main features include detecting when a user..
...is searching for parking (BETA), 
...has parked,  
...has vacated a parking spot,  
...is about to vacate a parking spot (Under-Development),  
...is using a specific mode of transportation (BETA).

You may use the parking updates to trigger events or notifications specific to your use case. The parking detection service stack leverages all available phone sensors. Sensor usage is extremely battery optimized. Incremental battery consumption does not exceed 10% in typical cases. Also we strongly suggest you ensure inclusion of LBS in your T&C and Privacy Policies. By using this API you explicitly agree to our license agreement, terms and conditions and privacy policy. If you are unsure about any item, please contact us at [support@parktag.mobi]()

We wish you great enjoyment with this API and welcome any feedback you might have for us. 
Your ParkTAG Dev Team,

## Sample Project:
Sample project is also attached in zip package, just add your sdk key in manifest file of project and run the project for quick demo.

## Integration Steps
Please follow these steps in order to integrate ParkTAG SDK in your project.

- Copy provided ParkTagSDK JAR to your app's libs folder  
- Open the build.gradle file inside your application module directory and add following lines under relevant tags  
```
android
{...
useLibrary  'org.apache.http.legacy'
}
dependencies
{...
compile  fileTree(dir: 'libs', include: ['*.jar'])
compile 'com.google.android.gms:play-services-location:9.0.1'
compile 'com.google.android.gms:play-services-base:9.0.1'
}
```

>  NOTE: If you are using some other libraries of play-service other than the mentioned above, must use of same version i-e 9.0.1, otherwise there is a high possibility of getting errors.

- Now create a class that implements the interface mobi.parktag.sdk.ParktagInterface (Preferably Application class), this interface includes following methods:  
```java 
  public void vacatedParking(Location startLocation, long timeOfDetection, TransportMode transportMode) 
```
This method is invoked when ParkTAG detects that the user has just vacated a   parking spot and have started a new trip.

```java 
public void vehicleParked(Location parkedLocation, long startTime, long endTime, TransportMode transportMode)
```
This method is invoked when ParkTAG detects that the user has just parked his vehicle.
```java 
public void vehicleParkedSuspected(Location parkedLocation, long startTime, long endTime, TransportMode transportMode)
```
This method is invoked when ParkTAG suspects that user has parked his vehicle. If you need only confirmed parked events, use vehicleParked method instead
```java 
public void lookingForParking(Location location)
```
This method is invoked when ParkTAG detects that the user is looking for parking. This method is in Beta state.
```java 
public void newLocationReceived(Location location)
```
This method is invoked when ParkTAG receives new location from its location provider
```java 
public void activationFailed(int errorCode)
```
This method is called when there is any failure in activation of ParkTAG
- Once this is done Override the onCreate() Method of Application class and pass the implementation of mobi.parktag.sdk.ParktagInterface interface to ParkTagSdk using following method:  
```java 
 public class YOUR_APPLICATION_CLASS extends Application {

        @Override
        public void onCreate() {
        super.onCreate();
        //ParkTAG SDK code
        Parktag parktag = Parktag.getInstance(getApplicationContext());
        // This notifies sdk that app is initialised, this is important for the performance of the detection.
        parktag.setAppOnCreate(this);
        // set this to get event callbacks
        parktag.setListener(parktagListenerInstance);	        		
        
        //Your app code..
        }
  }
```
- Add following tags in **AndroidManifest.xml**  

YOUR_API_KEY provided with meta-data tag  
```xml 
<meta-data android:name="mobi.parktag.sdk.api.key" android:value="YOUR_API_KEY" />
```
Google Play services version under Application tag in manifest i.e.
```xml 
<meta-data android:name="com.google.android.gms.version" android:value="@integer/google_play_services_version" />
```
Permission tags
```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
<uses-permission android:name="com.google.android.gms.permission.ACTIVITY_RECOGNITION"/>
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED"/>
```
Service tags
```xml 
<service android:name="mobi.parktag.sdk.detection.services.ParktagCellManager" android:exported="false" />
<service android:name="mobi.parktag.sdk.detection.services.ParktagService" android:exported="false" />
<service android:name="mobi.parktag.sdk.detection.services.ParktagLocationManager" android:exported="false" />
<service android:name="mobi.parktag.sdk.detection.services.ParktagComService" android:exported="false" />
```
Receiver tags
```xml
<receiver android:name="mobi.parktag.sdk.detection.recievers.ParktagReceiver" >
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
- Incase you are using ProGuard, add following lines in your ProGuard config file in order to prevent ProGuard from stripping away required classes.
```
###### For ParkTAG SDK ######
-keep public class mobi.parktag.sdk.**{*;}

###### For Google Play Services ######
-keep class * extends java.util.ListResourceBundle {
protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
@com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**

############## Rules needed for Apache HTTP Client  ##############
-keepnames class org.apache.** {*;}
-keep public class org.apache.** {*;}
-dontwarn org.apache.commons.logging.LogFactory
-dontwarn org.apache.http.annotation.ThreadSafe
-dontwarn org.apache.http.annotation.Immutable
-dontwarn org.apache.http.annotation.NotThreadSafe
```
That's it! Congratulations, you have successfully integrated ParkTagSdk with your app.
Now you just need to start and stop ParkTag tracker by using following methods accordingly

##### Start:
```java
Parktag instance = Parktag.getInstance(context);  
// To enable in full precision mode  
instance.enableFullPrecision(true);  
// To Finally start Algo call start i.e.  
instance.start();
```
##### Stop:
```java
Parktag.getInstance(context).stop();  
```
#### Marshmallow's Compatibility:

- As Marshmallow (API 23) has been rolled out, it is recommended to set your app target level to 23 or above and prompt users to grant permissions at runtime.
- For apps having target api level 23 or above.  
  - ParkTag tracker needs Locations access to get started. It is developer's responsibility to prompt the user to grant "Location" permission before starting the Tracker.
  - Since users can revoke "Location" permission at any time from the app Settings screen, note that this will also disable Tracker as well.
  - Please note, if user re-grants the same permission again from the app Settings, this will not automatically enable the Tracker.
  - ParkTAG Sdk is using 'Appache HTTP Client' and as marshmallow removes support for it, you need to add it's library declaration (with relevant proguarding rules) in your apps's gradle file. Ref: [Apache HTTP Client Removal](https://developer.android.com/about/versions/marshmallow/android-6.0-changes.html?hl=es#behavior-apache-http-client)
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
parktag.setAppOnCreate(this);
```
- In version 2.1.0 we added new sdk event method i-e "vehicleParkedSuspected", introduced multiclass vehicle detection (transport mode) and added wifi state change filter ("android.net.wifi.STATE_CHANGE") in ParktagReceiver receiver.  

## Methods
```java
public class Parktag {
    
        /**
        * @param context
        * @return instance of Parktag
        */
	static Parktag getInstance(Context context);
	
        /**
        * Starts the ParkTAG Tracker.
        * This method will validate the API KEY (Asynchronously), incase of failure
        * activationFailed method (of ParktagInterface) will be called with appropriate error code
        *
        * @throws Exception with message "Google Play Services Error" if Google Play services are not installed
        *                   with message "Permission is not granted by user." if required dangerous permission is not granted.
        *                   Ref: Permissions
        */
        @RequiresPermission(allOf = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION})
	void start();
	
	/**
	* Stops Parktag tracker and resets tracker state
	*/
	void stop();
	
	/**
	* This function has been deprecated, you don't need to call this function now.
	*/
	void restart();
	
        /**
        * This method determines whether tracker is enabled or not
        * This method has been deprecated since 2.1, to check running state refer to isTrackerRunning();
        *
        * @return true: if ParkTAG is enabled else false
        */	
	boolean isEnabled();
	
        /**
        * Use this method to verify if Tracker is active or not
        * @return true, if ParkTAG tracker is in running state otherwise false
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
        * @return true if ParkTAG is in full precision otherwise false
        */
	boolean isFullPrecisionEnabled();
	
	/**
	* Instance of implemented ParktagInterface must be set to receive Parktag events
	* @param listener Object which implements ParktagInterface
	*/
	void setListener(ParktagInterface listener);
	
	/**
	* Use this method to get unique id(Identifier) assigned to each device by ParkTAG SDK
	* @param Context
	* @return UUID
	*/
	String getDeviceIdentifier();
		
	/**
	* Dangerous Permissions Ref: Permissions
	* @return array of dangerous permissions required to start ParkTAG tracker
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
/**
* This is an interface which needs to be implemented
*/
public interface ParktagInterface {
        
        /**
        * This method is invoked when ParkTAG thinks that the user has just vacated
        * a parking spot and have started a new trip.
        *
        * @param startLocation
        * @param timeOfDetection
        * @param transportMode, multiclass vehicle detection (beta), possible values are:
        *   UNDETERMINED: if sdk was unable to determine any transport mode
        *   CAR: if transport mode detected by sdk is car
        *   OTHER: if transport mode detected by sdk is non-car (other than car)
        */
	public void vacatedParking(Location startLocation, long timeOfDetection, TransportMode transportMode);
	
        /**
        * This method is invoked when ParkTAG thinks that the user has just parked
        * his vehicle and have ended a trip.
        *
        * @param parkedLocation
        * @param startTime
        * @param endTime
        * @param transportMode, multiclass vehicle detection (beta), possible values are:
        *   UNDETERMINED: if sdk was unable to determine any transport mode
        *   CAR: if transport mode detected by sdk is car
        *   OTHER: if transport mode detected by sdk is non-car (other than car)
        */
	public void vehicleParked(Location parkedLocation, long startTime, long endTime, TransportMode transportMode);
    
        /**
        * This method is invoked when ParkTAG suspects that user has just parked his vehicle
        * Most of the time, it is followed by a confirmed vehicleParked event
        * If you need only confirmed parked events, use vehicleParked method (below) instead
        *
        * @param parkedLocation
        * @param startTime
        * @param endTime
        * @param transportMode, multiclass vehicle detection (beta), possible values are:
        *   UNDETERMINED: if sdk was unable to determine any transport mode
        *   CAR: if transport mode detected by sdk is car
        *   OTHER: if transport mode detected by sdk is non-car (other than car)
        */
	public void vehicleParkedSuspected(Location parkedLocation, long startTime, long endTime, TransportMode transportMode);
	
        /**
        * This method is invoked when ParkTAG thinks that user is searching for parking at his
        * current location
        *
        * @param location
        */
	public void lookingForParking(Location location);
	
        /**
        * This method is invoked when ParkTAG detected an invalid vacation
        */
	public void vacatedParkingCanceled();
	
	/**
	* This method is invoked when ever ParkTAG receives new location from its location provider.
	*
	* @param location
	*/
	public void newLocationReceived(Location location);
	
    /**
     * @param errorCode
     * -> Error codes for GoogleApiClient, Ref: GoogleApiClient
     *    SERVICE_MISSING = 1;
     *    SERVICE_VERSION_UPDATE_REQUIRED = 2;
     *    SERVICE_DISABLED = 3;
     *    SIGN_IN_REQUIRED = 4;
     *    INVALID_ACCOUNT = 5;
     *    RESOLUTION_REQUIRED = 6;
     *    NETWORK_ERROR = 7;
     *    INTERNAL_ERROR = 8;
     *    SERVICE_INVALID = 9; DEVELOPER_ERROR = 10;
     *    LICENSE_CHECK_FAILED = 11;
     *    DATE_INVALID = 12;
     *    CANCELED = 13;
     *    TIMEOUT = 14;
     *    INTERRUPTED = 15;
     *    DRIVE_EXTERNAL_STORAGE_REQUIRED = 1500;
     * -> Other Error Codes
     *    API_KEY_AUTHENTICATION_FAILED = 401;
     */
	public void activationFailed(int errorCode);
}
```
