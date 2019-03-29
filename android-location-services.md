# Android Location

## Android Location Services vs Google Play Services

There are two competing APIs designed to retrieve the users locations. There is the _official_ preincluded Android Location Service. The alternative is the Google Location Services API, included in the Google Play Services APK. The Google Location Services API uses Anroids Location Services, but provide an easier interface.

This is because the Google API does some of the work for the developer. When using the Android API, the developer can chose between different location providers (GPS, Network). The Google API automatically handles this choice for the developer.

## Prerequisites
### Permissions and Providers

The Android operating system requires that permission be given, before an application can retrieve the location of the phone. The two important permissions are `ACCESS_COARSE_LOCATION` and `ACCESS_FINE_LOCATION`. Which one you need, depends on the location provider you use. 

When using the `NETWORK_PROVIDER`, you need permissions to `ACCESS_COARSE_LOCATION`. This provider returns location data using WiFi and mobile cellphone data. Unlike the `GPS_PROVIDER`, it does not utilize the Global Positioning System (GPS).

When using the `GPS_PROVIDER`, you need permissions to `ACCESS_FINE_LOCATION`. This provider returns the most accurate location it can, using both the Global Positioning System (GPS), WiFi and mobile cellphone data.

#### Manifest

Use the `AndroidManifest.xml` file to specify the permissions required by your application. Permissions are granted by the user when the application is installed (on devices running Android 5.1 and lower) or while the app is running (on devices running Android 6.0 and higher). You can also use the `requestPermissions` method to request permissions at runtime, rather than install-time or start-time. See below.

```xml
<manifest 
    xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.google.android.gms.location.sample.basiclocationsample" >
    <>
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
</manifest>
```

#### Check
#### Request
### GPS vs Network

## `LocationManager`

> The `LocationManager` class provides access to the system location services. These services allow applications to obtain periodic updates of the device's geographical location, or to fire an application-specified Intent when the device enters the proximity of a given geographical location.

A `LocationManager` instance is obtained by calling the `getSystemService` method on a `Context`. The `getSystemService` method can return many different service managers based on the provided first arguments. To obtain an instance of `LocationManager`, call the method with `Context.LOCATION_SERVICE`:

```kotlin
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity

class LocationAwareActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_current_location)
        
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager 
    }
```

Note that you will need to cast the returned value.

### `Location`

Contains information about some position on earth. Instances of the class are returned from `LocationManager` methods. The most important properties on the class are:

- `altitude`: Get the altitude if available, in meters above the [WGS 84 reference ellipsoid](https://en.wikipedia.org/wiki/World_Geodetic_System). If this location does not have an altitude then `0.0` is returned.
- `longitude`: The longitude, in degrees.
- `latitude`: The latitude, in degrees.
- `accuracy`: The estimated horizontal accuracy of this location, radial, in meters. We define horizontal accuracy as the radius of 68% confidence. In other words, if you draw a circle centered at this location's latitude and longitude, and with a radius equal to the accuracy, then there is a 68% probability that the true location is inside the circle.
- `time`: The moment in time, when the reading was created.

Note that all these are kotlin properties, backed by normal Java getters, ie. `getLongitude`.

### `getLastKnownLocation`

`getLastKnownLocation` returns the last location retrieved by the android operating system. `getLastKnownLocation` does not attempt to retrieve new positioning data. The return value could therefore be outdated, if the phone has been turned off, or if the phone was unreachable. `getLastLocation` is therefore not suitable for most applications, since they require a more precise location. During development, `getLastKnownLocation` almost always returns `null`, since the phone has no location information.

### `requestLocationUpdates`

Instead of returning the last known location using `getLastKnownLocation`, we can also request new location data. This can be acomplished using the `requestLocationUpdates`. This method uses an event model, where a provided object is notified of changes to the phones position, and other information about the location provider.

There are four methods under the `requestLocationUpdates` name:

```kotlin
fun void requestLocationUpdates(String provider, long minTime, float minDistance, LocationListener listener);
fun void requestLocationUpdates(long minTime, float minDistance, Criteria criteria, LocationListener listener, Looper looper)
fun void requestLocationUpdates(String provider, long minTime, float minDistance, LocationListener listener, Looper looper)
fun void requestLocationUpdates(long minTime, float minDistance, Criteria criteria, PendingIntent intent)
```

They all perform mostly the same task, with minor variations. All the method variations take both the `minTime` and `minDistance` parameters. These arguments can be used to limit the number of updates recieved by the `LocationListener`. `minTime` is the minimum amount of time between updates passed to the `LocationListener`. `minDistance` is the minimum change in distance in meters required, for a new update event to be called.

The `provider` and `criteria` parameters are different ways to specify the location provider to use. The `provider` parameter takes a location provider and uses that provider to listen for location changes. The `criteria` parameter can be used to choose a provider based on different specified criteria. Providers may be ordered according to accuracy, power usage, ability to report properties such as altitude, speed and bearing.

The `listener` parameter accepts an instance of `LocationListener`. This object is notified when 

The `looper` parameters allows for running the listener events in another thread.