package lokas.ngotrain;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lokas.ngotrain.R;
import com.squareup.picasso.Picasso;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Bala on 21-09-2016.
 */
public class ProductDisplay extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbarLayout = null;
    FloatingActionButton floatingActionButton,floatneed,floatzoom;
    private ImageView pImg;
    String imgg;
    private TextView ptit,pdesc,mapv;
    private TextView cate,pro,pdate;
    private CoordinatorLayout coordinatorLayout;
    private Button btneed;
    private LinearLayout    DrawerLinear;

    SessionManager manager;
    DataBaseHelper myDbHelper;
    String myJSON;
    private static final String TAG_RESULTS="result";
    private static final String TAG = ProductDisplay.class.getSimpleName();

    JSONArray peoples = null;

    ArrayList<HashMap<String, String>> personList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_display);


        DrawerLinear = (LinearLayout) findViewById(R.id.listl);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.cdlay);
        ptit = (TextView) findViewById(R.id.prtitle);
        pdesc = (TextView) findViewById(R.id.prdesc);
        //cate = (TextView) findViewById(R.id.prcat);
        //pro = (TextView) findViewById(R.id.prprod);
        pdate = (TextView) findViewById(R.id.prdate);
       // mapv = (TextView) findViewById(R.id.map);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.fabpd);
        floatneed=(FloatingActionButton)findViewById(R.id.fabpd1);
        floatzoom=(FloatingActionButton)findViewById(R.id.fabpd2);
        //btneed = (Button) findViewById(R.id.needbtn);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarpd);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //session id

        manager = new SessionManager();

        String result=manager.getPreferences(getApplicationContext(),"cusID");
        final String cusUID= result.replaceAll("[^a-zA-Z0-9]+","");
       // Toast.makeText(getApplicationContext(), cusUID, Toast.LENGTH_LONG).show();
        ProductDisplay.this.setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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

        Intent intent = getIntent();

        final String Pimg = intent.getStringExtra("PIMG");
        String Ptitle = intent.getStringExtra("PTITLE");
        String Pdesc = intent.getStringExtra("PDESC");
        final String PListid = intent.getStringExtra("PROLISTID");
        final String Catid = intent.getStringExtra("CATID");
        final String Prodid = intent.getStringExtra("PRODID");
        final String Cid = intent.getStringExtra("CUSID");
        final String Pdate = intent.getStringExtra("PROdate");
        String Pselected = intent.getStringExtra("PRODSELECT");
        final String CSessid = cusUID;
        System.out.println("img product" +Pimg);
        System.out.println("CSessid" +CSessid);

        String PSS=myDbHelper.selectViewName("SELECT pro_bid_id FROM product_bids  WHERE cus_id='"+cusUID+"' and pro_user_id='"+PListid+"'", null);
        if(PSS==null)
        {
            PSS="";

        }
        //floatneed.setVisibility(View.GONE);
        if(PSS.equals("")){
            floatneed.setVisibility(View.VISIBLE);
        }else{
            floatneed.setVisibility(View.GONE);
        }

       // Toast.makeText(getApplicationContext(),"dataselected"+PSS,Toast.LENGTH_LONG).show();
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbarpd);
        collapsingToolbarLayout.setTitle(Ptitle);

        String CA=myDbHelper.selectViewName("SELECT cat_name FROM category  WHERE cat_id='"+Catid+"'", null);
        if(CA==null)
        {
            CA="";
        }
        String PR=myDbHelper.selectViewName("SELECT pro_name FROM product  WHERE pro_id='"+Prodid+"'", null);
        if(PR==null)
        {
            PR="";
        }
        Log.d("prrf111", String.valueOf(PR));
        //DateFormat originalFormat = new SimpleDateFormat(Pdate, Locale.ENGLISH);
       // DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        //Date date = new Date();
       // System.out.println(originalFormat.format(date));

        String date_s = Pdate;

        // *** note that it's "yyyy-MM-dd hh:mm:ss" not "yyyy-mm-dd hh:mm:ss"
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = dt.parse(date_s);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // *** same for the format String below
        SimpleDateFormat dt1 = new SimpleDateFormat("MMMM dd, yyyy");
        System.out.println(dt1.format(date));
        String prDate = dt1.format(date);

       // Toast.makeText(getApplicationContext(), Cid, Toast.LENGTH_SHORT).show();
        ptit.setText(Ptitle);
        pdesc.setText(Pdesc);
        //cate.setText(CA);
        //pro.setText(PR);
        pdate.setText(prDate);

       /* mapv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(Cid);
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "You clicked Map", Snackbar.LENGTH_LONG)
                        .setAction("NO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "You clicked Map", Snackbar.LENGTH_SHORT);
                                snackbar1.show();
                            }
                        });

                snackbar.show();
            }
        });*/



        System.out.println(Pimg);
       // Toast.makeText(getApplicationContext(),"getCusID"+Cid,Toast.LENGTH_LONG).show();;
       // new DownloadImageTask((ImageView) findViewById(R.id.profile_id)).execute("http://lokas.co.in/ngoapp/productImage/"+Pimg);
        Picasso.with(getApplicationContext())
                .load("http://lokas.in/ngoapp/productImage/"+Pimg)
                //.resize(500,500)
                .placeholder(R.drawable.placeholderone)
                .error(R.drawable.placeholderone)
                .into((ImageView) findViewById(R.id.profile_id));
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                        R.drawable.profile_pic);

        //final int mutedColor = R.color.primary;

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
              //  Palette.Swatch vibrant =
              //          palette.getVibrantSwatch();

               // collapsingToolbarLayout.setBackgroundColor(mutedColor);
                collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(getResources().getColor(R.color.accent)));
                collapsingToolbarLayout.setStatusBarScrimColor(palette.getMutedColor(getResources().getColor(R.color.accent)));
            }
        });
        //dynamicToolbarColor();

        toolbarTextAppernce();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(Cid);
                Snackbar.make(v, "You clicked on the map", Snackbar.LENGTH_SHORT).show();
            }
        });
        floatneed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Queryupd="";
                new createbids(Cid,PListid,Catid,Prodid,CSessid).execute();
                /*Queryupd = "UPDATE product_user SET bids_opt='bidsopened.png' Where pro_user_id='" +PListid+ "'";
                System.out.println("query"+Queryupd);
                myDbHelper.ExecStatement(Queryupd);*/
                Snackbar.make(v, "You want this product", Snackbar.LENGTH_SHORT).show();
            }
        });
        floatzoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductDisplay.this,FullScreenViewActivity.class);
                i.putExtra("position", ""+1);
                i.putExtra("imgname", String.valueOf(Pimg));
                System.out.println("imagenamebefore" +pImg);
                startActivity(i);
                Snackbar.make(v, "You click the image zoom", Snackbar.LENGTH_SHORT).show();
            }
        });
        /*btneed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new createbids(Cid,PListid,Catid,Prodid,CSessid).execute();
            }
        });*/

    }


    /*private void dynamicToolbarColor() {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.profile_pic);
        //Toast.makeText(getApplicationContext(), (CharSequence) pImg,Toast.LENGTH_LONG).show();
        ///System.out.println(pImg);
       // Bitmap bitmap = ((BitmapDrawable) pImg.getDrawable()).getBitmap();
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(R.attr.colorPrimary));
                collapsingToolbarLayout.setStatusBarScrimColor(palette.getMutedColor(R.attr.colorPrimaryDark));
            }
        });
    }*/


    private void toolbarTextAppernce() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
    }

    /*public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            Toast.makeText(getApplicationContext(),"Touch",Toast.LENGTH_LONG).show();
            return super.dispatchTouchEvent(motionEvent);
        } catch (NullPointerException e) {
            return false;
        }
    }*/

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }


            return mIcon11;

        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);

        }
    }

    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            System.out.println(peoples);
            //Toast.makeText(Products.this, (CharSequence) peoples, Toast.LENGTH_SHORT).show();


            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString("cus_id");
                String lati = c.getString("latitude");
                String longi = c.getString("longitude");

                /*HashMap<String,String> persons = new HashMap<String,String>();

                persons.put(TAG_ID,id);
                persons.put(TAG_NAME,name);
                persons.put(TAG_ADD,address);

                personList.add(persons);*/
                Intent mapInt = new Intent(ProductDisplay.this,MapShown.class);
                mapInt.putExtra("latitude",lati);
                mapInt.putExtra("longitude",longi);
                startActivity(mapInt);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(final String cusUID){
        class GetDataJSON extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://lokas.in/ngoapp/product_location_get.php/?id="+cusUID);

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
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }


    private class createbids extends AsyncTask<Void,Void,String> {

        private ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
        InputStream inputStream = null;
        String result = "";
        String result1 = "";
        String Resp="";
        String mess="";
        String bCusid,bPlistid,bCatid,bProdid,cSessid;

        public createbids(String cusid, String plistid, String catid, String proid,String sessid) {
            this.bCusid = cusid;
            this.bPlistid = plistid;
            this.bCatid = catid;
            this.bProdid = proid;
            this.cSessid = sessid;
        }


        protected void onPreExecute() {
            progressDialog.setMessage("Sending  the Request...");
            //progressDialog.show();
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                public void onCancel(DialogInterface arg0) {
                    createbids.this.cancel(true);
                }
            });
        }



        protected String doInBackground(Void... params) {

            String value1 = "";	 String encodedURL="";
            //String url_select = "http://182.18.160.121/IbharAppointment/api/APLOCAs";

            String url_select ="http://lokas.in/ngoapp/product_bids_insert.php";



            long timestamp = System.currentTimeMillis() / 1000L;

            String HMAC_SHA1_ALGORITHM = "HmacSHA1";
            String Unique= UUID.randomUUID().toString();
            String Appid="4d53bce03ec34c0a911182d4c228ee6c";
            String Method="POST";
            String Time=String.valueOf(timestamp);
            String key="A93reRTUJHsCuQSHR+L3GxqOJyDmQpCgps102ciuabc=";
            String format="UTF-8";


            try {
                encodedURL = URLEncoder.encode(url_select, "UTF-8").toLowerCase();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            String BodyEncrpt="";
            // BodyEncrpt=getMd5Hash(jsonobj.toString());
            String  stringToSign="";
            stringToSign=Appid+Method+encodedURL+Time+ Unique+BodyEncrpt;

            try {


                ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

                param.clear();

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

            //System.out.println(eName+"ooo");
            //Toast.makeText(getContext(),eName,Toast.LENGTH_LONG).show();

            HashMap<String, String> data = new HashMap<String,String>();
            data.put("cuid",bCusid);
            data.put("bprouserid",bPlistid);
            data.put("bcatid",bCatid);
            data.put("bprodid",bProdid);
            data.put("SESSid",cSessid);

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
                Toast.makeText(getApplicationContext(),"Your Request Sent Successfully",Toast.LENGTH_LONG).show();
                /*Query = "Update customer Set cus_name='"+eName +"',  cus_email='" + eEmail
                        + "', cus_phone='" + ePhone + "',modi_date='" +dateFormat.format(date) + "',"
                        + " status='1',flag='1' where cus_userId='" +cuid+ "'";
                System.out.println("query"+Query);
                myDbHelper.ExecStatement(Query);*/
                //btneed.setEnabled(false);
                floatneed.setVisibility(View.GONE);
            }else if(res.equals("2")) {
                Toast.makeText(getApplicationContext(),"You Have already selected the product",Toast.LENGTH_LONG).show();
                //btneed.setEnabled(false);
                floatneed.setVisibility(View.GONE);
            }else{
                Toast.makeText(getApplicationContext(),"err",Toast.LENGTH_LONG).show();
            }


            //this.progressDialog.dismiss();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       // switch (item.getItemId()) {
        //    case android.R.id.home:
               //// Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();
       //         break;
       // }
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
        //return super.onOptionsItemSelected(item);
    }



}
