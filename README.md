[<img width="400" src="https://github.com/Flourish-savings/flourish-sdk-flutter/blob/main/images/logo_flourish.png?raw=true"/>](https://flourishfi.com)
<br>
<br>
# Flourish Android Flutter example

This project is an example of how to integrate a native Android application with our Flutter module
<br>

# Getting Started
___
## Configuration
### Download dependencies
To start the integration, you will need to download the dependencies of our Flutter module (AAR Files) and save it in a directory of your choice.

URL to download:
https://github.com/Flourish-savings/flourish-flutter-module-libs


### Settings repository

To do this, edit settings.gradle in your host application to include the location where you save the dependencies you downloaded in the previous step.

```sh
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()

    maven {
      url '[the/place/where/you/saved/the/dependencies]'
    }
    maven {
      url 'https://storage.googleapis.com/download.flutter.io'
    }
  }
}
```

### Adding dependencies
The next step is adding the dependencies to build.gradle

```sh
dependencies {
    debugImplementation 'com.example.flourish_android_flutter:flutter_debug:1.0'
    profileImplementation 'com.example.flourish_android_flutter:flutter_profile:1.0'
    releaseImplementation 'com.example.flourish_android_flutter:flutter_release:1.0'
}
```

### Updating AndroidManifest
Add FlutterActivity to AndroidManifest.xml

```sh
<activity
    android:name="io.flutter.embedding.android.FlutterActivity"
    android:theme="@style/LaunchTheme"
    android:configChanges="orientation|keyboardHidden|keyboard|screenSize|locale|layoutDirection|fontScale|screenLayout|density|uiMode"
    android:hardwareAccelerated="true"
    android:windowSoftInputMode="adjustResize"
</activity>
```


## Implementation
After everything is configured, now let's integrate the Flutter module with your native Android app

The first thing you will need to do is initialize the Flutter module by configuring a FlutterEngine in your MainActivity

```kotlin
import io.flutter.embedding.android.FlutterActivity;

lateinit var flutterEngine : FlutterEngine

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        
        flutterEngine = FlutterEngine(this)
    
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )
    
        FlutterEngineCache
            .getInstance()
            .put("flourish", flutterEngine)
        
    }
}
```

So, with FlutterEngine initialized, you will now need to create a communication channel between your app and the Flutter module to send your credentials and settings.

you will do this using a MethodChannel that will use the created FlutterEngine
```kotlin
import io.flutter.embedding.android.FlutterActivity;

lateinit var flutterEngine : FlutterEngine

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        
        val jsonObject = JSONObject()

        jsonObject.put("partnerId", "YOUR-PARTNER-ID-HERE")
        jsonObject.put("secret", "YOUR-SECRET-KEY-HERE")
        jsonObject.put("env", "[staging/production]")
        jsonObject.put("language", "[en/es/pt]")
        jsonObject.put("customerCode", "YOUR-CUSTOMER-CODE-HERE")

        val yourArguments = jsonObject.toString()

        val methodChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "flourish")

        methodChannel.invokeMethod("initialize", yourArguments)
        
    }
}
```
With this, you finish the Flutter module initialization step, now the next step is to call and open the module when your user performs some action within the app.


### Open FlourishFI module
Now you will add the code to launch FlutterActivity from any desired point in your application

```kotlin
import io.flutter.embedding.android.FlutterActivity;

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        myButton.setOnClickListener {
            startActivity(
                FlutterActivity
                    .withCachedEngine("flourish")
                    .build(this)
            )
        }

    }
}
```

## Examples
___
Inside this repository, you have an example to show how to integrate:

https://github.com/Flourish-savings/flourish-android-flutter-example/tree/main/app/src/main/java/com/flourish/app
<br>
