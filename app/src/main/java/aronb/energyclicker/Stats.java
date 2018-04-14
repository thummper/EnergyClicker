package aronb.energyclicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Stats extends AppCompatActivity {
    Thread upThread;
    ProgressBar powerProg;
    TextView progressText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);


        progressText = (TextView) findViewById(R.id.powerProgressTextView);
        powerProg = (ProgressBar) findViewById(R.id.powerGenProgressBar);
        //Start loop for this activity.
        if(savedInstanceState == null) {
            upThread = new Thread() {
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(1000);
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    //Runs every second
                                    updateStatsUI();

                                }
                            });
                        }
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            };
            upThread.start();
        }

    }

    @Override
    protected void onDestroy() {
        //Stop the thread when activity is exited.
        if(upThread != null) {
            upThread.interrupt();
        }
        super.onDestroy();
    }

    public void updateStatsUI(){
        //Update progress bar
        //Progress is powerday * 365 / 23816
        double powerYear = Main.game.powerTick * 8 * 365;
        double powerYearTW = powerYear / 1000000000000L;
        double progress =  (powerYearTW / 23816)*100;
        powerProg.setProgress((int) progress );
        progressText.setText(Math.round(powerYearTW * 100.00)/100.00 + " TWh / 23816 TWh" );


    }



}
