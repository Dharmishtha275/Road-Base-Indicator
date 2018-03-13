package com.example.roadbaseindicator;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Cisco on 19-04-2016.
 */
public class view_all extends Activity {

    Button map,addrs,srch;
    protected void onCreate(Bundle s)
    {
        super.onCreate(s);
        setContentView(R.layout.view_all);


        final String source=getIntent().getStringExtra("source");
        final String dest=getIntent().getStringExtra("destination");
        map=(Button)findViewById(R.id.map);
        addrs=(Button)findViewById(R.id.adrs);
        srch=(Button)findViewById(R.id.srch);


        addrs.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent i= new Intent(getApplicationContext(),demomap.class);
                 i.putExtra("src",source);
                 i.putExtra("dest",dest);
                 startActivity(i);
             }
         });



                srch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i= new Intent(getApplicationContext(),direction.class);
                startActivity(i);
            }
        });

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               finish();
                }
        });


    }
}
