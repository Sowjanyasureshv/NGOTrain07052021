package lokas.ngotrain;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.DatabaseUtils.InsertHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;

public class DataAccess extends Activity {
	private InsertHelper ih;
	private Context myContext;
	DataHelper1 dh;
	String LogTxtFile = Environment.getExternalStorageDirectory() + "/IBHAR/EATANDEARN/"
			+ getDate() + "Log.txt";

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		dh = new DataHelper1(this);
	}

	public DataAccess() {
	}
	
	
	

	public String getmonthDateFormatMonth1(String Dte) {
		String Res = "";
		if (Dte.length() == 8) {
			String Yr = Dte.substring(0, 4);
			String Mn = getmonthString(Integer.parseInt(Dte.substring(4, 6)) - 1);
			// if (Integer.parseInt(Mn) < 10) {
			// Mn = "0" + Mn;
			// }
			String Dt = Dte.substring(6, 8);
			// if (Integer.parseInt(Dt) < 10) {
			// Dt = "0" + Dt;
			// }
			Res = Yr + "-"+ Mn ;
		}
		return Res;
	}


	public String getcurrentdate() {
		final Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		String DateStr = pad(mDay) + "-" + getmonthString(mMonth) + "-"
				+ String.valueOf(mYear);
		return DateStr;
	}

	

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	public String getTodaysDate() {
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
		String DateStr = mDay + "-" + getmonthString(mMonth) + "-" + mYear
				+ " " + timeformat(hHour) + ":" + timeformat(mMin) + " "
				+ am_pm;
		return DateStr;
	}

	
	
	
	
	
	
	
	
	public String getIMEI() {
		String IMEI="";
		
		TelephonyManager TM=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		
		IMEI=TM.getDeviceId();
		
		if(IMEI!=null)

		{
			IMEI=IMEI.trim();
		}
		else
		{
			IMEI="";
		}
		return IMEI;
	}

	public String getPhone() {
		String IMEI="";
		
		TelephonyManager TM=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		
		IMEI=TM.getLine1Number();
		
		if(IMEI!=null)

		{
			IMEI=IMEI.trim();
		}
		else
		{
			IMEI="";
		}
		return IMEI;
	}

	public String getManufacture() {
		String IMEI="";
		
		//TelephonyManager TM=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		
		IMEI= Build.MANUFACTURER;
		
		if(IMEI!=null)

		{
			IMEI=IMEI.trim();
		}
		else
		{
			IMEI="";
		}
		return IMEI;
	}

	public String getMobMod() {
		String IMEI="";
		
		//TelephonyManager TM=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		
		IMEI= Build.MODEL;
		
		if(IMEI!=null)

		{
			IMEI=IMEI.trim();
		}
		else
		{
			IMEI="";
		}
		return IMEI;
	}

	public String getAndroidVersion() {
		String IMEI="";
		
		//TelephonyManager TM=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		
		IMEI= Build.VERSION.RELEASE;
		
		if(IMEI!=null)

		{
			IMEI=IMEI.trim();
		}
		else
		{
			IMEI="";
		}
		return IMEI;
	}

	public String getVersionName() {
		String IMEI="";
		
		//TelephonyManager TM=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		
	
		try {
			IMEI = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		

		
		
		if(IMEI!=null)

		{
			IMEI=IMEI.trim();
		}
		else
		{
			IMEI="";
		}
		return IMEI;
	}

	
	public void Config(DataHelper1 dh,String UserCode,String IMEI,String PHONE,String Androidversion,String Model,String Manufacture,String VersionName)
	{
		/*String Max_ID = dh
				.selectViewName(
						"Select Max(APAUDT) from PMASUS Where substr(APAUDT,2,11) = ?",
						new String[] { UserCode });*/
		String userMaxID = dh
				.generateUserCode(UserCode,
						getDate(),
						getTimeFormat());

	/*	if ((userMaxID == null)
				|| (userMaxID.equals("null"))) {
			userMaxID = userMaxID.trim();
*/
			dh.ExecStatement("INSERT INTO PMASUS(APAUDT,APIMEI,APUSPH,APANVR,APMODL,APMANU,APUSER,APSYDT,APSYTM,APCFIG,APAPNA,APVERS,APCRBY,APCRDT,APCRTM,APDNBY,APDNDT,APDNTM,APRCST,APLOCK) Values"
					+ "('"
					+ userMaxID
					+ "','"
					+ IMEI
					+ "','"
					+ PHONE
					+ "','"
					+ Androidversion
					+ "','"
					+ Model
					+ "','"
					+ Manufacture
					+ "','"
					+ UserCode
					+ "','"
					+ getDate()
					+ "','"
					+ getTimeFormat()+ "','C','APPOINTMENT','"
					+ VersionName
					+ "','"
					+ UserCode
					+ "','"
					+ getDate()
					+ "','"
					+ getTimeFormat()
					+ "','"
					+ UserCode
					+ "','"
					+ getDate()
					+ "','"
					+ getTimeFormat()
					+ "','N','1')");
/*
		} else {

			if (Long.parseLong(Max_ID
					.substring(12, 20)) > Long
					.parseLong(userMaxID
							.substring(12, 20))) {
				long MaxValue = Long
						.parseLong(Max_ID
								.substring(12,
										20)) + 1;
				userMaxID = "M"
						+ UserCode
						+ String.format("%08d",
								MaxValue);
				dh.ExecStatement("Update PMUSER set USNUMB ='"
						+ MaxValue
						+ "',USDNBY = '"
						+ UserCode
						+ "',USDNDT='"
						+ getDate()
						+ "',USDNTM='"
						+ getTimeFormat()
						+ "',USLOCK='1' Where USCODE ='"
						+ UserCode + "'");

				dh.ExecStatement("INSERT INTO PMASUS(APAUDT,APIMEI,APUSPH,APANVR,APMODL,APMANU,APUSER,APSYDT,APSYTM,APCFIG,APAPNA,APVERS,APCRBY,APCRDT,APCRTM,APDNBY,APDNDT,APDNTM,APRCST,APLOCK) Values"
						+ "('"
						+ userMaxID
						+ "','"
						+ IMEI
						+ "','"
						+ PHONE
						+ "','"
						+ Androidversion
						+ "','"
						+ Model
						+ "','"
						+ Manufacture
						+ "','"
						+ UserCode
						+ "','"
						+ getDate()
						+ "','"
						+ getTimeFormat()+ "','C','DWM','"
						+ VersionName
						+ "','"
						+ UserCode
						+ "','"
						+ getDate()
						+ "','"
						+ getTimeFormat()
						+ "','"
						+ UserCode
						+ "','"
						+ getDate()
						+ "','"
						+ getTimeFormat()
						+ "','N','1')");

			}

		}
*/

	}
	
	
	public void Sync(DataHelper1 dh,String UserCode,String IMEI,String PHONE,String Androidversion,String Model,String Manufacture,String VersionName)
	{
		String Max_ID = dh
				.selectViewName(
						"Select Max(APAUDT) from PMASUS Where substr(APAUDT,2,11) = ?",
						new String[]{UserCode});
		String userMaxID = dh
				.generateUserCode1(UserCode,
						getDate(),
						getTimeFormat());

		if ((Max_ID == null)
				|| (Max_ID.equals("null"))) {
			userMaxID = userMaxID.trim();

			dh.ExecStatement("INSERT INTO PMASUS(APAUDT,APIMEI,APUSPH,APANVR,APMODL,APMANU,APUSER,APSYDT,APSYTM,APCFIG,APAPNA,APVERS,APCRBY,APCRDT,APCRTM,APDNBY,APDNDT,APDNTM,APRCST,APLOCK) Values"
					+ "('"
					+ userMaxID
					+ "','"
					+ IMEI
					+ "','"
					+ PHONE
					+ "','"
					+ Androidversion
					+ "','"
					+ Model
					+ "','"
					+ Manufacture
					+ "','"
					+ UserCode
					+ "','"
					+ getDate()
					+ "','"
					+ getTimeFormat()+ "','C','APPOINTMENT','"
					+ VersionName
					+ "','"
					+ UserCode
					+ "','"
					+ getDate()
					+ "','"
					+ getTimeFormat()
					+ "','"
					+ UserCode
					+ "','"
					+ getDate()
					+ "','"
					+ getTimeFormat()
					+ "','N','1')");

		} else {

			if (Long.parseLong(Max_ID
					.substring(12, 20)) > Long
					.parseLong(userMaxID
							.substring(12, 20))) {
				long MaxValue = Long
						.parseLong(Max_ID
								.substring(12,
										20)) + 1;
				userMaxID = "M"
						+ UserCode
						+ String.format("%08d",
						MaxValue);
				/*dh1.ExecStatement("Update PMUSER set USNUMB ='"
						+ MaxValue
						+ "',USDNBY = '"
						+ UserCode
						+ "',USDNDT='"
						+ getDate()
						+ "',USDNTM='"
						+ getTimeFormat()
						+ "',USLOCK='1' Where USCODE ='"
						+ UserCode + "'");
*/
				dh.ExecStatement("INSERT INTO PMASUS(APAUDT,APIMEI,APUSPH,APANVR,APMODL,APMANU,APUSER,APSYDT,APSYTM,APCFIG,APAPNA,APVERS,APCRBY,APCRDT,APCRTM,APDNBY,APDNDT,APDNTM,APRCST,APLOCK) Values"
						+ "('"
						+ userMaxID
						+ "','"
						+ IMEI
						+ "','"
						+ PHONE
						+ "','"
						+ Androidversion
						+ "','"
						+ Model
						+ "','"
						+ Manufacture
						+ "','"
						+ UserCode
						+ "','"
						+ getDate()
						+ "','"
						+ getTimeFormat()+ "','C','APPOINTMENT','"
						+ VersionName
						+ "','"
						+ UserCode
						+ "','"
						+ getDate()
						+ "','"
						+ getTimeFormat()
						+ "','"
						+ UserCode
						+ "','"
						+ getDate()
						+ "','"
						+ getTimeFormat()
						+ "','N','1')");

			}

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
		String DateStr = mYear + "-" + timeformat(mMonth + 1) + "-"
				+ timeformat(mDay) + "T" + timeformat(hHour) + ":"
				+ timeformat(mMin);
		return DateStr;
	}

	public String getTodaysDateinMonthFormat(int DateDiff) {
		// 2012-06-18T15:37
		final Calendar c = Calendar.getInstance();

		c.add(Calendar.DATE, DateDiff);
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		String Mn = getmonthString(mMonth);
		String DateStr = timeformat(mDay) + "-" + Mn + "-" + mYear;
		// String DateStr = timeformat(mDay) + "-" + timeformat(mMonth + 1) +
		// "-"
		// + mYear;
		return DateStr;
	}


	public String getPreviousOrNextDATE	(String Date,int DateDiff) {
		// 2012-06-18T15:37
		String DateStr ="";

		try{
			Calendar c = Calendar.getInstance();


			String Dte = Date.substring(6, 8);
			String Mon = Date.substring(4, 6);
			String Yer = Date.substring(0, 4);

			int D=-DateDiff;



			//Date date =new Date (20140915);

			c.set(Integer.parseInt(Yer), Integer.parseInt(Mon)-1, Integer.parseInt(Dte));
			c.add(Calendar.DATE, D);


			int mYear = c.get(Calendar.YEAR);

			int mMonth = c.get(Calendar.MONTH);
			int mDay = c.get(Calendar.DAY_OF_MONTH);
			String Mn = getmonthString(mMonth);
			DateStr = String.valueOf(mYear)+ timeformat(mMonth + 1)+timeformat(mDay);
			// String DateStr = timeformat(mDay) + "-" + timeformat(mMonth + 1) +
			// "-"
			// + mYear;
			return DateStr;

		}
		catch (Exception e) {
			// TODO: handle exception

			String error = e.toString().trim();

			dh.Toastinfo(getBaseContext(), error);
		}

		return DateStr;

	}



	public String getTodaysDateinMonthFormat1(String Date,int DateDiff) {
		// 2012-06-18T15:37
		String DateStr ="";
		
		try{
		 Calendar c = Calendar.getInstance();
		
		
		String Dte = Date.substring(6, 8);
		String Mon = Date.substring(4, 6);
		String Yer = Date.substring(0, 4);
		
		int D=-DateDiff;
		
		
		
		//Date date =new Date (20140915);
		
        c.set(Integer.parseInt(Yer), Integer.parseInt(Mon)-1, Integer.parseInt(Dte));
		c.add(Calendar.DATE, D);
		
		
		int mYear = c.get(Calendar.YEAR);
	
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		String Mn = getmonthString(mMonth);
		 DateStr = String.valueOf(mYear)+ timeformat(mMonth + 1)+timeformat(mDay);
		// String DateStr = timeformat(mDay) + "-" + timeformat(mMonth + 1) +
		// "-"
		// + mYear;
		return DateStr;
		
		}
		catch (Exception e) {
			// TODO: handle exception
			
			String error = e.toString().trim();

			dh.Toastinfo(getBaseContext(), error);
		}
		
		return DateStr;
		
	}


	public String getTodaysDateinMonthFormatweekend() {
		// 2012-06-18T15:37
		final Calendar c = Calendar.getInstance();
		int DateDiff, d = 0;
		DateDiff = c.get(Calendar.DAY_OF_WEEK);

		if (DateDiff == 1) {
			d = 6;
		} else if (DateDiff == 2) {
			d = 5;
		} else if (DateDiff == 3) {
			d = 4;
		} else if (DateDiff == 4) {
			d = 3;
		} else if (DateDiff == 5) {
			d = 2;
		} else if (DateDiff == 6) {
			d = 1;
		} else if (DateDiff == 7) {
			d = 0;
		}

		c.add(Calendar.DATE, d);
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		String Mn = getmonthString(mMonth);
		String DateStr = timeformat(mDay) + "-" + Mn + "-" + mYear;
		// String DateStr = timeformat(mDay) + "-" + timeformat(mMonth + 1) +
		// "-"
		// + mYear;
		return DateStr;
	}

	public String getTodaysDateinMonthFormatendofmonth() {
		// 2012-06-18T15:37
		final Calendar c = Calendar.getInstance();
		// int day=c.get(Calendar.);
		// c.add(Calendar.DATE, day);

		int day = c.getActualMaximum(Calendar.DAY_OF_MONTH);
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = day;
		String Mn = getmonthString(mMonth);
		String DateStr = timeformat(mDay) + "-" + Mn + "-" + mYear;
		// String DateStr = timeformat(mDay) + "-" + timeformat(mMonth + 1) +
		// "-"
		// + mYear;
		return DateStr;
	}

	public String getDate() {
		final Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		String DateStr = mYear + timeformat(mMonth + 1) + timeformat(mDay);
		return DateStr;
	}

	public String getDate(String DateDiff) {
		final Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, Integer.parseInt(DateDiff));
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		String DateStr = mYear + timeformat(mMonth + 1) + timeformat(mDay);
		System.out.println("Date is " + DateStr);
		return DateStr;
	}

	public String getDateinFormat(String Datestr) {
		String Dte = Datestr.substring(0, 2);
		String Mon = Datestr.substring(3, 5);
		String Yer = Datestr.substring(6, 10);
		String DateStr = Yer + Mon + Dte;
		System.out.println("Date is " + DateStr);
		return DateStr;
	}

	public String getDateinFormatReve(String Datestr) {
		String Dte = Datestr.substring(0, 2);
		String Mon = Datestr.substring(3, 5);
		String Yer = Datestr.substring(6, 10);
		String DateStr = Yer + Mon + Dte;
		System.out.println("Date is " + DateStr);
		return DateStr;
	}

	public String getDateinDayFormat(String Datestr) {
		String Dte = Datestr.substring(6, 8);
		String Mon = Datestr.substring(4, 6);
		String Yer = Datestr.substring(0, 4);
		String Mn = getmonthString(Integer.parseInt(Mon) - 1);
		String DateStr = Dte + "-" + Mn + "-" + Yer;
		System.out.println("Date is " + DateStr);
		return DateStr;
	}
	/* this method returns the month year form e.g Feb 2016*/
	public String getMn_Yr(String Datestr) {
		String Dte = Datestr.substring(6, 8);
		String Mon = Datestr.substring(4, 6);
		String Yer = Datestr.substring(0, 4);
		String Mn = getmonthString(Integer.parseInt(Mon) - 1);
		String DateStr =Mn+" "+Yer;
		System.out.println("Date is " + DateStr);
		return DateStr;
	}
	public String getDateYr_Mn_Dt(String Datestr) {
		String Dte = Datestr.substring(6, 8);
		String Mon = Datestr.substring(4, 6);
		String Yer = Datestr.substring(0, 4);
		String Mn = getmonthString(Integer.parseInt(Mon) - 1);
		String DateStr = Yer + "-" + Mon + "-" + Dte;
		System.out.println("Date is " + DateStr);
		return DateStr;
	}

	public String getTime() {
		final Calendar c = Calendar.getInstance();
		int hHour = c.get(Calendar.HOUR_OF_DAY);
		int mMin = c.get(Calendar.MINUTE);
		String DateStr = timeformat(hHour) + timeformat(mMin);
		return DateStr;
	}

	public String getTimeFormat() {
		final Calendar c = Calendar.getInstance();
		int hHour = c.get(Calendar.HOUR_OF_DAY);
		int mMin = c.get(Calendar.MINUTE);
		String DateStr = timeformat(hHour) + ":" + timeformat(mMin);
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
	
	public String dateformat(int m) {
		String result = "";
		if (m < 10) {
			result = "0" + String.valueOf(m);
		} else {
			result = String.valueOf(m);
		}
		return result;
	}

	public String getmonthString(int mMonth) {
		String monthh = "";
		if (mMonth == 0) {
			monthh = "Jan";
		} else if (mMonth == 1) {
			monthh = "Feb";
		} else if (mMonth == 2) {
			monthh = "Mar";
		} else if (mMonth == 3) {
			monthh = "Apr";
		} else if (mMonth == 4) {
			monthh = "May";
		} else if (mMonth == 5) {
			monthh = "Jun";
		} else if (mMonth == 6) {
			monthh = "Jul";
		} else if (mMonth == 7) {
			monthh = "Aug";
		} else if (mMonth == 8) {
			monthh = "Sep";
		} else if (mMonth == 9) {
			monthh = "Oct";
		} else if (mMonth == 10) {
			monthh = "Nov";
		} else if (mMonth == 11) {
			monthh = "Dec";
		}
		return monthh;
	}

	public String getmonthvalue(String mMonth) {
		String monthh = "";
		if (mMonth.equals("Jan")) {
			monthh = "01";
		} else if (mMonth.equals("Feb")) {
			monthh = "02";
		} else if (mMonth.equals("Mar")) {
			monthh = "03";
		} else if (mMonth.equals("Apr")) {
			monthh = "04";
		} else if (mMonth.equals("May")) {
			monthh = "05";
		} else if (mMonth.equals("Jun")) {
			monthh = "06";
		} else if (mMonth.equals("Jul")) {
			monthh = "07";
		} else if (mMonth.equals("Aug")) {
			monthh = "08";
		} else if (mMonth.equals("Sep")) {
			monthh = "09";
		} else if (mMonth.equals("Oct")) {
			monthh = "10";
		} else if (mMonth.equals("Nov")) {
			monthh = "11";
		} else if (mMonth.equals("Dec")) {
			monthh = "12";
		}
		return monthh;
	}

	public boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}



	public String getDay_dt_mon(String Dte) {
		String Res = "";
		if (Dte.length() == 8) {
			String Yr = Dte.substring(2, 4);
			String Mn = getmonthString(Integer.parseInt(Dte.substring(4, 6)) - 1);
			// if (Integer.parseInt(Mn) < 10) {
			// Mn = "0" + Mn;
			// }
			String Dt = Dte.substring(6, 8);
			// if (Integer.parseInt(Dt) < 10) {
			// Dt = "0" + Dt;
			// }
			final Calendar c = Calendar.getInstance();
			c.set(Integer.parseInt(Dte.substring(0, 4)), Integer.parseInt(Dte.substring(4, 6)) - 1, Integer.parseInt(Dte.substring(6, 8)));
			int day = c.get(Calendar.DAY_OF_WEEK);




			Res = DayOfWeek(day) + "," + Dt + " " + Mn +" '"+Yr;
		}
		return Res;
	}

public String DayOfWeek(int value)
{
	String Day="";

	switch (value) {
		case Calendar.SUNDAY:
			Day="Sun";
			break;
		case Calendar.MONDAY:
			Day="Mon";
			break;
		case Calendar.TUESDAY:
			Day="Tue";
			break;
		case Calendar.WEDNESDAY:
			Day="Wed";
			break;
		case Calendar.THURSDAY:
			Day="Thu";
			break;
		case Calendar.FRIDAY:
			Day="Fri";
			break;
		case Calendar.SATURDAY:
			Day="Sat";
			break;
	}


	return Day;
}

	public String getDateFormat(String Dte) {
		String Res = "";
		if (Dte.length() == 8) {
			String Yr = Dte.substring(0, 4);
			String Mn = getmonthString(Integer.parseInt(Dte.substring(4, 6)) - 1);
			// if (Integer.parseInt(Mn) < 10) {
			// Mn = "0" + Mn;
			// }
			String Dt = Dte.substring(6, 8);
			// if (Integer.parseInt(Dt) < 10) {
			// Dt = "0" + Dt;
			// }
			Res = Dt + "-" + Mn + "-" + Yr;
		}
		return Res;
	}
	
	
	public String getmonthDateFormat(String Dte) {
		String Res = "";
		if (Dte.length() == 8) {
			String Yr = Dte.substring(0, 4);
			String Mn = getmonthString(Integer.parseInt(Dte.substring(4, 6)) - 1);
			// if (Integer.parseInt(Mn) < 10) {
			// Mn = "0" + Mn;
			// }
			String Dt = Dte.substring(6, 8);
			// if (Integer.parseInt(Dt) < 10) {
			// Dt = "0" + Dt;
			// }
			Res = Dt + "-" + Mn ;
		}
		return Res;
	}


	public String getyr_mn_dt(String Dte) {
		String Res = "";
		if (Dte.length() == 8) {
			String Yr = Dte.substring(0, 4);
			String Mn = Dte.substring(4, 6);
			// if (Integer.parseInt(Mn) < 10) {
			// Mn = "0" + Mn;
			// }
			String Dt = Dte.substring(6, 8);
			// if (Integer.parseInt(Dt) < 10) {
			// Dt = "0" + Dt;
			// }
			Res = Yr + "-" + Mn +"-"+Dt;
		}
		return Res;
	}

	public String gettime_AMPM5(String time) {
		String Res = "";
		if (time.trim().replace("AM","").replace("PM","").length() == 5) {

			String array[]=time.split(":");
			String Hr=array[0].trim();
			String Mn=array[1].trim();
			String Am_Pm="";

			String Hour="";

			int hr= Integer.parseInt(Hr);

			if(hr>11)
			{
				Am_Pm="PM";
			}
			else
			{
				Am_Pm="AM";
			}


			if(hr>12)
			{
				if((hr-12)<10)
				{
					Hour = "0"+ String.valueOf((hr - 12));
				}
				else {
					Hour = String.valueOf((hr - 12));
				}
			}
			else
			{
				Hour=Hr.trim();
			}


			Res = Hour + ":" + Mn +" "+Am_Pm;
		}
		return Res;
	}




	public String getdt_monstr_yr(String time) {
		String Res = "";
		if (time.trim().length() > 18) {

/*
			String array[]=time.split(" ");
			String dt=array[0].trim();
			String tm=array[1].trim().replace("AM","").replace("PM", "");*/

			String dt= time.substring(0,10);

			dt=dt.replace("-", "").trim();

			String year="",month="",day="";

			year=dt.substring(0, 4).trim();
			month=dt.substring(4,6).trim();
			day=dt.substring(6,8).trim();
			String append="";

			int i= Integer.parseInt(day);

			if((i==1))
			{
				append="ST";
			}
			else if((i==2))
			{
				append="ND";
			}
			else if((i==3))
			{
				append=" rd";
			}
			else
			{
				append=" th";

			}

			Res = day+append+" "+getmonthString(Integer.parseInt(month) - 1)+" "+ year;

		}
		return Res;
	}


	public String gettime_AMPMR(String time) {
		String Res = "";
		if (time.trim().length() == 8) {


			String array[]=time.split(":");
			String Hr=array[0].trim();
			String Mn=array[1].trim().replace("AM","").replace("PM", "");

			String Hour="";
			if(time.contains("AM"))
			{

				Res = Hr + ":" + Mn ;
			}
			else
			{
				Hour = String.valueOf((Integer.parseInt(Hr) + 12));
				Res = Hour + ":" + Mn ;
			}

		}
		return Res;
	}




	public String gettime_AMPM(String time) {
		String Res = "";
		if ((time.length() == 8)||((time.length() == 5))) {

			String array[]=time.split(":");
			String Hr=array[0].trim();
			String Mn=array[1].trim().replace("AM","").replace("PM","");
			String Am_Pm="";

			String Hour="";

			int hr= Integer.parseInt(Hr);

			if(hr>11)
			{
				Am_Pm="PM";
			}
			else
			{
				Am_Pm="AM";
			}


			if(hr>12)
			{
				if((hr-12)<10)
				{
					Hour = "0"+ String.valueOf((hr - 12));
				}
				else {
					Hour = String.valueOf((hr - 12));
				}
			}
			else
			{
				Hour=Hr.trim();
			}


			Res = Hour + ":" + Mn +" "+Am_Pm;
		}
		return Res;
	}


	public String gettime_24(String time) {
		String Res = "";
		if (time.trim().length() == 8) {

			if(time.contains("PM"))
			{
				time=time.replace("PM","");


				String array[]=time.split(":");
				String Hr=array[0].trim();
				String Mn=array[1].trim();


				int hr = Integer.parseInt(Hr)+12;

				time= String.valueOf(hr)+":"+Mn;

			}
			else
			{
				time=time.replace("AM", "");
			}


			Res = time;
		}
		return Res;
	}

	public String getDateFormatfullyear(String Dte) {
		String Res = "";
		if (Dte.length() == 8) {
			String Yr = Dte.substring(0, 4);
			String Mn = getmonthString(Integer.parseInt(Dte.substring(4, 6)) - 1);
			// if (Integer.parseInt(Mn) < 10) {
			// Mn = "0" + Mn;
			// }
			String Dt = Dte.substring(6, 8);
			// if (Integer.parseInt(Dt) < 10) {
			// Dt = "0" + Dt;
			// }
			Res = Dt + "-" + Mn + "-" + Yr;
		}
		return Res;
	}

	public String getformatted1(String Dte) {
		String Res = "";
		if (Dte.length() == 10) {
			String Yr = Dte.substring(6, 10);
			String Mn = Dte.substring(3, 5);
			// if (Integer.parseInt(Mn) < 10) {
			// Mn = "0" + Mn;
			// }
			String Dt = Dte.substring(0, 2);
			// if (Integer.parseInt(Dt) < 10) {
			// Dt = "0" + Dt;
			// }
			Res = Yr + Mn + Dt;
		}
		return Res;
	}

	public String getformatted(String Dte) {
		String Res = "";
		if (Dte.length() == 11) {
			String Yr = Dte.substring(7, 11);
			String Mn = getmonthvalue(Dte.substring(3, 6));
			// if (Integer.parseInt(Mn) < 10) {
			// Mn = "0" + Mn;
			// }
			String Dt = Dte.substring(0, 2);
			// if (Integer.parseInt(Dt) < 10) {
			// Dt = "0" + Dt;
			// }
			Res = Yr + Mn + Dt;
		}
		return Res;
	}

	public String getformatted2(String Dte) {
		String Res = "";
		if (Dte.length() == 9) {
			String Yr = Dte.substring(7, 9);
			String Mn = getmonthvalue(Dte.substring(3, 6));
			// if (Integer.parseInt(Mn) < 10) {
			// Mn = "0" + Mn;
			// }
			String Dt = Dte.substring(0, 2);
			// if (Integer.parseInt(Dt) < 10) {
			// Dt = "0" + Dt;
			// }
			Res = "20" + Yr + Mn + Dt;
		}
		return Res;
	}

	public boolean deleteFile(String filename) {
		File file = new File(filename);
		boolean deleted = file.delete();
		return deleted;
	}

	/*public void WriteToZipXml(String FILENAME, String Content) {
		try {
			byte[] imageAsBytes = Base64.decode(Content);
			FileOutputStream fOut = new FileOutputStream(FILENAME);
			fOut.write(imageAsBytes);
			fOut.close();
		} catch (Exception e) {
		}
	}*/

	public void WriteToXml(String FILENAME, String Content) {
		try {
			System.out.println(Content);
			File myFile = new File(FILENAME);
			myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			System.out.println("OK");
			myOutWriter.append(Content);
			myOutWriter.close();
			fOut.close();
			System.out.println("OK");
		} catch (Exception e) {
		}
	}

	public void WriteToTextFile(String FILENAME, String Content) {
		try {
			System.out.println(Content);
			File myFile = new File(FILENAME);
			myFile.createNewFile();
			FileOutputStream fOut = new FileOutputStream(myFile, true);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
			System.out.println("OK");
			myOutWriter.append(Content);
			myOutWriter.append("\n");
			myOutWriter.close();
			fOut.close();
			System.out.println("OK");
		} catch (Exception e) {
		}
	}
	
	
	public void INBOX(DataHelper1 dh) 	
	{
		File dir;
		
		String LogTxtFile = Environment.getExternalStorageDirectory()
				+ "/IBHAR/EATANDEARN/XML/INBOX/";
	    //dir = getDir("xml", Context.MODE_PRIVATE);
		
	    dir = new File(LogTxtFile);
		dir.mkdirs();
		
		
		if(dir.isDirectory()==true)
		{
		File list[]=dir.getAbsoluteFile().listFiles();
		
		
		ArrayList<String> transTable=new ArrayList<String>();
		ArrayList<String> lst_keys=new ArrayList<String>();
		ArrayList<String> lst_tables=new ArrayList<String>();
		transTable = dh.selectListColumnDetails(
				"Select ICFILE,ICKEYF from ITHIIC", null);
		for (int i2 = 0; i2 < transTable.size(); i2++) {
			String[] split = transTable.get(i2).split("<>");
			lst_tables.add(split[0].trim());
			lst_keys.add(split[1].trim());
		}
		
		for(int i=0;i<list.length;i++)
		{
			
			
			if(!list[i].getName().contains("T10001"))
			{
			
			
			parseXml(dir.getAbsoluteFile()+"/"+list[i].getName(),lst_keys,
					lst_tables, dh,dir.getAbsoluteFile()+"/"+list[i].getName());	
			
			}
			else
			{
				deleteFile(dir.getAbsoluteFile()+"/"+list[i].getName());
			}
			
			
			
		}
		
		}
	}
	
	public void INBOXDELETE(DataHelper1 dh) 	
	{
		File dir;
		
		String LogTxtFile = Environment.getExternalStorageDirectory()
				+ "/IBHAR/EATANDEARN/XML/INBOX/";
	    //dir = getDir("xml", Context.MODE_PRIVATE);
		
	    dir = new File(LogTxtFile);
		dir.mkdirs();
		
		
		if(dir.isDirectory()==true)
		{
		File list[]=dir.getAbsoluteFile().listFiles();
		
		for(int i=0;i<list.length;i++)
		{
			
			
				deleteFile(dir.getAbsoluteFile()+"/"+list[i].getName());
		
			
			
		}
		
		}
	}
	
	/*public String ReadfromXml(String fileName, String charsetName)
			throws IOException {
		File myFile = new File(fileName);
		byte data[] = org.apache.commons.io.FileUtils
				.readFileToByteArray(myFile);
		return new String(data);
	}*/

	@SuppressWarnings("unused")
	public void parseXml(String docXml, ArrayList<String> lst_keys,
			ArrayList<String> lst_tables, DataHelper1 dh1, String sFileName) {
		docXml = docXml.replaceAll(">\\s+<", "><");
		int pKeyCount = 0;
		String insertCols = "", insertVals = "", itparmupdateCols = "", updateCols = "", startTAG = " ", endTAG = "";
		String curCol = "";
		String Query = "", whereQuery = "", pKeyVal = "";
		int count = 0, countRes = 0;
		ArrayList<String> pVal = new ArrayList<String>();
		ArrayList<String> pKey = new ArrayList<String>();
		String[] split;
		boolean pkeyval = false;
		boolean isEmptyTag = false, isWhiteSpaceTag = false;
		try {

			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			FileInputStream f = null;
			try {
				f = new FileInputStream(new File(docXml));
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			}
			
			xpp.setInput(f, null);
			int eventType = xpp.getEventType();
			String sTableName = "";
		
			sTableName = sFileName.substring(sFileName.lastIndexOf('/') + 1,
					sFileName.lastIndexOf('/') + 7);
			dh1.myDataBase.beginTransaction();
			
			ih = dh1.CreateInsertHelper(sTableName);
			
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {
				} else if (eventType == XmlPullParser.START_TAG) {
					if (xpp.getDepth() == 2) {
						ih.prepareForReplace();
					}
					if (xpp.getDepth() == 3) {
						startTAG = xpp.getName();
						if (sTableName.equals("IFLSXM")) {
							curCol = xpp.getName();
							isEmptyTag = xpp.isEmptyElementTag();
						}
					}
				} else if (eventType == XmlPullParser.TEXT) {
					if (xpp.getDepth() == 3) {
						if (sTableName.equals("IFLSXM")) {
							isWhiteSpaceTag = xpp.isWhitespace();
						}
						// System.out.println("Column Name " + startTAG);

						String dpname = "";
						try {
							
						//	System.out.println("Column Name " + startTAG);
						//	System.out.println("value " + xpp.getText().toString());
							
							
							int nColumnIndex = ih.getColumnIndex(startTAG);

							if (sTableName.trim().equals("SUDEPT")) {
								if (startTAG.trim().toLowerCase()
										.contains("dpname")) {
									dpname = xpp.getText();
								}
							}
							if (startTAG.toLowerCase().contains("lock")) {
								// if(xpp.getText().trim().equals("1"))
								ih.bind(nColumnIndex, "");
							} else {
								ih.bind(nColumnIndex, xpp.getText());
							}
						} catch (Exception e) {

						}

						// ih.bind(nColumnIndex, xpp.getText());
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					if (xpp.getDepth() == 3) {
						if (sTableName.equals("IFLSXM")
								&& curCol.equals("XLDCOD")) {
							if (isEmptyTag) {
								ih.bind(3, "");
							} else if (isWhiteSpaceTag) {
								ih.bind(3, "");
							}
						}
					}
					if (xpp.getDepth() == 2) {
						// System.out.println("Before Execute");
						ih.execute();
						// System.out.println("After Execute");
					}
				}
				eventType = xpp.nextToken();
			}

			ih.close();

			dh1.myDataBase.setTransactionSuccessful();

			dh1.myDataBase.endTransaction();
			
			deleteFile(sFileName);
			
		} catch (Exception e) {
			System.out.println(e.toString());
			dh1.myDataBase.endTransaction();
		}
	}

	public String getDatetime() {
		final Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		int hHour = c.get(Calendar.HOUR);
		int mMin = c.get(Calendar.MINUTE);
		int mSec = c.get(Calendar.SECOND);
		String DateStr = String.valueOf(mYear) + "-" + timeformat(mMonth + 1)
				+ "-" + timeformat(mDay) + " " + timeformat(hHour) + ":"
				+ timeformat(mMin) + ":" + timeformat(mSec);

		return DateStr;
	}

	/*public String parseZipXml(String docXml, File dir, File infile) {
		docXml = docXml.replaceAll(">\\s+<", "><");
		boolean isIRFILE = false;
		boolean isIRNAME = false;
		String IRNAME = "";
		try {

			// insitilazition of xmlpull parser
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();
			// giving the input string to xmlpull parser
			xpp.setInput(new StringReader(docXml));

			// getting the event type of the xml string
			// [END_DOCUMENT,START_DOCUMENT,START_TAG,TEXT,END_TAG]
			int eventType = xpp.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_DOCUMENT) {

				} else if (eventType == XmlPullParser.START_TAG) {
					if ((xpp.getName().equals("IRFILE"))) {
						isIRFILE = true;
					} else if ((xpp.getName().equals("IRNAME"))) {
						isIRNAME = true;
					}
				} else if (eventType == XmlPullParser.TEXT) {
					if (isIRFILE == true) {
						try {
							/*
							 * public void WriteToZipXml(String FILENAME, String
							 * Content) { try { byte[] imageAsBytes =
							 * Base64.decode(Content); FileOutputStream fOut =
							 * new FileOutputStream(FILENAME);
							 * fOut.write(imageAsBytes); fOut.close(); } catch
							 * (Exception e) { } }
							 */

					/*		System.out.println("Writing " + xpp.getText());
							infile = new File(dir, "INBOX");
							WriteToZipXml(
									Environment.getExternalStorageDirectory()
											+ "/IBHAR/EATANDEARN/XML/" + IRNAME,
									xpp.getText());
							WriteToZipXml(infile.getAbsolutePath() + IRNAME,
									xpp.getText());
						} catch (Exception e) {
							System.out.println("Error " + e.toString());
						}
						isIRFILE = false;
					}
					if (isIRNAME == true) {
						try {
							IRNAME = xpp.getText();
							IRNAME = IRNAME.replace(".xml", ".zip");
						} catch (Exception e) {
							System.out.println("Error " + e.toString());
						}
						isIRNAME = false;
					}
				} else if (eventType == XmlPullParser.END_TAG) {
				}
				eventType = xpp.next();
			}
		} catch (Exception e) {
			System.out.println(e.toString());
			WriteToTextFile(LogTxtFile, e.toString());
			IRNAME = "";
		}
		return IRNAME;
	}*/

	
	
	
	
	
	
	/*public String parseZipXmlForSingle(String docXml, File dir, File infile) {
	
	String IRNAME="SINGLE.ZIP";

			// insitilazition of xmlpull parser
		
						try {
							/*
							 * public void WriteToZipXml(String FILENAME, String
							 * Content) { try { byte[] imageAsBytes =
							 * Base64.decode(Content); FileOutputStream fOut =
							 * new FileOutputStream(FILENAME);
							 * fOut.write(imageAsBytes); fOut.close(); } catch
							 * (Exception e) { } }
							 */

						/*	System.out.println("Writing " + docXml);
							infile = new File(dir, "INBOX");
							WriteToZipXml(
									Environment.getExternalStorageDirectory()
											+ "/IBHAR/EATANDEARN/XML/" + IRNAME,
											docXml);
							WriteToZipXml(infile.getAbsolutePath() + IRNAME,
									docXml);
						} catch (Exception e) {
							System.out.println("Error " + e.toString());
						}
				
		
		return IRNAME;
	}*/

	
	
	
	
	public String getLogCatFiles() {
		String LogContent = "";
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("logcat -d");
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			StringBuilder log = new StringBuilder();
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				log.append(line);
			}
			LogContent = new String(log);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return LogContent;
	}

}
