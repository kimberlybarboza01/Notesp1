package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NoteListActivity extends AppCompatActivity {
    RecyclerView noteList;
    NoteAdapter noteAdapter;

    ArrayList<Note> notes;
    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
            int position = viewHolder.getAdapterPosition();
            int noteID = notes.get(position).getNoteID();
            Intent intent = new Intent(NoteListActivity.this, MainActivity.class);
            intent.putExtra("contactId", noteID);
            startActivity(intent);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initNoteButton();
        initListButton();
        initSettingButton();
        initAddNoteButton();
        initDeleteSwitch();

        BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                double batteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
                double levelScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE,0);
                int batteryPercent = (int) Math.floor(batteryLevel / levelScale * 100);
                TextView textBatteryState = findViewById(R.id.textBatteryLevel);
                textBatteryState.setText(batteryPercent + "%");
            }
        };

        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);

    }

    @Override
    public void onResume() {
        super.onResume();

        String sortBy = getSharedPreferences("MyNoteListPreferences", Context.MODE_PRIVATE).getString("sortfield", "subject");
        String sortOrder = getSharedPreferences("MyNoteListPreferences", Context.MODE_PRIVATE).getString("sortorder", "ASC");

        NoteDataSource ds = new NoteDataSource(this);
        try {
            ds.open();
            notes = ds.getNotes(sortBy, sortOrder);
            ds.close();
            if (notes.size() > 0) {
                noteList = findViewById(R.id.rvNotes);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
                noteList.setLayoutManager(layoutManager);
                noteAdapter = new NoteAdapter(notes, this);
                noteAdapter.setOnItemClickListener(onItemClickListener);
                noteList.setAdapter(noteAdapter);
            }
            else {
                Intent intent = new Intent(NoteListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
        catch (Exception e) {
            Toast.makeText(this, "Error retrieving notes", Toast.LENGTH_LONG).show();
        }

    }

    public void initNoteButton(){
        ImageButton ibList = findViewById(R.id.imageButtonNote);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(NoteListActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);
            }
        });
    }

    public void initSettingButton (){
        ImageButton ibMap = findViewById(R.id.imageButtonSettings);
        ibMap.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(NoteListActivity.this, NoteSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initListButton() {
        ImageButton ibSetting = findViewById(R.id.imageButtonList);
        ibSetting.setEnabled(false);

    }


    private void initAddNoteButton() {
        Button newNote = findViewById(R.id.buttonAddNote);
        newNote.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(NoteListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initDeleteSwitch() {
        Switch s = findViewById(R.id.switchDelete);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                boolean status = compoundButton.isChecked();
                noteAdapter.setDelete(status);
                noteAdapter.notifyDataSetChanged();
            }
        });
    }
}