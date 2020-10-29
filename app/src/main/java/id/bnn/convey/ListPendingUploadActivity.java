package id.bnn.convey;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.vision.text.Line;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.bnn.convey.Adapter.PendingUploadAdapter;
import id.bnn.convey.Model.PendingUploadModel;

public class ListPendingUploadActivity extends AppCompatActivity {

    LinearLayout tombolback;
    TextView texttoolbar;
    Context context;

    RecyclerView recyclerview;
    List<PendingUploadModel> list;
    PendingUploadAdapter adapter;

    Database database;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pending_upload);

        context = this;

        texttoolbar = findViewById(R.id.texttoolbar);
        texttoolbar.setText(getIntent().getStringExtra("toolbar"));
        tombolback = findViewById(R.id.tombolkembali);
        tombolback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endactivity();
            }
        });

        recyclerview = findViewById(R.id.viewrecycler);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(context));

        getDatabase();
    }

    public void endactivity(){
        finish();
    }

    public void getDatabase(){
        database = new Database(this);
        db = database.getReadableDatabase();

        String selection = "SELECT *"
                +" FROM "+database.TABLE_SURVEYIN_UPLOAD;

        String[] selectionArgs = {};

        Cursor cursor = db.rawQuery(selection, selectionArgs);
        list = new ArrayList<>();
        while(cursor.moveToNext()){
            try {
                String data = cursor.getString(cursor.getColumnIndex(database.COL_UPL_DATA));
                Log.d("TAGGAR", "ISI JSON "+data);
                String tipe = cursor.getString(cursor.getColumnIndex(database.COL_DB_TIPE));

                JSONObject datajson = new JSONObject(data);
                JSONObject datajson_n = new JSONObject(datajson.getString("surveyin"));

                String nocontainer = datajson_n.getString("no_container");
                String ket = "";

                switch (tipe){
                    case "P":
                        ket = "Survey selesai di upload";
                        break;
                    case "W":
                        ket = "Survey sedang di upload";
                        break;
                    case "X":
                        ket = "Survey selesai";
                }

                if(!ket.equals("")){
                    list.add(new PendingUploadModel(nocontainer, ket));
                }
            } catch (Exception e) {
                Log.d("TAGGAR", "TESTING CRON GAGAL "+e);
            }
        }

        adapter = new PendingUploadAdapter(context, list);
        recyclerview.setAdapter(adapter);
        db.close();
    }
}