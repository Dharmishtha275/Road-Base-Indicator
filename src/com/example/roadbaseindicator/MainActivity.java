package com.example.roadbaseindicator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.InflateException;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

//import com.google.android.gms.appindexing.AppIndex;
//import com.google.android.gms.common.api.GoogleApiClient;
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
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONObject;

public class MainActivity extends FragmentActivity {

   Button ok,adrs,clear;
     Marker marker1;
    MarkerOptions marker;
    private ArrayList<LatLng> arrayPoints;

    LatLng dest, source;
    PolylineOptions lineOptions = null;

   // private GoogleApiClient client2;
    double latitude, latitude1;
    double longitude, longitude1;
    GPSTracker gps;

   public String strAdd=null,strAdd1=null;
    private GoogleMap map;
    EditText et;

    RadioButton rbDriving;
    RadioButton rbBiCycling;
    RadioButton rbWalking, rbcar, rbbike;
    RadioGroup rgModes;
    ArrayList<LatLng> markerPoints;
    int mMode=0;
    final int MODE_DRIVING=0;
    final int MODE_BICYCLING=1;
    final int MODE_WALKING=2;
    final int MODE_CAR=3;
    final int MODE_BIKE=4;
    TextView tvDistanceDuration;
    Polyline polyline;
    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        arrayPoints = new ArrayList<LatLng>();
        et = (EditText) findViewById(R.id.textView);

        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setCompassEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
        map.getUiSettings().setRotateGesturesEnabled(true);

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

        adrs=(Button)findViewById(R.id.adrs);
        adrs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strAdd1 = getCompleteAddressString(latitude1, longitude1);
                strAdd = getCompleteAddressString(latitude, longitude);
                Intent i= new Intent(getApplicationContext(),view_all.class);
                i.putExtra("source",strAdd);
                i.putExtra("destination",strAdd1);
                startActivity(i);


           }
        });
        
        clear=(Button)findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              
            		map.clear();
            		map.addMarker(marker);
            		tvDistanceDuration.setText("");

           }
        });

        ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = et.getText().toString();

                Geocoder geocoder = new Geocoder(MainActivity.this);
                List<Address> list = null;
                try {
                    list = geocoder.getFromLocationName(location, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address add = list.get(0);
                String locality = add.getLocality();
                latitude1 = add.getLatitude();
                longitude1 = add.getLongitude();
                 dest = new LatLng(latitude1, longitude1);
                strAdd1 = getCompleteAddressString(latitude1, longitude1);

                if (marker1 != null)
                    marker1.remove();
                MarkerOptions markerOptions = new MarkerOptions()
                        .title(locality)
                        .position(new LatLng(add.getLatitude(), add.getLongitude()));
                marker1 = map.addMarker(markerOptions);
                markerOptions.icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    et.setText("");
              // polyline.remove();

                                    //showDirections(v);
            }
        });
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);
        Location l = null;

        for (int i = 0; i < providers.size(); i++) {
           // if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling


          //  }
            l = lm.getLastKnownLocation(providers.get(i));
            if (l != null) {
                latitude = l.getLatitude();
                longitude = l.getLongitude();
                source = new LatLng(latitude, longitude);
                strAdd = getCompleteAddressString(latitude, longitude);

                break;
            }
        }

        if (map != null) {

            marker = new MarkerOptions().position(
                    new LatLng(latitude, longitude)).title("Hello Maps").snippet("Discription");

            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(latitude, longitude)).zoom(12).build();

            map.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            map.addMarker(marker);

        }

        rgModes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {



            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                tvDistanceDuration.setVisibility(View.VISIBLE);

                // Checks, whether start and end locations are captured
             //   if(markerPoints.size() >= 2){
                   // LatLng origin = markerPoints.get(0);
                   // LatLng dest = markerPoints.get(1);

                    // Getting URL to the Google Directions API
                    String url = getDirectionsUrl(source, dest);

                    DownloadTask downloadTask = new DownloadTask();

                    // Start downloading json data from Google Directions API
                    downloadTask.execute(url);
                }
            //}
        });

        // client3 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

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


                if(mMode==MODE_DRIVING) 
                {
                	
              String src1=strAdd;
              String dest1=strAdd1;
    
//  MAHATMA GHANDI RD RAMNAGAR BARDOLI                	
                	double dest_lat=21.128781;
                	double dest_lang=73.103897;
              
 // PATIDAR JIN BARDOLI                	
                	double dest_lat1=21.128015;
                	double dest_lang1=73.106639;
                	
 // NITYANAD SOCIETY BARDOLI                	
                	double dest_lat2=21.122676;
                	double dest_lang2=73.108421;
              	// lat1 and lng1 are the values of a dest which place on map and lat2 lang 2 is getting latlang
                
              	if (distance(latitude1, longitude1, dest_lat, dest_lang) < 0.1)        	
              	//  	 if distance < 0.1 miles we take locations as equal
       
                {
              		 tvDistanceDuration.setText("No Route for Bus");

                	Toast.makeText(getApplicationContext(), "No Route for Bus", Toast.LENGTH_LONG).show();
                }
              	
              	else if (distance(latitude1, longitude1, dest_lat1, dest_lang1) < 0.1)
              	{
              		tvDistanceDuration.setText("No Route for Bus");

            	Toast.makeText(getApplicationContext(), "No Route for Bus", Toast.LENGTH_LONG).show();
            	}
              	
              	else if (distance(latitude1, longitude1, dest_lat2, dest_lang2) < 0.1)
              	{
              		tvDistanceDuration.setText("No Route for Bus");

            	Toast.makeText(getApplicationContext(), "No Route for Bus", Toast.LENGTH_LONG).show();
            	}
                else
                {
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
                }
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

             polyline = map.addPolyline(lineOptions);

           // map.addPolyline(lineOptions);


        }
    }
    
    public void onLocationChanged(Location location) {

	 double lat2 = location.getLatitude();
	 double lng2 = location.getLongitude();

	    // lat1 and lng1 are the values of a previously stored location
	    if (distance(latitude1, longitude1, lat2, lng2) < 0.1) { // if distance < 0.1 miles we take locations as equal
	       //do what you want to do...
	    }
	}
	/** calculates the distance between two locations in MILES */
	private double distance(double lat1, double lng1, double lat2, double lng2) {

	    double earthRadius = 3958.75; // in miles, change to 6371 for kilometer output

	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);

	    double sindLat = Math.sin(dLat / 2);
	    double sindLng = Math.sin(dLng / 2);

	    double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
	        * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));

	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

	    double dist = earthRadius * c;

	    return dist; // output distance, in MILES
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

        private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
               // Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                //Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }
}


//    public void showDirections(View view) {
//
//
//        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" + "saddr=" + latitude + "," + longitude + "&daddr=" + latitude1 + "," + longitude1));
//        intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
//        startActivity(intent);
//    }




