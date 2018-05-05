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


public class Tab4 extends Fragment {
    Button buyHydro;
    Button statsButton;
    Button upgradeHydro;
    TextView hydroEff;
    TextView hydroAmount;
    CircleImageView image;


    private OnFragmentInteractionListener mListener;

    public Tab4() {
    }


    public static Tab4 newInstance(String param1, String param2) {
        Tab4 fragment = new Tab4();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab4, container, false);
        image = (CircleImageView) view.findViewById(R.id.hydro_image);
        buyHydro = (Button) view.findViewById(R.id.buyHydroPlant);
        upgradeHydro = (Button) view.findViewById(R.id.upgradeHydroButton);
        hydroEff = (TextView) view.findViewById(R.id.hydroTextView);
        hydroAmount = (TextView) view.findViewById(R.id.hydroAmountTextView);

        statsButton = (Button) view.findViewById(R.id.tab4StatsButton);
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

        buyHydro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main.game.buy(2);
                Main.updateMainMoney();
                updateUI();
            }
        });

        upgradeHydro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), HydroUpgrade.class);
                startActivity(intent);
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



    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }



    public void updateUI(){
        buyHydro.setText("Buy Hydro Plant\n £ " + Main.roundP(Main.game.hydroCost));
        hydroEff.setText("Efficiency: " + Main.roundP(Main.game.hydroEfficiency * 100) + "%");
        hydroAmount.setText(Main.game.hydroAmount + "");


    }
}
