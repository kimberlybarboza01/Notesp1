package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class NoteDataSource {
    private SQLiteDatabase database;
    private final NoteDBHelper dbHelper;

    public NoteDataSource(Context context) {
        dbHelper = new NoteDBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertNote(Note n) {
        boolean didSucceed = false;
        try {
            ContentValues initialValues = new ContentValues();

            initialValues.put("subject", n.getSubject());
            initialValues.put("note", n.getNote());
            initialValues.put("priority", n.getPriority());

            didSucceed = database.insert("note", null, initialValues) > 0;
        } catch (Exception e) {
            //do nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    public boolean updateNote(Note n) {
        boolean didSucceed = false;
        try {
            Long rowId = (long) n.getNoteID();
            ContentValues updateValues = new ContentValues();

            updateValues.put("subject", n.getSubject());
            updateValues.put("note", n.getNote());
            updateValues.put("priority", n.getPriority());

            didSucceed = database.update("note", updateValues, "_id=" + rowId, null) > 0;
        } catch (Exception e) {
            //do nothing -will return false if there is an exception
        }
        return didSucceed;
    }

    public int getLastNoteId() {
        int lastId;
        try {
            String query = "Select MAX(_id) from note";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            lastId = cursor.getInt(0);
            cursor.close();
        } catch (Exception e) {
            lastId = -1;
        }
        return lastId;
    }

    public ArrayList<String> getNoteSubject() {
        ArrayList<String> NoteSubjects = new ArrayList<>();
        try {
            String query = "Select notesubject from note";
            Cursor cursor = database.rawQuery(query, null);

            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                NoteSubjects.add(cursor.getString(0));
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            NoteSubjects = new ArrayList<String>();
        }
        return NoteSubjects;
    }

    public ArrayList<Note> getNotes(String sortField, String sortOrder) {
        ArrayList<Note> Notes = new ArrayList<Note>();
        try {
            String query = "SELECT  * FROM note ORDER BY " + sortField + " " + sortOrder;
            Cursor cursor = database.rawQuery(query, null);

            Note newNote;
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                newNote = new Note();
                newNote.setNoteID(cursor.getInt(0));
                newNote.setSubject(cursor.getString(1));
                newNote.setNote(cursor.getString(2));
                newNote.setPriority(cursor.getString(3));

                Notes.add(newNote);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            Notes = new ArrayList<Note>();
        }
        return Notes;
    }

    public Note getSpecificNote(int noteId) {
        Note note = new Note();
        String query = "SELECT  * FROM note WHERE _id =" + noteId;
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            note.setNoteID(cursor.getInt(0));
            note.setSubject(cursor.getString(1));
            note.setNote(cursor.getString(2));
            note.setPriority(cursor.getString(3));
        }

        cursor.close();
        return note;
}


    public boolean deleteNote(int noteId) {
        boolean didDelete = false;
        try {
            didDelete = database.delete("note", "_id=" + noteId, null) > 0;
        }
        catch (Exception e) {
            //Do nothing -return value already set to false
        }
        return didDelete;
    }

}







