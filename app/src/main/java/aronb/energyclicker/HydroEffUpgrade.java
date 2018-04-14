package aronb.energyclicker;

import java.io.Serializable;

/**
 * Created by Aron on 28/03/2018.
 */

public class HydroEffUpgrade extends Upgrade implements Serializable {
    //Will increase hydro efficiency
    String[] names = {
            "Improve Management",
            "Adjust to closed loop system",
            "Add more turbines",
            "Replace all wiring"

    };
    //names and descriptions match up.
    String[] descriptions = {
            "Hire new management teams for each hydro facility, improving efficiency.",
            "Reduces environmental impacts",
            "More turbines = more power",
            "Replace old wiring with something more conductive"

    };




    public HydroEffUpgrade(double cost) {
        super(cost);
        //Pick a number for the name and desc.
        int max = names.length - 1;
        int min = 0;
        int random = Main.randomInt(min,max);
        super.setName(names[random]);
        super.setDescription(descriptions[random]);


    }
    public boolean doUpgrade(){
        if(Main.game.money > cost){
            //Will increase efficiency
            Main.game.money -= cost;
            Main.game.hydroEfficiency += 0.025;
            Main.game.hydroEffUpgradeBasePrice += (Main.game.hydroAmount * 2);
            return true;
        } else {
            return false;
        }
    }

}
