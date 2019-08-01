package lokas.ngotrain;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bala on 22-08-2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    //private static String DB_PATH = "/data/data/com.lokas.ngotrain/databases/";
    private static String DB_PATH = "";

    private static String DB_NAME = "NGO.db";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    String id = "";
    private String query;
    private String[] parameters;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context
     */
    public DataBaseHelper(Context context) {

        super(context, DB_NAME, null, 1);
        if (Build.VERSION.SDK_INT >= 17)
        {
            DB_PATH = context.getApplicationInfo().dataDir+"/databases/";
        }else {
            DB_PATH = "/data/data/"+context.getPackageName()+"/databases/";
        }
        this.myContext = context;
    }


    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if (dbExist) {
            //do nothing - database already exist
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        } catch (SQLiteException e) {

            //database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.

    public void ExecStatement(String Query) {
        try {
            this.myDataBase.execSQL(Query);
            // this.myDataBase.setTransactionSuccessful();
            System.out.println("Executed Successfully" + Query);
        } catch (SQLException e) {
			/*String error=e.toString().trim();

			Toastinfo(myContext,error);*/
            System.out.println(e.toString());

        }
    }

    public ArrayList<String> selectList(String ViewName, String[] Parameters,
                                        int n) {
        ArrayList<String> id = new ArrayList<String>();
        String rowValue = "";
        Cursor cursor = this.myDataBase.rawQuery(ViewName, Parameters);
        try {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        for (int i = 0; i < n; i++) {
                            rowValue = rowValue + cursor.getString(i) + " %";
                        }
                        id.add(rowValue);
                        // System.out.println(rowValue);
                        rowValue = "";
                    } while (cursor.moveToNext());
                }
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            System.out.println("Error " + e.toString());
        }
        return id;
    }

    public void Toastinfo(Context context, String msg) {
        //this.myContext = context;
        int time = 5000;
        Toast t = Toast.makeText(this.myContext, "INFORMATION ::" + msg.trim(), Toast.LENGTH_SHORT);
        t.show();
        t.setGravity(Gravity.BOTTOM, 0, 0);

    }

    public String selectViewName(String Query, String[] Parameters) {


        id = "";
        Cursor cur = myDataBase.rawQuery(Query, Parameters);
        System.out.println("Query " + Query);
        if (cur.moveToFirst()) {
            do {
                id = cur.getString(0);
            } while (cur.moveToNext());
        }
        if (cur != null && !cur.isClosed()) {
            cur.close();
        }


		/*}
		catch (Exception e) {

			String error=e.toString().trim();

			Toastinfo(myContext,error);

		}*/

        return id;
    }

    public ArrayList<String> selectList1(String ViewName, String[] Parameters,
                                        int n) {
        ArrayList<String> id = new ArrayList<String>();
        String rowValue = "";
        Cursor cursor = this.myDataBase.rawQuery(ViewName, Parameters);
        try {
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        for (int i = 0; i < n; i++) {
                            rowValue = rowValue + cursor.getString(i) + " %";
                        }
                        id.add(rowValue);
                        // System.out.println(rowValue);
                        rowValue = "";
                    } while (cursor.moveToNext());
                }
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            System.out.println("Error " + e.toString());
        }
        return id;
    }

    public List<String> getAllLabels() {
        List<String> list = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM category";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String ID = cursor.getString(0);
                list.add(cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return list;
    }

    public List<String> getAllLabelsP() {
        List<String> list = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM product";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(1));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return list;
    }

    public Cursor selectView() {



        String query = "SELECT DISTINCT pro_user_id FROM product_bids where pro_cus_id='NGO892'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null)

        {
            cursor.moveToFirst();
        }

        Log.d("cursor", String.valueOf(cursor));
        return cursor;
    }
    public List<String> getAllLabelsPc() {
        List<String> list = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT DISTINCT pro_user_id FROM product_bids where pro_cus_id='NGO892'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));//adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return list;
    }

    public boolean CheckGenderTableEmpty() {
        boolean isEmpty = false;
        String count = "SELECT * FROM product";
        Cursor mcursor = myDataBase.rawQuery(count, null);
        mcursor.moveToFirst();

        if (mcursor.getCount() != 0) {
            if (!mcursor.isClosed())
            {
                mcursor.close();
                Log.e("is not empty", "is not empty");
                isEmpty=true;
            }

        } else {
            if (!mcursor.isClosed())
            {
                mcursor.close();
                Log.e(" empty", "empty");
                isEmpty=false;
            }

        }
        return isEmpty;
    }

}
