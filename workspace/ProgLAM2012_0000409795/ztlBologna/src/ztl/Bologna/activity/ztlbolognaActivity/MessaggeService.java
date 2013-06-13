package ztl.Bologna.activity.ztlbolognaActivity;





import java.util.List;
import ztl.Bologna.*;
import ztl.Bologna.activity.Database.DBopenHelper;

import com.google.android.maps.GeoPoint;

import com.google.android.maps.OverlayItem;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;

import android.util.Log;


public class MessaggeService extends IntentService{
	public static final int UPDATE_UI_MSG = 1;
	public static final String MESSENGER = "messenger";
	public static final String CALLER = "caller";
	public static final String POSITION = "position";



	public MessaggeService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	public MessaggeService(){
		this("MessaggeService");
	}

	OverlayList overlay;
	double myLat;
	double myLong;

	private DBopenHelper db;

	LocationManager lm;
	GeoPoint gp;
	/*metodo che mette tutti i punti ztl nella lista */
	private OverlayList addPoint(GeoPoint g) {
		OverlayItem overlayitem = new OverlayItem(g, "", "");
		overlay.addOverlay(overlayitem);
		return overlay;
	}

	/*funzionache preleva i punti dello ztl dal db e poi richiama la funziona addPOint*/
	private void updateMapOverlay() {
		//	List<Overlay> mapOverlays = gmap.getOverlays();
		Drawable drawable = getResources().getDrawable(R.drawable.ic_launcher);
		//mapOverlays.clear();
		db = new DBopenHelper(getApplicationContext(), 1);
		overlay = new OverlayList(drawable, this); 
		/*chiamata alla funzione che prende i punti dal database*/
		List<GeoPoint> myList = db.getPointsList();
		for (GeoPoint p : myList) 
		{
			addPoint(p);
		}
	}

	void onStartCommand(){
	}
	protected void onHandleIntent(Intent intent) {

		while(true)
		{  

			/*chiamata funziona updatemapoverlay per popolamento della lista*/
			updateMapOverlay();

			/*verifico i punti vicini ala mia posizione attuale*/

			for(int i = 0 ; i< overlay.size();i++){

				//	Log.e("item : ","n"+overlay.size());
				OverlayItem item = overlay.getItem(i);
				Location l1 = new Location("");
				l1.setLatitude(item.getPoint().getLatitudeE6()/1e6);
				//	Log.e("l1", "l1"+l1.getLatitude());
				l1.setLongitude(item.getPoint().getLongitudeE6()/1e6);
				LocationManager managerLoc = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

				myLat = managerLoc.getLastKnownLocation(managerLoc.GPS_PROVIDER).getLatitude();
				myLong = managerLoc.getLastKnownLocation(managerLoc.GPS_PROVIDER).getLongitude();
				Location l2 = new Location("");
				float speed =l2.getSpeed();
				l2.setLatitude(myLat);
				l2.setLongitude(myLong);

				float d = l2.distanceTo(l1);
				/*visualizzazione alert in base alla velocità*/
				if(speed < 10){
					Log.e("VELOCITA",String.valueOf(speed));
					if (d <50) {

						Intent intDi = new Intent();
						intDi.setClass(this, DialogActivity.class);
						intDi.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intDi);       

					}

				}
				else if(speed > 10 && speed < 30){

					if (d <150) {

						Intent intDi = new Intent();
						intDi.setClass(this, DialogActivity.class);
						intDi.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intDi);       

					}

				}

			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}



}



