package com.twelo.mylist;


import java.util.ArrayList;

public class List_maker {

    String list_title;
    ArrayList<String> list;
    ArrayList<String> Date;

    public void setDate(ArrayList<String> date){
        this.Date = date;
    }

    public String getList_title() {

        return list_title;
    }

    public ArrayList<String> getDate(){
        return this.Date;
    }

    public void setList_title(String list_title) {
        this.list_title = list_title.toLowerCase();
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }


}
