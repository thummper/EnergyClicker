package aronb.energyclicker;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aron on 05/05/2018.
 */

public class SettingsLoader extends android.support.v4.content.AsyncTaskLoader<List<SettingItem>> {
    //I want to load settings in the form of "ID VALUE"
    private List<SettingItem> settingList;


    public SettingsLoader(Context context){
        super(context);


    }

    //Runs on worker thread
    @Override
    public List<SettingItem> loadInBackground(){
        System.out.println("LOADING IN BG");

        List<SettingItem> loadedData = new ArrayList<>();

        Cursor settingsFromProvider = this.getContext().getContentResolver().query(SettingsDBData.CONTENT_URI, null, null,null,null);

        if(settingsFromProvider.getCount() > 0){
            settingsFromProvider.moveToFirst();
            do{
                SettingItem item = new SettingItem(settingsFromProvider.getString(0), settingsFromProvider.getString(1));


                loadedData.add(item);
            } while (settingsFromProvider.moveToNext());

        }
    return loadedData;
    }

    //Called when there is new data to deliver to client
    @Override
    public void deliverResult(List<SettingItem> settings){
        if(isReset()) {
            //Loader has been reset, ignore result

            if(settings != null){
                releaseResources(settings);
                return;
            }
        }


        List<SettingItem> oldSettings = settingList;
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
    public void onCanceled(List<SettingItem> settings){
        super.onCanceled(settings);
        releaseResources(settings);
    }


    public void releaseResources(List<SettingItem> settings){

    }


}
