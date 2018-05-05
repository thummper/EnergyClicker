package aronb.energyclicker;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aron on 05/05/2018.
 */

public class SettingsLoader extends android.support.v4.content.AsyncTaskLoader<List<String>> {
    //I want to load settings in the form of "ID VALUE"
    private List<String> settingList;


    public SettingsLoader(Context context){
        super(context);


    }

    //Runs on worker thread
    @Override
    public List<String> loadInBackground(){
        System.out.println("LOADING IN BG");

        List<String> loadedData = new ArrayList<>();

        Cursor settingsFromProvider = this.getContext().getContentResolver().query(SettingsDBData.CONTENT_URI, null, null,null,null);

        if(settingsFromProvider.getCount() > 0){
            settingsFromProvider.moveToFirst();
            do{
                String setting = settingsFromProvider.getString(0);
                String value = settingsFromProvider.getString(1);

                loadedData.add(setting + " " + value);
            } while (settingsFromProvider.moveToNext());

        }
    return loadedData;
    }

    //Called when there is new data to deliver to client
    @Override
    public void deliverResult(List<String> settings){
        if(isReset()) {
            //Loader has been reset, ignore result

            if(settings != null){
                releaseResources(settings);
                return;
            }
        }


        List<String> oldSettings = settingList;
        settingList = settings;

        if (isStarted()) {

            //Loader in started state?
            super.deliverResult(settings);
        }
    }

    @Override
    protected void onStartLoading(){
        if(settingList != null){
            //Deliver any previously loaded data immediatly
            deliverResult(settingList);
        }
        if(settingList == null){
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading(){
        cancelLoad();
    }

    @Override
    protected void onReset(){
        onStopLoading();

        if(settingList !=null){
            releaseResources(settingList);
            settingList = null;
        }

    }

    @Override
    public void onCanceled(List<String> settings){
        super.onCanceled(settings);
        releaseResources(settings);
    }


    public void releaseResources(List<String> settings){

    }


}
