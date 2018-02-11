package com.twelo.mylist;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText search_text;
    private ListView search_lst;
    private RelativeLayout layout;
    private setThemeMain setthemeMain;
    ArrayList<String> Title = new ArrayList<String>();
    ArrayList<ArrayList> all_item = new ArrayList<ArrayList>();

    DataBaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        layout = (RelativeLayout)findViewById(R.id.SearchLayoutMain);
        db = new DataBaseHandler(getApplicationContext());
        setthemeMain = new setThemeMain(SearchActivity.this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        layout.setBackgroundColor(setthemeMain.Background);
        search_text = (EditText) findViewById(R.id.search_edit_text);
        search_lst = (ListView) findViewById(R.id.search_list);


        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                all_item = new ArrayList<ArrayList>();
                Title = new ArrayList<String>();

                CustomAdapter customAdapter = new CustomAdapter();
                search_lst.setAdapter(customAdapter);

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    Cursor cur = db.DatabaseToTitle();
                    while (cur.moveToNext()) {
                        if (cur.getString(0).toString().toLowerCase().startsWith(s.toString().toLowerCase())) {
                            Title.add(cur.getString(0));
                        }
                    }

                    for (int i = 0; i < Title.size(); i++) {
                        Cursor cursor = db.TitleToItem(Title.get(i));

                        ArrayList<String> item = new ArrayList<>();

                        while (cursor.moveToNext()) {
                            item.add(cursor.getString(0));

                        }
                        all_item.add(item);

                    }
                    CustomAdapter customAdapter = new CustomAdapter();
                    search_lst.setAdapter(customAdapter);

                }
            }
        });

        search_lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(SearchActivity.this, ShowmainListActivity.class);
                ArrayList<String> lst = all_item.get(position);
                String[] temp = new String[lst.size()];
                for (int i = 0; i < lst.size(); i++) {
                    temp[i] = lst.get(i);
                }
                intent.putExtra("Title", Title.get(position).toString());
                intent.putExtra("List", temp);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public class CustomAdapter extends BaseAdapter {

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

            if (setthemeMain.isdark){
                convertView = getLayoutInflater().inflate(R.layout.each_row_1, null);
            }else{
                convertView = getLayoutInflater().inflate(R.layout.eachrow, null);
            }
            TextView first = (TextView) convertView.findViewById(R.id.one_item);
            TextView num = (TextView) convertView.findViewById(R.id.total_item);
            num.setText( all_item.get(position).size() + "");
            TextView text = (TextView) convertView.findViewById(R.id.first_one);
            text.setText(Title.get(position).toString().substring(0, 1).toUpperCase() + Title.get(position).toString().substring(1).toLowerCase());

            if (all_item.get(position).size() == 1) {
                first.setText(all_item.get(position).get(0).toString());
            }
            if (all_item.get(position).size() == 2) {
                first.setText(all_item.get(position).get(0).toString()+"\n"+all_item.get(position).get(1).toString());
            }
            if (all_item.get(position).size() == 3) {
                first.setText(all_item.get(position).get(0).toString()+"\n"+all_item.get(position).get(1).toString()+"\n"+all_item.get(position).get(2).toString());
            }

            if (all_item.get(position).size() > 3) {
                first.setText(all_item.get(position).get(0).toString()+"\n"+all_item.get(position).get(1).toString()+"\n"+all_item.get(position).get(2).toString()+"...");
            }

            return convertView;
        }


    }
}
