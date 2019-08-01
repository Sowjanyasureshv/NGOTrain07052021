package lokas.ngotrain;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.lokas.ngotrain.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Created by Bala on 19-08-2016.
 */
public class Dash extends AppCompatActivity {

    SessionManager manager;
    private DrawerLayout mDrawerLayout;
    private CharSequence mTitle;
    DataBaseHelper myDbHelper;
    Bundle b;
    String Usercode;
    String FragmentName,FragmentName1,FragmentName2;


    ArrayList<String> C_List = new ArrayList<String>();
    ArrayList<String> CID = new ArrayList<String>();
    ArrayList<String> CuserId = new ArrayList<String>();
    ArrayList<String> Ctype = new ArrayList<String>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);


        //Usercode = b.getString("UserCode");

        //FragmentName = b.getString("FRAG");
        //if(FragmentName==null)
        //{
        //    FragmentName="";
        //}

        manager = new SessionManager();
        String status=manager.getPreferences(Dash.this,"cusID");
        Log.d("status", status);
        final String cusUID= status.replaceAll("[^a-zA-Z0-9]+","");
        //Toast.makeText(getApplicationContext(), cusUID, Toast.LENGTH_LONG).show();




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Set the padding to match the Status Bar height
        //toolbar.setPadding(0, getStatusBarHeight(), 0, 0);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setTitle("NGO");

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


        try {
            C_List.clear();
            CID.clear();
            CuserId.clear();
            Ctype.clear();

                /*CUSTOMER_LIST = dh
                        .selectList(
                                "Select cus_id,cus_name,cus_email,cus_phone,crtd_date,modi_date,status,flag from customer WHERE RLCRBY='"
                                        + usercode + "' ORDER BY RLRNAM", null, 8);*/
            C_List = myDbHelper
                    .selectList(
                            "Select cus_id,cus_userId,cus_name,cus_email,cus_phone,cus_pwd,cus_cpwd,cus_role,crtd_date,modi_date,status,flag from customer" +
                                    " where cus_userId='"+cusUID+"'ORDER BY cus_id", null, 12);
            Log.d("va2", String.valueOf(C_List));
            for (Iterator<String> i = C_List.iterator(); i.hasNext();) {
                String rowValue = (String) i.next();
                String[] parser = rowValue.split("%");
                CID.add(parser[0].trim().replace("null", ""));
                CuserId.add(parser[1].trim().replace("null", ""));
                Ctype.add(parser[7].trim().replace("null", ""));

            }
        } catch (Exception e) {
            String error = e.toString().trim();

            //myDbHelper.Toastinfo(getApplicationContext(), error);
        }
        if (savedInstanceState == null) {
            /*String notice = "";
            String notice1 = "";
            String uploaddPrd = "";
            String bidsdetails = "";


            notice1 = getIntent().getStringExtra("data");
            notice = getIntent().getStringExtra("cuname");
            uploaddPrd = getIntent().getStringExtra("message");
            bidsdetails = getIntent().getStringExtra("bidsdet");
            public void onMessageReceived(RemoteMessage remoteMessage) {
                //This will give you the topic string from curl request (/topics/news)
                Log.d(TAG, "From: " + remoteMessage.getFrom());
                //This will give you the Text property in the curl request(Sample Message):
                Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
                //This is where you get your click_action
                Log.d(TAG, "Notification Click Action: " + remoteMessage.getNotification().getClickAction());
                //put code here to navigate based on click_action
            }

            try {
                Bundle b = getIntent().getExtras();
                String someData = b.getString("bidsdet");
                Log.d("deto111o",someData);
                Log.d("detoo",notice1);
                Log.d("det",notice);
                Log.d("det1",uploaddPrd);
                Log.d("det2",bidsdetails);
                if (notice != null) {
                    Log.i("myStuff", notice);
                    ///// DISPLAY NOTIFICATION IN AN ALERT DIAGLOG all good!
                    //Toast.makeText(Dash.this, notice+"notii", Toast.LENGTH_SHORT).show();
                    displayView(6);
                } else if (uploaddPrd != null) {
                    Log.i("myStuff", notice);
                    Log.d("myStuff1", uploaddPrd);
                    ///// DISPLAY NOTIFICATION IN AN ALERT DIAGLOG all good!
                    //Toast.makeText(Dash.this, notice+"notii", Toast.LENGTH_SHORT).show();
                    displayView(0);
                } else if (someData != null) {
                    Log.i("myStuff", notice);
                    Log.d("myStuff2", bidsdetails);
                    ///// DISPLAY NOTIFICATION IN AN ALERT DIAGLOG all good!
                    //Toast.makeText(Dash.this, notice+"notii", Toast.LENGTH_SHORT).show();
                    displayView(5);
                }
                displayView(5);
            } catch (Exception ex) {
                Log.i("myStuff err", ex.getMessage());
            }*/
        }

        Log.d("va1", String.valueOf(C_List));
        Log.d("va", String.valueOf(Ctype));
        String strC = String.valueOf(Ctype);
        strC = strC.replaceAll("\\[", "").replaceAll("\\]","");

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        final ImageView avatar = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.avatar);
        final String finalStrC = strC;
        if(finalStrC.equals("1")) {
            navigationView.getMenu().getItem(2).setVisible(false);
            navigationView.getMenu().getItem(1).setVisible(false);
            navigationView.getMenu().getItem(5).setVisible(false);
        }else {
            navigationView.getMenu().getItem(0).setVisible(false);
            navigationView.getMenu().getItem(1).setVisible(false);
            navigationView.getMenu().getItem(4).setVisible(false);
        }

        /*Intent intent = getIntent();

        String upd = intent.getStringExtra("main");
        if(upd.equals("main")){
            displayView(0);
        }*/

        //Toast.makeText(getApplicationContext(),finalStrC,Toast.LENGTH_LONG).show();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {


                    if(menuItem.getTitle().equals("Products List"))
                    {
                        displayView(0);
                    }
                    else if(menuItem.getTitle().equals("Customers List"))
                    {
                        displayView(1);
                    }else if(menuItem.getTitle().equals("Upload Product")) {
                        displayView(2);
                    }else if(menuItem.getTitle().equals("Edit Account"))
                    {
                        displayView(4);
                    }else if(menuItem.getTitle().equals("Your Bids")) {
                        displayView(5);
                    }else if(menuItem.getTitle().equals("Your Products")) {
                        displayView(6);
                    }else if(menuItem.getTitle().equals("Logout")) {
                        displayView(3);
                    }else if(menuItem.getTitle().equals("About Us")) {
                        displayView(7);
                    }
                /*Intent intent = getIntent();

                String upd = intent.getStringExtra("main");
                if(upd.equals("main")){
                    displayView(0);
                }*/
                //onNavigationItemSelected(navigationView.getMenu().findItem(R.id.navigation_item_attachment));

                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                //Toast.makeText(Dash.this, menuItem.getTitle(), Toast.LENGTH_LONG).show();
                return true;
            }
        });
        //MenuItem item = navigationView.getMenu().findItem(R.id.navigation_item_attachment);
        //item.setCheckable(true);
        //item.setChecked(true);

        if (savedInstanceState == null) {
            Intent current = getIntent();

          if(finalStrC.equals("1")) {
                navigationView.getMenu().performIdentifierAction(R.id.navigation_item_attachment, 0);
            }else{
                //navigationView.getMenu().performIdentifierAction(R.id.navigation_item_location, 0);
              AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Dash.this);
              alertDialogBuilder.setMessage("You Type a wrong credentials");

              alertDialogBuilder.setNegativeButton("Go Back",new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      displayView(3);
                  }
              });

              AlertDialog alertDialog = alertDialogBuilder.create();
              alertDialog.show();
            }

            if (current != null ) {
                FragmentName = current.getStringExtra("FRAG");
                if(FragmentName != null && FragmentName.equals("main"))
                {
                    displayView(0);
                }
                FragmentName1 = current.getStringExtra("FRAG1");
                if(FragmentName1 != null && FragmentName1.equals("req"))
                {
                    displayView(6);
                }
                FragmentName2 = current.getStringExtra("FRAG2");
                if(FragmentName2 != null && FragmentName2.equals("bid"))
                {
                    displayView(5);
                }
               // Toast.makeText(this, "exiting", Toast.LENGTH_LONG).show();
            }

        }




        //navigationView.getMenu().getItem(0).setChecked(true);
        //navigationView.setCheckedItem(displayView(0));
        //mDrawerLayout.setDrawerListener(mDrawerToggle);
      /* if (savedInstanceState == null) {
            // on first time display view for first nav item

           Intent current = getIntent();
           if (current != null ) {
               FragmentName = current.getStringExtra("FRAG");
               if(FragmentName != null && FragmentName.equals("main"))
               {
                   displayView(0);
               }
               FragmentName1 = current.getStringExtra("FRAG1");
               if(FragmentName1 != null && FragmentName1.equals("req"))
               {
                   displayView(6);
               }
               Toast.makeText(this, "exiting", Toast.LENGTH_LONG).show();
           }

        }*/

        if (!isNetworkAvailable()){
            //Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Dash.this);
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
    }

    private void hideItem()
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        Menu nav_Menu = navigationView.getMenu();
        nav_Menu.findItem(R.id.navigation_item_attachment).setVisible(false);
    }

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
           // case R.id.action_settings:
           //     return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /*public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.action_settings).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }*/


    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new Fragmentone();
                if (fragment != null) {


                    Bundle args = new Bundle();
                    fragment.setArguments(args);
                    FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                    t.replace(R.id.frame_container, fragment);
                    t.commit();

                }
                break;

            case 1:
                fragment = new FragmentTwo();
                if (fragment != null) {

                    Bundle args = new Bundle();
                    fragment.setArguments(args);
                    FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                    t.replace(R.id.frame_container, fragment);
                    t.commit();

                }
                break;

            case 2:
                fragment = new UploadProducts();
                if (fragment != null) {

                    Bundle args = new Bundle();
                    fragment.setArguments(args);
                    FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                    t.replace(R.id.frame_container, fragment);
                    t.commit();

                }
                break;
            case 3:
                manager = new SessionManager();
                manager.setPreferences(Dash.this, "status", "0");
                finish();
                Intent logout = new Intent(Dash.this,MainActivity.class);
                startActivity(logout);
                break;

            case 4:
                fragment = new AccountEdit();
                if (fragment != null) {

                    Bundle args = new Bundle();
                    fragment.setArguments(args);
                    FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                    t.replace(R.id.frame_container, fragment);
                    t.commit();

                }
                break;
            case 5:
                fragment = new ProductBids();
                if (fragment != null) {

                    Bundle args = new Bundle();
                    fragment.setArguments(args);
                    FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                    t.replace(R.id.frame_container, fragment);
                    t.commit();

                }
                break;
            case 6:
                fragment = new ProductDonor();
                if (fragment != null) {

                    Bundle args = new Bundle();
                    fragment.setArguments(args);
                    FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                    t.replace(R.id.frame_container, fragment);
                    t.commit();

                }
                break;
            case 7:
                fragment = new AboutUs();
                if (fragment != null) {

                    Bundle args = new Bundle();
                    fragment.setArguments(args);
                    FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                    t.replace(R.id.frame_container, fragment);
                    t.commit();

                }
                break;
            default:

                break;
        }


    }

    public void setTitle(CharSequence title) {
        mTitle = title;
        Log.d("Menu", (String) mTitle);
        getSupportActionBar().setTitle(mTitle);
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

    // A method to find height of the status bar
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
