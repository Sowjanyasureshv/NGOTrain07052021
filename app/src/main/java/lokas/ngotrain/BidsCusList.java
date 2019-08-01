package lokas.ngotrain;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lokas.ngotrain.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Bala on 26-09-2016.
 */
public class BidsCusList extends Fragment {


    DataHelper1 dh;
    //DataAccess da;
    SessionManager manager;
    RecyclerView bcl;
    //MyAsyncTaskPCL  MY = null;
    DataBaseHelper myDbHelper;

    ArrayList<String> Bids_List = new ArrayList<String>();
    ArrayList<String> BidId = new ArrayList<String>();
    ArrayList<String> CusId = new ArrayList<String>();
    ArrayList<String> CusIds = new ArrayList<String>();
    ArrayList<String> ProdCusId = new ArrayList<String>();
    ArrayList<String> ProdUserID = new ArrayList<String>();


    ArrayList<String> CUS_List = new ArrayList<String>();
    ArrayList<String> CUS_id = new ArrayList<String>();
    ArrayList<String> CUS_ids = new ArrayList<String>();
    ArrayList<String> CUSU_ids = new ArrayList<String>();
    ArrayList<String> CUS_name = new ArrayList<String>();
    ArrayList<String> CUS_email = new ArrayList<String>();
    ArrayList<String> CUS_phone = new ArrayList<String>();
    String prodID;
    private CoordinatorLayout coordinatorLayout;
    String myJSON;
    private static final String TAG_RESULTS="result";
    private static final String TAG = UploadProducts.class.getSimpleName();
    JSONArray peoples = null;

    public BidsCusList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //setContentView(R.layout.bids_custlist);
        View Cview = inflater.inflate(R.layout.bids_prodlist, container, false);


        Bundle b = getArguments();
        final int proID = b.getInt("prodID");


       // mBundle.getString(prodID);
Toast.makeText(getContext(),"dfdf"+proID,Toast.LENGTH_LONG).show();

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


        bcl = (RecyclerView) Cview.findViewById(R.id.bidcuslist);

        // MY= new MyAsyncTask1();
        // MY.execute();

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bcl.setLayoutManager(layoutManager);
        //  r1.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        ///r1.setLayoutManager(new LinearLayoutManager(getActivity()));
        // r1.setItemAnimator(new FlipInBottomXAnimator());
        bcl.setHasFixedSize(true);
        Myadapter adap = new Myadapter(getActivity());
        //  AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adap);
        //  ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        bcl.setAdapter(adap);
        //r1.setItemAnimator(new DefaultItemAnimator());



        manager = new SessionManager();
        String result=manager.getPreferences(getActivity(),"cusID");
        final String cusUID= result.replaceAll("[^a-zA-Z0-9]+","");
        Toast.makeText(getActivity(), cusUID, Toast.LENGTH_LONG).show();

        try {
            Bids_List.clear();
            BidId.clear();
            CusId.clear();
            CusIds.clear();
            ProdCusId.clear();
            ProdUserID.clear();

                /*CUSTOMER_LIST = dh
                        .selectList(
                                "Select cus_id,cus_name,cus_email,cus_phone,crtd_date,modi_date,status,flag from customer WHERE RLCRBY='"
                                        + usercode + "' ORDER BY RLRNAM", null, 8);*/
            Bids_List = myDbHelper
                    .selectList(
                            "Select * from product_bids where pro_user_id='"+proID+"'ORDER BY pro_bid_id", null, 12);
            for (Iterator<String> i = Bids_List.iterator(); i.hasNext();) {
                String rowValue = (String) i.next();
                String[] parser = rowValue.split("%");
                BidId.add(parser[0].trim().replace("null", ""));
                CusId.add(parser[1].trim().replace("null", ""));
                CusIds.add(parser[1].trim().replace("null", ""));
                // AUDIT2.add(parser[1].trim().replace("null", ""));
                ProdCusId.add(parser[2].trim().replace("null", ""));
                ProdUserID.add(parser[3].trim().replace("null", ""));


            }
        } catch (Exception e) {
            String error = e.toString().trim();

            myDbHelper.Toastinfo(getActivity(), error);
        }

