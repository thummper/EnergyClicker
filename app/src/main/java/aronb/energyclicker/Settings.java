package aronb.energyclicker;


import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;

public class Settings extends AppCompatActivity {
    ListView settingsView;
    ArrayList<SettingItem> settings = new ArrayList<>();
    SettingsAdapter sAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_settings);


        settingsView = (ListView) findViewById(R.id.settingsContainer);


        loadSettings(savedInstanceState);


        sAdapter = new SettingsAdapter(this, settings);
        settingsView.setAdapter(sAdapter);





    }

    public void loadSettings(Bundle bundle){
        getSupportLoaderManager().initLoader(0, bundle, new LoaderManager.LoaderCallbacks<List<SettingItem>>() {

            @Override
            public android.support.v4.content.Loader<List<SettingItem>> onCreateLoader(int id, Bundle args) {
                return new SettingsLoader(Settings.this);
            }

            @Override
            public void onLoadFinished(android.support.v4.content.Loader<List<SettingItem>> loader, List<SettingItem> data) {
                if(data == null){
                    return;
                }
                for(SettingItem item : data ){
                    System.out.println("Setting: " + item.setting + " Value: " + item.value);
                    System.out.println(item.value);
                    settings.add(item);
                    Main.game.notifications = item.value;
                    sAdapter.notifyDataSetChanged();
                }



                    //Will have to pass data to a list or something.






            }

            @Override
            public void onLoaderReset(android.support.v4.content.Loader<List<SettingItem>> loader) {

            }
        });

    }

}
