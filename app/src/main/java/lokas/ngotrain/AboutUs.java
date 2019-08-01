package lokas.ngotrain;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lokas.ngotrain.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUs extends Fragment {

    private TextView abtint;
    public AboutUs() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        (getActivity()).setTitle("About Us");
        // Inflate the layout for this fragment
        View abtView = inflater.inflate(R.layout.fragment_about_us, container, false);
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        abtint = (TextView) abtView.findViewById(R.id.abtvisit);

        abtint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://www.igiver.org";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        return abtView;
    }

}