        Log.d("cust", String.valueOf(CusIds));


        Cview.setFocusableInTouchMode(true);
        Cview.requestFocus();
        Cview.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i(getTag(), "keyCode: " + keyCode);
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //getActivity().getActionBar().show();
                    Log.i(getTag(), "onKey Back listener is working!!!");
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ProductDonor fone = new ProductDonor();
                    ft.replace(R.id.frame_container, fone);
                    ft.commit();

                    // String cameback="CameBack";
                   // Intent i = new Intent(getActivity(), Products.class);
                    // i.putExtra("Comingback", cameback);
                   // startActivity(i);
                    return true;
                } else {
                    return false;
                }
            }
        });

        return Cview;
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
            View bclview = LayoutInflater.from(parent.getContext()).inflate(R.layout.bids_custlist,parent, false);
            ViewHolder VH1 = new ViewHolder(bclview);
            return VH1;
        }

        public void onBindViewHolder(ViewHolder holder, int position) {

            Log.d("cust1", String.valueOf(CusIds));
            Log.d("custfd",CusIds.get(position));
            ///String prDetails = String.valueOf(myDbHelper.selectList("Select * from product_user where pro_user_id='"+ProdUserID.get(position)+"'ORDER BY pro_user_id", null, 11));
            // Log.d("Products",prDetails);


            try {
                CUS_List.clear();
                CUS_id.clear();
                CUS_ids.clear();
                CUSU_ids.clear();
                CUS_name.clear();
                CUS_email.clear();
                CUS_phone.clear();
                //ProdSold.clear();

                /*CUSTOMER_LIST = dh
                        .selectList(
                                "Select cus_id,cus_name,cus_email,cus_phone,crtd_date,modi_date,status,flag from customer WHERE RLCRBY='"
                                        + usercode + "' ORDER BY RLRNAM", null, 8);*/
                CUS_List = myDbHelper
                        .selectList(
                                "Select * from customer where cus_userId='"+CusId.get(position)+"'ORDER BY cus_id", null, 12);
                for (Iterator<String> i = CUS_List.iterator(); i.hasNext();) {
                    String rowValue = (String) i.next();
                    String[] parser = rowValue.split("%");
                    CUS_id.add(parser[0].trim().replace("null", ""));
                    CUS_ids.add(parser[0].trim().replace("null", ""));
                    CUSU_ids.add(parser[1].trim().replace("null", ""));
                    CUS_name.add(parser[2].trim().replace("null", ""));
                    CUS_email.add(parser[3].trim().replace("null", ""));
                    CUS_phone.add(parser[4].trim().replace("null", ""));


                    //Toast.makeText(getActivity(), (CharSequence) PRDS_name,Toast.LENGTH_LONG).show();


                }
            } catch (Exception e) {
                String error = e.toString().trim();

                myDbHelper.Toastinfo(getActivity(), error);
            }

          /*  String BG=dh.selectViewName("SELECT BGNAME FROM APBLGP  WHERE BGCODE='" + BloodGroup.get(position) + "'", null);
            if(BG==null)
            {
                BG="";
            }*/
            String str = String.valueOf(CUS_name);
            str = str.replaceAll("\\[", "").replaceAll("\\]","");
            String str1 = String.valueOf(CUS_email);
            str1 = str1.replaceAll("\\[", "").replaceAll("\\]","");
            Log.d("det", str1);
            String str2 = String.valueOf(CUS_phone);
            str2 = str2.replaceAll("\\[", "").replaceAll("\\]","");
            String str3 = String.valueOf(CUS_id);
            str3 = str3.replaceAll("\\[", "").replaceAll("\\]","");
            //Log.d("Ids", CUS_ids.get(position));
            //new DownloadImageTask(holder.primg).execute("http://lokas.co.in/ngoapp/productImage/"+str1);
            holder.Name.setAllCaps(true);
            holder.Name.setText(str);
            holder.Email.setText(str3);




        }


        public class ViewHolder extends RecyclerView.ViewHolder  {
            TextView Name, Email ,maploc;
            Button btnaccept;



            public ViewHolder(final View View1) {
                super(View1);
                //coordinatorLayout = (CoordinatorLayout) View1.findViewById(R.id.coordlayout);
                Name = (TextView) View1.findViewById(R.id.bidcus_name);
                Email =  (TextView) View1.findViewById(R.id.bidcus_email);
                maploc = (TextView)  View1.findViewById(R.id.map);
                btnaccept = (Button) View1.findViewById(R.id.bidcus_accept);

                maploc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getData(CusId.get(getPosition()));
                    }
                });

                btnaccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), String.valueOf(CusId)+"as"+ProdUserID,Toast.LENGTH_LONG).show();
                        getData1(ProdCusId.get(getPosition()),ProdUserID.get(getPosition()),CusId.get(getPosition()));
                    }
                });
                String str3 = String.valueOf(CUS_id);
                str3 = str3.replaceAll("\\[", "").replaceAll("\\]","");
               //maploc.setOnClickListener(new View.OnClickListener() {
               //     @Override
               //     public void onClick(View v) {
                       // getData(CUS_id.get(getPosition()));
                //        Toast.makeText(getActivity(), String.valueOf(CUS_id.get(getPosition())), Toast.LENGTH_LONG).show();
                        //Snackbar snackbar = Snackbar
                        //       .make(coordinatorLayout, "You clicked Map", Snackbar.LENGTH_LONG);
                       /* .setAction("NO", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "You clicked Map", Snackbar.LENGTH_SHORT);
                                snackbar1.show();
                            }
                        });*/

                        // snackbar.show();
              //      }
            //    });

