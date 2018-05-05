package aronb.energyclicker;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Aron on 21/04/2018.
 */

public class WUTab2 extends android.support.v4.app.Fragment {
    //Contains the wind factories
    TextView windFactoryAmount;
    Button buyWindFactory;


    private WUTab2.OnFragmentInteractionListener mListener;

    public WUTab2() {
        // Required empty public constructor
    }


    public static WUTab2 newInstance() {
        WUTab2 fragment = new WUTab2();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.windfactorypage, container, false);

        windFactoryAmount = view.findViewById(R.id.windFactoryAmount);
        buyWindFactory = view.findViewById(R.id.buyWindFactory);

        buyWindFactory.setText("Buy Wind Factory: " + Main.roundP(Main.game.windFactoryCost));
        windFactoryAmount.setText(Main.game.windFactoryAmount + "");


        buyWindFactory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Main.game.buy(4);
                Main.updateMainMoney();
                buyWindFactory.setText("Buy Wind Factory: " + Main.roundP(Main.game.windFactoryCost));
                windFactoryAmount.setText(Main.game.windFactoryAmount + "");

            }
        });



        return view;

    }

    public void updateUpgradeUI(){


    }





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WUTab2.OnFragmentInteractionListener) {
            mListener = (WUTab2.OnFragmentInteractionListener) context;
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
