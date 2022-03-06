package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class NoteSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);
        initListButton();
        initNoteButton ();
        initSettingButton();
        initSettings ();
        initSortByClick();
        initSortOrderClick();


    }
    public void initListButton(){
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(NoteSettingsActivity.this, NoteListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);
            }
        });
    }

    public void initNoteButton (){
        ImageButton ibMap = findViewById(R.id.imageButtonNote);
        ibMap.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(NoteSettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initSettingButton(){
        ImageButton ibSetting = findViewById(R.id.imageButtonSettings);
        ibSetting.setEnabled(false);

    }

    private void initSettings (){
        String sortBy = getSharedPreferences("MyNoteListPreferences",
                Context.MODE_PRIVATE).getString("sortfield","notesubject");
        String sortOrder = getSharedPreferences("MyNoteListPreferences",
                Context.MODE_PRIVATE).getString("sortorder","ASC");

        RadioButton rdDate = findViewById(R.id.radioDate);
        RadioButton rdPriority = findViewById(R.id.radioPriority);
        RadioButton rbSubject = findViewById(R.id.radioSubject);
        if (sortBy.equalsIgnoreCase("date")){
            rdDate.setChecked(true);
        }
        else if (sortBy.equalsIgnoreCase("priority")){
            rdPriority.setChecked(true);
        }
        else {
            rbSubject.setChecked(true);
        }

        RadioButton rbAscending = findViewById(R.id.radioAscending);
        RadioButton rbDescending = findViewById(R.id.radioDescending);
        if (sortOrder.equalsIgnoreCase("ACS")){
            rbAscending.setChecked(true);
        }
        else {
            rbDescending.setChecked(true);
        }

    }

    private void initSortByClick() {
        RadioGroup rgSortBy = findViewById(R.id.radioGroupSortBy);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                RadioButton rbDate = findViewById(R.id.radioDate);
                RadioButton rbPriority = findViewById(R.id.radioPriority);
                if (rbDate.isChecked()) {
                    getSharedPreferences("MyNoteListPreferences",
                            Context.MODE_PRIVATE).edit().putString("sortfield", "date").apply();
                }
                else if (rbPriority.isChecked()) {
                    getSharedPreferences("MyNoteListPreferences",
                            Context.MODE_PRIVATE).edit().putString("sortfield", "priority").apply();
                }
                else {
                    getSharedPreferences("MyNoteListPreferences",
                            Context.MODE_PRIVATE).edit().putString("sortfield", "subject").apply();
                }
            }
        });
    }

    private void initSortOrderClick() {
        RadioGroup rgSortOrder = findViewById(R.id.radioGroupSortOrder);
        rgSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup arg0, int arg1) {
                RadioButton rbAscending = findViewById(R.id.radioAscending);
                if (rbAscending.isChecked()) {
                    getSharedPreferences("MyNoteListPreferences",
                            Context.MODE_PRIVATE).edit().putString("sortorder", "ASC").apply();
                }
                else {
                    getSharedPreferences("MyNoteListPreferences", Context.MODE_PRIVATE).edit().putString("sortorder", "DESC").apply();
                }
            }
        });
    }

}