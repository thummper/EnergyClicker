package aronb.energyclicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class WindUpgrade extends AppCompatActivity {
    Thread upThread;
    Game game = Main.game;
    Money money;

    TextView windSpeed;
    TextView basePower;
    TextView currentPower;
    ListView windUpgrades;
    UpgradeAdapter windListAdapter;
    ArrayList<Upgrade> windUpgradeList;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wind_upgrade);

        money = (Money) getSupportFragmentManager().findFragmentById(R.id.solarUpgradeMoneyFragment);

        windSpeed = (TextView) findViewById(R.id.windUpgradeWindSpeedTextView);
        basePower = (TextView) findViewById(R.id.windUpgradeBasePowerTextView);
        currentPower = (TextView) findViewById(R.id.windUpgradeTotalPowerTextView);
        windUpgrades = (ListView) findViewById(R.id.windUpgradeListView);
        windUpgradeList = game.windUpgradeList;
        windListAdapter = new UpgradeAdapter(this, windUpgradeList, windUpgrades);
        windUpgrades.setAdapter(windListAdapter);

        windUpgrades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                WindMaterialUpgrade upgrade = (WindMaterialUpgrade) windListAdapter.getItem(i);
                if(upgrade.doUpgrade()) {
                    windListAdapter.remove(upgrade);
                    game.numWindMaterial = game.windUpgradeList.size();
                    updateUpgradeUI();
                }

            }
        });





        //Start loop for this activity.
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
        windSpeed.setText("Wind Speed: " + game.windSpeed + " KMh");
        basePower.setText("Base Gen: " + Main.roundP(game.windTurbinePPT * game.windTurbineAmount ) + "Wh");
        currentPower.setText("Total Gen: " + Main.roundP(game.windTurbinePPT * game.windTurbineAmount * game.windSpeed ) + "Wh");

        refreshWindGrid();
    }

    public void refreshWindGrid(){
        if(game.numWindMaterial <= 5){
            if(game.numWindMaterial == 0){
                //Make 5 at once
                for(int i = 0; i < 5; i++){
                    double cost = Main.randomInt((int)game.windMaterialBasePrice, (int)(game.windMaterialBasePrice * 3) );
                    game.windUpgradeList.add(new WindMaterialUpgrade(cost));
                    game.numWindMaterial = game.windUpgradeList.size();
                }


            } else {
                //Make 1
                double cost = Main.randomInt((int)game.windMaterialBasePrice, (int)(game.windMaterialBasePrice * 3) );
                game.windUpgradeList.add(new WindMaterialUpgrade(cost));
                game.numWindMaterial = game.windUpgradeList.size();
            }
        }



        windListAdapter.notifyDataSetChanged();


    }


}
