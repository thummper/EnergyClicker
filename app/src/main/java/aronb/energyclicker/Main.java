package aronb.energyclicker;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.tickaroo.tikxml.XmlDataException;
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory;


import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;


public class Main extends AppCompatActivity implements aronb.energyclicker.Tab1.OnFragmentInteractionListener, aronb.energyclicker.Tab2.OnFragmentInteractionListener, aronb.energyclicker.Tab3.OnFragmentInteractionListener, aronb.energyclicker.Tab4.OnFragmentInteractionListener{


    //Static stuff because i sux
    static boolean threadStarted = false;
    static Money money;
    static Game game = new Game();

    //Game/Layout related
    TabLayout tabLayout;
    Thread gameThread;
    ViewPager viewPager;
    PagerAdapter adapter;
    //Used to determine which tab to update.
    int tabID;
    Tab1 tab1;
    Tab2 tab2;
    Tab3 tab3;
    Tab4 tab4;
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPrefs = getPreferences(MODE_PRIVATE);

        //Get location data to get wind
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        String longitude = Double.toString(location.getLongitude());
        String latitue = Double.toString(location.getLatitude());
        if(location != null){
            //Make request to get windspeed.
            final String baseURL = "http://api.weatherunlocked.com/api/";
            Retrofit retro = new Retrofit.Builder()
                    .baseUrl(baseURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RetroApi api = retro.create(RetroApi.class);

            Call<ResponseBody> call = api.getWeather(longitude, latitue, "REDACTED", "REDACTED");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    int statusCode = response.code();
                    try {
                        String resStr = response.body().string().toString();
                        JSONObject json = new JSONObject(resStr);
                        String windspeed = json.getString("windspd_kmh");
                        game.windSpeed = Double.parseDouble(windspeed);
                        game.autoWind = false;
                        System.out.println("API CALL: " + windspeed);
                    } catch (Exception e){
                        e.printStackTrace();
                    }



                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    System.out.println("FAIL CALL");

                    t.printStackTrace();

                }
            });


        } else {
            game.autoWind = true;
        }

        //If we can make an api call successfully, turn auto wind off and use the locations windspeed.



        loadSettings(savedInstanceState);
        money =  (Money) getSupportFragmentManager().findFragmentById(R.id.mainMoney);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Main"));
        tabLayout.addTab(tabLayout.newTab().setText("Solar"));
        tabLayout.addTab(tabLayout.newTab().setText("Wind"));
        tabLayout.addTab(tabLayout.newTab().setText("Hydro"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
                tabID = tab.getPosition();

                System.out.println("TAB ID : " + tabID);



            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tabID = -1;

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                tabID = tab.getPosition();

            }
        });

        if(savedInstanceState == null && threadStarted == false) {



            //Running for the first time, try and load game.
            try {
                loadGame();
            } catch (Exception e){
                System.out.println(e.toString());
            }


            gameThread = new Thread() {
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(1000);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    //Runs every second
                                    game.loop();
                                    if(game.ticks == 7){
                                        saveGame();
                                        if(Main.game.notifications.equals("On")){
                                            sendNotification();
                                        }

                                    }
                                    game.tick();
                                    updateMainMoney();
                                    //Only update the tab that is displayed.

                                    System.out.println("TAB ID IS: " + tabID);
                                    switch (tabID) {
                                        case 0:
                                            if (adapter.tab1 != null) {
                                                adapter.tab1.updateUI();
                                            }
                                        case 1:
                                            if (adapter.tab2 != null) {
                                                adapter.tab2.updateUI();
                                            }
                                        case 2:
                                            if (adapter.tab3 != null) {
                                                adapter.tab3.updateUI();
                                            }
                                        case 3:
                                            if (adapter.tab4 != null) {
                                                adapter.tab4.updateUI();
                                            }
                                        default:
                                            break;

                                    }


                                }
                            });
                        }
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            };
            gameThread.start();



        }







    }

    public void loadSettings(Bundle bundle){
        getSupportLoaderManager().initLoader(0, bundle, new LoaderManager.LoaderCallbacks<List<SettingItem>>() {

            @Override
            public android.support.v4.content.Loader<List<SettingItem>> onCreateLoader(int id, Bundle args) {
                return new SettingsLoader(Main.this);
            }

            @Override
            public void onLoadFinished(android.support.v4.content.Loader<List<SettingItem>> loader, List<SettingItem> data) {
                if(data == null){
                    return;
                }
                for(SettingItem item : data ){
                    if(item.setting.equals("Notification")){
                        if(item.value.equals("On")){
                            game.notifications = "On";
                        } else {
                            game.notifications = "Off";
                        }
                    }
                }



                //Will have to pass data to a list or something.






            }

            @Override
            public void onLoaderReset(android.support.v4.content.Loader<List<SettingItem>> loader) {

            }
        });

    }

    public void sendNotification(){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.rclicker_app_icon_round)
                .setContentTitle("Energy Clicker")
                .setContentText(roundP(game.money) + "")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager nmanager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        nmanager.notify(001, mBuilder.build());

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    public static String roundP(double number){
        //Not sure if trillion breaks this (Trillion = Tera)
        if(number > 1000000000000L){
            return round(number/1000000000000L) + " T";
        }
        else if(number > 1000000000) {
            //Billion
            return round(number/1000000000) + " B";
        } else if(number > 1000000){
            return  round(number/1000000) + " M";
        } else if(number > 1000){
            return  round(number/1000) + " K";
        } else {
            return round(number) + " ";
        }
    }

    public static double round(double number){
        //Rounds double to 2dp
        return Math.round(number * 100.0)/100.0;
    }

    public static void updateMainMoney(){
        money.setMoney(roundP(game.money), roundP(game.money - game.oldMoney));
        money.setPowerGen(roundP(game.powerTick), roundP(game.powerTick * 8), game.day);
    }
    public static int randomInt(int min, int max){
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }



    public void saveGame(){
        try {
            System.out.println("Saving");
            File cacheFile = new File(this.getCacheDir(), "renClickData.data");
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(cacheFile));
            out.writeObject(game);
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    public void loadGame(){
        try{
            System.out.println("Loading");
            File cacheFile = new File(this.getCacheDir(), "renClickData.data");
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(cacheFile));
            Game loadGame = (Game) in.readObject();
            game = loadGame;
            in.close();
        } catch (Exception e){
            System.out.println("            LOADING ERROR");
            e.printStackTrace();
        }



    }

}
