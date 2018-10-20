package app.com.hijaupadi.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Imam on 2016-03-15.
 */
public class Databasehelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "hijaupadiraimon";
    private static final int DATABASE_VERSION = 1;

    public Databasehelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        String sql = "create table history(gambar text,created_at timestamp);";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {

        String sql = "DROP TABLE IF EXISTS history";
        db.execSQL(sql);
        onCreate(db);

    }

}

