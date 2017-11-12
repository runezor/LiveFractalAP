package com.example.runezor.wallpaper1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;


import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;


public class MainActivity extends AppCompatActivity {

    public int interval=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn=findViewById(R.id.button);
        Button btn2=findViewById(R.id.button2);
        final EditText edtxt=findViewById(R.id.editText);
        final Context general_context=this;

        //Laver timer
        final AlarmManager mgr=(AlarmManager) getSystemService(Context.ALARM_SERVICE);
        final PendingIntent pi = PendingIntent.getBroadcast( this, 0, new Intent("com.example.filterMe"), FLAG_UPDATE_CURRENT);

        BroadcastReceiver br = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent i) {
                setWallpaper("http://www.kaarl.dk/mandelbrot/uploads/1.jpg");
                //mgr.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), interval, pi);
            }
        };
        registerReceiver(br, new IntentFilter("com.example.filterMe") );












        //Starter timer
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // schedule the task to run starting now and then every hour...

                //timer.schedule (hourlyTask, 0l, 1000*Integer.parseInt(edtxt.getText().toString()));
                interval=1000*Integer.parseInt(edtxt.getText().toString());
                mgr.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), interval, pi); // 30 second repeat for testing
                Toast.makeText(MainActivity.this, "Timer started", Toast.LENGTH_SHORT).show();
            }
        });

        //Stopper timer
        btn2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // schedule the task to run starting now and then every hour...
                //timer.cancel();
                Intent intentstop = new Intent("com.example.filterMe");
                PendingIntent senderstop = PendingIntent.getBroadcast(general_context,
                        1234567, intentstop, 0);
                AlarmManager alarmManagerstop = (AlarmManager) getSystemService(ALARM_SERVICE);

                alarmManagerstop.cancel(senderstop);


                Toast.makeText(MainActivity.this, "Timer stopped", Toast.LENGTH_SHORT).show();
            }
        });


    }


    protected void setWallpaper(String url){
        Picasso.with(this).load(url).memoryPolicy(MemoryPolicy.NO_CACHE) .networkPolicy(NetworkPolicy.NO_CACHE).into(new Target(){

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(MainActivity.this);
                try {
                    wallpaperManager.setBitmap(bitmap);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                Log.d("TAG", "onBitmapLoaded: ");
                Toast.makeText(MainActivity.this, "Wallpaper Changed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBitmapFailed(final Drawable errorDrawable) {
                Log.d("TAG", "FAILED");
                Toast.makeText(MainActivity.this, "Loading image failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPrepareLoad(final Drawable placeHolderDrawable) {
                Log.d("TAG", "Prepare Load");
                Toast.makeText(MainActivity.this, "Downloading image", Toast.LENGTH_SHORT).show();
            }
        });
    }

}


