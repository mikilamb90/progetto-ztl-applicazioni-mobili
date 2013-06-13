package ztl.Bologna.activity.ztlbolognaActivity;






import java.util.List;

import ztl.Bologna.activity.Database.DBopenHelper;
import ztl.Bologna.OverlayList;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;

import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;


import android.location.LocationManager;
import android.os.Bundle;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;

import android.view.Menu;



public class MapsActivity extends MapActivity{

	private MyLocationOverlay mOverlay;

	protected static MapView gmap;
	private DBopenHelper db;

	/*lista che conterrà i vari punti dello ztl*/
	OverlayList overlay;
	private MapController mapController;

	Intent serviceIntent;
	public Cursor cursor;
	int version ;

	LocationManager control ;

	//GeoPoint gp = new GeoPoint((int)(Lat),(int)(Long));



	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		setContentView(R.layout.activity_maps);

		/*oggetto db che chiama la classe dbhelper*/
		db = new DBopenHelper(this,1);


		/*controllo sulla mappa*/	
		gmap=(MapView)findViewById(R.id.mapview);
		gmap.setClickable(true);
		gmap.setBuiltInZoomControls(true);
		mOverlay=new MyLocationOverlay(this,gmap);
		mOverlay.enableMyLocation();
		mapController =gmap.getController();
		
/*cordinate di bologna per centrare la mappa*/
		String coordinates[] = {"44.502872","11.34304" }; 
		Double lat = Double.parseDouble(coordinates[0]);
		Double lng = Double.parseDouble(coordinates[1]);
		GeoPoint gp = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));



		mapController.animateTo(gp);
		gmap.getOverlays().add(mOverlay);

		/*chiamata alla funziona di aggiunta dei punti alla mappa*/	
		updateMapOverlay();

		db.close();




		/*istanza del service*/	
		serviceIntent = new Intent(MapsActivity.this,MessaggeService.class);

		/*avvio del service */
		startService(serviceIntent);

	}


	private void addOpenPoint(GeoPoint g) {
		OverlayItem overlayitem = new OverlayItem(g, "", "");
		overlay.addOverlay(overlayitem);

	}
	/*disabilita il gps quando va in pausa l'applicazione*/
	public void onPause() {
		super.onPause(); 
		// Remove MyLocation
		if (mOverlay != null) {
			mOverlay.disableMyLocation();
		}
	}

	protected void onResume() {
		super.onResume();
		mOverlay.enableMyLocation();
		mOverlay.runOnFirstFix(new Runnable() {

			//	@Override
			public void run() {

				/*aggiunta posizione*/	
				gmap.getController().animateTo(mOverlay.getMyLocation());

				/*abilitazione zoom*/	
				gmap.getController().setZoom(16);

			}

		});  	
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_maps, menu);
		return true;
	}

	/*metodo che aggiunge i punti sulla mappa*/
	void updateMapOverlay() {
		List<Overlay> mapOverlays = gmap.getOverlays();
		Drawable drawable = getResources().getDrawable(R.drawable.ic_launcher);
		//mapOverlays.clear();
		overlay = new OverlayList(drawable, this); 
		mapOverlays.add(overlay);
		List<GeoPoint> myList = db.getPointsList();
		for (GeoPoint p : myList) 

		{
			addOpenPoint(p);
		}
	}





	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
