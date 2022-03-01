package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         initListButton();
         initNoteButton ();
         initSettingButton();
        initPriority ();



}
    public void initListButton(){
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, List.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);
            }
        });
    }

    public void initSettingButton (){
        ImageButton ibMap = findViewById(R.id.imageButtonSettings);
        ibMap.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initNoteButton(){
        ImageButton ibSetting = findViewById(R.id.imageButtonNote);
        ibSetting.setEnabled(false);

    }

    private void initPriority(){
        String notePriority = getSharedPreferences("MyPriorityPreferences",
                Context.MODE_PRIVATE).getString("notePriority","noPriority");


        RadioButton rdLow = findViewById(R.id.radioLow);
        RadioButton rdMed = findViewById(R.id.radioMed);
        RadioButton rbHigh = findViewById(R.id.radioHigh);
        if (notePriority.equalsIgnoreCase("Low")){
            rdLow.setChecked(true);
        }
        else if (notePriority.equalsIgnoreCase("Med")){
            rdMed.setChecked(true);
        }
        else {
            rbHigh.setChecked(true);
        }


    }
}
