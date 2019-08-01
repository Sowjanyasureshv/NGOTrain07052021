package lokas.ngotrain;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DatabaseAssistant {
	// private static final String EXPORT_FILE_NAME = "/sdcard/DB/export.xml";

	private Context _ctx;
	private SQLiteDatabase _db;
	private Exporter _exporter;
	String tableName = "", _lockKey = "", _LogTxtFile = "", _column = "",code="";
	DataAccess da;
	DataHelper1 dh;

	public DatabaseAssistant(Context ctx, SQLiteDatabase db, String FileName,
							 String table, String column, String lockKey, String LogTxt, String c1) {
		_ctx = ctx;
		_db = db;
		_lockKey = lockKey;
		_LogTxtFile = LogTxt;
		tableName = table;
		_column = column;
		code=c1;
		da = new DataAccess();
		dh = new DataHelper1(ctx);
		try {
			dh.openDataBase();
			String myPath = db.getPath();
			db = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
			// create a file on the sdcard to export the
			// database contents to
			File myFile = new File(FileName);
			myFile.createNewFile();

			FileOutputStream fOut = new FileOutputStream(myFile);
			BufferedOutputStream bos = new BufferedOutputStream(fOut);
			_exporter = new Exporter(bos);
			// exportData();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void exportData() {
		log("Exporting Data");

		try {
			_exporter.startDbExport(_db.getPath());

			exportTable(tableName);
			_exporter.endDbExport();
			_exporter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public void exportData1() {
		log("Exporting Data");

		try {
			_exporter.startDbExport(_db.getPath());

			exportTable(tableName,code);
			_exporter.endDbExport();
			_exporter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void exportTable(String tableName,String Code) throws IOException {
		String sql = "";
		sql = "Select * from "+tableName+" Where IMCODE='"+Code.trim()+"'";
		log("Query " + sql);
		Cursor cur = _db.rawQuery(sql, new String[0]);
		int numcols = cur.getColumnCount();
		log("Query " + numcols);
		log("Start exporting table " + tableName);
		cur.moveToFirst();
		while (cur.getPosition() < cur.getCount()) {
			_exporter.startRow(tableName);
			String name;
			String val;
	
			for (int idx = 0; idx < numcols; idx++) {
				name = cur.getColumnName(idx);

				val = cur.getString(idx);
				System.out.println("name===" + name);
				System.out.println("value===" + val);
				if (val != null) {
					_exporter.addColumn(name, val);
				}
			}
			_exporter.endRow(tableName);
			cur.moveToNext();
		}
		cur.close();
	}

	
	
	

	private void exportTable(String tableName) throws IOException {
		String sql = "";
		sql = "Select " + _column + " from " + tableName + " Where " + _lockKey
				+ " <> ''";
		Cursor cur = _db.rawQuery(sql, new String[0]);
		int numcols = cur.getColumnCount();
		log("Start exporting table " + tableName);
		cur.moveToFirst();
		while (cur.getPosition() < cur.getCount()) {
			_exporter.startRow(tableName);
			String name;
			String val;
			for (int idx = 0; idx < numcols; idx++) {
				name = cur.getColumnName(idx);

				val = cur.getString(idx);
				System.out.println("name===" + name);
				System.out.println("value===" + val);
				if (val != null) {
					_exporter.addColumn(name, val);
				}
			}
			_exporter.endRow(tableName);
			cur.moveToNext();
		}
		cur.close();
		String cntTbl = dh.selectViewName("Select count(*) from " + tableName
				+ " Where " + _lockKey + " <> ''", null);
		da.WriteToTextFile(_LogTxtFile, da.getTodaysDate() + " "
				+ " No of Records Sent for " + tableName + " --> " + cntTbl);
	}

	private void log(String msg) {
		Log.d("DatabaseAssistant", msg);
		da.WriteToTextFile(_LogTxtFile, da.getTodaysDate() + " " + "Log " + msg);
	}

	class Exporter {

		private static final String CLOSING_WITH_TICK = ">";
		private static final String START_DB = "<DATA";
		private static final String END_DB = "</DATA>";
		private static final String START_TABLE = "<";
		private static final String END_TABLE = "</";
		private static final String START_COL = "<";
		private static final String END_COL = "</";

		private BufferedOutputStream _bos;

		public Exporter() throws FileNotFoundException {
		}

		public Exporter(BufferedOutputStream bos) {
			_bos = bos;
		}

		public void close() throws IOException {
			if (_bos != null) {
				_bos.close();
			}
		}

		public void startDbExport(String dbName) throws IOException {
			String stg = START_DB + CLOSING_WITH_TICK;
			_bos.write(stg.getBytes());
		}

		public void endDbExport() throws IOException {
			_bos.write(END_DB.getBytes());
		}

		public void startRow(String tableName) throws IOException {
			String stg = START_TABLE + tableName + CLOSING_WITH_TICK;
			_bos.write(stg.getBytes());
		}

		public void endRow(String tableName) throws IOException {
			String etg = END_TABLE + tableName + CLOSING_WITH_TICK;
			_bos.write(etg.getBytes());
		}

		public void addColumn(String name, String val) throws IOException {
			String stg = START_COL + name + CLOSING_WITH_TICK + val + END_COL
					+ name + CLOSING_WITH_TICK;
			_bos.write(stg.getBytes());
		}
	}

	class Importer {
	}
}