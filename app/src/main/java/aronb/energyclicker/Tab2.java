package aronb.energyclicker;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;



import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;


public class Tab2 extends Fragment {

    Button buySolarButton;
    Button statsButton;
    Button upgradeSolar;
    TextView day;
    TextView panelAmount;
    CircleImageView image;
    private OnFragmentInteractionListener mListener;


    public Tab2() {
        // Required empty public constructor
    }

    public static Tab2 newInstance() {
        Tab2 fragment = new Tab2();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab2, container, false);

        statsButton = (Button) view.findViewById(R.id.tab2StatsButton);
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intents = new Intent(view.getContext(), Stats.class);
                startActivity(intents);
            }
        });

        image = (CircleImageView) view.findViewById(R.id.solar_image);
        day = (TextView) view.findViewById(R.id.dayTextView);
        buySolarButton = (Button) view.findViewById(R.id.buySolarButton);
        panelAmount = (TextView) view.findViewById(R.id.solarAmountTextView);
        upgradeSolar = (Button) view.findViewById(R.id.upgradeSolarButton);


        if(getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            image.setVisibility(View.GONE);
        } else {
            image.setVisibility(View.VISIBLE);
        }


        buySolarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main.game.buy(0);
                updateUI();
                Main.updateMainMoney();

            }
        });
        upgradeSolar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SolarUpgrades.class);
                startActivity(intent);
            }
        });


        updateUI();


        return view;
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        System.out.println("CONFIG CHANGED");


        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            //Remove picture on landscape
            System.out.println("LANDSCAPE");
            image.setVisibility(View.GONE);
        }
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            image.setVisibility(View.VISIBLE);
        }

        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
    //My methods

    public void updateUI(){
        if (Main.game.day){
            day.setText("Day");
            Main.updateMainMoney();
        } else {
            day.setText("Night (Reduced Generation)");
            Main.updateMainMoney();

        }


        buySolarButton.setText("Solar Panel\n £" + Main.roundP(Main.game.solarPanelCost));
        panelAmount.setText(Main.game.solarPanelAmount + "");

    }
}
