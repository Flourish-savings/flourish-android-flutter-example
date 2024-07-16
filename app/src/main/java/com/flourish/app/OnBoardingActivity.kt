package com.flourish.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel
import org.json.JSONException
import org.json.JSONObject

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var flutterEngine : FlutterEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_on_boarding)

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

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "flourish").setMethodCallHandler { call, result ->
            when (call.method) {
                "onBackButtonPressedEvent" -> {
                    val event = call.arguments
                    print(event)
                    val jsonObject = JSONObject(event.toString())
                    val name = jsonObject.getString("name")
                    val data = jsonObject.getString("data")

                    println("Event Name: $name")
                    println("Event data: $data")

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else -> {
                    result.notImplemented()
                }
            }
        }

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "flourish").invokeMethod("initialize", yourArguments)

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