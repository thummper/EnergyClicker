package aronb.energyclicker;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class WUTab1 extends android.support.v4.app.Fragment {
    Thread upThread;
    Game game = Main.game;
    Money money;

    TextView windSpeed;
    TextView basePower;
    TextView currentPower;
    ListView windUpgrades;
    UpgradeAdapter windListAdapter;
    ArrayList<Upgrade> windUpgradeList;

    private WUTab1.OnFragmentInteractionListener mListener;

    public WUTab1() {
        // Required empty public constructor
    }


    public static WUTab1 newInstance() {
        WUTab1 fragment = new WUTab1();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wind_upgrade_tab1, container, false);




        windSpeed = (TextView) view.findViewById(R.id.windUpgradeWindSpeedTextView);
        basePower = (TextView) view.findViewById(R.id.windUpgradeBasePowerTextView);
        currentPower = (TextView) view.findViewById(R.id.windUpgradeTotalPowerTextView);
        windUpgrades = (ListView) view.findViewById(R.id.windUpgradeListView);
        windUpgradeList = game.windUpgradeList;
        windListAdapter = new UpgradeAdapter(getActivity(), windUpgradeList, windUpgrades);
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




        updateUpgradeUI();





        return view;
    }








    public void updateUpgradeUI(){
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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WUTab1.OnFragmentInteractionListener) {
            mListener = (WUTab1.OnFragmentInteractionListener) context;
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
