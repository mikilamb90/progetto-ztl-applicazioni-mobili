package ztl.Bologna.activity.Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.GeoPoint;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.OpenableColumns;
import android.util.Log;
import ztl.Bologna.activity.ztlbolognaActivity.*;


public class DBopenHelper extends SQLiteOpenHelper {
	private final static String DATABASE_NAME = "ztlBolo.db";
	static SQLiteDatabase db;
	private final static String SQL_PATH = "databaseZtlBolo.sql";
	MapsActivity ma = new MapsActivity();
	Double LATITUDE= 0.0;
	Double LONGITUDE = 0.0;
	private final Context myContext;
	private final String CREATE_TABLE_ZTL_STREET = "CREATE TABLE manageZtlstreet ("
			+ " via text not null,"
			+"latitude DOUBLE," 
			+ "longitude DOUBLE);";

	public DBopenHelper(Context context,int version) {

		super(context, DATABASE_NAME,null,1);
		this.myContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase database) {

		database.execSQL(this.CREATE_TABLE_ZTL_STREET);

		try {

			copyDataBase(database);
			Log.e("copy","copy");

		} catch (IOException e) {

			throw new Error("Error copying database");

		}
	}


/*metodo che popola il db leggendo dal file esterno i punti dello ztl*/
	private void copyDataBase(SQLiteDatabase db) throws IOException{


		//Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(SQL_PATH);
		/*leggo il file*/
		BufferedReader buffread =  new BufferedReader(new InputStreamReader(myInput));
		String line = null;

		while((line = buffread.readLine()) != null) {


			if(!line.equals(new String(""))){
				db.execSQL(line);
			}
		}

		myInput.close();

	}
/*metodo che apre il database per effettuare le operazioni necessarie*/
	public void openDataBase() throws SQLException{
		SQLiteDatabase myDataBase;
		//Open the database
		String myPath = DATABASE_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
		Log.e("path",""+myDataBase);

	}

	@Override
	public synchronized void close() {
		
		super.close();

	}

/*metodo che quando c'è un aggiornamento del db elimina la tabella manageZtlStreet*/
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS manageZtlStreet;");
		this.onCreate(database);
	}


	/*public Cursor getZtlStreet(){
		String query = "select latitude ,longitude from manageZtlstreet";
		return getReadableDatabase().rawQuery(query,null);

	}*/
	
	/*metodo che scorre nel database i punti dello ztl e li mette in una lista di geopoint*/
	
	public List<GeoPoint> getPointsList()
	{
		
		List<GeoPoint> pointsList = new ArrayList<GeoPoint>();
		String query = "select latitude,longitude from manageZtlstreet";
		Log.e("list","g");
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);

		if (cursor.moveToFirst()) {
			do {				
				Integer lat = (int) (cursor.getDouble(cursor.getColumnIndex("latitude")) * 1e6);
				Integer lon = (int) (cursor.getDouble(cursor.getColumnIndex("longitude")) * 1e6);

				GeoPoint p = new GeoPoint(lat, lon);
				pointsList.add(p);
			}
			while (cursor.moveToNext());
		}
		
		cursor.close();
		db.close();

		return pointsList;
	}
}