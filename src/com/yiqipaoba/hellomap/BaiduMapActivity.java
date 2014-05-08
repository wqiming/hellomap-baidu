package com.yiqipaoba.hellomap;

import android.content.Context;
//import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class BaiduMapActivity extends FragmentActivity {
	BMapManager mBMapMan = null;
	MapView mMapView = null;

	public void initMapView(MapView mapView)
	{
		if (mMapView!=null || mapView == null)
			return;
		
		mMapView=mapView;
		//	enable internal zoom control
		mMapView.setBuiltInZoomControls(true);
		//	get view
		MapController mMapController=mMapView.getController();
		
		Criteria criteria = new Criteria();
        LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        /*
        if (location==null){
        	//locationManager.requestSingleUpdate(criteria, this, Looper.myLooper());
        	locationManager.requestLocationUpdates((long)60000, (float)10, criteria, this, Looper.myLooper());
        	location = locationManager.getLastKnownLocation(provider);
        }
        */
        double lat = 39.915;
        double lng = 116.404;
        if (location!=null){
	        lat = location.getLatitude();
	        lng = location.getLongitude();
        }
        
		GeoPoint point = new GeoPoint((int)(lat *1E6), (int)(lng * 1E6));
		mMapController.setCenter(point);
		mMapController.setZoom(12);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//	before setContentView to avoid exception
		mBMapMan = new BMapManager(getApplication());
		mBMapMan.init(null);
		setContentView(R.layout.activity_baidu_map);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	@Override
	protected void onDestroy(){
		if (mMapView!=null)
			mMapView.destroy();
		if(mBMapMan!=null){
			mBMapMan.destroy();
			mBMapMan = null;
		}
		super.onDestroy();
	}
	
	@Override
	protected void onPause(){
		if(mMapView!=null)
			mMapView.onPause();
		if(mBMapMan!=null){
			mBMapMan.stop();
		}
		super.onPause();
	}
	
	@Override
	protected void onResume(){
		if(mMapView!=null)
			mMapView.onResume();
		if(mBMapMan!=null){
			mBMapMan.start();
		}
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.baidu_map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_google_map) {
			//Intent intent = new Intent(this, MainActivity.class);
			//	put location info
			//startActivity(intent);
			super.onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_baidu_map,
					container, false);
			
			MapView mapView=(MapView)rootView.findViewById(R.id.baidu_map_view);
			if (mapView!=null)
				((BaiduMapActivity)getActivity()).initMapView(mapView);
			
			return rootView;
		}
	}

}
