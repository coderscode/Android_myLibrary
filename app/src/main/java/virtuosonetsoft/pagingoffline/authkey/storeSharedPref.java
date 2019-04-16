package virtuosonetsoft.pagingoffline.authkey;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class storeSharedPref {
	public static final String MyPREFERENCES = "VNS_ParkingApplication";
	public static Context _ctx;

	public storeSharedPref(Context ctx) {
		_ctx = ctx;

	}

	public void storeSharedValue(String key, String value)

	{
		SharedPreferences sharedpreferences = ApplicationMain.getInstance().getSharedPreferences(
				MyPREFERENCES, Context.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.putString(key, value);
		editor.commit();

	}
	public void resetValues()
	{

	SharedPreferences sharedpreferences =  ApplicationMain.getInstance().getSharedPreferences(
				MyPREFERENCES, Context.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.clear().commit();
		editor.commit();

		
	}

	public void storeSharedValue(String key, Boolean value)

	{
		SharedPreferences sharedpreferences =  ApplicationMain.getInstance().getSharedPreferences(
				MyPREFERENCES, Context.MODE_PRIVATE);
		Editor editor = sharedpreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();

	}

	public String getSharedValue(String key) {

		SharedPreferences sharedpreferences =  ApplicationMain.getInstance().getSharedPreferences(
				MyPREFERENCES, Context.MODE_PRIVATE);

		// sharedpreferences.getString(key, "0");

		return sharedpreferences.getString(key, "NA");
	}

	public String getSharedValue(String key, String _default) {

		SharedPreferences sharedpreferences =  ApplicationMain.getInstance().getSharedPreferences(
				MyPREFERENCES, Context.MODE_PRIVATE);

		// sharedpreferences.getString(key, "0");

		return sharedpreferences.getString(key, _default);
	}


}
