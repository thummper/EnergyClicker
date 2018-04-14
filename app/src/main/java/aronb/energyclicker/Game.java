package aronb.energyclicker;

import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable{

    boolean day = true;
    public int ticks = 0;
    public int totalTicks = 0;
    double money = 100000;
    double oldMoney = 0;
    double powerBonus = 1;



    //Power Stuff
    public double powerAdd = 0;
    public double power = 0;
    public double powerTick = 0;
    public double oldPower = 0;
    public double powerCost = 0.14; //(PER KWH)

    //Solar Stuff
    public double solarPanelCost = 4000;
    public int solarPanelAmount = 0;
    public double solarPanelPPT = 5000;
    public double solarPanelNPPT = 2000;
    //Solar Upgrades.
    public int numSolarMaterial = 0;
    public double materialBasePrice = 1000;
    ArrayList<Upgrade> solarMaterialUpgrades = new ArrayList<>();




    //Wind Stuff
    public double windTurbineCost = 50000;
    public int windTurbineAmount = 0;
    public double windTurbinePPT = 2054;
    public double windSpeed = 5;
    public double windUpper = 33;
    public double windLower = 8;
    //Wind Upgrades
    public int numWindMaterial = 0;
    public double windMaterialBasePrice = 10000;
    ArrayList<Upgrade> windUpgradeList = new ArrayList<>();

    //Hydro Stuff
    public double hydroCost = 20000000000L;
    public int hydroAmount = 1;
    public double hydroPPT = 1500000000;
    public double hydroEfficiency = 0.5;
    public double effUpgradeCost = 500;
    public double hydroDiscountUpgradeCost = 750;
    public double hydroDiscountAmount = 1;
    //HydroEffUpgrades
    public int numHydroEffUpgrades = 0;
    public double hydroEffUpgradeBasePrice = 100000;
    ArrayList<Upgrade> hydroEffUpgradeList = new ArrayList<>();




    //Should probably refactor to work off of an array (redundancy)
    public void buy(int item){
        if(item == 0){
            //Buy solar panel.
            if(money >= solarPanelCost) {
                money -= solarPanelCost;
                solarPanelAmount++;

                if(solarPanelAmount > 0 && solarPanelAmount <= 30){

                    solarPanelCost = Math.pow(solarPanelCost, 1.02);

                } else if(solarPanelAmount > 30 && solarPanelAmount <= 50){

                    solarPanelCost = Math.pow(solarPanelCost, 1.01);

                } else if(solarPanelAmount > 50 && solarPanelAmount <= 80 ){

                    solarPanelCost = Math.pow(solarPanelCost, 1.004);

                } else if(solarPanelAmount > 80 && solarPanelAmount <= 100) {

                    solarPanelCost = Math.pow(solarPanelCost, 1.002);

                } else {

                    solarPanelCost = Math.pow(solarPanelCost, 1.001);
                }

            }
        } else if(item == 1){
            //Buy wind turbine
            if(money >= windTurbineCost){
                money -= windTurbineCost;
                windTurbineAmount++;


                if(windTurbineAmount > 0 && windTurbineAmount <= 3){
                    windTurbineCost = Math.pow(windTurbineCost, 1.04);
                } else if(windTurbineAmount > 3 && windTurbineAmount <= 6){
                    windTurbineCost = Math.pow(windTurbineCost, 1.02);
                } else if(windTurbineAmount > 6 && windTurbineAmount <= 9 ){
                    windTurbineCost = Math.pow(windTurbineCost, 1.012);
                } else {
                    windTurbineCost = Math.pow(windTurbineCost, 1.005);
                }

            }
        } else if(item == 2){
            //Buy hydro
            if(money >= hydroCost){
                money -= hydroCost;
                hydroAmount++;


                if(hydroAmount > 0 && hydroAmount <= 2){
                    hydroCost = Math.pow(hydroCost, 1.15) / hydroDiscountAmount;
                } else if(hydroAmount > 2 && hydroAmount <= 4){
                    hydroCost = Math.pow(hydroCost, 1.08)/ hydroDiscountAmount;
                } else if(hydroAmount > 4 && hydroAmount <= 6 ){
                    hydroCost = Math.pow(hydroCost, 1.03)/ hydroDiscountAmount;
                } else {
                    hydroCost = Math.pow(hydroCost, 1.003)/ hydroDiscountAmount;

                }
            }
        }

    }





    public void loop(){
        //Add solar power
        if(day){

            powerAdd += solarPanelAmount * solarPanelPPT;

        } else {
            //Night
            powerAdd += solarPanelAmount * solarPanelNPPT;
        }
        //Add wind power
        powerAdd += windTurbineAmount * windTurbinePPT * windSpeed;

        //Add hydro power

        powerAdd += hydroAmount * hydroPPT * hydroEfficiency;

        //Used to work out power/money per tick.
        oldPower = power;
        oldMoney = money;
        //Add all new power.
        if(powerAdd > 0){
            power += powerAdd;

        }
        powerAdd = 0;
        powerTick = power - oldPower;

        //Every tick we sell all power
        double profit = (power * powerCost)/1000; //(We add power in Watt/H not KWh so money can use the same round method
        money += profit;
        power = 0;


    }
    public void tick(){
        System.out.println(ticks);


        ticks += 1;
        totalTicks += 1;




        if (ticks == 8) {
            //Change day and wind speed.
            windSpeed = Math.round((Math.random() * (windUpper - windLower)) + windLower);


            if (day) {
                day = false;
            } else {
                day = true;
            }
            ticks = 0;
        }

    }


}
