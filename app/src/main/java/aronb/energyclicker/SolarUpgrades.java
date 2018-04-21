package aronb.energyclicker;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class SolarUpgrades extends AppCompatActivity implements aronb.energyclicker.SUTab1.OnFragmentInteractionListener, aronb.energyclicker.SUTab2.OnFragmentInteractionListener {


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //Game/Layout related
    Thread upThread;
    TabLayout tabLayout;
    ViewPager viewPager;
    SolarUpgradePageAdapter adapter;
    Money money;

    //Tab info
    int tabID;
    SUTab1 tab1;
    SUTab2 tab2;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_solar_upgrades);
        tabLayout = (TabLayout) findViewById(R.id.solartablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Upgrade"));
        tabLayout.addTab(tabLayout.newTab().setText("Factories"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        money = (Money) getSupportFragmentManager().findFragmentById(R.id.mainMoney);

        viewPager = (ViewPager) findViewById(R.id.solarviewpager);
        adapter = new SolarUpgradePageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
                tabID = tab.getPosition();





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


        upThread = new Thread() {
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //Runs every second
                                money.setMoney(Main.roundP(Main.game.money), Main.roundP(Main.game.money - Main.game.oldMoney));
                                money.setPowerGen(Main.roundP(Main.game.powerTick), Main.roundP(Main.game.powerTick * 4), Main.game.day);
                                System.out.println("TAB ID IS: " + tabID);
                                switch (tabID) {
                                    case 0:
                                        if (adapter.tab1 != null) {
                                            adapter.tab1.updateUpgradeUI();
                                        }
                                    case 1:
                                        if (adapter.tab2 != null) {
                                            adapter.tab2.updateUpgradeUI();
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
        upThread.start();




    }

    @Override
    protected void onDestroy() {
        //Stop the thread when activity is exited.
        if(upThread != null) {
            upThread.interrupt();
        }
        super.onDestroy();
    }
}
