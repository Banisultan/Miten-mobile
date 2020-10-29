package id.bnn.convey.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import id.bnn.convey.R;
import id.bnn.convey.VariableText;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "TAGGAR";
    VariableText var = new VariableText();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            try{
                JSONObject datajson = new JSONObject(remoteMessage.getData());
                String tipe = datajson.getString("tipe");

                if(tipe.equals("force_logout")){
                    SharedPreferences datauser = getSharedPreferences("dataakun", MODE_PRIVATE);
                    String ID = datauser.getString("idakun", "");
                    String TOKEN = datauser.getString("token", "");
                    String TOKEN_NOTIF = datajson.getString("token");

                    if(TOKEN.equals(TOKEN_NOTIF)){
                        var.unsubscribe(ID);
                        datauser.edit().clear().commit();
                        System.exit(0);
                    }
                }else
                if(tipe.equals("default")){
                    String body = datajson.getString("message");
                    String title = datajson.getString("title");

                    if(!body.equals("") && !title.equals("")){
                        sendNotification(body, title, new Intent());
                    }
                }


            }catch (Exception e){
                Log.d(TAG, "data aneh : "+e);
            }

            if (/* Check if data needs to be processed by long running job */ true) {
                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

    private void scheduleJob() {
    }

    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    private void sendRegistrationToServer(String token) {

    }

    private void sendNotification(String messageBody, String title, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "HAHAHAH";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.img_logo)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_ALL);

//        String CHANNEL_ID = "1";
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.img_logo_v3)
//                .setContentTitle(title)
//                .setContentText(messageBody)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//        notificationManager.notify(0, builder.build());

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }
}
