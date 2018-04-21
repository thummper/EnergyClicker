package aronb.energyclicker;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Aron on 21/04/2018.
 */

public class SUTab2 extends android.support.v4.app.Fragment {


    private SUTab2.OnFragmentInteractionListener mListener;

    public SUTab2() {
        // Required empty public constructor
    }


    public static SUTab2 newInstance() {
        SUTab2 fragment = new SUTab2();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab3, container, false);
        return view;

    }

    public void updateUpgradeUI(){


    }





    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SUTab2.OnFragmentInteractionListener) {
            mListener = (SUTab2.OnFragmentInteractionListener) context;
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
