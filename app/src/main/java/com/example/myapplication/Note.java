package com.example.myapplication;

public class Note {
    private int noteID;
    private String subject;
    private String note;
    private String priority;

    public Note (){
        noteID = -1;
    }

    public int getNoteID(){return noteID;}
    public void setNoteID(int i){noteID=i;}

    public String getSubject(){return subject;}
    public void setSubject(String s){subject = s;}

    public String getNote(){return note;}
    public void setNote(String s){note = s;}

    public String getPriority(){return priority;}
    public void setPriority(String s){priority = s;}
}


