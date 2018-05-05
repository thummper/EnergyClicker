package aronb.energyclicker;

import android.content.ContentValues;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aron on 05/05/2018.
 */

public class SettingsAdapter extends ArrayAdapter<SettingItem> {
    private final Context context;
    private final ArrayList<SettingItem> settingItems;

    public SettingsAdapter(Context context, ArrayList<SettingItem> settings){
        super(context, 0, settings);
        this.context = context;
        this.settingItems = settings;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        View v = convertView;
        if(v == null)
            v = LayoutInflater.from(context).inflate(R.layout.settingsrow, parent, false);

        SettingItem item = settingItems.get(position);


        final TextView settingName = v.findViewById(R.id.settingName);
        CheckBox settingValue = v.findViewById(R.id.settingValue);

        settingName.setText(item.setting);

        if(item.value.equals("On")){
            settingValue.setChecked(true);
        } else {
            settingValue.setChecked(false);
        }

        settingValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("PRESSED CHECKBOX: " + settingItems.get(position).setting + " VAL: " + settingItems.get(position).value);

                ContentValues cv = new ContentValues();
                cv.put("setting", settingItems.get(position).setting);
                if(settingItems.get(position).value.equals("On")){
                    cv.put("value", "Off");


                } else {
                    cv.put("value", "On");
                }
                String[] where = new String[1];
                where[0] = settingItems.get(position).setting;
                view.getContext().getContentResolver().update(SettingsDBData.CONTENT_URI, cv, null, null);
            }
        });




    return v;
    }
}