//

                //final String finalStr = str3;
                final String finalStr = str3;
                View1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //int position = getAdapterPosition();
                        //Log.d("position", String.valueOf(position));
                        //Log.d("cusIDDD", String.valueOf(CusId));
                        //Log.d("view", String.valueOf(View1));
                        //Toast.makeText(getApplicationContext(),pos,Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), String.valueOf(CusId),Toast.LENGTH_LONG).show();
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
            return  BidId.size();
        }
    }


    public void getData(final String cusUID){
        class GetDataJSON extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://lokas.co.in/ngoapp/product_location_get.php/?id="+cusUID);

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
                Intent mapInt = new Intent(getActivity(),MapShown.class);
                mapInt.putExtra("latitude",lati);
                mapInt.putExtra("longitude",longi);
                startActivity(mapInt);
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

    public void getData1(final String pcusid, final String prid, final String rcusid){
        class GetDataJSON extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                Log.d("pass value",prid+"as"+rcusid);
                HttpPost httppost = new HttpPost("http://lokas.co.in/ngoapp/donor_accept.php/?id="+pcusid+"&id1="+prid+"&id2="+rcusid);

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
                Log.d("va",result);
                //showList1();
                String Datas = result.trim();
                if(Datas.equals("1")){
                    Toast.makeText(getActivity(),"Sucess",Toast.LENGTH_SHORT).show();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    ProductDonor fone = new ProductDonor();
                    ft.replace(R.id.frame_container, fone);
                    ft.commit();

                }else {
                    Toast.makeText(getActivity()," errror",Toast.LENGTH_SHORT).show();
                }
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

    protected void showList1(){
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
                Intent mapInt = new Intent(getActivity(),MapShown.class);
                mapInt.putExtra("latitude",lati);
                mapInt.putExtra("longitude",longi);
                startActivity(mapInt);
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

    /*public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ProductDonor fone = new ProductDonor();
        ft.replace(R.id.frame_container, fone);
        ft.commit();

        if (fm.allowBackPressed()) { // and then you define a method allowBackPressed with the logic to allow back pressed or not
            super.onBackPressed();
        }
    }*/

    boolean doubleBackToExitPressedOnce = false;




    public void onBackPressed() {
        Toast.makeText(getActivity(),"back",Toast.LENGTH_LONG).show();
    }
}
