package lokas.ngotrain;


import android.content.Context;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lokas.ngotrain.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductCloseDonor extends Fragment {

    DataHelper1 dh;
    //DataAccess da;
    SessionManager manager;
    RecyclerView clsddonor;
    DataBaseHelper myDbHelper;


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

    public ProductCloseDonor() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View closdnr = inflater.inflate(R.layout.fragment_product_close_donor, container, false);

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

        clsddonor = (RecyclerView) closdnr.findViewById(R.id.clsdnr);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        clsddonor.setLayoutManager(layoutManager);
        //  r1.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        ///r1.setLayoutManager(new LinearLayoutManager(getActivity()));
        // r1.setItemAnimator(new FlipInBottomXAnimator());
        clsddonor.setHasFixedSize(true);
        Myadapter adap1 = new Myadapter(getActivity());
        //  AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adap);
        //  ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        clsddonor.setAdapter(adap1);
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
            CategId.clear();
            CreatedDate.clear();
            ModiDate.clear();
            Status.clear();
            Flag.clear();
            ProdID.clear();
            ProdSold.clear();
            ProdAward.clear();

            Bids_List=myDbHelper.selectList1("SELECT Distinct pro_user_id FROM product_bids WHERE pro_cus_id='"+cusUID+"' and pro_sold='0' ORDER BY pro_bid_id DESC", null,1);

            Log.d("jffsas", String.valueOf(Bids_List));

            for (Iterator<String> i = Bids_List.iterator(); i.hasNext();) {
                String rowValue = (String) i.next();
                String[] parser = rowValue.split("%");
                BidId.add(parser[0].trim().replace("null", ""));

            }
            Log.d("jffsas", String.valueOf(BidId));

        } catch (Exception e) {
            String error = e.toString().trim();

            myDbHelper.Toastinfo(getActivity(), error);
        }


        return closdnr;
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
            View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_open,parent, false);
            ViewHolder VH1 = new ViewHolder(itemview);
            return VH1;
        }

        public void onBindViewHolder(ViewHolder holder, int position) {

            Log.d("Products",BidId.get(position));
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
                                "Select * from product_user where pro_user_id='"+BidId.get(position)+"'ORDER BY pro_user_id ASC", null, 11);
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

          /*  String BG=dh.selectViewName("SELECT BGNAME FROM APBLGP  WHERE BGCODE='" + BloodGroup.get(position) + "'", null);
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
            Picasso.with(getActivity()).load("http://lokas.co.in/ngoapp/productImage/"+str1).placeholder(R.drawable.placeholderone).error(R.drawable.placeholderone).into(holder.primg);
            //new DownloadImageTask(holder.primg).execute("http://lokas.co.in/ngoapp/productImage/"+str1);
            holder.Pname.setAllCaps(true);
            holder.Pname.setText(str);
           // holder.Pdesc.setText(str2);
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


        public class ViewHolder extends RecyclerView.ViewHolder  {
            TextView Pname, Pdesc;
            ImageView primg;


            public ViewHolder(final View View1) {
                super(View1);
                Pname = (TextView) View1.findViewById(R.id.openprd_name);
                //Pdesc =  (TextView) View1.findViewById(R.id.openprd_desc);

                primg = (ImageView) View1.findViewById(R.id.openprd_img);


                View1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Toast.makeText(getActivity(), String.valueOf(BidId.get(getPosition())), Toast.LENGTH_LONG).show();
                        /*Intent Cusprod = new Intent(getActivity(),BidsCusList.class);
                        Cusprod.putExtra("ProdId",String.valueOf(ProdUserID.get(getPosition())));
                        startActivity(Cusprod);*/
                        /*BidsCusList newFragment = new BidsCusList();
                        Bundle args = new Bundle();
                        //args.putInt("prodID", Integer.parseInt(String.valueOf(ProdUserID.get(getPosition()))));
                        newFragment.setArguments(args);
                        // consider using Java coding conventions (upper first char class names!!!)
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();

                        // Replace whatever is in the fragment_container view with this fragment,
                        // and add the transaction to the back stack
                        transaction.replace(R.id.frame_container, newFragment);
                        transaction.addToBackStack(null);

                        // Commit the transaction
                        transaction.commit();*/

                    }
                });


            }


        }

        @Override
        public int getItemCount() {
            return  BidId.size();
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




}
