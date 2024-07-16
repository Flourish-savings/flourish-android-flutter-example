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
            jsonObject.put("partnerId", "2476f19f-bf9c-4b52-9c51-a5cbf56fc89e")
            jsonObject.put("secret", "4dc94959-1f7b-434b-bc98-c9cd3c9e3807")
            jsonObject.put("env", "staging")
            jsonObject.put("language", "en")
            jsonObject.put("customerCode", "referral_1234")
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