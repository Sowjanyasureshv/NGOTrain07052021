package lokas.ngotrain;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lokas.ngotrain.R;

/**
 * Created by Bala on 03-10-2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.d(TAG, "From1: " + remoteMessage.getData().get("cuname"));
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Log.d(TAG, "Notification Click Action: " + remoteMessage.getNotification().getClickAction());
        //put code here to navigate based on click_action

        String msg = remoteMessage.getData().get("message");
        String img = remoteMessage.getData().get("imageLink");

        String cusname = remoteMessage.getData().get("cuname");
        String prdname = remoteMessage.getData().get("prname");

        String bidsdet = remoteMessage.getData().get("bidsdet");
        String bidsdet1 = remoteMessage.getData().get("image1");
        //Log.d("c",cusname);
        /*if(cusname !=""){
            sendNotification1(cusname,prdname);
        }else{
            sendNotification(msg,img);
        }*/
        if(msg !=null){
            sendNotification(msg,img);
        }else if(cusname !=null) {
            sendNotification1(cusname,prdname);
        }else{
            sendNotification2(bidsdet,bidsdet1);
        }
        //Calling method to generate notification
        //sendNotification(remoteMessage.getNotification().getBody());

    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String msg, String messageBody) {
        //Log.d("pvale",msg);
        Intent intent = new Intent(this, Dash.class);
        intent.putExtra("FRAG", "main");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("New Product Available")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //int colr =0xff115556;
            int colr =getResources().getColor(R.color.colorPrimary);
            notificationBuilder.setColor(colr);
            notificationBuilder.setSmallIcon(R.drawable.pushicon);
        } else {
            int colr =getResources().getColor(R.color.colorPrimary);
            notificationBuilder.setColor(colr);
            notificationBuilder.setSmallIcon(R.drawable.pushicon);
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
    private void sendNotification1(String cusname, String prdname) {
        Intent intent = new Intent(this, Dash.class);
        intent.putExtra("FRAG1", "req");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("NGO Need your Product")
                .setContentText("Customer Name: "+cusname+"\n Product Name: "+prdname)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //int colr =0xff115556;
            int colr =getResources().getColor(R.color.colorPrimary);
            notificationBuilder.setColor(colr);
            notificationBuilder.setSmallIcon(R.drawable.pushicon);
        } else {
            notificationBuilder.setSmallIcon(R.drawable.pushicon);
        }


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendNotification2(String bidsdet, String bidsdet1) {
        Intent intent = new Intent(this, Dash.class);
        intent.putExtra("FRAG2", "bid");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle("NGO Product")
                .setContentText("Product Name: "+bidsdet)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //int colr =0xff115556;
            int colr =getResources().getColor(R.color.colorPrimary);
            notificationBuilder.setColor(colr);
            notificationBuilder.setSmallIcon(R.drawable.pushicon);
        } else {
            notificationBuilder.setSmallIcon(R.drawable.pushicon);
        }


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    private int getNotificationIcon() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            int color = 0x008000;
            //getApplicationContext().setColor(color);
            return R.mipmap.ic_launcher;

        } else {
            return R.mipmap.ic_launcher;
        }
    }
}
