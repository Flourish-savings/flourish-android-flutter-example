package com.flourish.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.flourish.app.databinding.ActivityMainBinding
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var flutterEngine : FlutterEngine
    private lateinit var flutterEngineCampaign : FlutterEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        flutterEngine = FlutterEngine(this)
        flutterEngineCampaign = FlutterEngine(this)

        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )

        flutterEngineCampaign.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint.createDefault()
        )

        FlutterEngineCache
            .getInstance()
            .put("flourish", flutterEngine)

        FlutterEngineCache
            .getInstance()
            .put("flourishCampaign", flutterEngineCampaign)


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

        val jsonObjectCampaign = JSONObject()
        try {
            jsonObjectCampaign.put("partnerId", "YOUR-PARTNER-ID-HERE")
            jsonObjectCampaign.put("secret", "YOUR-SECRET-KEY-HERE")
            jsonObjectCampaign.put("env", "[staging/production]")
            jsonObjectCampaign.put("language", "[en/es/pt]")
            jsonObjectCampaign.put("customerCode", "YOUR-CUSTOMER-CODE-HERE")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val argumentsCampaign = jsonObjectCampaign.toString()

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
                "onHomeBackButtonPressedEvent" -> {
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

        MethodChannel(flutterEngineCampaign.dartExecutor.binaryMessenger, "flourish").setMethodCallHandler { call, result ->
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
                "onHomeBackButtonPressedEvent" -> {
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
        MethodChannel(flutterEngineCampaign.dartExecutor.binaryMessenger, "flourish").invokeMethod("initialize", argumentsCampaign)

        val referralButton: Button = findViewById(R.id.referralButton)
        val onBoardingButton: Button = findViewById(R.id.onBoardingButton)
        val campaignButton: Button = findViewById(R.id.campaignButton)

        referralButton.setOnClickListener {
            startActivity(
                FlutterActivity
                    .withCachedEngine("flourish")
                    .build(this)
            )
        }

        onBoardingButton.setOnClickListener {
            val intent = Intent(this, OnBoardingActivity::class.java)
            startActivity(intent)
        }

        campaignButton.setOnClickListener {
            startActivity(
                FlutterActivity
                    .withCachedEngine("flourishCampaign")
                    .build(this)
            )
        }
    }
}