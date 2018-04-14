package aronb.energyclicker;

import java.io.Serializable;

import aronb.energyclicker.Main;
import aronb.energyclicker.Upgrade;


public class SolarMaterialUpgrade extends Upgrade implements Serializable{
    //Will upgrade the material solar panels are made from.
    String[] names = {"Change Solar Material", "Upgrade Solar Material", "Repair Solar Panels", "Clean Solar Panels", "Supercharge Solar Panels"};
    //names and descriptions match up.
    String[] descriptions = {"Replace our manufacturing material with something new",
                             "Replace out manufacturing material with something better",
                             "Repair all damaged solar panels",
                             "Clean off all solar panels",
                             "Enrich panels with pure vanilla"};




    public SolarMaterialUpgrade(double cost) {
        super(cost);
        //Pick a number for the name and desc.
        int max = names.length - 1;
        int min = 0;
        int random = Main.randomInt(min,max);
        super.setName(names[random]);
        super.setDescription(descriptions[random]);


    }

    public boolean doUpgrade(){
        //Material upgrades will increase day and night PPT
        if(Main.game.money > cost){
            Main.game.money -= cost;
            //Gets really OP at end?
            Main.game.solarPanelNPPT *= 1.015;
            Main.game.solarPanelPPT *= 1.035;
            Main.game.materialBasePrice += Main.game.solarPanelAmount;
            return true;
        } else {
            return false;
        }
    }




}
