package lokas.ngotrain;

/**
 * Created by Bala on 12-01-2016.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lokas.ngotrain.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FullScreenViewActivity extends AppCompatActivity {

    public int[] b ={R.drawable.profile_pic, R.drawable.add_user};



    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;
    private Context context;
    private int selectedImage = 0;
    private String yourText = "";
    List<Employee> employees = null;
    ArrayList<String> imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_view);

        //set the image, color,text to the action bar
        TextView tv1= new TextView(this);
        tv1.setTypeface(tv1.getTypeface(), Typeface.BOLD);
        tv1.setText(getString(R.string.app_name));
        tv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        tv1.setTextColor(Color.WHITE);
        tv1.setGravity(Gravity.LEFT);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(tv1);
        //getSupportActionBar().setCustomView(linearLayout);

        //getSupportActionBar().setLogo(R.drawable.ic_kannadasan_launcher);
       /* getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.BLACK));*/

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setPageTransformer(true,new DepthPageTransformer());

        context = this;

        Intent i = getIntent();

        String pos= i.getStringExtra("position");

        String pimg= i.getStringExtra("imgname");
        System.out.println("imagenameold" +pos);
        System.out.println("imagenameold" +pimg);
        int position = Integer.parseInt(pos);

        selectedImage = position;

        ArrayList<String> imagePath = new ArrayList<String>();
        imageName = new ArrayList<String>();

        try
        {
            employees = SAXXMLParser.parse(getAssets().open("kannadasangallary.xml"));
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }



        for(Employee emp:employees)
        {
            String name = emp.getName();
            String desc = emp.getDepartment();
            int id = emp.getId();
            String image = emp.getType();
            imageName.add(desc);
        }

        /*for(int j=0;j<b.length;j++)
        {
            imagePath.add("" + b[j]);
        }*/
        imagePath.add("" +pimg);

        System.out.println("imagenameold" +pimg);
        adapter = new FullScreenImageAdapter(context,FullScreenViewActivity.this,imagePath,imageName);

        viewPager.setAdapter(adapter);
        // displaying selected image first
        viewPager.setCurrentItem(position);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                //showToast("ps"+arg0);
                selectedImage = arg0;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
                //showToast("pss"+arg0);

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
                //showToast("pssc"+arg0);
            }
        });
    }

    public void showToast(String Message)
    {
        Toast.makeText(getApplicationContext(), Message, Toast.LENGTH_SHORT).show();
    }

    private class WallpaperAsync extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog progress;
        String data = null;
        public WallpaperAsync()
        {
            progress = new ProgressDialog(context);
        }
        @Override
        protected Void doInBackground(Void... url)
        {

            Log.i("Selected Wallpaper", ""+b[selectedImage]);

            try {
                setWall();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;

        }

        @SuppressWarnings("unused")
        protected void onPostExecute(String result)
        {
            progress.dismiss();
        }

        @Override
        protected void onPreExecute()
        {
            progress.setMessage("Setting Wallpaper..");
            progress.setCancelable(true);
            progress.show();
        }

    }

    public void setWall() throws IOException
    {
        /*Bitmap bmap2  = BitmapFactory.decodeResource(context.getResources(),b[selectedImage]);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int h,w;

        if(bmap2.getHeight() >= bmap2.getWidth())
        {

            h = metrics.DENSITY_XXXHIGH;
            w = metrics.DENSITY_XXXHIGH - bmap2.getWidth() ;

            Bitmap bitmap = Bitmap.createScaledBitmap(bmap2, w, h, true);

            WallpaperManager wallpaperManager = WallpaperManager.getInstance(FullScreenViewActivity.this);
            try {
                wallpaperManager.forgetLoadedWallpaper();
                wallpaperManager.setBitmap(bitmap);
                showToast("Wallpaper Changed");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showToast("Not Fit to Your Screen");
        }*/

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),b[selectedImage]);
        String fileName = "kannadasan_photo.png";
        File dest = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), fileName);
        FileOutputStream out = new FileOutputStream(dest);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
        out.close();

        Uri uri = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+ File.separator+fileName));
        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
        intent.setDataAndType(uri, "image/png");
        intent.putExtra("mimeType", "image/png");
        startActivityForResult(Intent.createChooser(intent, "Set As"), 1001);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main_menu, menu); //menu to share the image and set wallpaper
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
           // case R.id.action_refresh:
                //callWall();
                //return true;
            //case R.id.action_location_found:
                //share();
                //return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void share() {
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),b[selectedImage]);

            //get the photo desc
            yourText = imageName.get(selectedImage);

            String fileName = "kannadasan_photo.png";
            File dest = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), fileName);
            FileOutputStream out = new FileOutputStream(dest);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.close();

            Uri uri = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)+ File.separator+fileName));
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            //shareIntent.putExtra(Intent.EXTRA_TEXT, ""+yourText+"\n\nYou can download this app from Playstore URL - " + getResources().getString(R.string.kannadasan_app_url));
            startActivity(Intent.createChooser(shareIntent, "Share Photo Via"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void callWall() {
        FullScreenViewActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run()  {
                if( Looper.myLooper() == Looper.getMainLooper() ) {
                    WallpaperAsync wpAsync = new WallpaperAsync();
                    wpAsync.doInBackground(null);
                }
            }
        });
    }

}

