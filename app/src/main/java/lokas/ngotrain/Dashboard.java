package lokas.ngotrain;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.lokas.ngotrain.R;

/**
 * Created by Bala on 18-08-2016.
 */
public class Dashboard extends AppCompatActivity {

    private static final String TAG = "MyFirebaseMsgService";
    RemoteMessage remoteMessage;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        //onMessageReceived(remoteMessage);
        Fragment fragment = null;
        String notice = "";
        String notice1 = "";
        String uploaddPrd = "";
        String bidsdetails = "";
        String someData ="";
        try {
            Bundle b = getIntent().getExtras();
            notice = b.getString("cuname");
            //Log.d("deto111o",bidsdetails);
            //Log.d("detoo",notice1);
            //Log.d("det",notice);
           // Log.d("det1",uploaddPrd);
            //Log.d("det2",bidsdetails);
              if (notice != null) {
                  Log.d("myStuff", notice);
                    ///// DISPLAY NOTIFICATION IN AN ALERT DIAGLOG all good!
                  Intent intentn = new Intent(this, Dash.class);
                  intentn.putExtra("FRAG1", "req");
                  intentn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  startActivity(intentn);

                }
        } catch (Exception ex) {
            Log.i("myStuff err1", ex.getMessage());
        }
        try {
            Bundle b1 = getIntent().getExtras();
            uploaddPrd = b1.getString("message");
            //Log.d("deto111o",bidsdetails);
            //Log.d("detoo",notice1);

            if (uploaddPrd != null) {
                Log.d("myStuff1", uploaddPrd);
                ///// DISPLAY NOTIFICATION IN AN ALERT DIAGLOG all good!
                Intent intentu = new Intent(this, Dash.class);
                intentu.putExtra("FRAG", "main");
                intentu.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentu);

            }

        } catch (Exception ex) {
            Log.i("myStuff err2", ex.getMessage());
        }
        try {
            Bundle b2 = getIntent().getExtras();
            bidsdetails = b2.getString("bidsdet");
            //Log.d("deto111o",bidsdetails);
            //Log.d("detoo",notice1);

            if (bidsdetails != null) {
                Log.d("myStuff2", bidsdetails);
                ///// DISPLAY NOTIFICATION IN AN ALERT DIAGLOG all good!
                //Toast.makeText(Dash.this, notice+"notii", Toast.LENGTH_SHORT).show();
                //navigationView.getMenu().performIdentifierAction(R.id.navigation_item_bids, 0);
                //displayView(5);
                Intent intent = new Intent(this, Dash.class);
                intent.putExtra("FRAG2", "bid");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                /*fragment = new ProductBids();
                if (fragment != null) {

                    Bundle args = new Bundle();
                    fragment.setArguments(args);
                    FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                    t.replace(R.id.frame_container, fragment);
                    t.commit();

                }*/
            }

        } catch (Exception ex) {
            Log.i("myStuff err3", ex.getMessage());
        }
    }
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
        //Calling method to generate notification
        //sendNotification(remoteMessage.getNotification().getBody());

    }




}
