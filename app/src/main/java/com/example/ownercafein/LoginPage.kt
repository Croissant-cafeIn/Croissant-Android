package com.example.ownercafein

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.login_page.*
import kotlinx.android.synthetic.main.login_page.id
import kotlinx.android.synthetic.main.login_page.password
import kotlinx.android.synthetic.main.sign_up_page.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class LoginPage : AppCompatActivity() {
    lateinit var responseJSONObject: JSONObject
    lateinit var ownerId: String

    companion object{

        val servre = "http://10.0.2.2:9090/"
        //val servre = "http://192.168.35.122:9090/"
    }

    fun getServer() = servre

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)


        login.setOnClickListener {
            doLogin()

        }

    }

    fun doLogin() {
        var client = OkHttpClient()
        var jsonInput = JSONObject()


        try {
            jsonInput.put("email", id.text.toString())
            jsonInput.put("password", password.text.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        var rb = RequestBody.create(
            MediaType.parse("application/json; charse=UTF-8"),
            jsonInput.toString()
        )

        var request = Request.Builder().url(getServer() + "owner/login").post(rb).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("TEST", "ERROR Message : " + e.message)
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    responseJSONObject = JSONObject(response.body()?.string().toString())

                    if (responseJSONObject.has("message") && (responseJSONObject.getString("message") == "owner 이름을 찾을 수 없습니다.")) {
                        var handler = Handler(Looper.getMainLooper())
                        handler.post(Runnable {
                            kotlin.run {
                                Toast.makeText(
                                    this@LoginPage,
                                    "존재하지 않는 아이디입니다.",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        })
                    } else if (responseJSONObject.has("message") && (responseJSONObject.getString("message") == "비밀번호가 맞지 않습니다.")) {
                        var handler = Handler(Looper.getMainLooper())
                        handler.post(Runnable {
                            kotlin.run {
                                Toast.makeText(
                                    this@LoginPage,
                                    "잘못된 비밀번호입니다.",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        })
                    } else {
                        ownerId = responseJSONObject.getString("id")
                        Log.d("Login Success", ownerId)

                        val intent = Intent(applicationContext, Owners::class.java)
                        intent.putExtra("id", ownerId)
                        startActivity(intent)
                    }


                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        })
    }


}