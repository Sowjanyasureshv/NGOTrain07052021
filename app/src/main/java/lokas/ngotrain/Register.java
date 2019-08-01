package lokas.ngotrain;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lokas.ngotrain.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Bala on 08-08-2016.
 */
public class Register extends Activity implements AdapterView.OnItemSelectedListener {
    DataHelper1 dh;
    DataAccess da;
    // GPSTracker class
    GPSTracker gps;

    SessionManager manager;
    private EditText name;
    private EditText email;
    private EditText phone;
    private EditText pwd,cpwd;
    private EditText urlc;
    //private Spinner role;
    private Button btnSignup,btnFb;
    static final Integer LOCATION = 0x1;

    MyAsyncTaskL  MY = null;

    DataBaseHelper myDbHelper;
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

    private static final String TAG = Register.class.getSimpleName();

    ArrayList<String> reg_name = new ArrayList<>();
    ArrayList<String> reg_email = new ArrayList<>();
    ArrayList<String> reg_phone = new ArrayList<>();
    ArrayList<String> reg_pwd = new ArrayList<>();

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String urlregex = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        manager = new SessionManager();
        String status=manager.getPreferences(Register.this,"tkID");
        Log.d("status", status);
        final String custkid= status;
        // Toast.makeText(getApplicationContext(),custkid, Toast.LENGTH_LONG).show();

        name = (EditText) findViewById(R.id.reg_name);
        email = (EditText) findViewById(R.id.reg_email);
        phone = (EditText) findViewById(R.id.reg_pone);
        pwd = (EditText) findViewById(R.id.reg_pwd);
        cpwd = (EditText) findViewById(R.id.reg_cpwd);
        urlc = (EditText) findViewById(R.id.reg_curl);
        //role = (Spinner) findViewById(R.id.reg_role);


        btnSignup = (Button) findViewById(R.id.signup);
        //btnFb = (Button) findViewById(R.id.fbbtn);

        dh = new DataHelper1(getApplicationContext());

        try {
            dh.createDataBase();
        } catch (IOException ioe) {
            Log.e("Error in Cre Database ", ioe.getMessage());
            throw new Error("Unable to create database");
        }

