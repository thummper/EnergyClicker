package aronb.energyclicker;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


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
                                        sendNotification();
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

    public void sendNotification(){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.wturbine)
                .setContentTitle("Energy Clicker")
                .setContentText(Main.game.money + "")
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
