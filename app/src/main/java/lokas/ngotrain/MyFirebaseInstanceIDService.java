package lokas.ngotrain;

import android.content.Context;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bala on 03-10-2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    SessionManager manager;

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);

        /*TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = telephonyManager.getDeviceId();

        Log.d(TAG, "Phone Number: " + mPhoneNumber);*/

    }

    private void sendRegistrationToServer(String refreshedToken) {
        //You can implement this method to store the token on your server
        //Not required for current project
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String tkid = params[0];
                //String paramnames = params[1];

                Log.w("GCMReg token passed", "token:" + tkid);
                //String tkid = paramIds;
                //String namess = paramnames;
                //Toast.makeText(getApplicationContext(), "hjkh", Toast.LENGTH_LONG).show();

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("tokenid",tkid));
                // nameValuePairs.add(new BasicNameValuePair("name",namess));

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://lokas.in/ngoapp/gcm_insert.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }

                return tkid;
            }

            protected void onPostExecute(String result){
                super.onPostExecute(result);
                manager = new SessionManager();
                //manager.setPreferences(GCMRegistrationIntentService.this, "tokenStauts", "1");
                manager.setPreferences(MyFirebaseInstanceIDService.this,"tkID",result);


                //String tokenStauts=manager.getPreferences(GCMRegistrationIntentService.this,"tokenStauts");
                //String tkID=manager.getPreferences(cc,"tkID");
                //Log.d("statusgdfgdf", tkID);
                //Toast.makeText(getApplicationContext(),"ok"+result, Toast.LENGTH_LONG).show();
                //TextView textViewResult = (TextView) findViewById(R.id.textViewResult);
                //textViewResult.setText("Inserted");
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(refreshedToken);
    }
}
