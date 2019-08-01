package lokas.ngotrain;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lokas.ngotrain.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountEdit extends Fragment {

    SessionManager manager;
    String myJSON;

    private static final String TAG_RESULTS="result";

    JSONArray peoples = null;

    DataBaseHelper myDbHelper;
    EditText edName,edEmail,edPhone;
    Button edSubmit,edCancel;
    private static final String TAG = UploadProducts.class.getSimpleName();

    public AccountEdit() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account_edit, container, false);

        (getActivity()).setTitle("Edit Account");
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myDbHelper = new DataBaseHelper(getActivity());

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

        edName = (EditText) view.findViewById(R.id.ereg_name);
        edEmail = (EditText) view.findViewById(R.id.ereg_email);
        edPhone = (EditText) view.findViewById(R.id.ereg_pone);
        edSubmit = (Button) view.findViewById(R.id.update);
        edCancel = (Button) view.findViewById(R.id.cancel);



        manager = new SessionManager();

        String result=manager.getPreferences(getActivity(),"cusID");
        final String cusUID= result.replaceAll("[^a-zA-Z0-9]+","");
       // Toast.makeText(getActivity(), cusUID, Toast.LENGTH_LONG).show();
        //get data of customer
        getCusData(cusUID);

        edSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String rEDname = edName.getText().toString().trim().toLowerCase();
                String rEDemail = edEmail.getText().toString();
                String rEDpone = edPhone.getText().toString();

               // Toast.makeText(getActivity(),rEDname,Toast.LENGTH_LONG).show();
                new updCusData(cusUID,rEDname,rEDemail,rEDpone).execute();
            }
        });

        edCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inn = new Intent(getActivity(),Dash.class);
                startActivity(inn);
            }
        });

        return view;
    }


    protected void showCusData(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            System.out.println(peoples);
            //Toast.makeText(Products.this, (CharSequence) peoples, Toast.LENGTH_SHORT).show();


            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString("cus_id");
                String cuid = c.getString("cus_userId");
                String cname = c.getString("cus_name");
                String cemail = c.getString("cus_email");
                String cphone = c.getString("cus_phone");

                System.out.println(cname);
                edName.setText(cname);
                edEmail.setText(cemail);
                edPhone.setText(cphone);
                /*HashMap<String,String> persons = new HashMap<String,String>();

                persons.put(TAG_ID,id);
                persons.put(TAG_NAME,name);
                persons.put(TAG_ADD,address);

                personList.add(persons);
                Intent mapInt = new Intent(Products.this,MapShown.class);
                mapInt.putExtra("latitude",lati);
                mapInt.putExtra("longitude",longi);
                startActivity(mapInt);*/
            }

            /*ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, personList, R.layout.list_item,
                    new String[]{TAG_ID,TAG_NAME,TAG_ADD},
                    new int[]{R.id.id, R.id.name, R.id.address}
            );

            list.setAdapter(adapter);*/

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void getCusData(final String cusUID){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            private ProgressDialog progressDialog = new ProgressDialog(getActivity());

            protected void onPreExecute() {
                progressDialog.setMessage("Processing.....");
                progressDialog.show();
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface arg0) {
                        progressDialog.cancel();
                    }
                });

                //PB.setVisibility(View.VISIBLE);
            }
            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://lokas.in/ngoapp/customer_edit.php/?id="+cusUID);

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
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                myJSON=result;
                showCusData();
                this.progressDialog.dismiss();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }



    private class updCusData extends AsyncTask<Void,Void,String> {

        private ProgressDialog progressDialog = new ProgressDialog(getActivity());
        InputStream inputStream = null;
        String result = "";
        String result1 = "";
        String Resp="";
        String mess="";
        String eName,eEmail,ePhone,cuid;

        public updCusData(String cuids, String rename, String remail, String repone) {
            this.cuid = cuids;
            this.eName = rename;
            this.eEmail = remail;
            this.ePhone = repone;
        }


        protected void onPreExecute() {
            progressDialog.setMessage("Sending  the Request...");
            progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    updCusData.this.cancel(true);
                }
            });
        }



        protected String doInBackground(Void... params) {

            String value1 = "";	 String encodedURL="";
            //String url_select = "http://182.18.160.121/IbharAppointment/api/APLOCAs";

            String url_select ="http://lokas.in/ngoapp/customer_update.php";



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

            System.out.println(eName+"ooo");
            //Toast.makeText(getContext(),eName,Toast.LENGTH_LONG).show();

            HashMap<String, String> data = new HashMap<String,String>();
            data.put("cuid",cuid);
            data.put("ename",eName);
            data.put("eemail",eEmail);
            data.put("epone",ePhone);

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





        protected void onPostExecute(String results) {
            String Query="";

            super.onPostExecute(results);
            //date format
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            System.out.println(dateFormat.format(date));
            //Toast.makeText(getApplicationContext(),v,Toast.LENGTH_LONG).show();
            System.out.println("Value of json"+results+"yu");
            Log.d("jsonreturn",results);
            String res = results.trim();
            if(res.equals("1")){
                Toast.makeText(getActivity(),"Success",Toast.LENGTH_LONG).show();
                Query = "Update customer Set cus_name='"+eName +"',  cus_email='" + eEmail
                       + "', cus_phone='" + ePhone + "',modi_date='" +dateFormat.format(date) + "',"
                       + " status='1',flag='1' where cus_userId='" +cuid+ "'";
                System.out.println("query"+Query);
                myDbHelper.ExecStatement(Query);
            }else {
                Toast.makeText(getActivity(),"err",Toast.LENGTH_LONG).show();
            }


            this.progressDialog.dismiss();

        }


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



}
