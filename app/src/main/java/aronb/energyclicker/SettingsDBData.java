package aronb.energyclicker;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Aron on 04/05/2018.
 */

public class SettingsDBData {

    private SettingsDBData(){}
    public static final String DATABASE_NAME = "SettingsDB";
    public static final int DATABASE_VERSION = 1;

    public static final String AUTHORITY = "aronb.energyclicker.provider.Settings";
    public static final Uri CONTENT_URI = Uri.parse(
            "content://" + AUTHORITY + "/settings"
    );

    public static final String CONTENT_TYPE_ARTICLES_LIST = "vnd.android.cursor.dir/vnd.aronb.settings";
    public static final String CONTENT_TYPE_ARTICLE_ONE = "vnd.android.cursor.item/vnd.aronb.settings";


    public class SettingsTable implements BaseColumns{
        private SettingsTable(){}
        public static final String TABLE_NAME = "SettingsTable";
        public static final String ID = "_id";
        public static final String SETTING = "setting";
        public static final String VALUE = "value";

    }







}
