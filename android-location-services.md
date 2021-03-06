# Android Location



## Android Location Services vs Google Play Services

There are two competing APIs designed to retrieve the users locations. There is the _official_ preincluded Android Location Service. The alternative is the Google Location Services API, included in the Google Play Services APK. The Google Location Services API uses Anroids Location Services, but provide an easier interface.

This is because the Google API does some of the work for the developer. When using the Android API, the developer can chose between different location providers (GPS, Network). The Google API automatically handles this choice for the developer.

## Providers

When developing a location-aware application for Android, you can utilize GPS and Android's Network Location Provider to acquire the user location. Although GPS is most accurate, it only works outdoors, it quickly consumes battery power, and doesn't return the location as quickly as users want. Android's Network Location Provider determines user location using cell tower and Wi-Fi signals, providing location information in a way that works indoors and outdoors, responds faster, and uses less battery power. To obtain the user location in your application, you can use both GPS and the Network Location Provider, or just one.

Location providers are different methods of retrieving location updates. There are three providers available when using androids location services.

- `gps`
    
    This provider determines location using satellites. Depending on conditions, this provider may take a while to return a location fix. Requires the permission `ACCESS_FINE_LOCATION`. When using android level `>21` you should also declare that your application uses the feature `android.hardware.location.gps`.
- `network`

    This provider determines location based on availability of cell tower and WiFi access points. Results are retrieved by means of a network lookup. Requires either of the permissions `ACCESS_COARSE_LOCATION` or `ACCESS_FINE_LOCATION`. When using android level `>21` you should also declare that your application uses the feature `android.hardware.location.network`.

- `passive`.
    
    A special location provider for receiving locations without actually initiating a location fix. This provider can be used to passively receive location updates when other applications or services request them without actually requesting the locations yourself. This provider will return locations generated by other providers. Requires the permission `ACCESS_FINE_LOCATION`, although if the GPS is not enabled this provider might only return coarse fixes.

    This is what Android calls these location providers, however, the underlying technologies to make this stuff work is mapped to the specific set of hardware and telco provided capabilities (network service).

These providers has both pros and cons, and choosing a provider it not a simple choice. Choosing the most accurate location, comes with many drawbacks, such as a large power-consumption. The choice largely comes down to the application being built.

|Technology|Provider|Accuracy|Power Usage|Notes|
|---|---|---|---|---|
|Autonomous GPS|`gps`|~6 m.|High|<ul><li>GPS chip on the device</li><li>line of sight to the satellites</li><li>need about 7 to get a fix</li><li>takes a long time to get a fix</li><li>doesn’t work around tall buildings</li><ul>|
|Assisted GPS (AGPS)|`network`|~60 m.|Medium - Low|<ul><li>Assistance from the network (cellular network)</li><li>to provide a fast initial fix</li><li>very low power consumption</li><li>very accurate</li><li>works without any line of sight to the sky</li><li>depends on carrier and phone supporting this (even if phone supports it, and network does not then this does not work)</li></ul>|
|CellID lookup/WiFi MACID lookup|``network``<br>``passive``|~ 1600 m.|Low|<ul><li>very fast lock, and does not require GPS chip on device to be active</li><li>requires no extra power at all</li><li>has very low accuracy; sometimes can have better accuracy in populated and well mapped areas that have a lot of WiFi access points, and people who share their location with Google.</li></ul>|

## Permissions

The Android operating system requires that permission be given, before an application can retrieve the location of the phone. The two important permissions are `ACCESS_COARSE_LOCATION` and `ACCESS_FINE_LOCATION`. Which one you need, depends on the location provider you use. 

When using the `NETWORK_PROVIDER`, you need permissions to `ACCESS_COARSE_LOCATION`. This provider returns location data using WiFi and mobile cellphone data. Unlike the `GPS_PROVIDER`, it does not utilize the Global Positioning System (GPS).

When using the `GPS_PROVIDER`, you need permissions to `ACCESS_FINE_LOCATION`. This provider returns the most accurate location it can, using both the Global Positioning System (GPS), WiFi and mobile cellphone data.

Use the `AndroidManifest.xml` file to specify the permissions required by your application. Permissions are granted by the user when the application is installed (on devices running Android 5.1 and lower) or while the app is running (on devices running Android 6.0 and higher). You can also use the `requestPermissions` method to request permissions at runtime, rather than install-time or start-time. See below.

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>

<uses-feature android:name="android.hardware.location.network" />
<uses-feature android:name="android.hardware.location.gps" />
```

## Settings

Even if the user has granted permissions to location data, does guarantee that the application can access location data. The required providers may be turned off, or the users settings may be incompatible. Ensure that you have compatible settings under `Sikkerhed > Placering > Placeringsmetode`.

## Code

### `Location` data class

Contains information about some position on earth. Instances of the class are returned from `LocationManager` methods. The most important properties on the class are:

- `altitude`: Get the altitude if available, in meters above the [WGS 84 reference ellipsoid](https://en.wikipedia.org/wiki/World_Geodetic_System). If this location does not have an altitude then `0.0` is returned.
- `longitude`: The longitude, in degrees. This property is populated by all providers.
- `latitude`: The latitude, in degrees. This property is populated by all providers.
- `accuracy`: The estimated horizontal accuracy of this location, radial, in meters. We define horizontal accuracy as the radius of 68% confidence. In other words, if you draw a circle centered at this location's latitude and longitude, and with a radius equal to the accuracy, then there is a 68% probability that the true location is inside the circle.
- `time`: The moment in time, when the reading was created.

Note that all these are kotlin properties, backed by normal Java getters, ie. `getLongitude`.

## Using the `LocationManager` class

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

### Retrieving a cached location

`getLastKnownLocation` returns the last location retrieved by the android operating system. `getLastKnownLocation` does not attempt to retrieve new positioning data. The return value could therefore be outdated, if the phone has been turned off, or if the phone was unreachable. `getLastLocation` is therefore not suitable for most applications, since they require a more precise location. 

```kotlin
val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
val currentLocation = locationManager.getLastKnownLocation(provider)

