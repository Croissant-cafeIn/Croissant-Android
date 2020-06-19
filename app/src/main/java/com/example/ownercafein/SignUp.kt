package com.example.ownercafein

import android.content.ContentValues
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.PersistableBundle
import android.renderscript.ScriptGroup
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.load.resource.bitmap.InputStreamBitmapImageDecoderResourceDecoder
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.sign_up_page.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.lang.IllegalStateException
import java.lang.StringBuilder
import java.net.HttpCookie
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.file.WatchEvent

class SignUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up_page)

        signUpButton.setOnClickListener {
            updateUserInfo()
            var intent = Intent(applicationContext, LoginPage::class.java)
            startActivity(intent)
        }

    }
    fun updateUserInfo() {
        var client = OkHttpClient()

        var jsonInput = JSONObject()

        try{
            jsonInput.put("email",  id.text.toString())
            jsonInput.put("password",  password.text.toString())
        } catch (e: JSONException){
            e.printStackTrace()
        }


        var rb = RequestBody.create(
            MediaType.parse("application/json; charse=UTF-8"),
            jsonInput.toString()
        )

        var request = Request.Builder().url(LoginPage().getServer() + "client/android/new").post(rb).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("TEST", "ERROR Message : " + e.message);
            }

            override fun onResponse(call: Call, response: Response) {
                var responseData = response.body().toString()
                Log.d("TEST", "responseDatae : " + responseData)

            }
        })

    }


}