package com.example.roadbaseindicator;

   import java.io.IOException;
        import java.util.List;

   import android.content.Context;
   import android.content.Intent;
   import android.location.Address;
        import android.location.Geocoder;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.v4.app.FragmentActivity;
        import android.view.Menu;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.MarkerOptions;

public class demomap extends FragmentActivity {

    EditText src,dest;
    String sdata,ddata;
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_address);
        src=(EditText)findViewById(R.id.eadrs);

        dest=(EditText)findViewById(R.id.edest);
        sdata=getIntent().getStringExtra("src");
        ddata=getIntent().getStringExtra("dest");
        src.setText(sdata);
        dest.setText(ddata);

        back=(Button)findViewById(R.id.button2);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Intent i= new Intent(getApplicationContext(),MainActivity.class);
                //startActivity(i);
                finish();
            }
        });

    }
}