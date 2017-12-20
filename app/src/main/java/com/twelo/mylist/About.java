package com.twelo.mylist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

public class About extends AppCompatActivity {
    android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        TextView title = (TextView)findViewById(R.id.Main_Menu_Title);
        title.setText("About");
        setSupportActionBar(toolbar);
    }
}
