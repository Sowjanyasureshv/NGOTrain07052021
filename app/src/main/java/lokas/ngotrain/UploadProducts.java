package lokas.ngotrain;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.lokas.ngotrain.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class UploadProducts extends Fragment {

    SessionManager manager;
    //define global views variable
    public ImageView imageView;
    //public Button selectImage,uploadImage,selectCamera;
    private Button uploadImage;
    private ImageButton selectImage,selectCamera;
    public String SERVER = "http://lokas.co.in/ngoapp/image_upload2.php", timestamp;

    private EditText proTitle,proDesc;
    private Spinner cateSpin,prodSpin;

    //private Uri selectedImage;
    private Uri imageUri;
    //Uri selectedImageUri = null;
    Bitmap photo;
    String picturePath;
    Context context;
    private Bitmap bitmap;
    private ProgressDialog dialog;
    private static final int PICK_Camera_IMAGE = 2;

    //private ArrayList<String> imagesPathList;
    //private LinearLayout lnrImages;
    //private Bitmap yourbitmap;

    private static final String TAG = UploadProducts.class.getSimpleName();

    private static final int RESULT_SELECT_IMAGE = 1;


    ArrayList<String> CAT_List = new ArrayList<String>();
    ArrayList<String> CatId = new ArrayList<String>();
    ArrayList<String> CAtids = new ArrayList<String>();
    ArrayList<String> Catname = new ArrayList<String>();
    ArrayList<String> PRDS_id = new ArrayList<String>();

    ArrayList<String> PRO_List = new ArrayList<String>();
    ArrayList<String> PR_ID = new ArrayList<String>();
    ArrayList<String> CAT_IDP= new ArrayList<String>();
    ArrayList<String> PRD_NAME = new ArrayList<String>();
    DataHelper1 dh;
    DataBaseHelper myDbHelper;

    public UploadProducts() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_upload_products, container, false);
        (getActivity()).setTitle("Upload Product");

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

        //instantiate view
        imageView = (ImageView) view.findViewById(R.id.imageView);
        selectImage = (ImageButton) view.findViewById(R.id.selectImage);
        uploadImage = (Button) view.findViewById(R.id.uploadImage);
        selectCamera = (ImageButton) view.findViewById(R.id.selectCamera);

        proTitle = (EditText) view.findViewById(R.id.pro_title);
        proDesc = (EditText) view.findViewById(R.id.pro_desc);
        cateSpin = (Spinner) view.findViewById(R.id.cat_spinner);
        prodSpin =(Spinner) view.findViewById(R.id.prod_spinner);

        //lnrImages = (LinearLayout)view.findViewById(R.id.lnrImages);

        manager = new SessionManager();
        String result=manager.getPreferences(getActivity(),"cusID");
        final String cusUID= result.replaceAll("[^a-zA-Z0-9]+","");
        Toast.makeText(getActivity(), cusUID, Toast.LENGTH_LONG).show();

        List<String> fgg = myDbHelper.getAllLabelsPc();

        setupSpinnerListeners();

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

        //when selectImage button is pressed
        selectCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the function to select image from album
                // selectImage();
                //define the file-name to save photo taken by Camera activity
                clickpic();
            }
        });

        //when selectImage button is pressed
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //call the function to select image from album
                selectImage();
                /*AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getContext());
                myAlertDialog.setTitle("Pictures Option");
                myAlertDialog.setMessage("Select Picture Mode");

                myAlertDialog.setPositiveButton("Gallery", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                       Utility.pictureActionIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
                        Utility.pictureActionIntent.setType("image/*");
                        Utility.pictureActionIntent.putExtra("return-data", true);
                        startActivityForResult(Utility.pictureActionIntent, Utility.GALLERY_PICTURE);
                    }
                });

                myAlertDialog.setNegativeButton("Camera", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface arg0, int arg1)
                    {
                        Utility.pictureActionIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(Utility.pictureActionIntent, Utility.CAMERA_PICTURE);
                    }
                });
                myAlertDialog.show();*/

            }
        });

        //final String Product = prodSpin.getSelectedItem().toString();


        final List<String> categories = myDbHelper.getAllLabels();
        List<String> prrf = myDbHelper.getAllLabelsP();




        return view;
    }

    String catitem;
    private void setupSpinnerListeners() {
        CAT_List.clear();
        CatId.clear();
        Catname.clear();

        CAT_List = myDbHelper.selectList("Select * from category",null,6);
        for (Iterator<String> i = CAT_List.iterator(); i.hasNext();) {
            String rowValue = (String) i.next();
            String[] parser = rowValue.split("%");
            CatId.add(parser[0].trim().replace("null", ""));
            Catname.add(parser[1].trim().replace("null", ""));
        }



        String BG=myDbHelper.selectViewName("SELECT pro_user_id FROM product_bids  WHERE pro_cus_id='NGO892'", null);
        if(BG==null)
        {
            BG="";
        }
        Log.d("prrf111", String.valueOf(BG));

        //Toast.makeText(getActivity(), (Integer) categories.getSelectedItem(),Toast.LENGTH_LONG).show();
        //List<String> categories = new ArrayList<>();
        //categories.add(String.valueOf(Catname));

        List<String> products = new ArrayList<>();
        products.add("Mobile");
        products.add("Table");
        products.add("Bat");

        // Creating adapter for spinner
        ArrayAdapter<String> cat_dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Catname);


        // Drop down layout style - list view with radio button
        cat_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cateSpin.setPrompt("Select your Category!");
        prodSpin.setPrompt("Select your Product!");

        // attaching data adapter to spinner
        cateSpin.setAdapter(cat_dataAdapter);

        cateSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                // Message mSelected = (Message) parent.getItemAtPosition(pos);
                //Log.i("Id:", mSelected.getId());
                catitem = CatId.get(pos);
                Log.d("Id:", catitem);
                PRO_List.clear();
                PR_ID.clear();
                CAT_IDP.clear();
                PRD_NAME.clear();

                PRO_List = myDbHelper.selectList("Select * from product where cat_id='"+catitem+"'",null,7);
                for (Iterator<String> i = PRO_List.iterator(); i.hasNext();) {
                    String rowValue = (String) i.next();
                    String[] parser = rowValue.split("%");
                    PR_ID.add(parser[0].trim().replace("null", ""));
                    PRD_NAME.add(parser[1].trim().replace("null", ""));
                    CAT_IDP.add(parser[2].trim().replace("null", ""));
                }
                Log.d("pId:",String.valueOf(PRD_NAME));
                //Log.d("pId:",PR_ID.get()));
                final String Category = null;
                //setOnClick(Category, catitem);
                String prname = String.valueOf(PRD_NAME);

                //final String product = String.valueOf(PR_ID);


                ArrayAdapter<String> prod_dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, PRD_NAME);
                prod_dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                prodSpin.setAdapter(prod_dataAdapter);

                //setOnClick(product, String.valueOf(PR_ID));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                Log.i("Message", "Nothing is selected");

            }

        });

        //cateSpin.setSelection(((ArrayAdapter<String>)cateSpin.getAdapter()).getPosition(String.valueOf(CatId)));

        prodSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String pridd = PR_ID.get(position);
                Log.d("priiid",pridd);
                final String product = null;
                final String Category = null;
                //setOnClick(Category, catitem);
                setOnClick(catitem, pridd);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setOnClick(String category, final String Product) {
        manager = new SessionManager();
        String result=manager.getPreferences(getActivity(),"cusID");
        final String cusUID= result.replaceAll("[^a-zA-Z0-9]+","");
        //final String Product= pid.replaceAll("[^a-zA-Z0-9]+","");
        //final String Category= Cat.replaceAll("[^a-zA-Z0-9]+","");
        category ="1";
        Log.d("values",category+""+Product);
        //when uploadImage button is pressed
        final String finalCategory = category;
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //dialog = ProgressDialog.show(getContext(), "Uploading",
                //        "Please wait...", true);
                //get image in bitmap format
                Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                //get value
                String ProdTitle = proTitle.getText().toString();
                String ProdDesc = proDesc.getText().toString();
                //String Category = cateSpin.getSelectedItem().toString();
               // String Product = prodSpin.getSelectedItem().toString();


                if (ProdTitle.length() == 0) {
                    proTitle.setError("Product Title is required");
                    proTitle.requestFocus();
                } else if (ProdDesc.length() == 0) {
                    proDesc.setError("Product Description is required");
                    proDesc.requestFocus();
                } else {
                    //execute the async task and upload the image to server
                    new Upload(image, "IMG_" + timestamp, ProdTitle, ProdDesc, finalCategory, Product, cusUID).execute();
                    //new Upload(image, "IMG_" + timestamp, ProdTitle, ProdDesc, Category, Product, cusUID).execute();
                }
            }
        });

    }


    //function to select a image
    private void selectImage(){
        //open album to select image
        Intent gallaryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallaryIntent, RESULT_SELECT_IMAGE);
    }

    private void clickpic() {
        // Check Ca
            // Open default camera
            Intent cameraintent = new Intent(
                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraintent, 101);
    }





    /*
    * This function is called when we pick some image from the album
    * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri selectedImageUri = null;
        String filePath = null;
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_SELECT_IMAGE && resultCode == Activity.RESULT_OK && data != null){
           //set the selected image to image variable
            Uri image = data.getData();
            imageView.setImageURI(image);

            //get the current timeStamp and strore that in the time Variable
            Long tsLong = System.currentTimeMillis() / 1000;
            timestamp = tsLong.toString();

            Toast.makeText(getActivity(),timestamp,Toast.LENGTH_SHORT).show();
        }else if(requestCode == 101 && resultCode == Activity.RESULT_OK && data != null){
            /*selectedImage = data.getData();
            Log.d(TAG, "Media Uri: " + selectedImage);

            photo = (Bitmap) data.getExtras().get("data");

// Cursor to get image uri to display

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            //Cursor cursor = getActivity().getContentResolver().query(selectedImage,
           //         filePathColumn, null, null, null);
           // cursor.moveToFirst();

           // int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
           // picturePath = cursor.getString(columnIndex);
          //  cursor.close();

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            //ImageView imageView = (ImageView) findViewById(R.id.Imageprev);
            imageView.setImageBitmap(photo);*/

           /* String fileName = "new-photo-name.jpg";
            //create parameters for Intent with filename
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            values.put(MediaStore.Images.Media.DESCRIPTION,"Image captured by camera");
            //imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
            imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Log.d("image uri", String.valueOf(imageUri));
            //create new Intent
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(intent, PICK_Camera_IMAGE);*/

            Bitmap image = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(image);
            Log.d("image uri", String.valueOf(image));
            String fileName = "new-photo-name.jpg";
            //create parameters for Intent with filename
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, fileName);
            values.put(MediaStore.Images.Media.DESCRIPTION,"Image captured by camera");
            //imageUri is the current activity attribute, define and save it for later usage (also in onSaveInstanceState)
            imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Log.d("image uri", String.valueOf(imageUri));
            Long tsLong = System.currentTimeMillis() / 1000;
            timestamp = tsLong.toString();
            selectedImageUri = imageUri;


           /* if(selectedImageUri != null){
                try {
                    // OI FILE Manager
                    String filemanagerstring = selectedImageUri.getPath();

                    // MEDIA GALLERY
                    String selectedImagePath = getPath(selectedImageUri);

                    if (selectedImagePath != null) {
                        filePath = selectedImagePath;
                        Toast.makeText(getContext(), filePath,
                                Toast.LENGTH_LONG).show();
                    } else if (filemanagerstring != null) {
                        filePath = filemanagerstring;
                        Toast.makeText(getContext(), filemanagerstring,
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Unknown path",
                                Toast.LENGTH_LONG).show();
                        Log.e("Bitmap", "Unknown path");
                    }

                    if (filePath != null) {
                        decodeFile(filePath);
                    } else {
                        bitmap = null;
                    }
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Internal error",
                            Toast.LENGTH_LONG).show();
                    Log.e(e.getClass().getName(), e.getMessage(), e);
                }
            }*/

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


    //async task to upload image
    private class Upload extends AsyncTask<Void,Void,String> {
        //private ProgressDialog progressDialog = new ProgressDialog(getActivity());
        private Bitmap image;
        private String name;
        private String ptitle,pdesc,categ,prod,cusuid;

        public Upload(Bitmap image, String name, String prodTitle, String prodDesc, String category, String s,String cusid){
            this.image = image;
            this.name = name;
            this.ptitle = prodTitle;
            this.pdesc = prodDesc;
            this.categ = category;
            this.prod = s;
            this.cusuid = cusid;
        }

        protected void onPreExecute() {
            dialog = ProgressDialog.show(getActivity(), "Uploading",
                    "Please wait...", true);
        }



        @Override
        protected String doInBackground(Void... params) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            //compress the image to jpg format
            image.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
            /*
            * encode image to base64 so that it can be picked by saveImage.php file
            * */
            String encodeImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);

            //generate hashMap to store encodedImage and the name
            HashMap<String,String> detail = new HashMap<>();
            detail.put("name", name);
            detail.put("image", encodeImage);
            detail.put("prodTitle", ptitle);
            detail.put("prodDesc", pdesc);
            detail.put("category", categ);
            detail.put("product", prod);
            detail.put("cusunid", cusuid);

            try{
                //convert this HashMap to encodedUrl to send to php file
                String dataToSend = hashMapToUrl(detail);
                //make a Http request and send data to saveImage.php file
                String response = Request.post(SERVER,dataToSend);

                //return the response
                return response;

            }catch (Exception e){
                e.printStackTrace();
                Log.e(TAG,"ERROR  "+e);
                return null;
            }
        }



        @Override
        protected void onPostExecute(String s) {

            if (dialog.isShowing())
                dialog.dismiss();
            System.out.println("return value"+ s);
                //Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
            // show image uploaded
            String Datas = s.trim();
            if(Datas.equals("1")){
                Toast.makeText(getActivity(),"Sucessfully Image Uploaded",Toast.LENGTH_SHORT).show();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ProductDonor fone = new ProductDonor()                                               ;
                ft.replace(R.id.frame_container, fone);
                ft.commit();
               /* Fragment frg = null;
                frg = getActivity().getSupportFragmentManager().findFragmentByTag("Your_Fragment_TAG");
                final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();*/

            }else {
                Toast.makeText(getActivity(),"Upload  errror",Toast.LENGTH_SHORT).show();
            }

            //show image uploaded
            //Toast.makeText(getActivity(),"Image Uploaded",Toast.LENGTH_SHORT).show();
        }


    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    public void decodeFile(String filePath) {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 1024;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeFile(filePath, o2);

        imageView.setImageBitmap(bitmap);

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
