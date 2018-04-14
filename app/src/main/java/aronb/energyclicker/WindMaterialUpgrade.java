package aronb.energyclicker;


import java.io.Serializable;

public class WindMaterialUpgrade extends Upgrade implements Serializable {
    //Will upgrade the material solar panels are made from.
    String[] names = {
            "Increase Turbine Height",
            "Upgrade Turbine Material",
            "Grease up Turbines",
            "Increase Blade Length"
    };
    //names and descriptions match up.
    String[] descriptions = {
            "Taller turbines = more power",
            "Stronger turbines to reduce failure rate",
            "Turbines spin faster generating more power",
            "A larger surface area provides more contact with the air"

    };




    public WindMaterialUpgrade(double cost) {
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
            Main.game.windTurbinePPT += 5 * Main.game.windTurbineAmount/3;
            Main.game.windMaterialBasePrice += Main.game.windTurbineAmount;
            return true;

        } else {

            return false;
        }
    }



}
