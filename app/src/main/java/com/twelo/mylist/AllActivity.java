package com.twelo.mylist;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AllActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ListView listView;
    private ArrayList Title = new ArrayList();
    private ArrayList Checked = new ArrayList();
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all);

        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle!=null){
            Title = bundle.getStringArrayList("Title");
        }


        listView = (ListView)findViewById(R.id.merge_list);
        CustomAdapter customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox check = (CheckBox)view.findViewById(R.id.hist_checkbox);
                try {
                    if (Checked.contains(position)) {
                        check.setChecked(false);
                        Checked.remove(position);
                    } else {
                        check.setChecked(true);
                        Checked.add(position);
                    }
                } catch (Exception e) {
                    finish();
                    Toast.makeText(AllActivity.this, "Something went wrong....\n Please Re-Open 'Merge List' ", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return Title.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.history_eachrow,null);
            checkBox = (CheckBox)convertView.findViewById(R.id.hist_checkbox);
            checkBox.setText(Title.get(position).toString());
            if(Checked.contains(position)){
                checkBox.setChecked(true);
            }else{
                checkBox.setChecked(false);
            }
            return convertView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addlist_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.list_completed){


            final ArrayList<String> All_item = new ArrayList<>();

            final DataBaseHandler databse = new DataBaseHandler(getApplicationContext());

            for (int i=0;i<Checked.size();i++){
                Cursor cursor = databse.TitleToItem((String) Title.get((Integer) Checked.get(i)));
                while (cursor.moveToNext()){
                    All_item.add(cursor.getString(0));
                }
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(AllActivity.this);
            View viw  = getLayoutInflater().inflate(R.layout.merge_dialog,null);
            builder.setView(viw);

            final AlertDialog dialog = builder.create();
            dialog.show();

            final EditText name=  (EditText)viw.findViewById(R.id.merge_title);
            Button next = (Button)viw.findViewById(R.id.merge_next);

            next.setOnClickListener(new Button.OnClickListener(){

                @Override
                public void onClick(View v) {
                    List_maker list_maker = new List_maker();
                    list_maker.setList_title(name.getText().toString());
                    list_maker.setList(All_item);

                    databse.add_list(list_maker);
                    dialog.dismiss();
                    startActivity(new Intent(AllActivity.this,MainActivity.class));
                    finish();
                }
            });

        }
        return super.onOptionsItemSelected(item);
    }
}
