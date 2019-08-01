package lokas.ngotrain;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
 * Created by Bala on 22-09-2016.
 */
public class Tab1 extends Fragment {

    //Overriden method onCreateView
   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return inflater.inflate(R.layout.tab1, container, false);
    }*/

    DataHelper1 dh;
    //DataAccess da;
    SessionManager manager;
    RecyclerView bidsopen;
    MyAsyncTask1  MY = null;
    DataBaseHelper myDbHelper;
    private TextView emptyView;
    private static final int EMPTY_VIEW = 0;


    ArrayList<String> Bids_List = new ArrayList<String>();
    ArrayList<String> BidId = new ArrayList<String>();
    ArrayList<String> CusId = new ArrayList<String>();
    ArrayList<String> CusIds = new ArrayList<String>();
    ArrayList<String> ProdCusId = new ArrayList<String>();
    ArrayList<String> ProdUserID = new ArrayList<String>();
    ArrayList<String> CategId = new ArrayList<String>();
    ArrayList<String> CreatedDate = new ArrayList<String>();
    ArrayList<String> ModiDate = new ArrayList<String>();
    ArrayList<String> Status = new ArrayList<String>();
    ArrayList<String> Flag = new ArrayList<String>();
    ArrayList<String> ProdID = new ArrayList<String>();
    ArrayList<String> ProdSold = new ArrayList<String>();
    ArrayList<String> ProdAward = new ArrayList<String>();


    ArrayList<String> PRDS_List = new ArrayList<String>();
    ArrayList<String> PRDS_id = new ArrayList<String>();
    ArrayList<String> PRDS_ids = new ArrayList<String>();
    ArrayList<String> PRDS_name = new ArrayList<String>();
    ArrayList<String> PRDS_desc = new ArrayList<String>();
    ArrayList<String> PRDS_img = new ArrayList<String>();


    public Tab1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab1, container, false);
        emptyView = (TextView) view.findViewById(R.id.empty_view);
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        /*dh = new DataHelper1(getActivity());

        try {
            dh.openDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        da = new DataAccess();*/


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


        bidsopen = (RecyclerView) view.findViewById(R.id.bidopr);

        MY= new MyAsyncTask1();
        MY.execute();

        // Inflate the layout for this fragment
        return view;
    }

    class MyAsyncTask1 extends AsyncTask<String,String,Void> {

        private ProgressDialog progressDialog = new ProgressDialog(getActivity());
        InputStream inputStream = null;
        String result = "";
        String result1 = "";

        protected void onPreExecute() {
            progressDialog.setMessage("Retrieving  data...");
            progressDialog.show();
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



            url_select = "http://lokas.in/ngoapp/product_bids_get.php";





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

                    String BIDID = jObject.getString("pro_bid_id");
                    String CUSID = jObject.getString("cus_id");
                    String PRODCUSID = jObject.getString("pro_cus_id");
                    String PRODUSERID = jObject.getString("pro_user_id");
                    String CATEGID = jObject.getString("cat_id");
                    String BCRTDDATE = jObject.getString("crtd_date");
                    String BMODIDATE = jObject.getString("modi_date");
                    String BSTATUS = jObject.getString("status");
                    String BFLAG = jObject.getString("flag");
                    String PRODID = jObject.getString("pro_id");
                    String PRODSOLD = jObject.getString("pro_sold");
                    String PRODAWARD = jObject.getString("pro_awarded");





                    String QUERY="";

                    myDbHelper.ExecStatement("DELETE FROM product_bids WHERE pro_bid_id='" + BIDID + "'");

                    QUERY="INSERT INTO product_bids (pro_bid_id,cus_id,pro_cus_id,pro_user_id,cat_id,crtd_date,modi_date,status,flag,pro_id,pro_sold,pro_awarded) " + "VALUES " +
                            "('"+BIDID+"','"+CUSID+"','"+PRODCUSID+"','"+PRODUSERID+"','"+CATEGID+"','"+BCRTDDATE+"','"+BMODIDATE+"','"+BSTATUS+"','"+BFLAG+"','"+PRODID+"','"+PRODSOLD+"','"+PRODAWARD+"')";
                    myDbHelper.ExecStatement(QUERY);




                   /* dh.ExecStatement("DELETE FROM EEREST WHERE RECODE='" + SRECODE + "'");

                    QUERY="INSERT INTO EEREST (RECODE,REREID,RENAME,READD1,READD2,READD3,RELDMK,RECITY,RESTAT,RESUBR,REPHN1,REPHN2,REPHN3,REWEBS,REMAIL,REPNAM,REPPHN,REPMAI,REMEMS,RELONG,RELATI,REPICT,REOFID,REOFD1,REOFD2,REOFD3,REOFIM,REOFVD,REOFBO,REOFST,REOFRE,RECRBY,RECRDT,REDNBY,REDNDT,RESYDT,RERCST,RELOCK) " + "VALUES ('"+SRECODE+"','"+SREREID +"','"+SRENAME+"','"+SREADD1+"','"+SREADD2+"','"+SREADD3+"','"+SRELDMK+"','"+SRECITY+"','"+SRESTAT+"','"+SRESUBR+"','"+SREPHN1+"','"+SREPHN2+"','"+SREPHN3+"','"+SREWEBS+"','"+SREMAIL+"','"+SREPNAM+"','"+SREPPHN+"','"+SREPMAI+"','"+SREMEMS+"','"+SRELONG+"','"+SRELATI+"','"+SREPICT+"','"+SREOFID+"','"+SREOFD1+"','"+SREOFD2+"','"+SREOFD3+"','"+SREOFIM+"','"+SREOFVD+"','"+SREOFBO+"','"+SREOFST+"','"+SREOFRE+"','"+SRECRBY+"','"+SRECRDT+"','"+SREDNBY+"','"+SREDNDT+"','"+SRESYDT+"','"+SRERCST+"','"+SRELOCK+"')";

                    dh.ExecStatement(QUERY);*/



                } // End Loop

            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            } // catch (JSONException e)


            final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            bidsopen.setLayoutManager(layoutManager);
            //  r1.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
            ///r1.setLayoutManager(new LinearLayoutManager(getActivity()));
            // r1.setItemAnimator(new FlipInBottomXAnimator());
            bidsopen.setHasFixedSize(true);
            Myadapter adap = new Myadapter(getActivity());
            //AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adap);
            //  ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
            bidsopen.setAdapter(adap);

            //r1.setItemAnimator(new DefaultItemAnimator());



            manager = new SessionManager();
            String result=manager.getPreferences(getActivity(),"cusID");
            final String cusUID= result.replaceAll("[^a-zA-Z0-9]+","");
            //Toast.makeText(getActivity(), cusUID, Toast.LENGTH_LONG).show();

            try {
                Bids_List.clear();
                BidId.clear();
                CusId.clear();
                CusIds.clear();
                ProdCusId.clear();
                ProdUserID.clear();
                CategId.clear();
                CreatedDate.clear();
                ModiDate.clear();
                Status.clear();
                Flag.clear();
                ProdID.clear();
                ProdSold.clear();
                ProdAward.clear();

                /*CUSTOMER_LIST = dh
                        .selectList(
                                "Select cus_id,cus_name,cus_email,cus_phone,crtd_date,modi_date,status,flag from customer WHERE RLCRBY='"
                                        + usercode + "' ORDER BY RLRNAM", null, 8);*/
                Bids_List = myDbHelper
                        .selectList(
                                "Select * from product_bids where cus_id='"+cusUID+"'and pro_sold='1' ORDER BY pro_bid_id DESC", null, 12);
                for (Iterator<String> i = Bids_List.iterator(); i.hasNext();) {
                    String rowValue = (String) i.next();
                    String[] parser = rowValue.split("%");
                    BidId.add(parser[0].trim().replace("null", ""));
                    CusId.add(parser[1].trim().replace("null", ""));
                    CusIds.add(parser[1].trim().replace("null", ""));
                    // AUDIT2.add(parser[1].trim().replace("null", ""));
                    ProdCusId.add(parser[2].trim().replace("null", ""));
                    ProdUserID.add(parser[3].trim().replace("null", ""));
                    CategId.add(parser[4].trim().replace("null", ""));
                    CreatedDate.add(parser[5].trim().replace("null", ""));
                    ModiDate.add(parser[6].trim().replace("null", ""));
                    Status.add(parser[7].trim().replace("null", ""));
                    Flag.add(parser[8].trim().replace("null", ""));
                    ProdID.add(parser[9].trim().replace("null", ""));
                    ProdSold.add(parser[10].trim().replace("null", ""));
                    ProdAward.add(parser[11].trim().replace("null", ""));


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

        Context context;

        public Myadapter(Context context) {

            this.context = context;
        }

        @Override
        public void onClick(View v) {
            int pos = getItemCount();

        }


        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.bids_open, parent, false);
                ViewHolder VH1 = new ViewHolder(itemview);
                return VH1;

        }

        @Override

        public void onBindViewHolder(ViewHolder holder, int position) {

            Log.d("Products",ProdUserID.get(position));
            ///String prDetails = String.valueOf(myDbHelper.selectList("Select * from product_user where pro_user_id='"+ProdUserID.get(position)+"'ORDER BY pro_user_id", null, 11));
           // Log.d("Products",prDetails);


            try {
                PRDS_List.clear();
                PRDS_id.clear();
                PRDS_ids.clear();
                PRDS_name.clear();
                PRDS_desc.clear();
                PRDS_img.clear();
                //ProdSold.clear();

                /*CUSTOMER_LIST = dh
                        .selectList(
                                "Select cus_id,cus_name,cus_email,cus_phone,crtd_date,modi_date,status,flag from customer WHERE RLCRBY='"
                                        + usercode + "' ORDER BY RLRNAM", null, 8);*/
                PRDS_List = myDbHelper
                        .selectList(
                                "Select * from product_user where pro_user_id='"+ProdUserID.get(position)+"'ORDER BY pro_user_id", null, 11);
                for (Iterator<String> i = PRDS_List.iterator(); i.hasNext();) {
                    String rowValue = (String) i.next();
                    String[] parser = rowValue.split("%");
                    PRDS_id.add(parser[0].trim().replace("null", ""));
                    PRDS_ids.add(parser[0].trim().replace("null", ""));
                    PRDS_name.add(parser[4].trim().replace("null", ""));
                    PRDS_desc.add(parser[5].trim().replace("null", ""));
                    PRDS_img.add(parser[6].trim().replace("null", ""));


                    //Toast.makeText(getActivity(), (CharSequence) PRDS_name,Toast.LENGTH_LONG).show();


                }
            } catch (Exception e) {
                String error = e.toString().trim();

                myDbHelper.Toastinfo(getActivity(), error);
            }

           /*String BG=dh.selectViewName("SELECT pro_user_title FROM product_user  WHERE product_user='" + PRDS_ids.get(position) + "'", null);
            if(BG==null)
            {
                BG="";
            }*/
           String str = String.valueOf(PRDS_name);
            str = str.replaceAll("\\[", "").replaceAll("\\]","");
            String str1 = String.valueOf(PRDS_img);
            str1 = str1.replaceAll("\\[", "").replaceAll("\\]","");
            String str2 = String.valueOf(PRDS_desc);
            str2 = str2.replaceAll("\\[", "").replaceAll("\\]","");
            Log.d("det", str1);
            Picasso.with(getActivity()).load("http://lokas.in/ngoapp/productImage/"+str1).placeholder(R.drawable.placeholderone).error(R.drawable.placeholderone).into(holder.primg);
            //new DownloadImageTask(holder.primg).execute("http://lokas.co.in/ngoapp/productImage/"+str1);
            if(str.isEmpty()){
                //bidsopen.setVisibility(View.GONE);
                //emptyView.setVisibility(View.VISIBLE);
            }
            holder.Pname.setAllCaps(true);
            holder.Pname.setText(str);
            holder.Pdesc.setText(str2);
            animate(holder);
            // holder.relation.setAllCaps(true);

            //holder.BidId.setText(BidId.get(position));
            //holder.pname.setText(str);

            // holder.bg.setText( BG);
            //holder.DOB.setText("Date of Birth : " + dateofbirth.get(position));

            Log.d("Ids", String.valueOf(CusId));
           /* if(ProdUserID.get(position).trim().equals(""))
            {
                holder.emlay.setVisibility(View.GONE);
            }
            else
            {
                holder.emlay.setVisibility(View.VISIBLE);
            }*/
        }

        public int getItemViewType(int position) {
            if (PRDS_List.size() == 0) {
                return EMPTY_VIEW;
            }
            return super.getItemViewType(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder  {
            TextView Pname, Pdesc;
            ImageView primg;


            public ViewHolder(final View View1) {
                super(View1);
                Pname = (TextView) View1.findViewById(R.id.openprd_name);
                Pdesc =  (TextView) View1.findViewById(R.id.openprd_desc);

                primg = (ImageView) View1.findViewById(R.id.openprd_img);


                View1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //int position = getAdapterPosition();
                        //Log.d("position", String.valueOf(position));
                        //Log.d("cusIDDD", String.valueOf(CusId));
                        //Log.d("view", String.valueOf(View1));
                        //Toast.makeText(getApplicationContext(),pos,Toast.LENGTH_LONG).show();
                        //Toast.makeText(getActivity(), String.valueOf(ProdUserID.get(getPosition())), Toast.LENGTH_LONG).show();
                        //Intent edit = new Intent(getActivity(),CustomerEdit.class);
                        //edit.putExtra("CUSID", String.valueOf(CusIds.get(getPosition())));
                        //Log.d("view11", String.valueOf(CusIds.get(getPosition())));

                        // startActivity(edit);


                    }
                });


            }


        }

        @Override
        public int getItemCount() {
           /* if(ProdUserID.size() == 0) {
                //Toast.makeText(getContext(),"Emptyyy111",Toast.LENGTH_LONG).show();
                //bidsopen.setVisibility(View.GONE);
                //emptyView.setVisibility(View.VISIBLE);
                return EMPTY_VIEW;

            }*/
            return  BidId.size();
        }



        public void animate(RecyclerView.ViewHolder viewHolder) {
            final Animation animAnticipateOvershoot = AnimationUtils.loadAnimation(context, R.anim.anticipateovershoot_interpolator);
            viewHolder.itemView.setAnimation(animAnticipateOvershoot);
        }



    }


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
}
