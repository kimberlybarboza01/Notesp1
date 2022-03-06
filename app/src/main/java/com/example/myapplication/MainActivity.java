package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;


public class MainActivity extends AppCompatActivity {

    private Note currentNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initListButton();
        initNoteButton();
        initSettingsButton();
        initToggleButton();
        setForEditing(false);
        initTextChangedEvents();
        initSaveButton();
        initPriority();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            initNote(extras.getInt("noteId"));
        }
        else {
            currentNote = new Note();
        }
    }

    private void initListButton() {
        ImageButton ibList = findViewById(R.id.imageButtonList);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoteListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initNoteButton() {
        ImageButton ibList = findViewById(R.id.imageButtonNote);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                if (currentNote.getNoteID() == -1) {
                    Toast.makeText(getBaseContext(), "Note must be saved before it can be viewd", Toast.LENGTH_LONG).show();
                }
                else {
                    intent.putExtra("noteid", currentNote.getNoteID());
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void initSettingsButton() {
        ImageButton ibList = findViewById(R.id.imageButtonSettings);
        ibList.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoteSettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
    private void initPriority() {
        String notePriority = getSharedPreferences("MyPriorityPreferences",
                Context.MODE_PRIVATE).getString("notePriority", "noPriority");


        RadioButton rdLow = findViewById(R.id.radioLow);
        RadioButton rdMed = findViewById(R.id.radioMed);
        RadioButton rbHigh = findViewById(R.id.radioHigh);
        if (notePriority.equalsIgnoreCase("Low")) {
            rdLow.setChecked(true);
        } else if (notePriority.equalsIgnoreCase("Med")) {
            rdMed.setChecked(true);
        } else {
            rbHigh.setChecked(true);
        }
    }

    private void initToggleButton() {
        final ToggleButton editToggle = findViewById(R.id.toggleButtonEdit);
        editToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setForEditing(editToggle.isChecked());
            }
        });
    }


    private void setForEditing(boolean enabled) {
        EditText editSubject = findViewById(R.id.editSubject);
        EditText editNote = findViewById(R.id.editNote);
        Button buttonSave = findViewById(R.id.buttonSave);
        RadioButton rbLow = findViewById(R.id.radioLow);
        RadioButton rbMed = findViewById(R.id.radioMed);
        RadioButton rbHigh = findViewById(R.id.radioHigh);


        editSubject.setEnabled(enabled);
        editNote.setEnabled(enabled);
        buttonSave.setEnabled(enabled);
        rbLow.setEnabled(enabled);
        rbMed.setEnabled(enabled);
        rbHigh.setEnabled(enabled);

        if (enabled) {
            editSubject.requestFocus();
        }
    }



    private void initTextChangedEvents(){
        final EditText etNoteSubject = findViewById(R.id.editSubject);
        etNoteSubject.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                currentNote.setSubject(etNoteSubject.getText().toString());
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //  Auto-generated method stub
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  Auto-generated method stub
            }
        });


        final EditText etNote = findViewById(R.id.editNote);
        etNote.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                currentNote.setNote(etNote.getText().toString());
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                //  Auto-generated method stub
            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  Auto-generated method stub
            }
        });

        final RadioGroup radioPriority = findViewById(R.id.radioPriority);
        radioPriority.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                }
        });

    }

    private void initSaveButton() {
        Button saveButton = findViewById(R.id.buttonSave);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                boolean wasSuccessful;
                hideKeyboard();
                NoteDataSource ds = new NoteDataSource(MainActivity.this);
                try {
                    ds.open();

                    if (currentNote.getNoteID() == -1) {
                        wasSuccessful = ds.insertNote(currentNote);
                        if (wasSuccessful) {
                            int newId = ds.getLastNoteId();
                            currentNote.setNoteID(newId);
                        }

                    }
                    else {
                        wasSuccessful = ds.updateNote(currentNote);
                    }
                    ds.close();
                }
                catch (Exception e) {
                    wasSuccessful = false;
                }

                if (wasSuccessful) {
                    ToggleButton editToggle = findViewById(R.id.toggleButtonEdit);
                    editToggle.toggle();
                    setForEditing(false);
                }
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        EditText editSubject = findViewById(R.id.editSubject);
        imm.hideSoftInputFromWindow(editSubject.getWindowToken(), 0);
        EditText editNote = findViewById(R.id.editNote);
        imm.hideSoftInputFromWindow(editNote.getWindowToken(), 0);

    }

    private void initNote(int id) {

        NoteDataSource ds = new NoteDataSource(MainActivity.this);
        try {
            ds.open();
            currentNote = ds.getSpecificNote(id);
            ds.close();
        }
        catch (Exception e) {
            Toast.makeText(this, "Load Note Failed", Toast.LENGTH_LONG).show();
        }

        EditText editSubject = findViewById(R.id.editSubject);
        EditText editNote = findViewById(R.id.editNote);


        editSubject.setText(currentNote.getSubject());
        editNote.setText(currentNote.getNote());

    }


}
