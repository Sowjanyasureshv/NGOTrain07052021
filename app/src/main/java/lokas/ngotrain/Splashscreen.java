package lokas.ngotrain;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.lokas.ngotrain.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Key;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Bala on 18-08-2016.
 */
public class Splashscreen extends Activity {
    private static final int REQUEST_READ_PHONE_STATE = 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;
    private static int SPLASH_TIME_OUT = 3000;
    private static boolean splashLoaded = false;
    ImageView imageView;
    SessionManager manager;



    MyAsyncTask1  MY = null;
    MyAsyncTask2  MYC = null;
    MyAsyncTask3  MYP = null;
    DataBaseHelper myDbHelper;
    private static final String TAG = Splashscreen.class.getSimpleName();

    ArrayList<String> CUSTOMER_LIST = new ArrayList<String>();
    ArrayList<String> CusId = new ArrayList<String>();
    ArrayList<String> CusIds = new ArrayList<String>();
    ArrayList<String> CusUserId = new ArrayList<String>();
    ArrayList<String> Name = new ArrayList<String>();
    ArrayList<String> Email = new ArrayList<String>();
    ArrayList<String> Phone = new ArrayList<String>();
    ArrayList<String> CusPwd = new ArrayList<String>();
    ArrayList<String> CusCPwd = new ArrayList<String>();
    ArrayList<String> CusRole = new ArrayList<String>();
    ArrayList<String> CreatedDate = new ArrayList<String>();
    ArrayList<String> ModiDate = new ArrayList<String>();
    ArrayList<String> Status = new ArrayList<String>();
    ArrayList<String> Flag = new ArrayList<String>();
    ArrayList<String> NGOLink = new ArrayList<String>();


    ArrayList<String> CAT_List = new ArrayList<String>();
    ArrayList<String> CatId = new ArrayList<String>();
    ArrayList<String> CAtids = new ArrayList<String>();
    ArrayList<String> Catname = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        manager = new SessionManager();


        myDbHelper = new DataBaseHelper(getApplicationContext());

        try {

            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            myDbHelper.openDataBase();

        }catch(SQLException sqle){

            throw sqle;

        }

        /*String BG1=myDbHelper.selectViewName("SELECT MAX(crtd_date) AS HighestPrice FROM customer", null);
        if(BG1==null)
        {
            BG1="";
            MY= new MyAsyncTask1();
            MY.execute();
        }else{
            Log.d("highdates1", String.valueOf(BG1));
            String output = String.valueOf(BG1).substring(0, 10);
            Log.d("highdates2", output);
            setCustomerData(output);
        }
        Log.d("highdates", String.valueOf(BG1));*/


        if(myDbHelper.CheckGenderTableEmpty()==true)
        {
            //Toast.makeText(getApplicationContext(), "is not empty",    Toast.LENGTH_SHORT).show();
        }
        else
        {
            //Toast.makeText(getApplicationContext(), "empty", Toast.LENGTH_SHORT).show();
        }

        MY= new MyAsyncTask1();
        MY.execute();
        MYC= new MyAsyncTask2();
        MYC.execute();
        MYP= new MyAsyncTask3();
        MYP.execute();
        /*imageView = (ImageView) findViewById(R.id.imageView1);
        imageView.getBackground().setAlpha(120);
        //Picasso.with(getApplicationContext()).load("http://lokas.co.in/gcm/img/loadgif.gif").placeholder(R.drawable.load).into(imageView);
        //Picasso.with(getApplicationContext()).load("http://lokas.co.in/gcm/img/loadgif.gif").into(imageView);
        Picasso.with( getApplicationContext() )
                .load("http://lokas.co.in/gcm/img/")
                .placeholder( R.layout.progress_animation )
                .into( imageView ); ;*/



        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                /*Intent i = new Intent(Splashscreen.this, MainActivity.class);
               // startActivity(i);
                String status=manager.getPreferences(Splashscreen.this,"status");
                Log.d("status",status);
                if (status.equals("1")){
                    Intent i=new Intent(Splashscreen.this,Dash.class);
                    startActivity(i);
                }else{
                    Intent i=new Intent(Splashscreen.this,MainActivity.class);
                    startActivity(i);
                }


                finish();
            }
        },SPLASH_TIME_OUT);
        splashLoaded = true;*/

