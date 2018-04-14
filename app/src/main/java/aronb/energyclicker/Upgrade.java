package aronb.energyclicker;


import java.io.Serializable;

public class Upgrade implements Serializable{
    //So each upgrade has each of these - generated at start/over time, with a random price (semi-random)
    String upgradeName;
    String upgradeDescription;
    double cost;

    public Upgrade(double cost){
        this.cost = cost;
    }
    public void setName(String name){
        this.upgradeName = name;
    }
    public void setDescription(String desc){
        this.upgradeDescription = desc;
    }


}
