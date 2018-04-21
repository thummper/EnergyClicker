package aronb.energyclicker;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aron on 21/04/2018.
 */

public class SUTab1 extends android.support.v4.app.Fragment {

    Game game = Main.game;



    TextView solarDayPower;
    TextView solarNightPower;
    TextView solarDayNight;
    ListView solarUpgradeListView;
    UpgradeAdapter solarUpgradeAdapter;
    ArrayList<Upgrade> solarUpgradeList;


    private SUTab1.OnFragmentInteractionListener mListener;

    public SUTab1() {
        // Required empty public constructor
    }


    public static SUTab1 newInstance() {
        SUTab1 fragment = new SUTab1();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.solar_upgrade_tab1, container, false);



        solarDayPower = (TextView) view.findViewById(R.id.solarDayPower);
        solarNightPower = (TextView) view.findViewById(R.id.solarNightPower);
        solarDayNight = (TextView) view.findViewById(R.id.solarDayNight);
        solarUpgradeListView = (ListView) view.findViewById(R.id.solarUpgradeListView);

        solarUpgradeList = game.solarMaterialUpgrades;


        solarUpgradeAdapter = new UpgradeAdapter(getActivity(), solarUpgradeList, solarUpgradeListView);
        solarUpgradeListView.setAdapter(solarUpgradeAdapter);

        solarUpgradeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SolarMaterialUpgrade upgrade = (SolarMaterialUpgrade) solarUpgradeAdapter.getItem(i);
                if(upgrade.doUpgrade()) {
                    solarUpgradeAdapter.remove(upgrade);
                    game.numSolarMaterial = game.solarMaterialUpgrades.size();
                    updateUpgradeUI();
                }

            }
        });




        updateUpgradeUI();





        return view;
    }


    public void updateUpgradeUI(){

        solarDayPower.setText(Main.round(Main.game.solarPanelAmount * Main.game.solarPanelPPT) + "");
        solarNightPower.setText(Main.round(Main.game.solarPanelAmount * Main.game.solarPanelNPPT) + "");
        String time = "";
        if(Main.game.day) {time = "Day";} else time = "Night";
        solarDayNight.setText(time);

        refreshSolarGrid();

    }

    public void refreshSolarGrid(){
        if(game.numSolarMaterial <= 5){
            if(game.numSolarMaterial == 0){
                //Make 5 at once
                for(int i = 0; i < 5; i++){
                    double cost = Main.randomInt((int)game.materialBasePrice, (int)(game.materialBasePrice * 3) );
                    game.solarMaterialUpgrades.add(new SolarMaterialUpgrade(cost));
                    game.numSolarMaterial = game.solarMaterialUpgrades.size();
                }


            } else {
                //Make 1
                double cost = Main.randomInt((int)game.materialBasePrice, (int)(game.materialBasePrice * 3) );
                game.solarMaterialUpgrades.add(new SolarMaterialUpgrade(cost));
                game.numSolarMaterial = game.solarMaterialUpgrades.size();
            }
        }



        solarUpgradeAdapter.notifyDataSetChanged();


    }





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SUTab1.OnFragmentInteractionListener) {
            mListener = (SUTab1.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
