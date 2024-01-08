package com.flourish.app

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.flourish.app.databinding.ActivityMainBinding
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var flutterEngine : FlutterEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        flutterEngine = FlutterEngine(this)

        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )

        FlutterEngineCache
            .getInstance()
            .put("flourish", flutterEngine)


        val jsonObject = JSONObject()
        try {
            jsonObject.put("partnerId", "YOUR-PARTNER-ID-HERE")
            jsonObject.put("secret", "YOUR-SECRET-KEY-HERE")
            jsonObject.put("env", "[staging/production]")
            jsonObject.put("language", "[en/es/pt]")
            jsonObject.put("customerCode", "YOUR-CUSTOMER-CODE-HERE")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val yourArguments = jsonObject.toString()

        val methodChannel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "flourish")

        methodChannel.invokeMethod("initialize", yourArguments)

        val myButton: Button = findViewById(R.id.myButton)

        myButton.setOnClickListener {
            startActivity(
                FlutterActivity
                    .withCachedEngine("flourish")
                    .build(this)
            )
        }
    }
}