        /****** Create Thread that will sleep for 3 seconds *************/
        Thread background = new Thread() {
            public void run() {

                try {
                    // Thread will sleep for 3 seconds
                    sleep(3*1000);

                    // After 5 seconds redirect to another intent
                    String status=manager.getPreferences(Splashscreen.this,"status");
                    Log.d("status",status);
                    if (status.equals("1")){
                        Intent i=new Intent(Splashscreen.this,Dash.class);
                        startActivity(i);
                    }else{
                        Intent i=new Intent(Splashscreen.this,MainActivity.class);
                        startActivity(i);
                    }


                    //Remove activity
                    finish();

                } catch (Exception e) {

                }
            }
        };

        // start thread
        background.start();

        //checkPermission();

        /*TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        String device_id = tm.getDeviceId();

        Log.d("pone",device_id);

        String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        Log.d("Android","Android ID : "+android_id);*/
        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int hasLocationPermission = getApplicationContext().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
            Log.d(TAG, "location permission: " + hasLocationPermission); // 0

            if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Splashscreen.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            hasLocationPermission = Splashscreen.this.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
            Log.d(TAG, "location permission1: " + hasLocationPermission); // still 0

        }*/


    }


    private void checkPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(Splashscreen.this,
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Splashscreen.this,
                    Manifest.permission.READ_PHONE_STATE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                //Toast.makeText(Splashscreen.this, "TEstA", Toast.LENGTH_SHORT).show();
            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(Splashscreen.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

                // MY_PERMISSIONS_REQUEST_READ_PHONE_STATE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                //Toast.makeText(Splashscreen.this, "TEstB", Toast.LENGTH_SHORT).show();
            }
            //Toast.makeText(Splashscreen.this, "TEstC", Toast.LENGTH_SHORT).show();
        }
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                    //String device_id = tm.getDeviceId();
                    //String sim_id = tm.getSimSerialNumber();
                    String poneNum= tm.getLine1Number();

                    //Log.d("pone",device_id);
                    Log.d("sim",poneNum);
                    Toast.makeText(Splashscreen.this, "TEst0", Toast.LENGTH_SHORT).show();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(Splashscreen.this, "TEst1", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }*/
    class MyAsyncTask1 extends AsyncTask<String,String,Void> {

        private ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
        InputStream inputStream = null;
        String result = "";
        String result1 = "";

        protected void onPreExecute() {
            progressDialog.setMessage("Refreshing  data...");
            //progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    MyAsyncTask1.this.cancel(true);
                }
            });

            //PB.setVisibility(View.VISIBLE);
        }


        @Override
        protected Void doInBackground(String... params) {

            String value1 = "";	 String encodedURL="";

            String url_select ="";



            url_select = "http://lokas.in/ngoapp/customer_get.php";





            try {
                encodedURL = URLEncoder.encode(url_select, "UTF-8").toLowerCase();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            long timestamp = System.currentTimeMillis() / 1000L;
            String Unique= UUID.randomUUID().toString();
            String Appid="4d53bce03ec34c0a911182d4c228ee6c";
            String Method="GET";
            String Time=String.valueOf(timestamp);
            String stringToSign=Appid+Method+encodedURL+Time+ Unique;
            String key="A93reRTUJHsCuQSHR+L3GxqOJyDmQpCgps102ciuabc=";
            String format="UTF-8";


            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            try {
                // Set up HTTP post
                try {

                    result1 = getHmacMD5(key, stringToSign);
                }
                catch(Exception e)
                {

                }

                DefaultHttpClient httpClient = new DefaultHttpClient();

                //HttpPost httpPost = new HttpPost(url_select);
                HttpGet httpget = new HttpGet(url_select);
                //httpget.setHeader("Authorization","amx "+Appid+":"+result1+":"+Unique+":"+Time);
                //httpPost.setEntity(new UrlEncodedFormEntity(param));
                httpget.setHeader("Authorization","amx "+Appid+":"+result1+":"+Unique+":"+Time);
                HttpResponse httpResponse = httpClient.execute(httpget);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();
            } catch (UnsupportedEncodingException e1) {
                Log.e("UnsupportedEncodingExc", e1.toString());
                e1.printStackTrace();
            } catch (ClientProtocolException e2) {
                Log.e("ClientProtocolException", e2.toString());
                e2.printStackTrace();
            } catch (IllegalStateException e3) {
                Log.e("IllegalStateException", e3.toString());
                e3.printStackTrace();
            } catch (IOException e4) {
                Log.e("IOException", e4.toString());
                e4.printStackTrace();
            }
            // Convert response to string using String Builder
            try {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sBuilder.append(line + "\n");
                }

                inputStream.close();
                result = sBuilder.toString();

            } catch (Exception e) {
                Log.e("StringBuildi & Buffer", "Error converting result " + e.toString());
            }


            try
            {

                if(result != null) {

                    String data = result;
                    Object json = new JSONTokener(data).nextValue();
                    if (json instanceof JSONObject)
                    {
                        Log.i("JSON", "is OBJECT");
                    }
                    //you have an object
                    else if (json instanceof JSONArray)
                    {
                        Log.i("JSON", "is Array");

                    }

                }

            }
            catch (Exception e)
            {
                Log.e("Finson obj or array", "Error  " + e.toString());
            }

            return null;

        } // protected Void doInBackground(String... params)


        public String getHmacMD5(String privateKey, String input) throws Exception{
            String algorithm = "HmacSHA256";
            String Ret="";
            byte[] keyBytes = Base64.decode(privateKey, Base64.NO_WRAP);
            Key key = new SecretKeySpec(keyBytes, 0, keyBytes.length, algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(key);



            try {


                byte[] bytes = mac.doFinal(input.getBytes("UTF-8"));

                Ret=Base64.encodeToString(bytes,Base64.URL_SAFE|Base64.NO_WRAP).replace('-','+').replace('_', '/');;

            }
            catch(Exception e)
            {

            }
            return Ret;
        }




        protected void onPostExecute(Void v) {
            //parse JSON data


            try {


                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject jObject = jArray.getJSONObject(i);

                    System.out.println(jObject);
                    Log.d("data", String.valueOf(jObject));

                    Iterator iterator = jObject.keys();

                    String CICUSID = jObject.getString("cus_id");
                    String CICUSUSERID = jObject.getString("cus_userId");
                    String CICUSNAME = jObject.getString("cus_name");
                    String CICUSEMAIL = jObject.getString("cus_email");
                    String CICUSPHONE = jObject.getString("cus_phone");
                    String CICUSPWD = jObject.getString("cus_pwd");
                    String CICUSCPWD = jObject.getString("cus_cpwd");
                    String CICUSROLE = jObject.getString("cus_role");
                    String CICUSCRTDDATE = jObject.getString("crtd_date");
                    String CICUSMODIIDATE = jObject.getString("modi_date");
                    String CICUSSTATUS = jObject.getString("status");
                    String CICUSFLAG = jObject.getString("flag");
                    String CICUSNGOLINK = jObject.getString("ngo_link");





                    String QUERY="";

                    myDbHelper.ExecStatement("DELETE FROM customer WHERE cus_id='" + CICUSID + "'");

                    QUERY="INSERT INTO customer (cus_id,cus_userId,cus_name,cus_email,cus_phone,cus_pwd,cus_cpwd,cus_role,crtd_date,modi_date,status,flag,ngo_link) " + "VALUES " +
                            "('"+CICUSID+"','"+CICUSUSERID+"','"+CICUSNAME+"','"+CICUSEMAIL+"','"+CICUSPHONE+"','"+CICUSPWD+"','"+CICUSCPWD+"','"+CICUSROLE+"','"+CICUSCRTDDATE+"','"+CICUSMODIIDATE+"','"+CICUSSTATUS+"','"+CICUSFLAG+"','"+CICUSNGOLINK+"')";
                    myDbHelper.ExecStatement(QUERY);




                   /* dh.ExecStatement("DELETE FROM EEREST WHERE RECODE='" + SRECODE + "'");

                    QUERY="INSERT INTO EEREST (RECODE,REREID,RENAME,READD1,READD2,READD3,RELDMK,RECITY,RESTAT,RESUBR,REPHN1,REPHN2,REPHN3,REWEBS,REMAIL,REPNAM,REPPHN,REPMAI,REMEMS,RELONG,RELATI,REPICT,REOFID,REOFD1,REOFD2,REOFD3,REOFIM,REOFVD,REOFBO,REOFST,REOFRE,RECRBY,RECRDT,REDNBY,REDNDT,RESYDT,RERCST,RELOCK) " + "VALUES ('"+SRECODE+"','"+SREREID +"','"+SRENAME+"','"+SREADD1+"','"+SREADD2+"','"+SREADD3+"','"+SRELDMK+"','"+SRECITY+"','"+SRESTAT+"','"+SRESUBR+"','"+SREPHN1+"','"+SREPHN2+"','"+SREPHN3+"','"+SREWEBS+"','"+SREMAIL+"','"+SREPNAM+"','"+SREPPHN+"','"+SREPMAI+"','"+SREMEMS+"','"+SRELONG+"','"+SRELATI+"','"+SREPICT+"','"+SREOFID+"','"+SREOFD1+"','"+SREOFD2+"','"+SREOFD3+"','"+SREOFIM+"','"+SREOFVD+"','"+SREOFBO+"','"+SREOFST+"','"+SREOFRE+"','"+SRECRBY+"','"+SRECRDT+"','"+SREDNBY+"','"+SREDNDT+"','"+SRESYDT+"','"+SRERCST+"','"+SRELOCK+"')";

                    dh.ExecStatement(QUERY);*/



                } // End Loop

            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            } // catch (JSONException e)







            try {
                CUSTOMER_LIST.clear();
                CusId.clear();
                CusIds.clear();
                CusUserId.clear();
                Name.clear();
                Email.clear();
                Phone.clear();
                CusPwd.clear();
                CusCPwd.clear();
                CusRole.clear();
                CreatedDate.clear();
                ModiDate.clear();
                Status.clear();
                Flag.clear();
                NGOLink.clear();

                /*CUSTOMER_LIST = dh
                        .selectList(
                                "Select cus_id,cus_name,cus_email,cus_phone,crtd_date,modi_date,status,flag from customer WHERE RLCRBY='"
                                        + usercode + "' ORDER BY RLRNAM", null, 8);*/
                CUSTOMER_LIST = myDbHelper
                        .selectList(
                                "Select * from customer ORDER BY cus_id", null, 13);
                for (Iterator<String> i = CUSTOMER_LIST.iterator(); i.hasNext();) {
                    String rowValue = (String) i.next();
                    String[] parser = rowValue.split("%");
                    CusId.add(parser[0].trim().replace("null", ""));
                    CusIds.add(parser[0].trim().replace("null", ""));
                    // AUDIT2.add(parser[1].trim().replace("null", ""));
                    CusUserId.add(parser[1].trim().replace("null", ""));
                    Name.add(parser[2].trim().replace("null", ""));
                    Email.add(parser[3].trim().replace("null", ""));
                    Phone.add(parser[4].trim().replace("null", ""));
                    CusPwd.add(parser[5].trim().replace("null", ""));
                    CusCPwd.add(parser[6].trim().replace("null", ""));
                    CusRole.add(parser[7].trim().replace("null", ""));
                    CreatedDate.add(parser[8].trim().replace("null", ""));
                    ModiDate.add(parser[9].trim().replace("null", ""));
                    Status.add(parser[10].trim().replace("null", ""));
                    Flag.add(parser[11].trim().replace("null", ""));
                    NGOLink.add(parser[12].trim().replace("null", ""));

                    /*String Date ="";
                    if(parser[7].trim().replace("null", "").length()==10)
                    {
                        Date=parser[7].trim().replace("null", "").substring(0, 4)+parser[7].trim().replace("null", "").substring(5, 7)+parser[7].trim().replace("null", "").substring(8, 10);
                    }

                    dateofbirth.add(da.getDateFormat(Date));*/

                }
            } catch (Exception e) {
                String error = e.toString().trim();

                myDbHelper.Toastinfo(getApplicationContext(), error);
            }


        }




    }




    class MyAsyncTask2 extends AsyncTask<String,String,Void> {

        private ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
        InputStream inputStream = null;
        String result = "";
        String result1 = "";

        protected void onPreExecute() {
            progressDialog.setMessage("Refreshing  data...");
            //progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    MyAsyncTask2.this.cancel(true);
                }
            });

            //PB.setVisibility(View.VISIBLE);
        }


        @Override
        protected Void doInBackground(String... params) {

            String value1 = "";	 String encodedURL="";

            String url_select ="";



            url_select = "http://lokas.in/ngoapp/category_get.php";





            try {
                encodedURL = URLEncoder.encode(url_select, "UTF-8").toLowerCase();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            long timestamp = System.currentTimeMillis() / 1000L;
            String Unique= UUID.randomUUID().toString();
            String Appid="4d53bce03ec34c0a911182d4c228ee6c";
            String Method="GET";
            String Time=String.valueOf(timestamp);
            String stringToSign=Appid+Method+encodedURL+Time+ Unique;
            String key="A93reRTUJHsCuQSHR+L3GxqOJyDmQpCgps102ciuabc=";
            String format="UTF-8";


            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            try {
                // Set up HTTP post
                try {

                    result1 = getHmacMD5(key, stringToSign);
                }
                catch(Exception e)
                {

                }

                DefaultHttpClient httpClient = new DefaultHttpClient();

                //HttpPost httpPost = new HttpPost(url_select);
                HttpGet httpget = new HttpGet(url_select);
                //httpget.setHeader("Authorization","amx "+Appid+":"+result1+":"+Unique+":"+Time);
                //httpPost.setEntity(new UrlEncodedFormEntity(param));
                httpget.setHeader("Authorization","amx "+Appid+":"+result1+":"+Unique+":"+Time);
                HttpResponse httpResponse = httpClient.execute(httpget);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();
            } catch (UnsupportedEncodingException e1) {
                Log.e("UnsupportedEncodingExc", e1.toString());
                e1.printStackTrace();
            } catch (ClientProtocolException e2) {
                Log.e("ClientProtocolException", e2.toString());
                e2.printStackTrace();
            } catch (IllegalStateException e3) {
                Log.e("IllegalStateException", e3.toString());
                e3.printStackTrace();
            } catch (IOException e4) {
                Log.e("IOException", e4.toString());
                e4.printStackTrace();
            }
            // Convert response to string using String Builder
            try {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sBuilder.append(line + "\n");
                }

                inputStream.close();
                result = sBuilder.toString();

            } catch (Exception e) {
                Log.e("StringBuildi & Buffer", "Error converting result " + e.toString());
            }


            try
            {

                if(result != null) {

                    String data = result;
                    Object json = new JSONTokener(data).nextValue();
                    if (json instanceof JSONObject)
                    {
                        Log.i("JSON", "is OBJECT");
                    }
                    //you have an object
                    else if (json instanceof JSONArray)
                    {
                        Log.i("JSON", "is Array");

                    }

                }

            }
            catch (Exception e)
            {
                Log.e("Finson obj or array", "Error  " + e.toString());
            }

            return null;

        } // protected Void doInBackground(String... params)


        public String getHmacMD5(String privateKey, String input) throws Exception{
            String algorithm = "HmacSHA256";
            String Ret="";
            byte[] keyBytes = Base64.decode(privateKey, Base64.NO_WRAP);
            Key key = new SecretKeySpec(keyBytes, 0, keyBytes.length, algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(key);



            try {


                byte[] bytes = mac.doFinal(input.getBytes("UTF-8"));

                Ret=Base64.encodeToString(bytes,Base64.URL_SAFE|Base64.NO_WRAP).replace('-','+').replace('_', '/');;

            }
            catch(Exception e)
            {

            }
            return Ret;
        }




        protected void onPostExecute(Void v) {
            //parse JSON data


            try {


                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject jObject = jArray.getJSONObject(i);

                    System.out.println(jObject);
                    Log.d("data", String.valueOf(jObject));

                    Iterator iterator = jObject.keys();

                    String CATID = jObject.getString("cat_id");
                    String CATNAME = jObject.getString("cat_name");
                    String CICUSCRTDDATE = jObject.getString("created_date");
                    String CICUSMODIIDATE = jObject.getString("modified_date");
                    String CICUSSTATUS = jObject.getString("status");
                    String CICUSFLAG = jObject.getString("flag");





                    String QUERY="";

                    myDbHelper.ExecStatement("DELETE FROM category WHERE cat_id='" + CATID + "'");

                    QUERY="INSERT INTO category (cat_id,cat_name,created_date,modified_date,status,flag) " + "VALUES " +
                            "('"+CATID+"','"+CATNAME+"','"+CICUSCRTDDATE+"','"+CICUSMODIIDATE+"','"+CICUSSTATUS+"','"+CICUSFLAG+"')";
                    myDbHelper.ExecStatement(QUERY);




                   /* dh.ExecStatement("DELETE FROM EEREST WHERE RECODE='" + SRECODE + "'");

                    QUERY="INSERT INTO EEREST (RECODE,REREID,RENAME,READD1,READD2,READD3,RELDMK,RECITY,RESTAT,RESUBR,REPHN1,REPHN2,REPHN3,REWEBS,REMAIL,REPNAM,REPPHN,REPMAI,REMEMS,RELONG,RELATI,REPICT,REOFID,REOFD1,REOFD2,REOFD3,REOFIM,REOFVD,REOFBO,REOFST,REOFRE,RECRBY,RECRDT,REDNBY,REDNDT,RESYDT,RERCST,RELOCK) " + "VALUES ('"+SRECODE+"','"+SREREID +"','"+SRENAME+"','"+SREADD1+"','"+SREADD2+"','"+SREADD3+"','"+SRELDMK+"','"+SRECITY+"','"+SRESTAT+"','"+SRESUBR+"','"+SREPHN1+"','"+SREPHN2+"','"+SREPHN3+"','"+SREWEBS+"','"+SREMAIL+"','"+SREPNAM+"','"+SREPPHN+"','"+SREPMAI+"','"+SREMEMS+"','"+SRELONG+"','"+SRELATI+"','"+SREPICT+"','"+SREOFID+"','"+SREOFD1+"','"+SREOFD2+"','"+SREOFD3+"','"+SREOFIM+"','"+SREOFVD+"','"+SREOFBO+"','"+SREOFST+"','"+SREOFRE+"','"+SRECRBY+"','"+SRECRDT+"','"+SREDNBY+"','"+SREDNDT+"','"+SRESYDT+"','"+SRERCST+"','"+SRELOCK+"')";

                    dh.ExecStatement(QUERY);*/



                } // End Loop

            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            } // catch (JSONException e)




        }




    }


    class MyAsyncTask3 extends AsyncTask<String,String,Void> {

        private ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
        InputStream inputStream = null;
        String result = "";
        String result1 = "";

        protected void onPreExecute() {
            progressDialog.setMessage("Refreshing  data...");
            //progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    MyAsyncTask3.this.cancel(true);
                }
            });

            //PB.setVisibility(View.VISIBLE);
        }


        @Override
        protected Void doInBackground(String... params) {

            String value1 = "";	 String encodedURL="";

            String url_select ="";



            url_select = "http://lokas.in/ngoapp/product_get.php";




            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            try {
                // Set up HTTP post
                try {

                    //result1 = getHmacMD5(key, stringToSign);
                }
                catch(Exception e)
                {

                }

                DefaultHttpClient httpClient = new DefaultHttpClient();

                //HttpPost httpPost = new HttpPost(url_select);
                HttpGet httpget = new HttpGet(url_select);
                //httpget.setHeader("Authorization","amx "+Appid+":"+result1+":"+Unique+":"+Time);
                //httpPost.setEntity(new UrlEncodedFormEntity(param));
                //httpget.setHeader("Authorization","amx "+Appid+":"+result1+":"+Unique+":"+Time);
                HttpResponse httpResponse = httpClient.execute(httpget);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();
            } catch (UnsupportedEncodingException e1) {
                Log.e("UnsupportedEncodingExc", e1.toString());
                e1.printStackTrace();
            } catch (ClientProtocolException e2) {
                Log.e("ClientProtocolException", e2.toString());
                e2.printStackTrace();
            } catch (IllegalStateException e3) {
                Log.e("IllegalStateException", e3.toString());
                e3.printStackTrace();
            } catch (IOException e4) {
                Log.e("IOException", e4.toString());
                e4.printStackTrace();
            }
            // Convert response to string using String Builder
            try {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sBuilder.append(line + "\n");
                }

                inputStream.close();
                result = sBuilder.toString();

            } catch (Exception e) {
                Log.e("StringBuildi & Buffer", "Error converting result " + e.toString());
            }


            try
            {

                if(result != null) {

                    String data = result;
                    Object json = new JSONTokener(data).nextValue();
                    if (json instanceof JSONObject)
                    {
                        Log.i("JSON", "is OBJECT");
                    }
                    //you have an object
                    else if (json instanceof JSONArray)
                    {
                        Log.i("JSON", "is Array");

                    }

                }

            }
            catch (Exception e)
            {
                Log.e("Finson obj or array", "Error  " + e.toString());
            }

            return null;

        } // protected Void doInBackground(String... params)





        protected void onPostExecute(Void v) {
            //parse JSON data


            try {


                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject jObject = jArray.getJSONObject(i);

                    System.out.println(jObject);
                    Log.d("data", String.valueOf(jObject));

                    Iterator iterator = jObject.keys();

                    String PRODID = jObject.getString("pro_id");
                    String PRODNAME = jObject.getString("pro_name");
                    String CATIDP = jObject.getString("cat_id");
                    String PCRTDDATE = jObject.getString("crdt");
                    String PMODIIDATE = jObject.getString("dndt");
                    String PSTATUS = jObject.getString("status");
                    String PFLAG = jObject.getString("flag");





                    String QUERY="";

                    myDbHelper.ExecStatement("DELETE FROM product WHERE pro_id='" + PRODID + "'");

                    QUERY="INSERT INTO product (pro_id,pro_name,cat_id,crdt,dndt,status,flag) " + "VALUES " +
                            "('"+PRODID+"','"+PRODNAME+"','"+CATIDP+"','"+PCRTDDATE+"','"+PMODIIDATE+"','"+PSTATUS+"','"+PFLAG+"')";
                    myDbHelper.ExecStatement(QUERY);




                   /* dh.ExecStatement("DELETE FROM EEREST WHERE RECODE='" + SRECODE + "'");

                    QUERY="INSERT INTO EEREST (RECODE,REREID,RENAME,READD1,READD2,READD3,RELDMK,RECITY,RESTAT,RESUBR,REPHN1,REPHN2,REPHN3,REWEBS,REMAIL,REPNAM,REPPHN,REPMAI,REMEMS,RELONG,RELATI,REPICT,REOFID,REOFD1,REOFD2,REOFD3,REOFIM,REOFVD,REOFBO,REOFST,REOFRE,RECRBY,RECRDT,REDNBY,REDNDT,RESYDT,RERCST,RELOCK) " + "VALUES ('"+SRECODE+"','"+SREREID +"','"+SRENAME+"','"+SREADD1+"','"+SREADD2+"','"+SREADD3+"','"+SRELDMK+"','"+SRECITY+"','"+SRESTAT+"','"+SRESUBR+"','"+SREPHN1+"','"+SREPHN2+"','"+SREPHN3+"','"+SREWEBS+"','"+SREMAIL+"','"+SREPNAM+"','"+SREPPHN+"','"+SREPMAI+"','"+SREMEMS+"','"+SRELONG+"','"+SRELATI+"','"+SREPICT+"','"+SREOFID+"','"+SREOFD1+"','"+SREOFD2+"','"+SREOFD3+"','"+SREOFIM+"','"+SREOFVD+"','"+SREOFBO+"','"+SREOFST+"','"+SREOFRE+"','"+SRECRBY+"','"+SRECRDT+"','"+SREDNBY+"','"+SREDNDT+"','"+SRESYDT+"','"+SRERCST+"','"+SRELOCK+"')";

                    dh.ExecStatement(QUERY);*/



                } // End Loop

            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            } // catch (JSONException e)




        }




    }

    public void setCustomerData(final String cusUID){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            private ProgressDialog progressDialog = new ProgressDialog(Splashscreen.this);
            protected void onPreExecute() {
                progressDialog.setMessage("Please Wait....");
                progressDialog.show();
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface arg0) {
                        GetDataJSON.this.cancel(true);
                    }
                });

                //PB.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://lokas.co.in/ngoapp/customer_get_date.php/?id="+cusUID);

                // Depends on your web service
                httppost.setHeader("Content-type", "application/json");

                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    // Oops
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }

                try {


                    JSONArray jArray = new JSONArray(result);
                    for (int i = 0; i < jArray.length(); i++) {

                        JSONObject jObject = jArray.getJSONObject(i);

                        System.out.println(jObject);
                        Log.d("data", String.valueOf(jObject));

                        Iterator iterator = jObject.keys();

                        String CICUSID = jObject.getString("cus_id");
                        String CICUSUSERID = jObject.getString("cus_userId");
                        String CICUSNAME = jObject.getString("cus_name");
                        String CICUSEMAIL = jObject.getString("cus_email");
                        String CICUSPHONE = jObject.getString("cus_phone");
                        String CICUSPWD = jObject.getString("cus_pwd");
                        String CICUSCPWD = jObject.getString("cus_cpwd");
                        String CICUSROLE = jObject.getString("cus_role");
                        String CICUSCRTDDATE = jObject.getString("crtd_date");
                        String CICUSMODIIDATE = jObject.getString("modi_date");
                        String CICUSSTATUS = jObject.getString("status");
                        String CICUSFLAG = jObject.getString("flag");





                        String QUERY="";

                        myDbHelper.ExecStatement("DELETE FROM customer WHERE cus_id='" + CICUSID + "'");

                        QUERY="INSERT INTO customer (cus_id,cus_userId,cus_name,cus_email,cus_phone,cus_pwd,cus_cpwd,cus_role,crtd_date,modi_date,status,flag) " + "VALUES " +
                                "('"+CICUSID+"','"+CICUSUSERID+"','"+CICUSNAME+"','"+CICUSEMAIL+"','"+CICUSPHONE+"','"+CICUSPWD+"','"+CICUSCPWD+"','"+CICUSROLE+"','"+CICUSCRTDDATE+"','"+CICUSMODIIDATE+"','"+CICUSSTATUS+"','"+CICUSFLAG+"')";
                        myDbHelper.ExecStatement(QUERY);

                    } // End Loop

                } catch (JSONException e) {
                    Log.e("JSONException", "Error: " + e.toString());
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                //myJSON=result;
                //showList();
                this.progressDialog.dismiss();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }


}
