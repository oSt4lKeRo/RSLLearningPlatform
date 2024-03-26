package com.example.rsllearningplatform;

import static JavaClass.Test.getContent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CacheRequest;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.SQLData;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import JavaClass.DBHelper;
import JavaClass.ErrorUtils;
import JavaClass.HttpHelper;
import JavaClass.JsonHelper;
import JavaClass.NetworkUtils;
import JavaClass.StructureClass;
import JavaClass.Test;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS moduls (id INTEGER NOT NULL PRIMARY KEY, title TEXT, description TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS lessons (id INTEGER NOT NULL PRIMARY KEY, moduleNumber INTEGER, title TEXT, description TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS steps (id INTEGER NOT NULL PRIMARY KEY, moduleNumber INTEGER, lectureNumber INTEGER, title TEXT, description TEXT)");

        db.execSQL("INSERT OR REPLACE INTO moduls VALUES (1, 'First module', 'Word \"Hello\"')");
        if(NetworkUtils.isNetworkAvailable(this)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String str = getContent("https://certain-delight-275b321500.strapiapp.com/api/modules?publicationState=preview&populate=*");
                        Log.i("GetModules", str);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<StructureClass.Module> modules = new ArrayList<>();
                                try {
                                    modules = JsonHelper.readModule(str);
                                } catch (JSONException e) {
                                    Log.e("JSONTrouble", e.getLocalizedMessage());
                                }
                                for (StructureClass.Module module : modules) {
                                    Button button = createModuleButton(module);
                                    linearLayout.addView(button);
                                }
                            }
                        });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        } else {
            Toast.makeText(this, "Нет интернета", Toast.LENGTH_SHORT).show();
            Log.e("NoInternet", "Нет интернета");

            Button dbClearButton = new Button(this);
            dbClearButton.setText("Очистить БД");
            dbClearButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBHelper.clearDB(db);
                }
            });
            linearLayout.addView(dbClearButton);

            Button restartButton = new Button(this);
            restartButton.setText("Попробовать еще раз");
            restartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recreate();
                }
            });
            linearLayout.addView(restartButton);

            //Создание кнопок по Бд
            ArrayList<StructureClass.Module> modules = DBHelper.insertModule(db);
            for (StructureClass.Module module : modules) {
                Button button = createModuleButton(module);
                linearLayout.addView(button);
            }
        }
        setContentView(linearLayout);
    }

//    private String JsonReader(String fileName){
//        String json = null;
//        try {
//            InputStream inputStream = getAssets().open(fileName);
//            int sizeOfFile = inputStream.available();
//            byte[] bufferData = new byte[sizeOfFile];
//            inputStream.read(bufferData);
//            inputStream.close();
//            json = new String(bufferData, "UTF-8");
//
//        } catch (IOException e) {
//            Log.e("Jopa", e.getLocalizedMessage());
//            return null;
//        }
//        return json;
//    }


    public Button createModuleButton(StructureClass.Module module) {
        Button button = new Button(this);
        button.setText(String.valueOf(module.getId() + "\ntitle: " + module.getAttributes().getTitle() + "\nDescription: " + module.getAttributes().getDescription()));
//        switch (module.status){
//            case "finish":
//                button.setBackgroundColor(Color.GREEN);
//                break;
//            case "in process":
//                button.setBackgroundColor(Color.YELLOW);
//                break;
//            case "close":
//                button.setBackgroundColor(Color.GRAY);
//                break;
//    }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LessonPage.class);
                intent.putExtra("lessonsNumber", module.getId());
                startActivity(intent);
            }
        });

        return button;
    }
}