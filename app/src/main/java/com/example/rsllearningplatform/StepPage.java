package com.example.rsllearningplatform;

import static JavaClass.Test.getContent;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONException;

import java.io.Console;
import java.io.IOException;
import java.util.ArrayList;

import JavaClass.JsonHelper;
import JavaClass.NetworkUtils;
import JavaClass.StructureClass;

public class StepPage extends AppCompatActivity {

    int actualPage = 0;
    ArrayList<StructureClass.Step> stepList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_page);
        TextView textView = findViewById(R.id.textStep);
        Bundle arguments = getIntent().getExtras();
        String lessonsNumber = arguments.getString("lessonsNumber");

        if(NetworkUtils.isNetworkAvailable(this)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String str = getContent("https://certain-delight-275b321500.strapiapp.com/api/lectures/1?populate=*");
                        Log.i("GetStepFromLecture" + lessonsNumber, str);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<Integer> steps = new ArrayList<>();
                                try {
                                    steps = JsonHelper.readStepFromLectures(str);
                                    generateStepList(steps);
                                } catch (JSONException e) {
                                    Log.e("JSONTroubleStep", e.getLocalizedMessage());
                                }

                            }
                        });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        } else {
            LinearLayout linearLayout = new LinearLayout(this);
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
            setContentView(linearLayout);
        }

    }

    public void generateStepList(ArrayList<Integer> steps){

        if(NetworkUtils.isNetworkAvailable(this)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        String str = getContent("https://certain-delight-275b321500.strapiapp.com/api/steps/1?populate=*");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    StructureClass.Step step;
                                    step = JsonHelper.readStep(str);
                                    Log.i("Proverka", step.getAttributes().getTitle());
                                    stepList.add(step);

                                    ArrayList<StructureClass.Content> contents = new ArrayList<>();
                                    StructureClass.Content content = new StructureClass.Content(2, null, "Тут пока ничего нет)");
                                    contents.add(content);
                                    StructureClass.AttributesStep attributesStep = new StructureClass.AttributesStep("Test", null, null, null, null , contents, null);
                                    StructureClass.Step step2 = new StructureClass.Step(2, attributesStep);
                                    stepList.add(step2);

                                    setPage();

                                } catch (JSONException e) {
                                    Log.e("JSONTroubleGenerateStep", e.getLocalizedMessage());
                                }
                            }
                        });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        } else {
            LinearLayout linearLayout = new LinearLayout(this);
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
            setContentView(linearLayout);
        }
    }

    public void setPage(){
        TextView textView = findViewById(R.id.textStep);
        String str = stepList.get(actualPage).getAttributes().getTitle() + "\n";
        for(StructureClass.Content content: stepList.get(actualPage).getAttributes().getContent()){
            str += content.getText() + " \n";
        }
        textView.setText(str);
    }

    public void nextPage(View v){
        Log.i("nextPageButton", "Активирована кнопка вперед");
        if(actualPage != stepList.size()-1){
            actualPage++;
            setPage();
        } else {
          Toast.makeText(this, "Это последняя страница", Toast.LENGTH_SHORT).show();
        }
    }
    public void backPage(View v){
        Log.i("backPageButton", "Активирована кнопка назад");
        if(actualPage != 0){
            actualPage--;
            setPage();
        } else {
            Toast.makeText(this, "Это первая страница", Toast.LENGTH_SHORT).show();
        }
    }

}