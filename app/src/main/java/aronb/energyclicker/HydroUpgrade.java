package aronb.energyclicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class HydroUpgrade extends AppCompatActivity {
    Thread upThread;

    TextView hydroEffTextView;
    TextView hydroTotalPowerTextView;
    ListView hydroUpgradesListView;
    UpgradeAdapter hydroAdapter;
    ArrayList<Upgrade> hydroUpgradeList;

    Game game = Main.game;
    Money money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hydro_upgrade);

        hydroEffTextView = (TextView) findViewById(R.id.hydroEffTextView);
        hydroTotalPowerTextView = (TextView) findViewById(R.id.hydroTotalPowerTextView);
        money = (Money) getSupportFragmentManager().findFragmentById(R.id.solarUpgradeMoneyFragment);

        hydroUpgradesListView = (ListView) findViewById(R.id.hydroUpgradeListView);
        hydroUpgradeList = game.hydroEffUpgradeList;
        hydroAdapter = new UpgradeAdapter(this, hydroUpgradeList, hydroUpgradesListView);
        hydroUpgradesListView.setAdapter(hydroAdapter);

        hydroUpgradesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                HydroEffUpgrade upgrade = (HydroEffUpgrade) hydroAdapter.getItem(i);
                if(upgrade.doUpgrade()) {
                    hydroAdapter.remove(upgrade);
                    game.numHydroEffUpgrades = game.hydroEffUpgradeList.size();
                    updateUpgradeUI();
                }

            }
        });



        //Probably a bad way of doing this but i suck.
        if(savedInstanceState == null) {
            upThread = new Thread() {
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(1000);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    //Runs every second
                                    updateUpgradeUI();

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


    updateUpgradeUI();

    }

    @Override
    protected void onDestroy() {
        //Stop the thread when activity is exited.
        if(upThread != null) {
            upThread.interrupt();
        }
        super.onDestroy();
    }
    public void updateUpgradeUI(){
        money.setMoney(Main.roundP(game.money), Main.roundP(game.money - game.oldMoney));
        money.setPowerGen(Main.roundP(game.powerTick), Main.roundP(game.powerTick * 4), game.day);

        hydroTotalPowerTextView.setText("Power/Tick: " + Main.roundP(game.hydroAmount * game.hydroPPT * game.hydroEfficiency) + "Wh");
        hydroEffTextView.setText("Efficiency: " + Main.roundP(game.hydroEfficiency * 100) + "%");
        refreshHydroList();


    }
    public void refreshHydroList(){
        if(game.numHydroEffUpgrades <= 5){
            if(game.numHydroEffUpgrades == 0){
                //Make 5 at once
                for(int i = 0; i < 5; i++){
                    double cost = Main.randomInt((int)game.hydroEffUpgradeBasePrice, (int)(game.hydroEffUpgradeBasePrice * 3.5) );
                    game.hydroEffUpgradeList.add(new HydroEffUpgrade(cost));
                    game.numHydroEffUpgrades = game.hydroEffUpgradeList.size();
                }


            } else {
                //Make 1
                double cost = Main.randomInt((int)game.hydroEffUpgradeBasePrice, (int)(game.hydroEffUpgradeBasePrice * 3.5) );
                game.hydroEffUpgradeList.add(new HydroEffUpgrade(cost));
                game.numHydroEffUpgrades = game.hydroEffUpgradeList.size();
            }
        }



        hydroAdapter.notifyDataSetChanged();


    }


}
