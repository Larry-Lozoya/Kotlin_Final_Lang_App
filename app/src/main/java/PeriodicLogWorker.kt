import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.Worker
import androidx.work.WorkerParameters

class PeriodicLogWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams){
    companion object{
        const val CHANNEL_ID = "periodic_notification_channel"
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        Log.d("PeriodicLogWorker", "Periodic Worker Is Running")

        sendNotification()
        return Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendNotification() {
        // From the slides i was getting an error about this: Unresolved reference: createNotificationChannel(channel)
        // Looked online for some help and found this on stack overflow where the person had that same issue and resolved it
        // Using the "as NotificationManager"
        // Why does this work I have no idea and the Page does not tell why it works
        // Source: https://stackoverflow.com/questions/48984785/unresolved-reference-createnotificationchannel?rq=3
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Periodic Notification Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        val notification: Notification = Notification.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("LEARN")
            .setContentText("You must get back on the app and keep learning you got this!!")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()
        notificationManager.notify(1, notification)
    }
}