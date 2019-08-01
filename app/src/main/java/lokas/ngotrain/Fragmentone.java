package lokas.ngotrain;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.lokas.ngotrain.R;
import com.squareup.picasso.Picasso;

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


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragmentone extends Fragment {

    RecyclerView r2;
    MyAsyncTask2  MY = null;
    DataBaseHelper myDbHelper;
    SessionManager manager;

    ArrayList<String> PRO_BIDS_LIST = new ArrayList<String>();
    ArrayList<String> PRO_BID_ID = new ArrayList<String>();
    ArrayList<String> PROCUSID = new ArrayList<String>();
    ArrayList<String> PROUSERID = new ArrayList<String>();

    ArrayList<String> PRODUCT_LIST = new ArrayList<String>();
    ArrayList<String> ProdLISTID = new ArrayList<String>();
    ArrayList<String> CusIds = new ArrayList<String>();
    ArrayList<String> ProdId = new ArrayList<String>();
    ArrayList<String> CatID = new ArrayList<String>();
    ArrayList<String> ProdTitle = new ArrayList<String>();
    ArrayList<String> ProdDesc = new ArrayList<String>();
    ArrayList<String> ProdImg = new ArrayList<String>();
    ArrayList<String> CreatedDate = new ArrayList<String>();
    ArrayList<String> ModiDate = new ArrayList<String>();
    ArrayList<String> Status = new ArrayList<String>();
    ArrayList<String> Flag = new ArrayList<String>();
    ArrayList<String> BidsOpt = new ArrayList<String>();


    public Fragmentone() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragmentone, container, false);
        ((Dash) getActivity()).setTitle("Product lists");


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



        r2 = (RecyclerView) view.findViewById(R.id.prodView1);

        if (!isNetworkAvailable()){
            //Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
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
                    getActivity().finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        String MD = myDbHelper.selectViewName("SELECT  MAX (dndt) AS 'Max Date' FROM product_user", null);
        if (MD == null) {
            MD = "";
        }
        Log.d("Lastdate",MD);

        MY= new MyAsyncTask2();
        MY.execute();
       /* if(MD.equals("")){
            MY= new MyAsyncTask2();
            MY.execute();
        }else {
            Log.d("Lastdate1",MD);
        }*/



        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        r2.setItemAnimator(itemAnimator);
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        return view;
    }

    class MyAsyncTask2 extends AsyncTask<String,String,Void> {

        private ProgressDialog progressDialog = new ProgressDialog(getActivity());
        InputStream inputStream = null;
        String result = "";
        String result1 = "";

        protected void onPreExecute() {
            progressDialog.setMessage("Refreshing  data...");
            progressDialog.show();
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



            url_select = "http://lokas.in/ngoapp/product_user_get.php";





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

                    String PUID = jObject.getString("pro_user_id");
                    String CUSID = jObject.getString("cus_id");
                    String PID = jObject.getString("pro_id");
                    String CATID = jObject.getString("cat_id");
                    String PUTITLE = jObject.getString("pro_user_title");
                    String PUDESC = jObject.getString("pro_user_desc");
                    String PUIMG = jObject.getString("pro_user_img");
                    String PUCRTDDATE = jObject.getString("crdt");
                    String PUMODIDATE = jObject.getString("dndt");
                    String PUSTATUS = jObject.getString("status");
                    String PUFLAG = jObject.getString("flag");
                    String PUBIDS = jObject.getString("bids_opt");





                    String QUERY="";
                    //String QUERY1="";


                    myDbHelper.ExecStatement("DELETE FROM product_user WHERE pro_user_id='" + PUID + "'");

                    QUERY="INSERT INTO product_user(pro_user_id,cus_id,pro_id,cat_id,pro_user_title,pro_user_desc,pro_user_img,crdt,dndt,status,flag,bids_opt) " + "VALUES ('"+PUID+"','"+CUSID+"','"+PID+"','"+CATID+"','"+PUTITLE+"','"+PUDESC+"','"+PUIMG+"','"+PUCRTDDATE+"','"+PUMODIDATE+"','"+PUSTATUS+"','"+PUFLAG+"','"+PUBIDS+"')";

                    myDbHelper.ExecStatement(QUERY);


                    // myDbHelper.ExecStatement("DELETE FROM product_image WHERE pro_img_id='" + PIMID + "'");

                    // QUERY1="INSERT INTO product_image(pro_img_id,cus_id,pro_user_id,pro_img1,pro_img2,pro_img3,crtd_date,modi_date,status,flag) " + "VALUES ('"+PIMID+"','"+CUSID+"','"+PUSERID+"','"+PIMG1+"','"+PIMG2+"','"+PIMG3+"','"+PIMGCRTDDATE+"','"+PIMGMODIDATE+"','"+PIMGSTATUS+"','"+PIMGFLAG+"')";
                    //  myDbHelper.ExecStatement(QUERY1);




                   /* dh.ExecStatement("DELETE FROM EEREST WHERE RECODE='" + SRECODE + "'");

                    QUERY="INSERT INTO EEREST (RECODE,REREID,RENAME,READD1,READD2,READD3,RELDMK,RECITY,RESTAT,RESUBR,REPHN1,REPHN2,REPHN3,REWEBS,REMAIL,REPNAM,REPPHN,REPMAI,REMEMS,RELONG,RELATI,REPICT,REOFID,REOFD1,REOFD2,REOFD3,REOFIM,REOFVD,REOFBO,REOFST,REOFRE,RECRBY,RECRDT,REDNBY,REDNDT,RESYDT,RERCST,RELOCK) " + "VALUES ('"+SRECODE+"','"+SREREID +"','"+SRENAME+"','"+SREADD1+"','"+SREADD2+"','"+SREADD3+"','"+SRELDMK+"','"+SRECITY+"','"+SRESTAT+"','"+SRESUBR+"','"+SREPHN1+"','"+SREPHN2+"','"+SREPHN3+"','"+SREWEBS+"','"+SREMAIL+"','"+SREPNAM+"','"+SREPPHN+"','"+SREPMAI+"','"+SREMEMS+"','"+SRELONG+"','"+SRELATI+"','"+SREPICT+"','"+SREOFID+"','"+SREOFD1+"','"+SREOFD2+"','"+SREOFD3+"','"+SREOFIM+"','"+SREOFVD+"','"+SREOFBO+"','"+SREOFST+"','"+SREOFRE+"','"+SRECRBY+"','"+SRECRDT+"','"+SREDNBY+"','"+SREDNDT+"','"+SRESYDT+"','"+SRERCST+"','"+SRELOCK+"')";

                    dh.ExecStatement(QUERY);*/



                } // End Loop

            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            } // catch (JSONException e)


            //  r1.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
            r2.setLayoutManager(new LinearLayoutManager(getActivity()));
            // r1.setItemAnimator(new FlipInBottomXAnimator());
            r2.setHasFixedSize(true);
            Myadapter adap = new Myadapter(getActivity());
            //  AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adap);
            //  ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
            r2.setAdapter(adap);
            //r1.setItemAnimator(new DefaultItemAnimator());


            try {
                PRODUCT_LIST.clear();
                ProdLISTID.clear();
                CusIds.clear();
                ProdId.clear();
                CatID.clear();
                ProdTitle.clear();
                ProdDesc.clear();
                ProdImg.clear();
                CreatedDate.clear();
                ModiDate.clear();
                Status.clear();
                Flag.clear();
                BidsOpt.clear();

                /*CUSTOMER_LIST = dh
                        .selectList(
                                "Select cus_id,cus_name,cus_email,cus_phone,crtd_date,modi_date,status,flag from customer WHERE RLCRBY='"
                                        + usercode + "' ORDER BY RLRNAM", null, 8);*/
                PRODUCT_LIST = myDbHelper
                        .selectList(
                                "Select * from product_user where flag = 1 ORDER BY pro_user_id DESC", null, 12);
                for (Iterator<String> i = PRODUCT_LIST.iterator(); i.hasNext();) {
                    String rowValue = (String) i.next();
                    String[] parser = rowValue.split("%");
                    ProdLISTID.add(parser[0].trim().replace("null", ""));
                    CusIds.add(parser[1].trim().replace("null", ""));
                    ProdId.add(parser[2].trim().replace("null", ""));
                    // AUDIT2.add(parser[1].trim().replace("null", ""));
                    CatID.add(parser[3].trim().replace("null", ""));
                    ProdTitle.add(parser[4].trim().replace("null", ""));
                    ProdDesc.add(parser[5].trim().replace("null", ""));
                    ProdImg.add(parser[6].trim().replace("null", ""));
                    CreatedDate.add(parser[7].trim().replace("null", ""));
                    ModiDate.add(parser[8].trim().replace("null", ""));
                    Status.add(parser[9].trim().replace("null", ""));
                    Flag.add(parser[10].trim().replace("null", ""));
                    BidsOpt.add(parser[11].trim().replace("null", ""));

                    /*String Date ="";
                    if(parser[7].trim().replace("null", "").length()==10)
                    {
                        Date=parser[7].trim().replace("null", "").substring(0, 4)+parser[7].trim().replace("null", "").substring(5, 7)+parser[7].trim().replace("null", "").substring(8, 10);
                    }

                    dateofbirth.add(da.getDateFormat(Date));*/

                }
            } catch (Exception e) {
                String error = e.toString().trim();

                myDbHelper.Toastinfo(getActivity(), error);
            }


            progressDialog.dismiss();

        }




    }


    public class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder> implements View.OnClickListener {

        private int lastPosition = -1;
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
            View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list,parent, false);
            ViewHolder VH1 = new ViewHolder(itemview);
            return VH1;
        }
        /*public View getView(int position, View convertView, ViewGroup parent) {
            //Load your view, populate it, etc...
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.product_list, parent, false);

            Animation animation = AnimationUtils.loadAnimation(getContext(), (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
            rowView.startAnimation(animation);
            lastPosition = position;

            return rowView;
        }*/

        public void onBindViewHolder(ViewHolder holder, int position) {

            manager = new SessionManager();
            String status=manager.getPreferences(getContext(),"cusID");
            Log.d("status", status);
            final String cusUID= status.replaceAll("[^a-zA-Z0-9]+","");
            //Toast.makeText(getActivity(), cusUID, Toast.LENGTH_LONG).show();
            try {
                PRO_BIDS_LIST.clear();
                PRO_BID_ID.clear();
                PROCUSID.clear();
                PROUSERID.clear();

                PRO_BIDS_LIST = myDbHelper
                        .selectList(
                                "Select * from product_bids where cus_id='"+cusUID+"' and pro_user_id='"+ProdLISTID.get(position)+"'", null, 11);
                for (Iterator<String> i = PRO_BIDS_LIST.iterator(); i.hasNext();) {
                    String rowValue = (String) i.next();
                    String[] parser = rowValue.split("%");
                    PRO_BID_ID.add(parser[0].trim().replace("null", ""));
                    PROCUSID.add(parser[1].trim().replace("null", ""));
                    PROUSERID.add(parser[3].trim().replace("null", ""));


                    //Toast.makeText(getActivity(), (CharSequence) PRDS_name,Toast.LENGTH_LONG).show();


                }
            } catch (Exception e) {
                String error = e.toString().trim();

                myDbHelper.Toastinfo(getActivity(), error);
            }
            String strbid = String.valueOf(PROUSERID);
            strbid = strbid.replaceAll("\\[", "").replaceAll("\\]","");
            Log.d("Idsdasds", String.valueOf(strbid));
            Log.d("prod id", String.valueOf(ProdLISTID.get(position)));
           /*  holder.im4.setVisibility(View.VISIBLE);
            if(strbid == ProdLISTID.get(position)){
                Toast.makeText(getActivity(), "saddsa",Toast.LENGTH_LONG).show();
                    holder.im4.setVisibility(View.VISIBLE);
            }else{
                holder.im4.setVisibility(View.GONE);
            }
           String ProdIMGAGE=myDbHelper.selectViewName("SELECT pro_img1 FROM product_image  WHERE pro_user_id='" + ProdLISTID.get(position) + "'", null);
            if(ProdIMGAGE==null)
            {
                ProdIMGAGE="";
            }*/

           /* String BG=dh.selectViewName("SELECT BGNAME FROM APBLGP  WHERE BGCODE='" + BloodGroup.get(position) + "'", null);
            if(BG==null)
            {
                BG="";
            }*/

            //holder.Name.setAllCaps(true);
            // holder.im1.setImageDrawable("http://lokas.co.in/ngoapp/productImage/"+Img3.get(position));
            //holder.im1.setImageResource(Integer.parseInt(Img3.get(position)));
            //Picasso.with(context).load("http://lokas.co.in/ngoapp/productImage/"+ProdIMGAGE).into(holder.im1);
            Picasso.with(context).load("http://lokas.in/ngoapp/productImage/"+ProdImg.get(position)).into(holder.im1);
            holder.im2.setText(ProdTitle.get(position));
            // holder.relation.setAllCaps(true);
            holder.im3.setText(ProdDesc.get(position));

            // holder.bg.setText( BG);
            //holder.DOB.setText("Date of Birth : " + dateofbirth.get(position));
            //Toast.makeText(getActivity(),"", Toast.LENGTH_SHORT).show();
            // Log.d("Ids", String.valueOf(CusId));
            if(ProdTitle.get(position).trim().equals(""))
            {
                holder.im2.setVisibility(View.GONE);
            }
            else
            {
                holder.im2.setVisibility(View.VISIBLE);
            }

            if(ProdDesc.get(position).trim().equals(""))
            {
                holder.im3.setVisibility(View.GONE);
            }
            else
            {
                holder.im3.setVisibility(View.VISIBLE);
            }
            if(strbid.trim().equals(""))
            {
                holder.im4.setVisibility(View.GONE);
            }
            else
            {
                holder.im4.setVisibility(View.VISIBLE);
            }

            //animate(holder);
            setAnimation(holder.itemView, position);

        }


        public class ViewHolder extends RecyclerView.ViewHolder  {
            ImageView im1,im4;
            TextView im2,im3;

            public ViewHolder(final View View1) {
                super(View1);
                im1 = (ImageView) View1.findViewById(R.id.thumbnail);
                im2 = (TextView) View1.findViewById(R.id.title);
                im3 =  (TextView) View1.findViewById(R.id.desc);
                im4 =  (ImageView) View1.findViewById(R.id.corctimg);

                /*age = (TextView) View1.findViewById(R.id.txtAge);

                bg = (TextView) View1
                        .findViewById(R.id.txtBloodGroup);

                last = (TextView) View1.findViewById(R.id.txtLast);

                next = (TextView) View1.findViewById(R.id.txtNext);
                lage = (LinearLayout) View1.findViewById(R.id.linage);*/
                //emlay = (LinearLayout) View1.findViewById(R.id.emailLayout);
                //final String[] sel = {null};


                final String[] finalSel = new String[1];
                View1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {

                                System.out.println();



                        }catch (Exception e){
                            Log.i("myStuff err1", e.getMessage());
                        }

                        //Intent prodlist = new Intent(getContext(),Products.class);
                        Intent prodlist = new Intent(getActivity(),ProductDisplay.class);
                        prodlist.putExtra("PIMG", String.valueOf(ProdImg.get(getPosition())));
                        prodlist.putExtra("PTITLE", String.valueOf(ProdTitle.get(getPosition())));
                        prodlist.putExtra("PDESC", String.valueOf(ProdDesc.get(getPosition())));
                        prodlist.putExtra("CUSID", String.valueOf(CusIds.get(getPosition())));
                        prodlist.putExtra("PROLISTID", String.valueOf(ProdLISTID.get(getPosition())));
                        prodlist.putExtra("PROdate", String.valueOf(CreatedDate.get(getPosition())));
                        prodlist.putExtra("CATID", String.valueOf(CatID.get(getPosition())));
                        prodlist.putExtra("PRODID", String.valueOf(ProdId.get(getPosition())));
                        prodlist.putExtra("PRODSELECT", String.valueOf(PROUSERID));
                        //prodlist.putExtra("CUSID", String.valueOf(ProdTitle.get(getPosition())));
                        startActivity(prodlist);
                    }
                });


            }


        }

        @Override
        public int getItemCount() {
            return  ProdTitle.size();
        }

        public void animate(RecyclerView.ViewHolder viewHolder) {
            final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.anticipateovershoot_interpolator);
            viewHolder.itemView.setAnimation(animAnticipateOvershoot);
        }

        private void setAnimation(View viewToAnimate, int position)
        {
            // If the bound view wasn't previously displayed on screen, it's animated  android.R.anim.slide_in_left
            if (position > lastPosition)
            {
                Animation animation = AnimationUtils.loadAnimation(context, R.anim.up_from_bottom);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null, otherwise check
        // if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

}
