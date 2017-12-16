package com.twelo.mylist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class NewmainlistActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private LinearLayout lv;
    ArrayList<String> list_item = new ArrayList<>();
    private EditText main_title;
    DataBaseHandler db;

    String task = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newmainlist);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        lv = (LinearLayout) findViewById(R.id.main_liner_lay);
        main_title = (EditText) findViewById(R.id.add_list_title);
        db = new DataBaseHandler(getApplicationContext());

        if (bundle != null) {
            String got_title = bundle.getString("Title");
            String[] got_lst = bundle.getStringArray("List");
            task = bundle.getString("Task");
            main_title.setText(got_title);
            for (String item : got_lst) {
                new_item_edit(item);
            }

        }

        Button add_item_btn = (Button) findViewById(R.id.new_itm_btn);

        add_item_btn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                new_item_edit(null);
            }
        });

    }


    public void new_item_edit(String text) {


        EditText ed = new EditText(getApplicationContext());
        ed.setText(text);
        ed.setHint("Enter List Item");
        ed.setHintTextColor(Color.GRAY);
        ed.setTextColor(Color.BLACK);

        lv.addView(ed);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addlist_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.list_completed) {


            if (((!main_title.getText().toString().equals("")) && (!db.TitlePresent(main_title.getText().toString())) && (task.equals(""))) || ((!main_title.getText().toString().equals("")) && (task.equals("Add")))) {


                for (int i = 0; i < lv.getChildCount(); i++) {
                    EditText ed = (EditText) lv.getChildAt(i);
                    list_item.add(String.valueOf(ed.getText()).trim());
                }

                if (list_item.size() == 0) {
                    Toast.makeText(this, "You don't have any item for this list", Toast.LENGTH_SHORT).show();
                } else {

                    add_data();
                    Toast.makeText(this, "List Created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }

            } else {
                Toast.makeText(this, "List with same name already present.", Toast.LENGTH_SHORT).show();
            }


        }
        return super.onOptionsItemSelected(item);
    }

    private void add_data() {
        List_maker list_maker = new List_maker();
        ArrayList<String> Date_Time = new ArrayList<>();
        list_maker.setList_title(main_title.getText().toString());
        ArrayList<String> new_list = new ArrayList<>();
        for (int i=0 ; i<list_item.size() ; i++){
            if (!list_item.get(i).equals("")){
                new_list.add(list_item.get(i));
            }
        }
        list_maker.setList(new_list);
        for (int i =0 ;i<new_list.size() ; i++){
            Date_Time.add(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime()));
        }
        if (task.equals("Add")) {
            try {
                db.Delete_List(main_title.getText().toString());
            } catch (Exception e) {
            }
        }
        list_maker.setDate(Date_Time);
        db.add_list(list_maker);
    }
}
