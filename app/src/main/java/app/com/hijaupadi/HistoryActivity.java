package app.com.hijaupadi;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import app.com.hijaupadi.helper.CustomAdapter;
import app.com.hijaupadi.helper.Databasehelper;

public class HistoryActivity extends AppCompatActivity {
    String[] image;
    Databasehelper dbcenter;
    Cursor cursor;
    ListView ListView01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        dbcenter = new Databasehelper(this);
        ListView01 = (ListView) findViewById(R.id.listView1);
        new getdata().execute();
    }

    class getdata extends AsyncTask<Bitmap, Void, Void> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(HistoryActivity.this);
            dialog.setMessage("Mohon tunggu... ");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Bitmap... params) {
            SQLiteDatabase db = dbcenter.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM history order by created_at ASC", null);
            List<String> image_loc = new ArrayList<String>();
            int i = 1;
            if (cursor.moveToFirst()) {
                do {
                    image_loc.add(cursor.getString(0).toString());
                } while (cursor.moveToNext());
            }
            image = new String[image_loc.size()];

            image_loc.toArray(image);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ListView01 = (ListView) findViewById(R.id.listView1);

            ListAdapter adapter = new CustomAdapter(HistoryActivity.this, image);

            ListView01.setAdapter(adapter);
            ListView01.setSelected(true);
            dialog.dismiss();
        }
    }
}
