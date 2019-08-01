package lokas.ngotrain;

/**
 * Created by Bala on 12-01-2016.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lokas.ngotrain.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Logon on 28/10/2015.
 */
public class FullScreenImageAdapter extends PagerAdapter {

    private Activity _activity;
    private ArrayList<String> _imagePaths;
    private LayoutInflater inflater;
    private Context context;
    ArrayList<String> imgNames;
    TouchImageView imgDisplay;

    TextView readMore;
    String yourText;

    // constructor
    public FullScreenImageAdapter(Context mContext, Activity activity, ArrayList<String> imagePaths, ArrayList<String> imgName) {
        context = mContext;
        this._activity = activity;
        this._imagePaths = imagePaths;
        this.imgNames = imgName;
    }

    @Override
    public int getCount() {
        return this._imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater) _activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_fullscreen_image, container, false);
        imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.imgDisplay);
        //readMore = (TextView)viewLayout.findViewById(R.id.readMore);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        String drawName = _imagePaths.get(position);
        int id = context.getResources().getIdentifier(drawName, "drawable", context.getPackageName());
        System.out.println("imagename" +drawName);

        ///readMore.setText(imgNames.get(position));
        //yourText = readMore.getText().toString();

       // Bitmap bitmap = BitmapFactory.decodeResource(context.getApplicationContext().getResources(),id);
        //imgDisplay.setImageBitmap(bitmap);
        Picasso.with(context).load("http://lokas.co.in/ngoapp/productImage/"+drawName).into(imgDisplay);

         /*readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

       Button bannerButton = (Button) viewLayout.findViewById(R.id.bannerButton);
        bannerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //Intent viewIntent =  new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.kannadasan_url)));
               // _activity.startActivity(viewIntent);
            }
        });*/


        ((ViewPager) container).addView(viewLayout);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);

    }
}