print(currentLocation.latitude)
print(currentLocation.longitude)
```

`getLastLocation` can be useful if your application only needs a one-time rough location.

### Listening for location changes

Instead of returning the last known location using `getLastKnownLocation`, we can also request new location data. This can be acomplished using the `requestLocationUpdates`. This method uses an event model, where a provided *listener* is notified of changes to the phones position, and other information about the location provider.

The most basic `requestLocationUpdates` signature is:

```kotlin
fun void requestLocationUpdates(String provider, long minTime, float minDistance, LocationListener listener);
```

The `provider` parameter is the location provider that is used to retrieve location updates.

The `minTime` and `minDistance` arguments can be used to limit the number of updates recieved by the `listener`. `minTime` is the minimum amount of time between updates passed to the `listener`. `minDistance` is the minimum change in distance in meters required, for a new update event to be called. These arguments should be set, based on the application requirements.

The `listener` parameter accepts an object of type `LocationListener`. This object is notified when the location is updated. It is also notified when the provider status changes, ei. when the user shuts off their GPS while the application is using the `gps` provider.

```kotlin
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MyActivity : AppCompatActivity() {

    class MyListener : LocationListener {

        /**
         * Called when the position of the user has changed.
         * @param location The most recent location known.
         */
        override fun onLocationChanged(location: Location?) {
            if (location != null) {
                print(location.latitude)
                print(location.longitude)
            }
        }

        /**
         * Called when the status of some provider changes. 
         * For example, when the gps provider first finds a GPS fix.
         */
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            print("Provider $provider had a status change.")
        }

        /**
         * Called when a location provider has been turned on.
         * Can be used to upgrade the used provider.
         *
         * @param provider The provider that was turned on.
         */
        override fun onProviderEnabled(provider: String?) {
            print("Provider $provider was turned on")
        }

        /**
         * Called when a location provider has been turned off.
         * Can be used to downgrade the used provider.
         *
         * @param provider The provider that was turned off.
         */
        override fun onProviderDisabled(provider: String?) {
            print("Provider $provider was turned off")
        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.layout)

        val listener = MyListener()
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates("gps", 10 * 1000, 0f, listener)
    }
}
```

### Removing listeners

`removeUpdates` remove any listeners attached using `requestLocationUpdates`. This is necessarry for applications, where the location provider changed often. `removeUpdates` takes the listener to remove as the first parameter. Note that the listener to remove must be the same `object` as the listener that was registered.

```kotlin
val manager  = getSystemService(Context.LOCATION_SERVICE) as LocationManager
val listener = this // this implements OnItemSelectedListener

manager.requestLocationUpdates(provider, minTime, minDistance, listener) // listener attached
manager.removeUpdates(listener) // listener detached
```

The listeners are __not__ automatically removed, if the activity is destroyed. You must manually remove the listener. This can easily be done by using the `onDestroy` method on `AppCompatActivity`.

```kotlin
/**
    * Remove the active listener.
    */
override fun onDestroy() {
    super.onDestroy()
    locationManager.removeUpdates(this)
}
```

### Finding the best provider

The `Criteria` class can be used by the application to indicate the criteria for selecting a location provider. Providers may be ordered according to accuracy, power usage, ability to report altitude, speed, bearing, and monetary cost. Can be used to decide on the best possible location provider, based on the active providers and phone status.

```kotlin
/**
 * Returns location provider criteria for coarse positioning.
 */
fun createCoarseCriteria(): Criteria {

    val c = Criteria()
    c.accuracy = Criteria.ACCURACY_COARSE
    c.isAltitudeRequired = false
    c.isBearingRequired = false
    c.isSpeedRequired = false
    c.isCostAllowed = true
    c.powerRequirement = Criteria.POWER_HIGH
    return c
}

/**
 * Returns location provider criteria for fine positioning.
 */
fun createFineCriteria(): Criteria {

    val c = Criteria()
    c.accuracy = Criteria.ACCURACY_FINE
    c.isAltitudeRequired = false
    c.isBearingRequired = false
    c.isSpeedRequired = false
    c.isCostAllowed = true
    c.powerRequirement = Criteria.POWER_HIGH
    return c
}
```

The `LocationManager.getBestProvider` method can then be used to calculate the best provider given the chosen criteria.

### Geocoding

> Geocoding is the process of transforming a street address or other description of a location into a (latitude, longitude) coordinate. Reverse geocoding is the process of transforming a (latitude, longitude) coordinate into a (partial) address. The amount of detail in a reverse geocoded location description may vary, for example one might contain the full street address of the closest building, while another might contain only a city name and postal code.

Geocoding is useful when translating positional data from machine to human language, or vice versa. I will be using the `android.location.Geocoder` class to perform reverse geocoding.

#### `GeoCoder.getFromLocation`

```java
public List<Address> getFromLocation(double latitude, double longitude, int maxResults)
```

The `getFromLocation` method returns a list of `Address` instances, based on the provided `latitude` and `longitude`. The `maxResults` parameter can additionally be used to limit the number of returned addresses. Most of the time, setting `maxResults = 1` is adequate.

#### `Address`

https://developer.android.com/reference/android/location/Address

The address class obviously contains information about some address, the most inportant properties are the following:

- `countryName` The name of the country.
- `locality` The name of the locality (city).
- `postalCode` The post code.
- `featureName` The name of any near features or landmarks.
- `locale` Returns the locale (language) of the address.
- `addressLines` Contains street and street number information.