# GeoSpark Android SDK Example

An example app for android which implements the GeoSpark SDK and shows your location history.

GeoSpark Android Documentation link: https://dashboard.geospark.co/docs/android/

## Example App

### To get started:

Download this project and open it in Android Studio.

1. [Request](https://geospark.co) for a GeoSpark developer account to get your SDK key and Secret.

2. In the projects MainActivity, update this line `GeoSpark.initialize(this,”YOUR-SDK-KEY”,”YOUR-SECRET”);` to contain your GeoSpark SDK key and Secret.

3. Update the AndroidManifest.xml file to contain your google app ID and enable Google Maps Android API for your project. (you can get one here: https://console.developers.google.com/)

```
<meta-data
  android:name="com.google.android.geo.API_KEY"
  android:value="YOUR-GOOGLE-APP-ID" />
```

4. Import your google-services.json file into app folder. Follow the instructions here to get one https://support.google.com/firebase/answer/7015592

# Installation into your Application

Installing the GeoSpark SDK is done in 3 steps.

1. **Import SDK into project**
2. **Enable Location and Run-time Permissions**
3. **Initialize the SDK** 

### Import SDK into your project

After downloading the SDK from [here](https://s3.amazonaws.com/geospark-framework/Android/GeoSpark.aar), click `File > Project Structure` in Android Studio. Click the green plus icon in the top left, click `Import AAR`, select the GeoSpark SDK file, press OK and wait for Gradle to finish syncing.

Once Gradle is finished (only a few seconds), click `File > Project Structure` again. Click on `app`, then `Depenencies` tab, then the green icon (top right), select `Module dependency`, click on GeoSpark, then press Ok and wait for Gradle to sync again.

### Enable Location and Run-time permissions
To enable location, call the `requestPermissions` and `requestLocationServices` method. For Android 6.0 and above, calling this method will trigger a location permission popup that the user has to allow.

```
/**
* Call this method to check Location Settings before proceeding for User
* Login
*/
     
if (!GeoSpark.checkPermission(MainActivity.this)) {
            GeoSpark.requestPermission(MainActivity.this);
        } else if (!GeoSpark.checkLocationServices(this)) {
            GeoSpark.requestLocationServices(this);
        } else {
            GeoSpark.startLocationTracking(this);
        }

@Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case GeoSpark.REQUEST_CODE_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GeoSpark.REQUEST_CODE_LOCATION_ENABLED) {
        }
    }
```

Location popup for Android 6.0 and above:

### Initialize the SDK

To initialize the SDK, you must call the `GeoSpark.initialize` method when your app is started.

```
GeoSpark.initialize(this,”YOUR-SDK-KEY”,”YOUR-SECRET”);
```

You should call this method in your Main Activity's onCreate method. Put in your own sdk key and secret here.

[Request](https://geospark.co) for SDK keys if you don't have already.

### Set up Firebase and the FCM SDK (Optional)

1. If you haven't already, [add Firebase to your Android project](https://firebase.google.com/docs/android/setup).
2. In Android Studio, add the FCM dependency to your app-level build.gradle file:

```
dependencies {
     compile 'com.google.firebase:firebase-messaging:11.4.2'
}
```
3. Add the following to your app's manifest: A service that extends FirebaseInstanceIdService to handle the creation, rotation, and updating of registration tokens. This is required for sending to specific devices or for creating device groups.

```
<service
    android:name=".MyFirebaseInstanceIDService">
    <intent-filter>
        <action android:name="com.google.firebase.INSTANCE_ID_EVENT"/>
    </intent-filter>
</service>
```

4. When you need to retrieve the current token, call `FirebaseInstanceId.getInstance().getToken()`. This method returns null if the token has not yet been generated.

## Create User

The SDK needs an User ID object to identify the device. The SDK has a convenience method `createUser()` to create a user which returns USer ID. 

Method parameters

| Parameter | Description |
|-----------|-------------|
| deviceToken  | deviceToken of the device |


```
//Create a User for given deviceToken on GeoSpark Server. 

GeoSpark.createUser(this, deviceToken,
  new GeoSparkCallback() {
            @Override
            public void success(@NonNull SuccessResponse successResponse) {
               successResponse.getUserID();
                
               // Handle createUser success here, if required
               // On UserLogin success
               onUserLoginSuccess();
            }

            @Override
            public void failure(@NonNull ErrorResponse errorResponse) {
                // Handle createUser Failure here, if required
                errorResponse.getErrorMessage();
                        
            }
        });        
```

## Get User

If you already have an User ID object. The SDK has a convenience method `getUser()` to to start the session for the existing user.

Method parameters

| Parameter    | Description |
|--------------|-------------|
| userID       | User ID from your API Server |
| deviceToken  | deviceToken of the device |

```
/**
*Implement your API call for User Login and get back a GeoSpark
*UserId from your API Server to be configured in the GeoSpark SDK
*along with deviceToken.
*/

GeoSpark.getUser(this,userID, deviceToken,
  new GeoSparkCallback() {
            @Override
            public void success(@NonNull SuccessResponse successResponse) {
               successResponse.getUserID();
                
               // Handle getUser success here, if required
               // On UserLogin success
               onUserLoginSuccess();
            }

            @Override
            public void failure(@NonNull ErrorResponse errorResponse) {
                // Handle getUser Failure here, if required
                errorResponse.getErrorMessage();
                        
            }
        });        
```

## Start Location Tracking

To start tracking the location, use the `startLocationTracking()` method. You can keep SDK to track location, or turn it off if you want to stop tracking the user at any point of time using the stopLocationTracking()  method.

```
GeoSpark.startLocationTracking(this);
```

## Stop Location Tracking

You can stop tracking the user at any point of time using the `stopLocationTracking()` method.

```
GeoSpark.stopLocationTracking(this);
```

## View Dashboard

Install your app with the GeoSpark SDK on a device and begin tracking on the Dashboard. You would see the user’s current state on the GeoSpark dashboard. If you click on the user, you should be able to view the user's location history.





