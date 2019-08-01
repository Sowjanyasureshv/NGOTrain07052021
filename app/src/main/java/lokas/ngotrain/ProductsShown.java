package lokas.ngotrain;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lokas.ngotrain.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bala on 20-09-2016.
 */
public class ProductsShown extends AppCompatActivity {

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton floatingActionButton;
    int mutedColor = R.attr.colorPrimary;
    //RecyclerView recyclerView;
    RecyclerView recy;


    CardAdapter adapter;
    List<Flower> flowers;

    ArrayList<String> PROD_NAME = new ArrayList<String>();
    ArrayList<String> PROD_DESC = new ArrayList<String>();
    ArrayList<String> CusIds = new ArrayList<String>();
    ArrayList<String> PROD_IMG = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_shown);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.fab);
        //recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recy = (RecyclerView) findViewById(R.id.recyclerView);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar1);
        toolbar.setTitle("Hello");
        collapsingToolbarLayout.setTitle("Demo");

        setSupportActionBar(toolbar);

        //recyclerview
       // LinearLayoutManager llm = new LinearLayoutManager(this);
       // llm.setOrientation(LinearLayoutManager.VERTICAL);
       // recyclerView.setLayoutManager(llm);
        //recyclerView.setHasFixedSize(true);
        //initializeData();
        //adapter = new CardAdapter(getApplicationContext(), flowers);
        //recyclerView.setAdapter(adapter);

        //r1 = (RecyclerView) findViewById(R.id.recyclerView);

        //r1.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //r1.setOrientation(LinearLayoutManager.VERTICAL);
        // r1.setItemAnimator(new FlipInBottomXAnimator());
       // r1.setHasFixedSize(true);
        //Myadapter adap = new Myadapter(getApplicationContext());
        //  AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adap);
        //  ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);
        //r1.setAdapter(adap);
        //r1.setItemAnimator(new DefaultItemAnimator());


        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recy.setLayoutManager(llm);
        recy.setHasFixedSize(true);
        Myadapter adapt = new Myadapter(getApplicationContext());
        recy.setAdapter(adapt);


        Intent intent = getIntent();

        String PROD_IMG = intent.getStringExtra("PIMG");
        String PROD_NAME = intent.getStringExtra("PTITLE");
        String PROD_DESC = intent.getStringExtra("PDESC");
        String CusIds = intent.getStringExtra("CUSID");

        Toast.makeText(getApplicationContext(),"set"+PROD_DESC,Toast.LENGTH_LONG).show();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "You clicked on the fab", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /*private void initializeData() {
        flowers = new ArrayList<>();
        flowers.add(new Flower("Flower 1", R.drawable.image2));
        flowers.add(new Flower("Flower 2", R.drawable.images3));
        flowers.add(new Flower("Flower 3", R.drawable.images4));
        flowers.add(new Flower("Flower 4", R.drawable.images6));
        flowers.add(new Flower("Flower 5", R.drawable.images7));
        flowers.add(new Flower("Flower 6", R.drawable.images10));
        flowers.add(new Flower("Flower 7", R.drawable.images11));
        flowers.add(new Flower("Flower 8", R.drawable.images12));
        flowers.add(new Flower("Flower 9", R.drawable.images14));
        flowers.add(new Flower("Flower 10", R.drawable.images17));
        flowers.add(new Flower("Flower 11", R.drawable.images18));
        flowers.add(new Flower("Flower 12", R.drawable.index));

    }*/

    //@Override
  /*  public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    public class Myadapter extends RecyclerView.Adapter<Myadapter.ViewHolder> {


        Context context;

        public Myadapter(Context context) {

            this.context = context;
        }


        public void onClick(View v) {
            int pos = getItemCount();

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            System.out.println("test");
            View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,parent, false);
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
            holder.Name.setText(PROD_NAME.get(position));
            holder.Email.setText(PROD_DESC.get(position));
            // holder.relation.setAllCaps(true);
            holder.CusId.setText(CusIds.get(position));
            // holder.bg.setText( BG);
            //holder.DOB.setText("Date of Birth : " + dateofbirth.get(position));

            Log.d("Ids", String.valueOf(CusIds));
            if(PROD_DESC.equals(""))
            {
                holder.emlay.setVisibility(View.GONE);
            }
            else
            {
                holder.emlay.setVisibility(View.VISIBLE);
            }

        System.out.println(PROD_NAME);
            Toast.makeText(getApplicationContext(), (CharSequence) PROD_NAME,Toast.LENGTH_LONG).show();

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
            return  PROD_NAME.size();
        }
    }
}
