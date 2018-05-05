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


public class Tab3 extends Fragment {
    //Wind Turbines

    Button buyTurbines;
    Button statsButton;
    TextView windSpeed;
    Button upgradeTurbines;
    TextView turbineAmount;
    CircleImageView image;


    private OnFragmentInteractionListener mListener;

    public Tab3() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab3, container, false);
        image = (CircleImageView) view.findViewById(R.id.turbine_image);
        buyTurbines = (Button) view.findViewById(R.id.buyWindTurbine);
        upgradeTurbines = (Button) view.findViewById(R.id.upgradeWindTurbine);
        windSpeed =  (TextView) view.findViewById(R.id.windTextview);
        turbineAmount = (TextView) view.findViewById(R.id.windAmountTextView);

        statsButton = (Button) view.findViewById(R.id.tab3StatsButton);
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intents = new Intent(view.getContext(), Stats.class);
                startActivity(intents);
            }
        });

        if(getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            image.setVisibility(View.GONE);
        } else {
            image.setVisibility(View.VISIBLE);
        }

        buyTurbines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main.game.buy(1);
                updateUI();
            }
        });
        upgradeTurbines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intents = new Intent(view.getContext(), WindUpgrade.class);
                startActivity(intents);
            }
        });
        updateUI();


        return view;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            //Remove picture on landscape
            image.setVisibility(View.GONE);
        } else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            image.setVisibility(View.VISIBLE);
        }

        super.onConfigurationChanged(newConfig);
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
    public void updateUI(){
        Main.updateMainMoney();
        windSpeed.setText("Wind Speed: " + Main.roundP(Main.game.windSpeed) + "KMh");
        buyTurbines.setText("Wind Turbine\n £ " + Main.roundP(Main.game.windTurbineCost));
        turbineAmount.setText(Main.game.windTurbineAmount + "");

    }
}
