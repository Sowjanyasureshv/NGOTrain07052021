package lokas.ngotrain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils.InsertHelper;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class DataHelper1 extends SQLiteOpenHelper {

	// The Android's default system path of your application database.
	private static String myDataBase_PATH = "/data/data/com.lokas.ngotrain/databases/";

	private static String myDataBase_NAME = "NGO.db";

	public SQLiteDatabase myDataBase;

	private Context myContext;
	private SQLiteStatement insertTEMP;

	private InsertHelper ih;
	String id = "";

	DataAccess da;

	private SQLiteStatement insertIMAG;
	private static final String INSERTIMAG = "insert into PMIMAG"
			+ "(IMCODE,IMPATH,IMINCD,IMRCST,IMLOCK,IMCRBY,IMCRDT,IMCRTM,IMDNBY,IMDNDT,IMDNTM) values (?,?,?,?,?,?,?,?,?,?,?)";

	/**
	 * Constructor Takes and keeps a reference of the passed context in order to
	 * access to the application assets and resources.
	 * 
	 * @param context
	 */
	public DataHelper1(Context context) {

		super(context, myDataBase_NAME, null, 1);
		this.myContext = context;
		da = new DataAccess();
	}
	public ArrayList<String> selectListColumnDetailscolor(String ViewName,
			String[] Parameters) {
		ArrayList<String> id = new ArrayList<String>();
		String rowValue = "  ";
		Cursor cursor = this.myDataBase.rawQuery(ViewName, Parameters);

		try {
			if (cursor != null) {
				int no_of_col = cursor.getColumnCount();
				if (cursor.moveToFirst()) {
					do {
						for (int i = 0; i < no_of_col; i++) {
							rowValue = rowValue + " " + cursor.getString(i)
									+ "@";
						}
						id.add(rowValue + "@" + " ");
						System.out.println(rowValue);
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
	
	public String getmonthString(int mMonth) {
		String monthh = "";

		if (mMonth == 1) {
			monthh = "Jan";
		} else if (mMonth == 2) {
			monthh = "Feb";
		} else if (mMonth == 3) {
			monthh = "Mar";
		} else if (mMonth == 4) {
			monthh = "Apr";
		} else if (mMonth == 5) {
			monthh = "May";
		} else if (mMonth == 6) {
			monthh = "Jun";
		} else if (mMonth == 7) {
			monthh = "Jul";
		} else if (mMonth == 8) {
			monthh = "Aug";
		} else if (mMonth == 9) {
			monthh = "Sep";
		} else if (mMonth == 10) {
			monthh = "Oct";
		} else if (mMonth == 11) {
			monthh = "Nov";
		} else if (mMonth == 12) {
			monthh = "Dec";
		}
		return monthh;
	}


	
	public ArrayList<Bitmap> selectTemp(String secid, String gapid) {
		ArrayList<Bitmap> id = new ArrayList<Bitmap>();
		Cursor cursor = this.myDataBase.query("PMIMAG",
				new String[] { "IMPATH" },
				"IMINCD=?", new String[] {
						 gapid.trim() }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				try {
					byte[] imageAsBytes = Base64.decode(cursor.getString(0),
							Base64.DEFAULT);
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 4;
					Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes,
							0, imageAsBytes.length);
					//
					//
					// Bitmap bitmap = BitmapFactory.decodeByteArray(
					// cursor.getBlob(0), 0, cursor.getBlob(0).length);
					id.add(bitmap);
				} catch (Exception e) {
					// TODO: handle exception
				}

			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return id;
	}
	
	public void CreateXMLStringUser(String Table, String Columns, String KeyField,
			String FileName, String LogTxtFile) {
		try {
			
			
			String cntTbl = selectViewName("Select count(*) from " + Table
					+ " Where " + KeyField + " = '1'", null);
			da.WriteToTextFile(LogTxtFile, da.getTodaysDate() + " "
					+ " No of Records Sent for " + Table + " --> " + cntTbl);			
			
			if(!cntTbl.equals("0"))
			{

			da.WriteToTextFile(LogTxtFile, da.getTodaysDate() + " " + "Table "
					+ Table);
			da.WriteToTextFile(LogTxtFile, da.getTodaysDate() + " "
					+ "KeyField " + KeyField);
			da.WriteToTextFile(LogTxtFile, da.getTodaysDate() + " "
					+ "FileName " + FileName);
			DatabaseAssistant db = new DatabaseAssistant(myContext,
					this.myDataBase, FileName, Table, Columns, KeyField,
					LogTxtFile,"");
			db.exportData();
			ExecStatement("Update " + Table + " SET " + KeyField + "='X'"
					+ " Where " + KeyField + "='1'");
			ExecStatement("Update " + Table + " SET " + KeyField + "='' Where " + KeyField + "='X'");
			getWritableDatabase().close();
			// File nFile = new File(Environment.getExternalStorageDirectory()
			// + "/NABHBACKUPv1" + "/" + Table + ".txt");
			// File oFile = new File(FileName);
			// IOUtils.copy(new FileInputStream(oFile),
			// new FileOutputStream(nFile));

			String path = Environment.getExternalStorageDirectory() + "/IBHAR/EATANDEARN/"
					+ da.getDate() + "/" + getTodaysDateinFormat();
			// + "/" + Table + ".txt";
			File nFile = new File(path);
			nFile.mkdirs();
			File file = new File(path + "/" + Table + ".txt");
			File oFile = new File(FileName);

			//IOUtils.copy(new FileInputStream(oFile), new FileOutputStream(file));
			
			}
		} catch (Exception e) {
			
			
String error=e.toString().trim();
			
			Toastinfo(myContext,error);
			System.out.println("Error " + e.toString());
			da.WriteToTextFile(LogTxtFile, da.getTodaysDate() + " "
					+ "Error in DatabaseAssistant  " + e.toString());
		}
	}


	
	public ArrayList<Bitmap> selectryotimg1(String ryot) {
		ArrayList<Bitmap> id = new ArrayList<Bitmap>();
		Cursor cursor = this.myDataBase.query("PMIMAG", new String[] { "IMPATH" },
				"IMCODE=? AND IMRCST='N' ", new String[] { ryot }, null, null,
				"IMDNDT DESC,IMDNTM  DESC");
		if (cursor.moveToFirst()) {
			do {
				
				if(cursor.getString(0)!=null)
				{
				byte[] decodedString = Base64.decode(cursor.getString(0),
						Base64.DEFAULT);
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;
				Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0,
						decodedString.length);
				id.add(bitmap);
				
				}
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return id;
	}
	
	
	public void keep(String value)
	{
		
		
		
		if(value.length()>0)
		{
			
			
			
			ArrayList<String> lst_tables = new ArrayList<String>();
			ArrayList<String> lst_keys = new ArrayList<String>();
			ArrayList<String> lst_updfield = new ArrayList<String>();
			
			
			
			ArrayList<String> transTable = selectListColumnDetails(
							"Select ICFILE,ICKEYF,ICPRFX from ITHIIC WHERE  ICMSTR IN ('X','N') ",null);

			for (int i = 0; i < transTable.size(); i++) {
				
				String[] split = transTable.get(i).split("<>");
				lst_tables.add(split[0].trim().replace("null",""));
				lst_keys.add(split[1].trim().replace("null",""));
				lst_updfield.add(split[2].trim().replace("null",""));
			}

			if(lst_tables.size()>0)
			{
				for(int i=0;i<lst_tables.size();i++)
				{
				
					ExecStatement("DELETE FROM "+lst_tables.get(i)+" WHERE  "+lst_updfield.get(i)+"RCST ='Y' OR   "+lst_updfield.get(i)+"CRDT  < "+ da.getTodaysDateinMonthFormat1(da.getDate(), Integer.parseInt(value))  +" AND  "+lst_updfield.get(i)+"LOCK ='' " );
				}
				
							
			}
						
		}
				
	}
	
	public void Send(String value)
	{
		
		if(value.length()>0)
		{
			
			
			
			ArrayList<String> lst_tables = new ArrayList<String>();
			ArrayList<String> lst_keys = new ArrayList<String>();
			ArrayList<String> lst_updfield = new ArrayList<String>();
			
			
			
			ArrayList<String> transTable = selectListColumnDetails(
							"Select ICFILE,ICKEYF,ICPRFX from ITHIIC WHERE  ICMSTR IN ('X','N','Z') ",
							null);

			for (int i = 0; i < transTable.size(); i++) {
				String[] split = transTable.get(i)
						.split("<>");
				lst_tables.add(split[0].trim());
				lst_keys.add(split[1].trim());
				lst_updfield.add(split[2].trim());
			}

			if(lst_tables.size()>0)
			{
				for(int i=0;i<lst_tables.size();i++)
				{
				
					if (selectViewName(
							"SELECT name FROM sqlite_master WHERE type='table' AND name='"+lst_tables.get(i)+"';",
							null).length() != 0) {
						
						
						ExecStatement("UPDATE "+lst_tables.get(i)+"  SET "+lst_updfield.get(i)+"LOCK ='1'  WHERE  "+lst_updfield.get(i)+"DNDT  >= "+ da.getTodaysDateinMonthFormat1(da.getDate(), Integer.parseInt(value)));
						
					}
					
					
				}
				
							
			}
						
		}
				
	}
	

	
	
	public void Toastinfo(Context context,String msg) {
		this.myContext = context;
		int time=5000;
		Toast t= Toast.makeText(this.myContext, "INFORMATION ::" + msg.trim(), Toast.LENGTH_SHORT);
		t.show();
		t.setGravity(Gravity.BOTTOM, 0, 0);

	}
	
	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

		boolean myDataBaseExist = checkDataBase();

		if (myDataBaseExist) {
			// do nothing - database already exist
		} else {
			// By calling this method and empty database will be created into
			// the default system path
			// of your application so we are gonna be able to overwrite that
			// database with our database.
			
		
				
				this.getReadableDatabase();
				try {
					copyDataBase();
				} catch (IOException e) {
					throw new Error("Error copying database");
				}
			
			
			
			
			
		}
	}

	
	public void createDataBase1() throws IOException {

	
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		
	}
	
	public void createDataBase2() throws IOException {

		
		this.getReadableDatabase();
		try {
			copyDataBase1();
		} catch (IOException e) {
			throw new Error("Error copying database");
		}
	
}

	
	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	public boolean checkDataBase() {

		File dbFile = new File(myDataBase_PATH + myDataBase_NAME);
		return dbFile.exists();
	}

	public boolean checkDataBase1() {

	
		File dbFile = new File( Environment.getExternalStorageDirectory()
				+ "/IBHAR/EATANDEARN/BACKUP/survey.db");
		return dbFile.exists();

	
	}

	
	
	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {

		// Open your local myDataBase as the input stream
		InputStream myInput = myContext.getAssets().open(myDataBase_NAME);
		//InputStream myInput = myContext.getAssets().open(myDataBase_NAME);

		/*String LogTxtFile = Environment.getExternalStorageDirectory()
				+ "/IBHAR/survey.db";

		File	dir = new File(LogTxtFile);

		InputStream myInput = new FileInputStream(dir);*/

		// Path to the just created empty myDataBase
		String outFileName = myDataBase_PATH + myDataBase_NAME;

		// Open the empty myDataBase as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public InputStream stream() throws FileNotFoundException,IOException
	{
		

		String LogTxtFile = Environment.getExternalStorageDirectory()
				+ "/IBHAR/EATANDEARN/BACKUP/survey.db";
	
		File dir = new File(LogTxtFile);
			
		InputStream is = new FileInputStream(dir);
		return is;
	}
	
	
	public void copyDataBase1() throws IOException {

		// Open your local myDataBase as the input stream
		// path
		
		
		
		
		InputStream myInput =  stream();

		// Path to the just created empty myDataBase
		String outFileName = myDataBase_PATH + myDataBase_NAME;

		// Open the empty myDataBase as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	
	
	public void openDataBase() throws SQLException {

		// Open the database
		String myPath = myDataBase_PATH + myDataBase_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);

		//this.insertIMAG = this.myDataBase.compileStatement(INSERTIMAG);

	}

	public long insertTemp(String position, String secid, String imgpath,
			String imgcrby, String imgcrdt, String imgcrtm, String imgdnby,
			String imgdndt, String imgdntm, String imgrcst, String imglock,
			String imresp) {
		this.insertTEMP.bindString(1, position);
		this.insertTEMP.bindString(2, secid);
		this.insertTEMP.bindString(3, imgpath);
		this.insertTEMP.bindString(4, imgcrby);
		this.insertTEMP.bindString(5, imgcrdt);
		this.insertTEMP.bindString(6, imgcrtm);
		this.insertTEMP.bindString(7, imgdnby);
		this.insertTEMP.bindString(8, imgdndt);
		this.insertTEMP.bindString(9, imgdntm);
		this.insertTEMP.bindString(10, imgrcst);
		this.insertTEMP.bindString(11, imglock);
		this.insertTEMP.bindString(12, imresp);
		return this.insertTEMP.executeInsert();
	}
	public ArrayList<Bitmap> selectTempASSE(String secid) {
		ArrayList<Bitmap> id = new ArrayList<Bitmap>();
		Cursor cursor = this.myDataBase.query("ASIMAG",
				new String[] { "IMIMAG" }, "IMRESP =? AND length(IMRESP)!=0",
				new String[] { secid }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				byte[] imageAsBytes = Base64.decode(cursor.getString(0),
						Base64.DEFAULT);
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;
				Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0,
						imageAsBytes.length);
				//
				//
				// Bitmap bitmap = BitmapFactory.decodeByteArray(
				// cursor.getBlob(0), 0, cursor.getBlob(0).length);
				id.add(bitmap);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return id;
	}

	@Override
	public synchronized void close() {

		if (myDataBase != null)
			myDataBase.close();

		super.close();

	}

	@Override
	public void onCreate(SQLiteDatabase myDataBase) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase myDataBase, int oldVersion,
			int newVersion) {

	}

	public String generateUserCode(String UserCode, String dndt, String dntm) {
	
		
		
		
		
		
		String Max_id = selectViewName(
				"Select USNUMB from PMUSER Where USCODE =?",
				new String[] { UserCode });
		if ((Max_id == null) || (Max_id.length() == 0)) {
			Max_id = "0";
		}
		int id = Integer.parseInt(Max_id) + 1;
		ExecStatement("Update PMUSER set USNUMB ='" + id + "',USDNBY = '"
				+ UserCode + "',USDNDT='" + dndt + "',USDNTM='" + dntm
				+ "',USLOCK='1' Where USCODE ='" + UserCode + "'");
		return "M" + UserCode + String.format("%08d", id);
	
	
	
	
	}

	
	
	public String generateUserCode1(String UserCode, String dndt, String dntm) {
	
		
		
		
		
		
		String Max_id = selectViewName(
				"Select PMVALU from ITPARM Where PMHARD ='USER' AND PMSOFT='ASUS'",
				null);
		if ((Max_id == null) || (Max_id.length() == 0)) {
			Max_id = "0";
		}
		int id = Integer.parseInt(Max_id) + 1;
		ExecStatement("Update ITPARM set PMVALU ='"
				+ id
				+ "' Where PMHARD ='USER' AND PMSOFT='ASUS'");

		return "M" + UserCode + String.format("%08d", id);
	
	
	
	
	}

	
	
	
	
	
	public ArrayList<String> selectsingleList(String ViewName,
			String[] Parameters, int n) {
		ArrayList<String> id = new ArrayList<String>();
		String rowValue = "";
		Cursor cursor = this.myDataBase.rawQuery(ViewName, Parameters);
		try {
			if (cursor != null) {
				int no_of_col = cursor.getColumnCount();
				if (cursor.moveToFirst()) {
					do {
						for (int i = 0; i < n; i++) {
							rowValue = rowValue + cursor.getString(0);
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

	// Add your public helper methods to access and get content from the
	// database.
	// You could return cursors by doing "return myDataBase.query(....)" so it'd
	// be easy
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
	
	
	
	
	public void ExecStatement1(String Query) {
		try {
			this.myDataBase.execSQL(Query);
			// this.myDataBase.setTransactionSuccessful();
			System.out.println("Executed Successfully" + Query);
		} catch (SQLException e) {
			String error=e.toString().trim();
			
		/*	Toastinfo(myContext,error);
			System.out.println(e.toString());*/
		}
	}
	
	public Bitmap selectIMAGE(String ryot) {
		Bitmap id=null;
		Cursor cursor = this.myDataBase.query("PMIMAG", new String[] { "IMPATH" },
				"IMINCD=? AND IMRCST='N' ", new String[] { ryot }, null, null,
				"IMDNDT DESC,IMDNTM  DESC");
		
		
		
		
		if (cursor.moveToFirst()) {
			do {
				if(cursor.getString(0)!=null)
				{
				byte[] decodedString = Base64.decode(cursor.getString(0),
						Base64.DEFAULT);
	
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;
				Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0,
						decodedString.length);
				id=bitmap;
				}
				return id;
				
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		
		return id;
	}
	
	
	public Bitmap selectIMAGE1(String ryot) {
		Bitmap id=null;
		Cursor cursor = this.myDataBase.query("PMIMAG", new String[] { "IMPATH" },
				"IMCODE=?  ", new String[] { ryot }, null, null,
				"IMDNDT DESC,IMDNTM  DESC");
		
		
		
		
		if (cursor.moveToFirst()) {
			do {
				if(cursor.getString(0)!=null)
				{
				byte[] decodedString = Base64.decode(cursor.getString(0),
						Base64.DEFAULT);
	
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;
				Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0,
						decodedString.length);
				id=bitmap;
				}
				return id;
				
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		
		return id;
	}
	
	

	public ArrayList<Bitmap> selectTempRecord(String secid) {
		ArrayList<Bitmap> id = new ArrayList<Bitmap>();
		Cursor cursor = this.myDataBase.query("ASIMAG",
				new String[] { "IMIMAG" }, "IMRESP =?", new String[] { secid },
				null, null, null);
		if (cursor.moveToFirst()) {
			do {
				byte[] imageAsBytes = Base64.decode(cursor.getString(0),
						Base64.DEFAULT);
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;
				Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0,
						imageAsBytes.length);
				id.add(bitmap);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return id;
	}

	/*public void CreateImageXMLString(String TableName, String[] Parameters,
			String[] Values, File Directory, File Inbox, File Outbox,
			String Url, String UrlUserCode, String AsyncDate) {
		ArrayList<String> FileList = new ArrayList<String>();
		String base64xml = "";
		String rowValue = "", pKey = "";
		Cursor cursor = this.myDataBase.rawQuery(TableName, Parameters);
		System.out.println("Select Value " + TableName);
		try {
			if (cursor != null) {
				int no_of_col = cursor.getColumnCount();
				if (cursor.moveToFirst()) {
					do {
						rowValue = rowValue + "<" + Values[0] + ">";
						for (int i = 0; i < no_of_col; i++) {
							rowValue = rowValue + "<" + cursor.getColumnName(i)
									+ ">";
							if (cursor.getColumnName(i).equals("IMIMAG")) {
								String base64Image1 = "";
								if (cursor.getBlob(i).length > 0) {
									base64Image1 = Base64.encodeToString(
											cursor.getBlob(i), Base64.DEFAULT);
								}
								rowValue = rowValue + base64Image1;
							} else {
								rowValue = rowValue + cursor.getString(i);
							}
							rowValue = rowValue + "</"
									+ cursor.getColumnName(i) + ">";
							if (cursor.getColumnName(i).equals(Values[1])) {
								pKey = cursor.getString(i);
								System.out.println("Pkey " + pKey);
							}
						}
						rowValue = rowValue + "</" + Values[0] + ">";
						String imxml = "<DATA>" + rowValue + "</DATA>";
						// ExecStatement("Update " + Values[0].trim() + " SET "
						// + Values[2].trim() + "='X'" + " Where "
						// + Values[1].trim() + "='" + pKey + "'");
						// System.out.println("Xml is " + imxml);
						if (rowValue.length() > 0) {
							da.WriteToXml(Outbox.getAbsolutePath() + "/IMAG"
									+ pKey + ".XML", imxml);
							FileList.add(Outbox.getAbsolutePath() + "/IMAG"
									+ pKey + ".XML");
							String[] filel = new String[FileList.size()];
							filel = FileList
									.toArray(new String[FileList.size()]);
							Compress c = new Compress(filel,
							Outbox.getAbsolutePath() + "/IMAG" + pKey
											+ ".ZIP");
							c.zip();
							System.out.println("Zipping Successful IMAG" + pKey
									+ ".ZIP");
							try {
								byte[] b = org.apache.commons.io.FileUtils
										.readFileToByteArray(new File(Outbox
												.getAbsolutePath()
												+ "/IMAG"
												+ pKey + ".ZIP"));
								System.out.println("Byte Array Successful");
								String bas64 = Base64.encodeToString(b,
										Base64.DEFAULT);
								String body = "<ITIIIR>" + "<IRAUDT>3</IRAUDT>"
										+ "<IRNAME>" + UrlUserCode
										+ ".XML</IRNAME>" + "<IRUSER>"
										+ UrlUserCode + "</IRUSER>"
										+ "<IRPRDT>"
										+ da.getTodaysDateinFormat()
										+ "</IRPRDT>" + "<IRSTAT>C</IRSTAT>"
										+ "<IRFILE>" + bas64 + "</IRFILE>"
										+ "<IRCRDT>"
										+ da.getTodaysDateinFormat()
										+ "</IRCRDT>" + "</ITIIIR>";
								base64xml = body;
								String[] columns = new String[] { "sUserCode",
										"sReceiveXMLString", "sSyncDate",
										"sCallingProgram" };
								String[] values = new String[] { UrlUserCode,
										base64xml, AsyncDate, "Assessment" };
								KsoapDataAccess da1 = new KsoapDataAccess(
										Url.trim(), "DataSync", columns,
										values, "0");
								da1.getHttpResult();
								System.out.println("IMAG" + pKey + " Sent");
								da.deleteFile(Outbox.getAbsolutePath()
										+ "/IMAG" + pKey + ".ZIP");
								FileList.clear();
							} catch (OutOfMemoryError e) {
								System.out.println(e.toString());
							} catch (Exception e) {
								System.out.println(e.toString());
							}
						}
						imxml = "";
						rowValue = "";
						ExecStatement("Update " + Values[0].trim() + " SET "
								+ Values[2].trim() + "=''" + " Where "
								+ Values[1].trim() + "='" + pKey + "'");
					} while (cursor.moveToNext());
				}
				System.out.println(rowValue);
			}
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		} catch (Exception e) {
			
String error=e.toString().trim();
			
			Toastinfo(myContext,error);
			System.out.println("Error " + e.toString());
		}
	} */

	public ArrayList<String> selectTempPath(String secid) {
		ArrayList<String> id = new ArrayList<String>();
		Cursor cursor = this.myDataBase.query("ASIMAG",
				new String[] { "IMAUDT" }, "IMRESP=?", new String[] { secid },
				null, null, null);
		if (cursor.moveToFirst()) {
			do {
				id.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return id;
	}

	public void deleteTempPath(String imgpath, String secid) {
		this.myDataBase.delete("ASIMAG", "IMSEQN=? AND IMRESP=?", new String[] {
				imgpath, secid });
	}
	
	
	public ArrayList<Bitmap> selectryotimg(String ryot) {
		ArrayList<Bitmap> id = new ArrayList<Bitmap>();
		Cursor cursor = this.myDataBase.query("PMIMAG", new String[] { "IMPATH" },
				"IMINCD=? AND IMRCST='N' ", new String[] { ryot }, null, null,
				"IMDNDT DESC,IMDNTM  DESC");
		if (cursor.moveToFirst()) {
			do {
				
				if(cursor.getString(0)!=null)
				{
				byte[] decodedString = Base64.decode(cursor.getString(0),
						Base64.DEFAULT);
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;
				Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0,
						decodedString.length);
				id.add(bitmap);
				
				}
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return id;
	}
	
	public boolean updatecmimage(String IMCODE, String IMPATH,
			String IMCRBY, String IMCRDT, String IMCRTM,
			String IMDNBY, String IMDNDT, String IMDNTM, String IMRCST,
			String IMLOCK ) {
		ContentValues args = new ContentValues();

		
		args.put("IMPATH", IMPATH);
		args.put("IMDNBY", IMDNBY);
		args.put("IMDNDT", IMDNDT);
		args.put("IMDNTM", IMDNTM);
		args.put("IMRCST", IMRCST);
		args.put("IMLOCK", IMLOCK);
		

		return myDataBase.update("PMIMAG", args, "IMCODE" + "=" + "'" + IMCODE + "'",
				null) > 0;

	}

	

	public ArrayList<String> selectListColumnDetailsN(String ViewName,
			String[] Parameters, String[] columns) {
		ArrayList<String> id = new ArrayList<String>();
		String rowValue = "";
		Cursor cursor = this.myDataBase.rawQuery(ViewName, Parameters);
		try {
			if (cursor != null) {
				int no_of_col = cursor.getColumnCount();
				System.out.println("No. of Columns " + no_of_col);
				if (cursor.moveToFirst()) {
					do {
						for (int i = 0; i < no_of_col; i++) {
							System.out.println("columns[i] " + columns[i]);
							System.out.println("cursor.getColumnName(i) "
									+ cursor.getColumnName(i));
							if (columns[i].equals(cursor.getColumnName(i))) {
								rowValue = rowValue + " " + cursor.getString(i)
										+ "<>";
							}
						}
						id.add(rowValue + "<>" + " ");
						System.out.println(rowValue);
						rowValue = "";
					} while (cursor.moveToNext());
				}
			}
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		} catch (Exception e) {
			
String error=e.toString().trim();
			
			Toastinfo(myContext,error);
			System.out.println("Error " + e.toString());
			
		}
		return id;
	}

	public InsertHelper CreateInsertHelper(String tableName) {
		
		InsertHelper ih = new InsertHelper(this.myDataBase, tableName);
		
		return ih;
	}

	
	
	public void createView(String viewName, String Query) {
		myDataBase.execSQL("CREATE VIEW " + viewName + " AS " + Query);
		System.out.println("View Inserted Successfully");
	}

	public ArrayList<String> selectListColumnDetails(String ViewName,
			String[] Parameters) {
		ArrayList<String> id = new ArrayList<String>();
		String rowValue = "";
		Cursor cursor = this.myDataBase.rawQuery(ViewName, Parameters);
		try {
			if (cursor != null) {
				int no_of_col = cursor.getColumnCount();
				System.out.println("Col Count " + no_of_col);
				if (cursor.moveToFirst()) {
					do {
						for (int i = 0; i < no_of_col; i++) {
							rowValue = rowValue + " " + cursor.getString(i)
									+ "<>";
						}
						id.add(rowValue + "<>" + " ");
						System.out.println("Row Value " + rowValue);
						rowValue = "";
					} while (cursor.moveToNext());
				}
			}
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		} catch (Exception e) {
String error=e.toString().trim();
			
			Toastinfo(myContext,error);
			
			System.out.println("Error " + e.toString());
		}
		return id;
	}

	public String selectViewName(String Query, String[] Parameters) {


		//try{
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


	/*	}
		catch (Exception e) {

			String error=e.toString().trim();

			Toastinfo(myContext,error);

		}*/

		return id;
	}

	public ArrayList<String> selectListColumnDetailsM(String ViewName,
			String[] Parameters) {
		ArrayList<String> id = new ArrayList<String>();
		Cursor cursor = this.myDataBase.query(ViewName, Parameters, null, null,
				null, null, null);
		if (cursor.moveToFirst()) {
			do {
				id.add(cursor.getString(0) + "<>" + cursor.getString(1));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return id;
	}

	public ArrayList<String> selectListDetails(String Query, String[] Parameters) {
		ArrayList<String> id = new ArrayList<String>();
		Cursor cur = myDataBase.rawQuery(Query, Parameters);
		System.out.println("Query " + Query);
		if (cur.moveToFirst()) {
			do {
				id.add(cur.getString(0));
				System.out.println("Value Id " + id);
			} while (cur.moveToNext());
		}
		if (cur != null && !cur.isClosed()) {
			cur.close();
		}
		return id;
	}

	public void CreateXMLString(String Table, String Columns, String KeyField,
			String FileName, String LogTxtFile) {
		try {
			
			
			String cntTbl = selectViewName("Select count(*) from " + Table
					+ " Where " + KeyField + " <> ''", null);
			da.WriteToTextFile(LogTxtFile, da.getTodaysDate() + " "
					+ " No of Records Sent for " + Table + " --> " + cntTbl);			
			
			if(!cntTbl.equals("0"))
			{

			da.WriteToTextFile(LogTxtFile, da.getTodaysDate() + " " + "Table "
					+ Table);
			da.WriteToTextFile(LogTxtFile, da.getTodaysDate() + " "
					+ "KeyField " + KeyField);
			da.WriteToTextFile(LogTxtFile, da.getTodaysDate() + " "
					+ "FileName " + FileName);
			DatabaseAssistant db = new DatabaseAssistant(myContext,
					this.myDataBase, FileName, Table, Columns, KeyField,
					LogTxtFile,"");
			db.exportData();
			ExecStatement("Update " + Table + " SET " + KeyField + "='X'"
					+ " Where " + KeyField + "='1'");
			getWritableDatabase().close();
			// File nFile = new File(Environment.getExternalStorageDirectory()
			// + "/NABHBACKUPv1" + "/" + Table + ".txt");
			// File oFile = new File(FileName);
			// IOUtils.copy(new FileInputStream(oFile),
			// new FileOutputStream(nFile));

			String path = Environment.getExternalStorageDirectory() + "/IBHAR/EATANDEARN/"
					+ da.getDate() + "/" + getTodaysDateinFormat();
			// + "/" + Table + ".txt";
			File nFile = new File(path);
			nFile.mkdirs();
			File file = new File(path + "/" + Table + ".txt");
			File oFile = new File(FileName);

			//IOUtils.copy(new FileInputStream(oFile), new FileOutputStream(file));
			
			}
		} catch (Exception e) {
			
			
String error=e.toString().trim();
			
			Toastinfo(myContext,error);
			System.out.println("Error " + e.toString());
			da.WriteToTextFile(LogTxtFile, da.getTodaysDate() + " "
					+ "Error in DatabaseAssistant  " + e.toString());
		}
	}

	
	
	
	
	
	
	
	public String getTodaysDateinFormat() {
		// 2012-06-18T15:37
		final Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		int hHour = c.get(Calendar.HOUR_OF_DAY);
		int mMin = c.get(Calendar.MINUTE);
		int pm = c.get(Calendar.AM_PM);
		String am_pm = "";
		if (pm == 0) {
			am_pm = "AM";
		} else {
			am_pm = "PM";
		}
		String DateStr = mYear + "" + timeformat(mMonth + 1) + ""
				+ timeformat(mDay) + "T" + timeformat(hHour) + "T"
				+ timeformat(mMin);
		return DateStr;
	}

	public String timeformat(int m) {
		String result = "";
		if (m < 10) {
			result = "0" + String.valueOf(m);
		} else {
			result = String.valueOf(m);
		}
		return result;
	}

	public String CreateXMLStringimage(String Table, String TableName,
			String[] Parameters, String[] Values) {
		String rowValue = "", whereQuery = "";
		// ArrayList<String> pVal = new ArrayList<String>();
		// ArrayList<String> pKey = new ArrayList<String>();
		Cursor cursor = this.myDataBase.rawQuery(TableName, Parameters);
		try {
			if (cursor != null) {
				int no_of_col = cursor.getColumnCount();
				if (cursor.moveToFirst()) {
					do {
						rowValue = rowValue + "<" + Table + ">";
						for (int i = 0; i < no_of_col; i++) {
							if (cursor.getString(i) == null
									|| cursor.getString(i).equals("null")) {
							} else {
								rowValue = rowValue + "<"
										+ cursor.getColumnName(i) + ">";
								rowValue = rowValue + cursor.getString(i);
								rowValue = rowValue + "</"
										+ cursor.getColumnName(i) + ">";
								// String[] pkeyArr = Values[1].split("~");
								// for (int j = 0; j < pkeyArr.length; j++) {
								// if
								// (cursor.getColumnName(i).equals(pkeyArr[j]))
								// {
								// pKey.add(cursor.getColumnName(i));
								// pVal.add(cursor.getString(i));
								// System.out.println("Pkey "
								// + cursor.getString(i));
								// }
								// }
							}
						}
						rowValue = rowValue + "</" + Table + ">";
						// for (int i = 0; i < pVal.size(); i++) {
						// whereQuery = whereQuery + pKey.get(i) + "= '"
						// + pVal.get(i) + "' AND ";
						// }
						// whereQuery = whereQuery.substring(0,
						// whereQuery.lastIndexOf(" AND "));
						// System.out.println("Update " + Values[0].trim()
						// + " SET " + Values[2].trim() + "=' '"
						// + " Where " + whereQuery);
					} while (cursor.moveToNext());
				}
				// System.out.println(rowValue);
				// ExecStatement("Update " + Values[0].trim() + " SET "
				// + Values[2].trim() + "='X'" + " Where "
				// + Values[2].trim() + "='1'");
			}
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		} catch (Exception e) {
			
String error=e.toString().trim();
			
			Toastinfo(myContext,error);
			
			System.out.println("Error " + e.toString());
		}
		return rowValue;
	}

	public boolean importDatabase(String dbPath) throws IOException {
		File newDb = new File(dbPath);
		File oldDb = new File(
				"/data/data/com.ibhar.www.eatandearn/databases/survey.db");
		//IOUtils.copy(new FileInputStream(oldDb), new FileOutputStream(newDb));
		System.out.println("Finished Success");
		return true;
	}

	public void finalize() throws Throwable {
		if (myDataBase != null) {
			myDataBase.close();
		}
	}

	/*---------------------------------------------------------------------------------*/

	public String selectmaxCaptureID() {
		String id = "";
		Cursor cursor = this.myDataBase.query("TMPRDI",
				new String[] { "MAX(PIAUDT)" }, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				id = cursor.getString(0);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return id;
	}

	public String selectmaxCaptureid() {
		String id = "";
		Cursor cursor = this.myDataBase.query("PMINCD",
				new String[] { "MAX(INCODE)" }, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				id = cursor.getString(0);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return id;
	}

	
	public void CreateXMLStringIMAGE(String Table, String Columns, String KeyField,
			String FileName, String LogTxtFile,String code) {
		try {
			
			String cntTbl = selectViewName("Select count(*) from "+ Table +" Where IMCODE='"+code.trim()+"'", null);
			da.WriteToTextFile(LogTxtFile, da.getTodaysDate() + "  No of Records Sent for "+Table+" --> " + cntTbl);			
			
		/*	if(!cntTbl.equals("0"))
			{*/
			
			da.WriteToTextFile(LogTxtFile, da.getTodaysDate() + " " + "Table "+ Table);
			da.WriteToTextFile(LogTxtFile, da.getTodaysDate() + " "	+ "KeyField " + KeyField);
			da.WriteToTextFile(LogTxtFile, da.getTodaysDate() + " "	+ "FileName " + FileName);
			DatabaseAssistant db = new DatabaseAssistant(myContext,this.myDataBase, FileName, Table, Columns, KeyField,LogTxtFile,code.trim());
			db.exportData1();
			ExecStatement("Update " + Table + " SET " + KeyField + "='X'  where IMCODE='"+code.trim()+"' ");
			getWritableDatabase().close();
			// File nFile = new File(Environment.getExternalStorageDirectory()
			// + "/NABHBACKUPv1" + "/" + Table + ".txt");
			// File oFile = new File(FileName);
			// IOUtils.copy(new FileInputStream(oFile),
			// new FileOutputStream(nFile));

			String path = Environment.getExternalStorageDirectory()
					+ "/IBHAR/EATANDEARN/" + da.getDate() + "/"
					+ getTodaysDateinFormat();
			// + "/" + Table + ".txt";
			File nFile = new File(path);
			nFile.mkdirs();
			File file = new File(path+"/" +Table+code.trim()+".txt");
			File oFile = new File(FileName);

			//IOUtils.copy(new FileInputStream(oFile), new FileOutputStream(file));
		/*	}*/
		} catch (Exception e) {
			System.out.println("Error " + e.toString());
			da.WriteToTextFile(LogTxtFile, da.getTodaysDate() + " "
					+ "Error in DatabaseAssistant  " + e.toString());
		}
	}

	
	
	
	public String selectmaxCaptureStales() {
		String id = "";
		Cursor cursor = this.myDataBase.query("TMPRST",
				new String[] { "MAX(PIAUDT)" }, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				id = cursor.getString(0);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return id;
	}

	public String selectmaxCaptureIMG() {
		String id = "";
		Cursor cursor = this.myDataBase.query("PMIMAG",
				new String[] { "MAX(IMCODE)" }, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				id = cursor.getString(0);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return id;
	}

	public long insertPMIMAG(String imcode, String impath, String imincd,
			String code, String dt1, String tm1, String code1, String dt2,
			String tm2) {
		this.insertIMAG.bindString(1, imcode);
		this.insertIMAG.bindString(2, impath);
		this.insertIMAG.bindString(3, imincd);
		this.insertIMAG.bindString(4, "N");
		this.insertIMAG.bindString(5, "1");
		this.insertIMAG.bindString(6, code);
		this.insertIMAG.bindString(7, dt1);
		this.insertIMAG.bindString(8, tm1);
		this.insertIMAG.bindString(9, code1);
		this.insertIMAG.bindString(10, dt2);
		this.insertIMAG.bindString(11, tm2);
		return this.insertIMAG.executeInsert();
	}

	public ArrayList<Bitmap> selectTemp(String secid) {
		ArrayList<Bitmap> id = new ArrayList<Bitmap>();
		try {
			Cursor cursor = this.myDataBase.query("PMIMAG",
					new String[] { "IMPATH" }, "IMINCD =? AND IMRCST='N'",
					new String[] { secid }, null, null, null);
			if (cursor.moveToFirst()) {
				do {
					byte[] decodedString = Base64.decode(cursor.getString(0),
							Base64.DEFAULT);
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 4;
					Bitmap bitmap = BitmapFactory.decodeByteArray(
							decodedString, 0, decodedString.length);
					id.add(bitmap);
				} while (cursor.moveToNext());
			}
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
String error=e.toString().trim();
			
			Toastinfo(myContext,error);

		}

		return id;
	}

	public ArrayList<Bitmap> selectTemp1(String secid) {
		ArrayList<Bitmap> id = new ArrayList<Bitmap>();
		Cursor cursor = this.myDataBase.query("ASIMAG",
				new String[] { "IMIMAG" }, "IMRESP =? AND length(IMRESP)!=0",
				new String[] { secid }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				byte[] imageAsBytes = Base64.decode(cursor.getString(0),
						Base64.DEFAULT);
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 4;
				Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0,
						imageAsBytes.length);
				//
				//
				// Bitmap bitmap = BitmapFactory.decodeByteArray(
				// cursor.getBlob(0), 0, cursor.getBlob(0).length);
				id.add(bitmap);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return id;
	}

	public String selectmaxCapture() {
		String id = "";
		Cursor cursor = this.myDataBase.query("TMCMPT",
				new String[] { "MAX(PIAUDT)" }, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				id = cursor.getString(0);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return id;
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

	public String selectlistbystring(String ViewName, String[] Parameters, int n) {
		String id = "";
		String rowValue = " ";
		Cursor cursor = this.myDataBase.rawQuery(ViewName, Parameters);
		try {
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					do {
						for (int i = 0; i < n; i++) {
							rowValue = rowValue + cursor.getString(i) + "%";
						}
						id = id + rowValue;
						// System.out.println(rowValue);
						rowValue = " ";
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

	public String CreateXMLString(String Table, String TableName,
			String[] Parameters, String[] Values) {
		String rowValue = "";
		Cursor cursor = this.myDataBase.rawQuery(TableName, Parameters);
		System.out.println("Select Value " + TableName);
		try {
			if (cursor != null) {
				int no_of_col = cursor.getColumnCount();
				if (cursor.moveToFirst()) {
					do {
						rowValue = rowValue + "<" + Table + ">";
						for (int i = 0; i < no_of_col; i++) {
							if (cursor.getString(i) == null
									|| cursor.getString(i).equals("null")) {
							} else {
								rowValue = rowValue + "<"
										+ cursor.getColumnName(i) + ">";
								rowValue = rowValue + cursor.getString(i);
								rowValue = rowValue + "</"
										+ cursor.getColumnName(i) + ">";
							}
						}
						rowValue = rowValue + "</" + Table + ">";
					} while (cursor.moveToNext());
				}
			}
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
			ExecStatement("Update " + Values[0].trim() + " SET "
					+ Values[2].trim() + "='X'" + " Where " + Values[2].trim()
					+ "='1'");
		} catch (Exception e) {
			System.out.println("Error " + e.toString());
		}
		return rowValue;
	}



	public String selectViewName1(String Query, String[] Parameters) {


		Log.d("qu",Query);
		Log.d("pa", String.valueOf(Parameters));
		/*String q = "SELECT * FROM todo WHERE category='" ;

		Cursor cursor = myDataBase.rawQuery(q, null);

		if (cursor != null) {
			cursor.moveToFirst();
		}
		return String.valueOf(cursor);*/
		return "1";
	}

}
