package aronb.energyclicker;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;



public class UpgradeAdapter extends ArrayAdapter<Upgrade> {
    private final Activity context;
    private ArrayList<Upgrade> upgradeArray;
    private final ListView lvUpgrades;

    public UpgradeAdapter(Activity context, ArrayList<Upgrade> upgradeArray, ListView lvUpgrades){
        super(context, R.layout.material_upgrade_listview, upgradeArray);
        this.context = context;
        this.upgradeArray = upgradeArray;
        this.lvUpgrades = lvUpgrades;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent){
        LayoutInflater inflater = context.getLayoutInflater();
        View listV = inflater.inflate(R.layout.material_upgrade_listview, parent, false);
        TextView name = (TextView) listV.findViewById(R.id.upgradeName);
        TextView cost = (TextView) listV.findViewById(R.id.upgradeCost);
        TextView desc = (TextView) listV.findViewById(R.id.upgradeDesc);

        name.setText(upgradeArray.get(position).upgradeName);
        cost.setText("£ " + Main.roundP(upgradeArray.get(position).cost));
        desc.setText(upgradeArray.get(position).upgradeDescription);


        return listV;

    }


}
