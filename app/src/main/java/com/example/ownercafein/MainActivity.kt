package com.example.ownercafein

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.app.NotificationCompat

class MainActivity : AppCompatActivity() {
    val SPLASH_TIME_OUT: Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (intent.hasExtra("go"))
            notification()

        Handler().postDelayed({
            startActivity((Intent(this, LoginPage::class.java)))
            finish()
        }, SPLASH_TIME_OUT)

    }

    fun notification() {
        var notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var intent = Intent(this, MainActivity::class.java)
        var pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        var builder = NotificationCompat.Builder(this, "1001")
            .setContentTitle("CafeIn Owner")
            .setContentText("주문이 들어왔습니다.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setSmallIcon(R.drawable.drink)

            var channel =
                NotificationChannel("1001", "notification", NotificationManager.IMPORTANCE_HIGH)
            channel.description = "오레오 이상"
            notificationManager.createNotificationChannel(channel)
        } else builder.setSmallIcon(R.drawable.drink)

        assert(notificationManager != null)
        notificationManager.notify(1001, builder.build())
    }

}
