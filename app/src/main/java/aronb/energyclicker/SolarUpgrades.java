package aronb.energyclicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class SolarUpgrades extends AppCompatActivity {
    Thread upThread;


    ListView solarUpgradeList;

    TextView dayPower;
    TextView nightPower;
    TextView daytick;
    TextView nighttick;
    Money money;
    //Need an array adapter to add items dynamically to the grid view?
    UpgradeAdapter materialGridAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solar_upgrades);


        materialGridAdapter = new UpgradeAdapter(this, Main.game.solarMaterialUpgrades, solarUpgradeList);
        solarUpgradeList = (ListView) findViewById(R.id.solarUpgradeListView);
        solarUpgradeList.setAdapter(materialGridAdapter);
        solarUpgradeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SolarMaterialUpgrade upgrade = (SolarMaterialUpgrade) materialGridAdapter.getItem(i);
                if(upgrade.doUpgrade()) {
                    materialGridAdapter.remove(upgrade);
                    Main.game.numSolarMaterial = Main.game.solarMaterialUpgrades.size();
                    updateUpgradeUI();
                }
            }
        });



        daytick = (TextView) findViewById(R.id.solarDayTickTextView);
        nighttick = (TextView) findViewById(R.id.solarNightTickTextView);
        dayPower = (TextView) findViewById(R.id.solarDayPower);
        nightPower = (TextView) findViewById(R.id.solarNightPower);
        money = (Money) getSupportFragmentManager().findFragmentById(R.id.solarUpgradeMoneyFragment);





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
        money.setMoney(Main.roundP(Main.game.money), Main.roundP(Main.game.money - Main.game.oldMoney));
        money.setPowerGen(Main.roundP(Main.game.powerTick), Main.roundP(Main.game.powerTick * 4), Main.game.day);

        daytick.setText("Day: " + Main.roundP(Main.game.solarPanelPPT) + "Wh");
        nighttick.setText("Night: " + Main.roundP(Main.game.solarPanelNPPT) + "Wh");
        dayPower.setText("Day Power: " + Main.roundP(Main.game.solarPanelAmount * Main.game.solarPanelPPT) + "Wh");
        nightPower.setText("Night Power: " + Main.roundP(Main.game.solarPanelAmount * Main.game.solarPanelNPPT) + "Wh");
        refreshSolarGrid();


    }



    public void refreshSolarGrid(){
        //Will display all upgrades on gridview.
        if(Main.game.numSolarMaterial <= 5){
            //If 0, make 5 at once.
            if(Main.game.numSolarMaterial == 0){
                for(int i = 0; i < 5; i++){
                    //need to pick a random cost.
                    double cost = Main.randomInt((int)Main.game.materialBasePrice, (int)(Main.game. materialBasePrice * 2.5));
                    Main.game.solarMaterialUpgrades.add(new SolarMaterialUpgrade(cost));
                    Main.game.numSolarMaterial = Main.game.solarMaterialUpgrades.size();

                }

            } else {
                //Just make 1.
                double cost = Main.randomInt((int)Main.game.materialBasePrice, (int)(Main.game. materialBasePrice * 2.5));
                Main.game.solarMaterialUpgrades.add(new SolarMaterialUpgrade(cost));
                Main.game.numSolarMaterial = Main.game.solarMaterialUpgrades.size();
            }
        }
        materialGridAdapter.notifyDataSetChanged();
    }


}
