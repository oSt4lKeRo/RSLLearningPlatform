package com.example.rsllearningplatform;

import static JavaClass.Test.getContent;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import JavaClass.JsonHelper;
import JavaClass.NetworkUtils;
import JavaClass.StructureClass;

public class LessonPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView textView = new TextView(this);
        Bundle arguments = getIntent().getExtras();
        String lessonsNumber = arguments.getString("lessonsNumber");
        if (lessonsNumber == null) {
            Log.i("lessonsNumber", "ПЗдц");
        } else {
            Log.i("lessonsNumber", lessonsNumber);
        }
        if(NetworkUtils.isNetworkAvailable(this)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String str = getContent("https://certain-delight-275b321500.strapiapp.com/api/modules/1?populate=*");
                        Log.i("GetLectures", str);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<StructureClass.Lesson> lessons = new ArrayList<>();

                                try {
                                    lessons = JsonHelper.readLectures(str);
                                } catch (JSONException e) {
                                    Log.e("JSONTroubleLecture", e.getLocalizedMessage());
                                }
                                for (StructureClass.Lesson lesson : lessons) {
                                    Button button = createLessonButton(lesson);
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
            Button restartButton = new Button(this);
            restartButton.setText("Попробовать еще раз");
            restartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recreate();
                }
            });
            linearLayout.addView(restartButton);
        }
        setContentView(linearLayout);


    }

    public Button createLessonButton(StructureClass.Lesson lessons) {
        Button button = new Button(this);
        button.setText(String.valueOf(lessons.getId() + "\ntitle: " + lessons.getAttributes().getTitle() + "\nDescription: " + lessons.getAttributes().getDescription()));
//        switch (lessons.status){
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
                Intent intent = new Intent(LessonPage.this, StepPage.class);
                intent.putExtra("lessonsNumber", lessons.getId());
                startActivity(intent);
            }
        });

        return button;
    }

}