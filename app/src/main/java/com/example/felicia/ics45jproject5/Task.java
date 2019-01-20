package com.example.felicia.ics45jproject5;

public class Task {
   public int ID;
    public String Description;



    public Task() {

    }

    public Task(int a, String b){
        this.ID = a;
        this.Description = b;
    }

    public String getTask() {
        return Description;
    }

    public int getID() {

        return ID;
    }

    public void setTask(String description) {

        this.Description = description;
    }

    public void setID(int id) {
        this.ID = id;
    }
}