        try {
            dh.openDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        da = new DataAccess();

        if (!isNetworkAvailable()){
            //Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Register.this);
            alertDialogBuilder.setMessage("Check Your Internet Connection");

            alertDialogBuilder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    //Toast.makeText(MainActivity.this,"You clicked yes button",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            });

            alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
        // gps.showSettingsAlert();
        gps = new GPSTracker(Register.this);

        if(!gps.canGetLocation()){
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
        //askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int hasLocationPermission = getApplicationContext().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
            Log.d(TAG, "location permission: " + hasLocationPermission); // 0

            if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Register.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
            hasLocationPermission = Register.this.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
            Log.d(TAG, "location permission1: " + hasLocationPermission); // still 0
            if(gps.canGetLocation()){
                double latitude = 0;
                double longitude = 0;
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

                // \n is for new line
                // Toast.makeText(getApplicationContext(), "Your Location is1 - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            }else{
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }
        }



        /*// Spinner click listener
        role.setOnItemSelectedListener(Register.this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Select a Role");
        categories.add("NGO");
        categories.add("Non-NGO");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        role.setAdapter(dataAdapter);*/

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String rname = name.getText().toString().trim().toLowerCase();
                String remail = email.getText().toString().trim().toLowerCase();
                String rpone = phone.getText().toString().trim().toLowerCase();
                String rpwd = pwd.getText().toString().trim().toLowerCase();
                String rcpwd = cpwd.getText().toString().trim().toLowerCase();
                String rcurl = urlc.getText().toString();
                //String rrole = role.getSelectedItem().toString();
                String rrole = "NGO";

                // Toast.makeText(getApplicationContext(),rrole,Toast.LENGTH_LONG).show();
                //ask(view);
                if(!gps.canGetLocation()){
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();

                }else if(rname.length()==0){
                    name.setError("First name is required");
                    name.requestFocus();
                }else if(remail.length()==0){
                    email.setError("Email id is required");
                    email.requestFocus();
                }else if (!remail.matches(emailPattern))
                {
                    //Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
                    // or
                    email.setError("Valid Email id is required");
                    email.requestFocus();
                }else if(rpone.length()==0){
                    phone.setError("Phone Number is required");
                    phone.requestFocus();
                }else if(rpwd.length()==0){
                    pwd.setError("Password is required");
                    pwd.requestFocus();
                }else if(!rpwd.equals(rcpwd)){
                    cpwd.setError("Password Not matched");
                    cpwd.requestFocus();
                }else if((rpwd.length())<8 ) {
                    pwd.setError("Password should be atleast of 8 charactors");
                    pwd.requestFocus();
                }else if(rcurl.length()==0){
                    urlc.setError("Enter valid Website Address ");
                    urlc.requestFocus();
                }else if (!Patterns.WEB_URL.matcher(rcurl).matches())
                {
                    //Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
                    // or || !Patterns.WEB_URL.matcher(rcurl).matches()
                    urlc.setError("Enter valid Website Address");
                    urlc.requestFocus();
                }
                /* else if(rrole.trim().equals("Select a Role")){
                    Toast.makeText(getApplicationContext(),"Please Select a role",Toast.LENGTH_LONG).show();
                    role.requestFocus();
            }*/else{
                    double latitude = 0;
                    double longitude = 0;
                    // create class object
                    //gps = new GPSTracker(Register.this);

                    // check if GPS enabled
                    if(gps.canGetLocation()){

                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();

                        // \n is for new line
                        // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    }else{
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        gps.showSettingsAlert();
                    }
                    //String lattt = String.valueOf(latitude);


                   new Signup(rname,remail,rpone,rpwd,latitude,longitude,rrole,custkid,rcurl).execute();

                    Log.d(TAG, "onClick: "+rname+ " "+remail+ " "+rpone+ " "+rpwd+ " "+latitude+ " "+longitude+ " "+rrole+ " "+custkid+ " "+rcurl);
                }
                                /*if(rcpwd.length()==0){
                    cpwd.setError("Confirm Password is required");
                    cpwd.requestFocus();
                }*/

                //new Signup(rname,remail,rpone,rpwd).execute();
            }
        });

       /* btnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fbint = new Intent(Register.this,FBIntegration.class);
                startActivity(fbint);
            }
        });*/
    }

    private String hashMapToUrl(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        // Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    private class Signup extends AsyncTask<Void,Void,String> {

        private ProgressDialog progressDialog = new ProgressDialog(Register.this);
        InputStream inputStream = null;
        String result = "";
        String result1 = "";
        String Resp="";
        String mess="";
        String regName,regEmail,regPhone,regPwd,regrole,tkId,regCurl;
        double lati,longi;


        public Signup(String rname, String remail, String rpone, String rpwd, double latitude, double longitude, String rrole, String tkid, String rcurl) {
            this.regName = rname;
            this.regEmail = remail;
            this.regPhone = rpone;
            this.regPwd = rpwd;
            this.lati = latitude;
            this.longi = longitude;
            this.regrole = rrole;
            this.tkId = tkid;
            this.regCurl = rcurl;
        }


        protected void onPreExecute() {
            progressDialog.setMessage("Sending  the Request...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    Signup.this.cancel(true);
                }
            });
        }



        protected String doInBackground(Void... params) {

            String value1 = "";	 String encodedURL="";
            //String url_select = "http://182.18.160.121/IbharAppointment/api/APLOCAs";

            String url_select ="http://lokas.in/ngoapp/customer_insert.php";



            long timestamp = System.currentTimeMillis() / 1000L;

            String HMAC_SHA1_ALGORITHM = "HmacSHA1";
            String Unique= UUID.randomUUID().toString();
            String Appid="4d53bce03ec34c0a911182d4c228ee6c";
            String Method="POST";
            String Time=String.valueOf(timestamp);
            String key="A93reRTUJHsCuQSHR+L3GxqOJyDmQpCgps102ciuabc=";
            String format="UTF-8";


           /* String Count =dh.selectViewName("SELECT COUNT(*) FROM APRLSP WHERE RLAUDT='"+code.trim()+"'",null);

            if(Count==null)
            {
                Count="0";
            }
            if(Count.equals("0")) {
                Method = "POST";
                url_select ="http://182.18.160.121/EATNEARN/api/APRLSPs";
            }
            else
            {
                Method = "PUT";
                url_select ="http://182.18.160.121/EATNEARN/api/APRLSPs/"+code.trim();
            }
*/
            try {
                encodedURL = URLEncoder.encode(url_select, "UTF-8").toLowerCase();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            /*String NameValue[]=params[0].split(",");

            JSONObject jsonobj = new JSONObject();

            for(int b=0 ;b<NameValue.length;b++ ) {

                String Name = "", Value = "";

                String NamVal[] = NameValue[b].split("=");

                Name = NamVal[0];

                Value = NamVal[1];

                if (!Value.equals("EMPTY"))
                //param.add(new BasicNameValuePair(Name, Value));
                {
                    try {

                        //Name = Name.replace("RLAUDT","id");
                        jsonobj.put(Name, String.valueOf(Value));

                    } catch (JSONException e) {
                        String error = e.toString();
                        error = error.trim();


                    }
                }


            }*/
            String BodyEncrpt="";
            // BodyEncrpt=getMd5Hash(jsonobj.toString());
            String  stringToSign="";
            stringToSign=Appid+Method+encodedURL+Time+ Unique+BodyEncrpt;

            try {


                ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

                param.clear();

                /*if(Method.equals("PUT")) {

                    ArrayList<String> stringData = new ArrayList<String>();
                    DefaultHttpClient httpClient = new DefaultHttpClient();
                    ResponseHandler<String> resonseHandler = new BasicResponseHandler();
                    HttpPut httpPost = new HttpPut(url_select);
                    httpPost.setHeader("Content-Type", "application/json");
                    httpPost.setEntity(new ByteArrayEntity(jsonobj.toString().getBytes("UTF8")));
                    httpPost.setHeader("Authorization", "amx " + Appid + ":" + result1 + ":" + Unique + ":" + Time);
                    HttpResponse httpResponse = httpClient.execute(httpPost);
                    Resp = httpResponse.getStatusLine().toString();

                    mess="";

                    if(Resp.contains("204"))
                    {
                        mess=" Successfully";
                        Resp="";
                    }
                    else
                    {
                        mess=" Failure. Pls try again later.";
                        Resp="\nStaus Code: "+Resp;
                    }
                    Handler hand=new Handler(getActivity().getMainLooper());
                    hand.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            try {
                                Toast.makeText(getActivity(), "Updated " + mess + Resp, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {

                            }

                        }
                    });

                    HttpEntity httpEntity = httpResponse.getEntity();
                    //inputStream = httpEntity.getContent();
                }
                else
                {*/
                ArrayList<String> stringData = new ArrayList<String>();
                DefaultHttpClient httpClient = new DefaultHttpClient();
                ResponseHandler<String> resonseHandler = new BasicResponseHandler();
                HttpPost httpPost = new HttpPost(url_select);
                httpPost.setHeader("Content-Type", "application/json");
                //httpPost.setEntity(new ByteArrayEntity(jsonobj.toString().getBytes("UTF8")));
                httpPost.setHeader("Authorization", "amx " + Appid + ":" + result1 + ":" + Unique + ":" + Time);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                Resp = httpResponse.getStatusLine().toString();

                mess="";

                   /* if(Resp.contains("Cre"))
                    {
                        mess=" Successfully";
                        Resp="";
                    }
                    else
                    {
                        mess=" Failure. Pls try again later.";
                        Resp="\nStaus Code: "+Resp;
                    }
                    Handler hand=new Handler(getApplicationContext().getMainLooper());
                    hand.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            try {
                                Toast.makeText(getActivity(), "Added " + mess + Resp, Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {

                            }

                        }
                    });*/

                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();

                //}




            } catch (UnsupportedEncodingException e1) {
                Log.e("UnsupportedEncodin", e1.toString());
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

            HashMap<String, String> data = new HashMap<String,String>();
            data.put("rname",regName);
            data.put("remail",regEmail);
            data.put("rpone",regPhone);
            data.put("rpwd",regPwd);
            data.put("rlati",String.valueOf(lati));
            data.put("rlongi",String.valueOf(longi));
            data.put("rrole",regrole);
            data.put("rtkid",tkId);
            data.put("rcurl",regCurl);

            try{
                //convert this HashMap to encodedUrl to send to php file
                String dataToSend = hashMapToUrl(data);
                //make a Http request and send data to saveImage.php file
                String response = Request.post(url_select,dataToSend);

                //return the response
                return response;

            }catch (Exception e){
                e.printStackTrace();
                Log.e(TAG,"ERROR  "+e);
                return null;
            }



        }






        /*public  String getMd5Hash(String input) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] messageDigest = md.digest(input.getBytes());
                //BigInteger number = new BigInteger(1, messageDigest);
                String md5 = Base64.encodeToString(messageDigest, Base64.URL_SAFE | Base64.NO_WRAP).replace('-','+').replace('_', '/');

			/*	while (md5.length() < 32)
					md5 = "0" + md5;
                return md5;
            } catch (NoSuchAlgorithmException e) {
                Log.e("MD5", e.getLocalizedMessage());
                return null;
            }
        }


        public String getHmacMD5(String privateKey, String input) throws Exception{
            String algorithm = "HmacSHA256";
            String Ret="";
            byte[] keyBytes = Base64.decode(privateKey, Base64.NO_WRAP);
            Key key = new SecretKeySpec(keyBytes, 0, keyBytes.length, algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(key);

            try {
                byte[] bytes = mac.doFinal(input.getBytes("UTF-8"));
                Ret=Base64.encodeToString(bytes,Base64.URL_SAFE|Base64.NO_WRAP).replace('-','+').replace('_', '/');

            }
            catch(Exception e)
            {

            }
            return Ret;
        }*/





        protected void onPostExecute(String v) {


            super.onPostExecute(v);

            //Toast.makeText(getApplicationContext(),v,Toast.LENGTH_LONG).show();
            System.out.println("Value of json"+v+"yu");
            Log.d("jsonreturn",v);

            int jsonResult = returnParsedJsonObject(v);

            if(jsonResult == 0){
                //Toast.makeText(getApplicationContext(),v,Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),"Phone number or email already exist",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"Successfully Registered",Toast.LENGTH_LONG).show();
                MY= new MyAsyncTaskL();
                MY.execute();
                Intent loginIntent = new Intent(Register.this, MainActivity.class);
                startActivity(loginIntent);
            }
           /* String Query="";

            String Count =dh.selectViewName("SELECT COUNT(*) FROM APRLSP WHERE RLAUDT='" + code.trim() + "'", null);

            if(Count==null)
            {
                Count="0";
            }

            Age=Age.replace("EMPTY","");
            Bg=Bg.replace("EMPTY","");

            if (!Count.equals("0")) {

                Query = "Update APRLSP Set RLDOBH='"+DOB +"',  RLRNAM='" + Name
                        + "', RLRLSP='" + Rele + "',RLRAGE='" +Age.trim() + "',"
                        + " RLDNBY='" + usercode.trim() + "',RLDNDT='"
                        + currentDateandTime + "',RLRCST='N',RLLOCK='1' where  RLAUDT='" + code.trim() + "'";

                dh.ExecStatement(Query);


            } else {



                Query = "Insert into  APRLSP(RLAUDT,RLDOBH,RLRNAM,RLRLSP,RLRAGE,RLCRBY,RLCRDT,RLSYDT,RLDNBY,RLDNDT,RLRCST,RLLOCK) Values('"
                        + code.trim()
                        + "','"
                        + DOB.trim()
                        + "','"
                        + Name.trim()
                        + "','"
                        + Rele.trim()
                        + "','"
                        + Age.trim()
                        + "','"
                        + usercode.trim()
                        + "', '"
                        + currentDateandTime
                        + "','"
                        + currentDateandTime
                        + "','"
                        + usercode.trim()
                        + "','"
                        + currentDateandTime
                        + "','N','1')";
                dh.ExecStatement(Query);

            }


            if(screen.equals("H"))
            {

                Intent i = new Intent(getActivity(), Activity123.class);
                i.putExtra("UserCode", usercode);
                getActivity().startActivity(i);
                getActivity().finish();

            }
            else  if(screen.equals("F"))
            {

                Fragment detailFragment = new ListRelation();

                FragmentTransaction t = getActivity()
                        .getSupportFragmentManager().beginTransaction();
                // t.remove(new listpageFrag());
                Bundle args = new Bundle();

                args.putString("UserCode", usercode);


                detailFragment.setArguments(args);

                t.replace(R.id.frame_container, detailFragment);
                t.commit();

            }*/



            this.progressDialog.dismiss();

        }


    }

    private int returnParsedJsonObject(String result){

        JSONObject resultObject = null;

        int returnedResult = 0;

        try {

            resultObject = new JSONObject(result);

            returnedResult = resultObject.getInt("success");

            //Toast.makeText(getApplicationContext(),returnedResult,Toast.LENGTH_LONG).show();
        } catch (JSONException e) {

            e.printStackTrace();

        }
        Log.d("jsonreturn", String.valueOf(returnedResult));
        return returnedResult;

    }


    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null, otherwise check
        // if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }


    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(Register.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Register.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(Register.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(Register.this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(getApplicationContext(), "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            onRequestPermissionsResult(requestCode,new String[]{permission}, new int[]{1});
        }
    }

    public void ask(){




    }
    /*public void ask(View v){
        switch (v.getId()){
           case R.id.signup:
                askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION);
                break;

            default:
                break;
        }
    }*/
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("requestcode", String.valueOf(requestCode));
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {
                //camera
                //Location
                case 1:
                    gps.canGetLocation();
                    break;

            }
            //Toast.makeText(getContext(), (requestCode), Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }


    class MyAsyncTaskL extends AsyncTask<String,String,Void> {

        private ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
        InputStream inputStream = null;
        String result = "";
        String result1 = "";

        protected void onPreExecute() {
            progressDialog.setMessage("Refreshing  data...");
            //progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    MyAsyncTaskL.this.cancel(true);
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





                    String QUERY="";

                    dh.ExecStatement("DELETE FROM customer WHERE cus_id='" + CICUSID + "'");

                    QUERY="INSERT INTO customer (cus_id,cus_userId,cus_name,cus_email,cus_phone,cus_pwd,cus_cpwd,cus_role,crtd_date,modi_date,status,flag) " + "VALUES " +
                            "('"+CICUSID+"','"+CICUSUSERID+"','"+CICUSNAME+"','"+CICUSEMAIL+"','"+CICUSPHONE+"','"+CICUSPWD+"','"+CICUSCPWD+"','"+CICUSROLE+"','"+CICUSCRTDDATE+"','"+CICUSMODIIDATE+"','"+CICUSSTATUS+"','"+CICUSFLAG+"')";
                    dh.ExecStatement(QUERY);




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

                /*CUSTOMER_LIST = dh
                        .selectList(
                                "Select cus_id,cus_name,cus_email,cus_phone,crtd_date,modi_date,status,flag from customer WHERE RLCRBY='"
                                        + usercode + "' ORDER BY RLRNAM", null, 8);*/
                CUSTOMER_LIST = dh
                        .selectList(
                                "Select * from customer ORDER BY cus_id", null, 12);
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

                    /*String Date ="";
                    if(parser[7].trim().replace("null", "").length()==10)
                    {
                        Date=parser[7].trim().replace("null", "").substring(0, 4)+parser[7].trim().replace("null", "").substring(5, 7)+parser[7].trim().replace("null", "").substring(8, 10);
                    }

                    dateofbirth.add(da.getDateFormat(Date));*/

                }
            } catch (Exception e) {
                String error = e.toString().trim();

                dh.Toastinfo(getApplicationContext(), error);
            }


        }

    }



}
