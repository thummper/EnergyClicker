package aronb.energyclicker;


import android.content.Loader;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import java.util.List;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.content_settings);
        loadSettings(savedInstanceState);





    }

    public void loadSettings(Bundle bundle){
        getSupportLoaderManager().initLoader(0, bundle, new LoaderManager.LoaderCallbacks<List<String>>() {

            @Override
            public android.support.v4.content.Loader<List<String>> onCreateLoader(int id, Bundle args) {
                return new SettingsLoader(Settings.this);
            }

            @Override
            public void onLoadFinished(android.support.v4.content.Loader<List<String>> loader, List<String> data) {
                Log.d("HELLO", "LOADER");
                if(data == null){
                    System.out.println("DATA IS NULL");
                    Log.d("DATA", "NODATA");
                    return;
                }
                    System.out.println("LOADER DATA: " + data);
                    Log.d("DATA", data.get(0));
                    //Will have to pass data to a list or something. 






            }

            @Override
            public void onLoaderReset(android.support.v4.content.Loader<List<String>> loader) {

            }
        });

    }

}
