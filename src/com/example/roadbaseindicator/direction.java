package com.example.roadbaseindicator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;







import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class direction extends Activity {


    private SupportMapFragment map;

    FragmentManager fragmentManager;
    LocationManager mLocationManager;

    String provider;
    Button ok;
    EditText txtadd;
    Marker marker1;
    MarkerOptions marker;
    private ArrayList<LatLng> arrayPoints;
    PolylineOptions polylineOptions;
    LatLng ll, ll1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    double latitude, latitude1;
    double longitude, longitude1;
    GPSTracker gps;
    PolylineOptions lineOptions = null;
    Polyline polyline;
    String strAdd;
    private GoogleMap googleMap;
    EditText et;
    JSONParser jParser = new JSONParser();

    RadioButton rbDriving;
    RadioButton rbBiCycling;
    RadioButton rbWalking, rbcar, rbbike;
    RadioGroup rgModes;
    TextView tvDistanceDuration;
    int mMode=0;
    final int MODE_DRIVING=0;
    final int MODE_BICYCLING=1;
    final int MODE_WALKING=2;
    final int MODE_CAR=3;
    final int MODE_BIKE=4;
    LatLng origin,dest;
   Button back;
    @SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.direction);
        txtadd = (EditText)findViewById(R.id.textView);
        tvDistanceDuration = (TextView) findViewById(R.id.tv_distance_time);
        // Getting reference to rb_driving
        rbDriving = (RadioButton) findViewById(R.id.rb_driving);

        // Getting reference to rb_bicylcing
        rbBiCycling = (RadioButton) findViewById(R.id.rb_bicycling);

        // Getting reference to rb_walking
        rbWalking = (RadioButton) findViewById(R.id.rb_walking);
        rbcar = (RadioButton) findViewById(R.id.rb_car);
        rbbike = (RadioButton) findViewById(R.id.rb_bike);
        // Getting Reference to rg_modes
        rgModes = (RadioGroup) findViewById(R.id.rg_modes);

back=(Button)findViewById(R.id.back);
back.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent i= new Intent(getApplicationContext(),view_all.class);
		startActivity(i);
	}
});
        arrayPoints = new ArrayList<LatLng>();

        et = (EditText)findViewById(R.id.textView);
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);

        
        // Setting onclick event listener for the map
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

                // Already two locations
                if (arrayPoints.size() > 1) {
                    arrayPoints.clear();
                    googleMap.clear();
                }

                // Adding new item to the ArrayList
                arrayPoints.add(point);

                // Creating MarkerOptions
                MarkerOptions options = new MarkerOptions();

                // Setting the position of the marker
                options.position(point);

                /**
                 * For the start location, the color of marker is GREEN and
                 * for the end location, the color of marker is RED.
                 */
                if (arrayPoints.size() == 1) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                } else if (arrayPoints.size() == 2) {
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                }

                // Add new marker to the Google Map Android API V2
                googleMap.addMarker(options);

                // Checks, whether start and end locations are captured
                if (arrayPoints.size() >= 2) {
                   origin = arrayPoints.get(0);
                     dest = arrayPoints.get(1);

                    
//                    // Getting URL to the Google Directions API
//                    String url = getDirectionsUrl(origin, dest);
//
//                    DownloadTask downloadTask = new DownloadTask();
//
//                    // Start downloading json data from Google Directions API
//                    downloadTask.execute(url);
                    
                    
                }
            }
        });
        
        rgModes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {



            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                tvDistanceDuration.setVisibility(View.VISIBLE);

                // Checks, whether start and end locations are captured
             //   if(markerPoints.size() >= 2){
                   // LatLng origin = markerPoints.get(0);
                   // LatLng dest = markerPoints.get(1);

                    // Getting URL to the Google Directions API
                    String url = getDirectionsUrl(origin, dest);

                    DownloadTask downloadTask = new DownloadTask();

                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);
                }
            //}
        });


    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Travelling Mode
        String mode = "mode=driving";

        if(rbDriving.isChecked()){
            mode = "mode=driving";
            mMode = 0 ;
        }

        else if(rbcar.isChecked()){
            mode = "mode=driving";
            mMode = 3;
        }
        else if(rbbike.isChecked()){
            mode = "mode=driving";
            mMode = 4;
        }
        else if(rbBiCycling.isChecked()){
            mode = "mode=bicycling";
            mMode = 1;
        }
        else if(rbWalking.isChecked()){
            mode = "mode=walking";
            mMode = 2;
        }




        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+mode;

        // Output format
        String output = "json";
        
        

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;


        return url;
    }
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {


            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            String distance = "";
            String duration = "";
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++)
                {
                    HashMap<String,String> point = path.get(j);
                    
                    if(j==0){    // Get distance from the list
                        distance = (String)point.get("distance");
                        continue;
                    }else if(j==1){ // Get duration from the list
                        duration = (String)point.get("duration");
                        continue;
                    }
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    //   strAdd = getCompleteAddressString(lat, lng);
                    //   EditText txtadd=(EditText)findViewById(R.id.editText);
                    //  txtadd.setText("Complete Address : " + strAdd);
                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);


                if(mMode==MODE_DRIVING) {
              
              //  	21.1267243 73.1115531 bardoli
             
                	//ITC 21.1811185, 72.8191557
                	
                	// 21.1702° N, 72.8311° E surat
              //  String s1=strAdd;
               // String d1=strAdd1;
                	//if(latitude==21.1811185 && longitude==72.8191557  && latitude1==21.1267243 && longitude1==73.1115531)
              /*  if(strAdd.equals(s1) && strAdd1.equals(d1))	
                {
                		
                	Toast.makeText(getApplicationContext(), "No route found", Toast.LENGTH_LONG).show();	
                	}
                	else
                	{*/
                	
                    lineOptions.color(Color.RED);
                    try {
                        int time= NumberFormat.getInstance().parse(duration).intValue();
                        time = time + 5;
                        tvDistanceDuration.setText("Distance:" + distance + ", Duration:" + time + " mins");

                    }
                    catch (ParseException e) {
                        e.printStackTrace();
                    }
                	}
                //}

                else if(mMode==MODE_BICYCLING)
                {
                    lineOptions.color(Color.GREEN);
                    tvDistanceDuration.setText("Distance:"+distance + ", Duration:"+duration);
                }
                else if(mMode==MODE_WALKING)
                {
                    lineOptions.color(Color.BLUE);
                    tvDistanceDuration.setText("Distance:"+distance + ", Duration:"+duration);
                }
                else if(mMode==MODE_CAR)
                {
                    lineOptions.color(Color.BLACK);
                    tvDistanceDuration.setText("Distance:"+distance + ", Duration:"+duration);

                }

                else if(mMode==MODE_BIKE) {
                    lineOptions.color(Color.MAGENTA);
                    try {
                        int time = NumberFormat.getInstance().parse(duration).intValue();
                        time = time + 10;
                        tvDistanceDuration.setText("Distance:" + distance + ", Duration:" + time + " mins");

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

            if(result.size()<1){
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }
//            tvDistanceDuration.setText("Distance:"+distance + ", Duration:"+duration);
            // Drawing polyline in the Google Map for the i-th route

             polyline = googleMap.addPolyline(lineOptions);

           // map.addPolyline(lineOptions);


        }
    }
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            //Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    
}

