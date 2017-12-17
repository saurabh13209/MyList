package com.twelo.mylist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView list;
    private DataBaseHandler data;
    private FloatingActionButton fab;

    private ArrayList<String> Title = new ArrayList<>();
    private ArrayList<ArrayList> all_item = new ArrayList<>();
    private RelativeLayout MainView;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MainView = (RelativeLayout) findViewById(R.id.Main_List_Main_View);
        sharedPreferences = MainActivity.this.getSharedPreferences("Database", MODE_PRIVATE);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        list = (ListView) findViewById(R.id.list);
        data = new DataBaseHandler(this);

        CustomAdapter custom = new CustomAdapter();
        list.setAdapter(custom);

        fab = (FloatingActionButton) findViewById(R.id.main_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NewmainlistActivity.class));
            }
        });

        database();
        click();
    }


    private void click() {
        list.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, ShowmainListActivity.class);
                ArrayList<String> lst = all_item.get(position);
                String[] temp = new String[lst.size()];
                for (int i = 0; i < lst.size(); i++) {
                    temp[i] = lst.get(i);
                }
                intent.putExtra("Title", Title.get(position).toString());
                intent.putExtra("List", temp);
                startActivity(intent);


            }
        });

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                View box = getLayoutInflater().inflate(R.layout.mainlist_popup, null);
                alert.setView(box);

                final AlertDialog builder_1 = alert.create();
                builder_1.show();

                Button share = (Button) box.findViewById(R.id.share);
                Button del = (Button) box.findViewById(R.id.delete);

                share.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Share_it share = new Share_it(Title.get(position).toString(), all_item.get(position));
                        Intent intent = share.getIntent();
                        startActivity(Intent.createChooser(intent, "Send " + Title.get(position).toString() + " List"));
                        builder_1.dismiss();
                    }
                });

                del.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        View box = getLayoutInflater().inflate(R.layout.del_layout, null);
                        builder.setView(box);
                        final AlertDialog dialog = builder.create();
                        dialog.show();

                        Button yes = (Button) box.findViewById(R.id.del_yes_btn);
                        Button no = (Button) box.findViewById(R.id.del_no_btn);

                        yes.setOnClickListener(new Button.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                data.Delete_List(Title.get(position).toString());
                                Title = new ArrayList<String>();
                                all_item = new ArrayList<ArrayList>();
                                CustomAdapter custom = new CustomAdapter();
                                list.setAdapter(custom);
                                database();
                                custom = new CustomAdapter();
                                list.setAdapter(custom);
                                dialog.dismiss();
                                builder_1.dismiss();

                            }
                        });

                        no.setOnClickListener(new Button.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                builder_1.dismiss();
                            }
                        });


                    }
                });
                return true;
            }
        });

    }


    private void database() {

        Cursor cur = data.DatabaseToTitle();
        Title = new ArrayList<>();
        while (cur.moveToNext()) {
            Title.add(cur.getString(0));
        }

        cur = data.getHiddenList();
        ArrayList array_hidden = new ArrayList();
        while (cur.moveToNext()) {
            array_hidden.add(cur.getString(0).toString());
        }
        if (sharedPreferences.getString("0", "").equals("false") || sharedPreferences.getString("0","").equals("")) {
            for (int i = 0; i < array_hidden.size(); i++) {
                if (Title.contains(array_hidden.get(i))) {
                    Title.remove(array_hidden.get(i));
                }
            }
        }


        for (int i = 0; i < Title.size(); i++) {
            Cursor cursor = data.TitleToItem(Title.get(i));
            ArrayList<String> item = new ArrayList<>();
            while (cursor.moveToNext()) {
                item.add(cursor.getString(0));
            }
            all_item.add(item);
        }

        if (all_item.size() == 0) {
            MainView.setBackgroundResource(R.drawable.min);
        }

    }

    public class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return all_item.size();
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


            if (all_item.get(position).size() == 1) {
                convertView = getLayoutInflater().inflate(R.layout.each_row_1, null);

                TextView first = (TextView) convertView.findViewById(R.id.one_item);
                TextView num = (TextView) convertView.findViewById(R.id.total_item);
                num.setText("[" + all_item.get(position).size() + "]");
                first.setText(all_item.get(position).get(0).toString());

                TextView text = (TextView) convertView.findViewById(R.id.first_one);
                text.setText(Title.get(position).toString().substring(0, 1).toUpperCase() + Title.get(position).toString().substring(1).toLowerCase());

            }
            if (all_item.get(position).size() == 2) {
                convertView = getLayoutInflater().inflate(R.layout.each_row_2, null);
                TextView first = (TextView) convertView.findViewById(R.id.one_item);
                TextView sec = (TextView) convertView.findViewById(R.id.two_item);
                TextView num = (TextView) convertView.findViewById(R.id.total_item);
                num.setText("[" + all_item.get(position).size() + "]");
                first.setText(all_item.get(position).get(0).toString());
                sec.setText(all_item.get(position).get(1).toString());

                TextView text = (TextView) convertView.findViewById(R.id.first_one);
                text.setText(Title.get(position).toString().substring(0, 1).toUpperCase() + Title.get(position).toString().substring(1).toLowerCase());
            }
            if (all_item.get(position).size() == 3) {
                convertView = getLayoutInflater().inflate(R.layout.eachrow, null);

                TextView first = (TextView) convertView.findViewById(R.id.one_item);
                TextView sec = (TextView) convertView.findViewById(R.id.two_item);
                TextView thr = (TextView) convertView.findViewById(R.id.three_item);
                TextView num = (TextView) convertView.findViewById(R.id.total_item);
                num.setText("[" + all_item.get(position).size() + "]");

                first.setText(all_item.get(position).get(0).toString());
                sec.setText(all_item.get(position).get(1).toString());
                thr.setText(all_item.get(position).get(2).toString());

                TextView text = (TextView) convertView.findViewById(R.id.first_one);
                text.setText(Title.get(position).toString().substring(0, 1).toUpperCase() + Title.get(position).toString().substring(1).toLowerCase());


            }

            if (all_item.get(position).size() > 3) {
                convertView = getLayoutInflater().inflate(R.layout.eachrow, null);

                TextView first = (TextView) convertView.findViewById(R.id.one_item);
                TextView sec = (TextView) convertView.findViewById(R.id.two_item);
                TextView thr = (TextView) convertView.findViewById(R.id.three_item);
                TextView num = (TextView) convertView.findViewById(R.id.total_item);
                num.setText("[" + all_item.get(position).size() + "]");

                first.setText(all_item.get(position).get(0).toString());
                sec.setText(all_item.get(position).get(1).toString());
                thr.setText(all_item.get(position).get(2).toString() + "....");

                TextView text = (TextView) convertView.findViewById(R.id.first_one);
                text.setText(Title.get(position).toString().substring(0, 1).toUpperCase() + Title.get(position).toString().substring(1).toLowerCase());


            }


            return convertView;
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.search_btn) {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
        }

        if (id == R.id.Setting) {
            startActivity(new Intent(MainActivity.this, Setting.class));
        }

        if (id == R.id.merge) {
            Intent intent = new Intent(MainActivity.this, AllActivity.class);
            intent.putExtra("Title", Title);
            startActivity(intent);
        }

        if (id == R.id.Delete_List) {
            Intent intent = new Intent(MainActivity.this, Delete_List.class);
            intent.putExtra("Title", Title);
            startActivity(intent);
        }

        if (id == R.id.about) {
            Intent intent = new Intent(MainActivity.this, About.class);
            intent.putExtra("Title", Title);
            startActivity(intent);
        }


        return super.onOptionsItemSelected(item);

    }


}
