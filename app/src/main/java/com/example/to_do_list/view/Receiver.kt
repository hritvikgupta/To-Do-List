package com.example.to_do_list.view

import android.app.*
import android.app.PendingIntent.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDeepLinkBuilder
import com.example.to_do_list.MainActivity
import com.example.to_do_list.R


class Receiver:BroadcastReceiver(){
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    var builder:MutableLiveData<Notification.Builder>?=null
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onReceive(p0: Context?, p1: Intent?) {
        if (p1?.action == "MyBroadcastReceiverAction") {
            //val taskInfo = p1.getSerializableExtra("task_info") as? TaskInfo
            val taskName = p1.getStringExtra("TaskName")
            Toast.makeText(p0, "Received", Toast.LENGTH_SHORT).show()
            Log.d("ALARM", "RECEIVED")

            val i = Intent(p0,MainActivity::class.java)
            p1!!.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            //val pendingIntent = PendingIntent.getActivity(p0,0,i,0)
            val pendingIntent = NavDeepLinkBuilder(p0!!)
                .setComponentName(MainActivity::class.java)
                .setGraph(R.navigation.navigation)
                .setDestination(R.id.itemFragment)
                .createPendingIntent()

            val builder = NotificationCompat.Builder(p0!!,"foxandroid")
                .setSmallIcon(R.drawable.reminder)
                .setContentTitle("Task Reminder")
                .setContentText("Please Complete your task ${taskName}")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)

            val notificationManager = NotificationManagerCompat.from(p0)
            notificationManager.notify(123,builder.build())



        }



    }
    /*  val resultIntent = Intent(p0, MainActivity::class.java)
// Create the TaskStackBuilder
            val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(p0).run {
                // Add the intent, which inflates the back stack
                addNextIntentWithParentStack(resultIntent)
                // Get the PendingIntent containing the entire back stack
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }
            val builder = p0?.let {
                NotificationCompat.Builder(it,  "AlarmId").apply {
                    setContentIntent(resultPendingIntent)
                    setSmallIcon(R.drawable.reminder)

                }
            }
            with(p0?.let { NotificationManagerCompat.from(it) }) {
                this?.notify(0, builder?.build()!!)
            }*/

}


