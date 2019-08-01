package lokas.ngotrain;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lokas.ngotrain.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
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
import java.util.Iterator;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;


public class MainActivityD extends AppCompatActivity {

    DataHelper1 dh;
    DataAccess da;
    RecyclerView r2;
    MyAsyncTask1  MY = null;


    ArrayList<String> CUSTOMER_LIST = new ArrayList<String>();
    ArrayList<String> CusId = new ArrayList<String>();
    ArrayList<String> CusIds = new ArrayList<String>();
    ArrayList<String> Name = new ArrayList<String>();
    ArrayList<String> Email = new ArrayList<String>();
    ArrayList<String> Phone = new ArrayList<String>();
    ArrayList<String> CreatedDate = new ArrayList<String>();
    ArrayList<String> ModiDate = new ArrayList<String>();
    ArrayList<String> Status = new ArrayList<String>();
    ArrayList<String> Flag = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maind);

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


       // dh.ExecStatement("insert into customer(cus_name,cus_email,cus_phone,crtd_date,modi_date,status,flag) values('SETT','CODE','1',45,78,1,0)");

        r2 = (RecyclerView) findViewById(R.id.listView2);

        MY= new MyAsyncTask1();
        MY.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }


    class MyAsyncTask1 extends AsyncTask<String,String,Void> {

        private ProgressDialog progressDialog = new ProgressDialog(MainActivityD.this);
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



            url_select = "http://lokas.co.in/ngoapp/customer_get.php";





            try {
                encodedURL = URLEncoder.encode(url_select, "UTF-8").toLowerCase();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            long timestamp = System.currentTimeMillis() / 1000L;
            String Unique= UUID.randomUUID().toString();
            String Appid="4d53bce03ec34c0a911182d4c228ee6c";
            String Method="GET";
            String Time= String.valueOf(timestamp);
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


        public String getHmacMD5(String privateKey, String input) throws Exception {
            String algorithm = "HmacSHA256";
            String Ret="";
            byte[] keyBytes = Base64.decode(privateKey, Base64.NO_WRAP);
            Key key = new SecretKeySpec(keyBytes, 0, keyBytes.length, algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(key);



            try {


                byte[] bytes = mac.doFinal(input.getBytes("UTF-8"));

                Ret= Base64.encodeToString(bytes, Base64.URL_SAFE| Base64.NO_WRAP).replace('-','+').replace('_', '/');;

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
                    String CICUSNAME = jObject.getString("cus_name");
                    String CICUSEMAIL = jObject.getString("cus_email");
                    String CICUSPHONE = jObject.getString("cus_phone");
                    String CICUSCRTDDATE = jObject.getString("crtd_date");
                    String CICUSMODIIDATE = jObject.getString("modi_date");
                    String CICUSSTATUS = jObject.getString("status");
                    String CICUSFLAG = jObject.getString("flag");





                    String QUERY="";

                    dh.ExecStatement("DELETE FROM customer WHERE cus_id='" + CICUSID + "'");

                    QUERY="INSERT INTO customer (cus_id,cus_name,cus_email,cus_phone,crtd_date,modi_date,status,flag) " + "VALUES ('"+CICUSID+"','"+CICUSNAME+"','"+CICUSEMAIL+"','"+CICUSPHONE+"','"+CICUSCRTDDATE+"','"+CICUSMODIIDATE+"','"+CICUSSTATUS+"','"+CICUSFLAG+"')";
                    dh.ExecStatement(QUERY);




                   /* dh.ExecStatement("DELETE FROM EEREST WHERE RECODE='" + SRECODE + "'");

                    QUERY="INSERT INTO EEREST (RECODE,REREID,RENAME,READD1,READD2,READD3,RELDMK,RECITY,RESTAT,RESUBR,REPHN1,REPHN2,REPHN3,REWEBS,REMAIL,REPNAM,REPPHN,REPMAI,REMEMS,RELONG,RELATI,REPICT,REOFID,REOFD1,REOFD2,REOFD3,REOFIM,REOFVD,REOFBO,REOFST,REOFRE,RECRBY,RECRDT,REDNBY,REDNDT,RESYDT,RERCST,RELOCK) " + "VALUES ('"+SRECODE+"','"+SREREID +"','"+SRENAME+"','"+SREADD1+"','"+SREADD2+"','"+SREADD3+"','"+SRELDMK+"','"+SRECITY+"','"+SRESTAT+"','"+SRESUBR+"','"+SREPHN1+"','"+SREPHN2+"','"+SREPHN3+"','"+SREWEBS+"','"+SREMAIL+"','"+SREPNAM+"','"+SREPPHN+"','"+SREPMAI+"','"+SREMEMS+"','"+SRELONG+"','"+SRELATI+"','"+SREPICT+"','"+SREOFID+"','"+SREOFD1+"','"+SREOFD2+"','"+SREOFD3+"','"+SREOFIM+"','"+SREOFVD+"','"+SREOFBO+"','"+SREOFST+"','"+SREOFRE+"','"+SRECRBY+"','"+SRECRDT+"','"+SREDNBY+"','"+SREDNDT+"','"+SRESYDT+"','"+SRERCST+"','"+SRELOCK+"')";

                    dh.ExecStatement(QUERY);*/



                } // End Loop

            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            } // catch (JSONException e)

            r2.setAdapter(new Myadapter(getApplicationContext()));
            r2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            r2.setHasFixedSize(true);



          //  r1.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
              // r2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
           // r1.setItemAnimator(new FlipInBottomXAnimator());

          //  r2.setHasFixedSize(true);
          //  Myadapter adap1 = new Myadapter(getApplicationContext());
          //  AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adap);
          //  ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
         //   r2.setAdapter(adap1);
            //r1.setItemAnimator(new DefaultItemAnimator());


            try {
                CUSTOMER_LIST.clear();
                CusId.clear();
                CusIds.clear();
                Name.clear();
                Email.clear();
                Phone.clear();
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
                                "Select * from customer ORDER BY cus_id", null, 8);
                for (Iterator<String> i = CUSTOMER_LIST.iterator(); i.hasNext();) {
                    String rowValue = (String) i.next();
                    String[] parser = rowValue.split("%");
                    CusId.add(parser[0].trim().replace("null", ""));
                    CusIds.add(parser[0].trim().replace("null", ""));
                    // AUDIT2.add(parser[1].trim().replace("null", ""));
                    Name.add(parser[1].trim().replace("null", ""));
                    Email.add(parser[2].trim().replace("null", ""));
                    Phone.add(parser[3].trim().replace("null", ""));
                    CreatedDate.add(parser[4].trim().replace("null", ""));
                    ModiDate.add(parser[5].trim().replace("null", ""));
                    Status.add(parser[6].trim().replace("null", ""));
                    Flag.add(parser[6].trim().replace("null", ""));

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


    public class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder> implements View.OnClickListener {

        Context context;

        public Myadapter(Context context) {

            this.context = context;
        }

        @Override
        public void onClick(View v) {
            int pos = getItemCount();

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_list,parent, false);
            ViewHolder VH1 = new ViewHolder(itemview);
            return VH1;
        }

        public void onBindViewHolder(ViewHolder holder, int position) {

           /* String rlsp=dh.selectViewName("SELECT RENAME FROM APRELA  WHERE RECODE='" + RelationShip.get(position) + "'", null);
            if(rlsp==null)
            {
                rlsp="";
            }

            String BG=dh.selectViewName("SELECT BGNAME FROM APBLGP  WHERE BGCODE='" + BloodGroup.get(position) + "'", null);
            if(BG==null)
            {
                BG="";
            }*/

            holder.Name.setAllCaps(true);
            holder.Name.setText( Name.get(position));
            holder.Email.setText(Email.get(position));
           // holder.relation.setAllCaps(true);
            holder.CusId.setText(CusId.get(position));
           // holder.bg.setText( BG);
            //holder.DOB.setText("Date of Birth : " + dateofbirth.get(position));

            Log.d("Ids", String.valueOf(CusId));
            if(Email.get(position).trim().equals(""))
            {
                holder.emlay.setVisibility(View.GONE);
            }
            else
            {
                holder.emlay.setVisibility(View.VISIBLE);
            }

            /*if(BloodGroup.get(position).trim().equals(""))
            {
                holder.lbg.setVisibility(View.GONE);
            }
            else
            {
                holder.lbg.setVisibility(View.VISIBLE);
            }

            if(Next.get(position).equals(""))
            {
                holder.next.setText("");
            }
            else
            {
                holder.next.setText("Next : " + Next.get(position));
            }
            if(Last.get(position).equals(""))
            {
                holder.last.setText("");
            }
            else
            {
                holder.last.setText("Last : " + Last.get(position));
            }*/


        }

        public int getItemCount() {
            return  Name.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder  {
            TextView Name, Email, CusId, last, next, bg,DOB;
            LinearLayout lage,emlay;


            public ViewHolder(final View View1) {
                super(View1);
                Name = (TextView) View1.findViewById(R.id.cusName);
                Email =  (TextView) View1.findViewById(R.id.cusEmail);

                CusId = (TextView) View1
                        .findViewById(R.id.cusId);

                /*age = (TextView) View1.findViewById(R.id.txtAge);

                bg = (TextView) View1
                        .findViewById(R.id.txtBloodGroup);

                last = (TextView) View1.findViewById(R.id.txtLast);

                next = (TextView) View1.findViewById(R.id.txtNext);
                lage = (LinearLayout) View1.findViewById(R.id.linage);*/
                emlay = (LinearLayout) View1.findViewById(R.id.emailLayout);

                View1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                       //int position = getAdapterPosition();
                        //Log.d("position", String.valueOf(position));
                        //Log.d("cusIDDD", String.valueOf(CusId));
                        //Log.d("view", String.valueOf(View1));
                        //Toast.makeText(getApplicationContext(),pos,Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), String.valueOf(CusIds.get(getPosition())), Toast.LENGTH_LONG).show();
                        Intent edit = new Intent(getApplicationContext(),CustomerEdit.class);
                        edit.putExtra("CUSID", String.valueOf(CusIds.get(getPosition())));
                        Log.d("view11", String.valueOf(CusIds.get(getPosition())));

                        startActivity(edit);


                    }
                });


            }


        }


    }

}
