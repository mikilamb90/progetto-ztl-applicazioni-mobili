package ztl.Bologna;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class OverlayList extends  ItemizedOverlay<OverlayItem> {
	
	private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
	private Paint innerPaint;
	private float circleRadius; 
	
	public OverlayList(Drawable defaultMarker , Context context) {
		super(boundCenterBottom(defaultMarker));
		populate();
		
	}

	public void draw(Canvas canvas, MapView mapview, boolean shadow) {
		Point p = new Point();
		circleRadius = mapview.getProjection().metersToEquatorPixels(50);
	    for(OverlayItem item : mOverlays) {
	        drawCircle(canvas, mapview.getProjection().toPixels(item.getPoint(), p));
	    }
	}

	public void addOverlay(OverlayItem overlay) {
		mOverlays.add(overlay);
		populate();
		
	}

	@Override
	protected OverlayItem createItem(int i) {
		return mOverlays.get(i);
	}
		

	
	public int size() {
	
		return mOverlays.size();
	}

	public void drawCircle(Canvas canvas, Point curScreenCoords) {
	    canvas.drawCircle((float) curScreenCoords.x, (float) curScreenCoords.y, circleRadius, getInnerPaint());
	}
	
	public Paint getInnerPaint() {
		if (innerPaint == null) {
			innerPaint = new Paint();
			
	        innerPaint.setARGB(80,80, 255, 150);
	        innerPaint.setAntiAlias(true);
		}
	    return innerPaint;
	}
}
