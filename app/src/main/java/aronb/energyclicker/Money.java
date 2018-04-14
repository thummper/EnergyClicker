package aronb.energyclicker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Money extends Fragment {
    public TextView moneyTextView;
    TextView powerTextView;







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_money, container, false);
        moneyTextView = (TextView) view.findViewById(R.id.moneyTextView);
        powerTextView = (TextView) view.findViewById(R.id.powerTextView);

        return view;
    }
    public void setMoney(String money, String mTick){
        moneyTextView.setText("Money: £ " + money + " ( "+ mTick +")" );
    }
    public void setPowerGen(String powerTick, String powerD, boolean day){
        if(day){
            powerTextView.setText("Power/Day:   " + powerD + "Wh    (" + powerTick + "Wh)" );

        } else {
            powerTextView.setText("Power/Night:   " + powerD + "Wh    (" + powerTick + "Wh)" );

        }



    }

}
