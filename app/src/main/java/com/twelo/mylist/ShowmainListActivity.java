package com.twelo.mylist;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ShowmainListActivity extends AppCompatActivity {
    private TextView title;
    private ListView list;
    private String Title;
    private String[] lst;
    private Toolbar toolbar;
    private DataBaseHandler data;
    private String DELETED_ITEM = new String();
    private ArrayList<String> Date_Time = new ArrayList<>();
    private Menu menu;


    private FloatingActionButton floatingActionButton;

    @Override
    protected void onResume() {
        CustomAdapter customAdapter = new CustomAdapter();
        list.setAdapter(customAdapter);

        Cursor cursor = data.TitleToItem(Title);
        lst = new String[cursor.getCount()];
        int i = 0;
        while (cursor.moveToNext()) {
            lst[i] = cursor.getString(0);
            i++;
        }


        Date_Time = new ArrayList<>();
        cursor = data.TitleToDate(Title);
        while (cursor.moveToNext()) {
            Date_Time.add(cursor.getString(0));
        }
        customAdapter = new CustomAdapter();
        list.setAdapter(customAdapter);

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.showlist_menu, menu);
        Cursor cursor = data.getHiddenList();
        while (cursor.moveToNext()) {
            if (cursor.getString(0).equals(Title)) {
                MenuItem menuItem = menu.findItem(R.id.Hide_List);
                menuItem.setTitle("Un-Hide Item");
            }
        }

        cursor = data.getProtectList();
        while (cursor.moveToNext()) {
            if (cursor.getString(0).equals(Title)) {
                MenuItem menuItem = menu.findItem(R.id.protect_me);
                menuItem.setTitle("Un-Protect Item");
            }
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showmain_list);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = new DataBaseHandler(getApplicationContext());

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Title = bundle.getString("Title");
            Cursor cursor = data.TitleToItem(Title.toLowerCase());
            int i=0 , l=0;
            while (cursor.moveToNext()){
                l++;
            }
            lst = new String[l];
            cursor = data.TitleToItem(Title.toLowerCase());
            while (cursor.moveToNext()){
                lst[i] = cursor.getString(0);
                i++;
            }

            title = (TextView) findViewById(R.id.show_list_title);
            list = (ListView) findViewById(R.id.show_list);


            title.setText(Title.substring(0, 1).toUpperCase() + Title.substring(1).toLowerCase());
            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ShowmainListActivity.this);
                    final View view = getLayoutInflater().inflate(R.layout.merge_dialog, null);
                    final TextView in_title = (TextView) view.findViewById(R.id.SetTitleId);
                    in_title.setText("Rename List");
                    in_title.setTextSize(25);
                    builder.setView(view);
                    final AlertDialog di = builder.create();
                    di.show();
                    final Button next = (Button) view.findViewById(R.id.merge_next);
                    final EditText text_view = (EditText) view.findViewById(R.id.merge_title);
                    text_view.setText(title.getText().toString());
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (text_view.getText().toString().equals("")) {
                                Toast.makeText(ShowmainListActivity.this, "Please Enter a Name", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            DataBaseHandler data = new DataBaseHandler(ShowmainListActivity.this);
                            data.Delete_List(Title);
                            List_maker list_maker = new List_maker();
                            ArrayList list_item = new ArrayList();
                            for (int i = 0; i < lst.length; i++) {
                                list_item.add(lst[i].toString());
                            }
                            list_maker.setList(list_item);
                            list_maker.setList_title(text_view.getText().toString());
                            list_maker.setDate(Date_Time);
                            data.add_list(list_maker);
                            title.setText(text_view.getText().toString());
                            di.dismiss();
                        }
                    });
                }
            });

            Date_Time = new ArrayList<>();
            cursor = data.TitleToDate(Title);
            while (cursor.moveToNext()) {
                Date_Time.add(cursor.getString(0));
            }


            CustomAdapter custom = new CustomAdapter();
            list.setAdapter(custom);


        }


        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowmainListActivity.this);
                View vew = getLayoutInflater().inflate(R.layout.merge_dialog, null);
                TextView textView = (TextView) vew.findViewById(R.id.SetTitleId);
                textView.setText("Add Item");
                builder.setView(vew);
                final AlertDialog dialog = builder.create();
                dialog.show();

                Button save = (Button) vew.findViewById(R.id.merge_next);
                final EditText editText = (EditText) vew.findViewById(R.id.merge_title);

                save.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        ArrayList<String> temp = new ArrayList<>();
                        ArrayList<String> temp2 = new ArrayList<>();
                        temp.add(editText.getText().toString().trim());
                        temp2.add(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime()));
                        List_maker list_maker = new List_maker();
                        list_maker.setList(temp);
                        list_maker.setList_title(Title);
                        list_maker.setDate(temp2);
                        data.add_list(list_maker);
                        lst = new String[0];
                        CustomAdapter customAdapter = new CustomAdapter();
                        list.setAdapter(customAdapter);
                        Cursor cursor = data.TitleToItem(Title);
                        lst = new String[cursor.getCount()];
                        int i = 0;
                        while (cursor.moveToNext()) {
                            lst[i] = cursor.getString(0);
                            i++;
                        }

                        Date_Time = new ArrayList<>();
                        cursor = data.TitleToDate(Title);
                        while (cursor.moveToNext()) {
                            Date_Time.add(cursor.getString(0));
                        }
                        customAdapter = new CustomAdapter();
                        list.setAdapter(customAdapter);
                        dialog.dismiss();
                    }
                });


            }
        });


    }

    public class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return lst.length;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.item_eachrow, null);

            final TextView item_text = (TextView) convertView.findViewById(R.id.item_text_view);
            ImageButton cross = (ImageButton) convertView.findViewById(R.id.item_delete_image);
            final TextView date_time = (TextView) convertView.findViewById(R.id.date_item);
            date_time.setText(Date_Time.get(position).toString());
            item_text.setText(lst[position].toString().substring(0, 1).toUpperCase() + lst[position].toString().substring(1).toLowerCase());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(ShowmainListActivity.this);
                    final View view = getLayoutInflater().inflate(R.layout.merge_dialog, null);
                    TextView title = (TextView) view.findViewById(R.id.SetTitleId);
                    title.setText("Rename Item");
                    title.setTextSize(25);
                    builder.setView(view);
                    final AlertDialog di = builder.create();
                    di.show();
                    final Button next = (Button) view.findViewById(R.id.merge_next);
                    final EditText text_view = (EditText) view.findViewById(R.id.merge_title);
                    text_view.setText(lst[position]);
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (text_view.getText().toString().equals("")) {
                                Toast.makeText(ShowmainListActivity.this, "Please Enter a Name", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            DataBaseHandler data = new DataBaseHandler(ShowmainListActivity.this);
                            data.Delete_List(Title);
                            List_maker list_maker = new List_maker();
                            ArrayList list_item = new ArrayList();
                            for (int i = 0; i < lst.length; i++) {
                                if (!lst[i].equals(lst[position])) {
                                    list_item.add(lst[i].toString());
                                }
                            }
                            list_item.add(text_view.getText().toString());
                            list_maker.setList(list_item);
                            list_maker.setList_title(Title);
                            list_maker.setDate(Date_Time);
                            data.add_list(list_maker);
                            di.dismiss();
                            String[] new_list = new String[lst.length];
                            int i = 0;
                            Cursor c = data.TitleToItem(Title);
                            while (c.moveToNext()) {
                                new_list[i] = c.getString(0);
                                i++;
                            }
                            lst = new String[new_list.length];
                            i = 0;
                            for (String item : new_list) {
                                lst[i] = item;
                                i++;
                            }
                            CustomAdapter customAdapter = new CustomAdapter();
                            list.setAdapter(customAdapter);


                        }
                    });
                }
            });

            cross.setOnClickListener(new ImageButton.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int i = 0;
                    data.Delete_Item(Title, lst[position].toString());
                    DELETED_ITEM = lst[position].toString();

                    Cursor cur = data.TitleToItem(Title);
                    ArrayList<String> next_lst = new ArrayList<String>();
                    while (cur.moveToNext()) {
                        next_lst.add(cur.getString(0));
                    }
                    lst = new String[next_lst.size()];
                    for (i = 0; i < next_lst.size(); i++) {
                        lst[i] = next_lst.get(i);
                    }
                    cur = data.TitleToDate(Title);
                    Date_Time = new ArrayList<String>();
                    while (cur.moveToNext()) {
                        Date_Time.add(cur.getString(0));
                    }
                    CustomAdapter customAdapter = new CustomAdapter();
                    list.setAdapter(customAdapter);

                    Snackbar mySnackbar = Snackbar.make(findViewById(R.id.show_main_list_id), "Undo delete", Snackbar.LENGTH_LONG);
                    mySnackbar.setAction("Undo", new MyUndoListener());
                    mySnackbar.show();

                    if (next_lst.size() == 0) {
                        finish();
                        startActivity(new Intent(ShowmainListActivity.this, MainActivity.class));
                    }
                }
            });
            return convertView;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.protect_me) {
            SharedPreferences sharedPreferences = ShowmainListActivity.this.getSharedPreferences("Database", MODE_PRIVATE);
            if (sharedPreferences.getString("3", "").equals("")) {
                Toast.makeText(this, "Please Set password before using this option", Toast.LENGTH_LONG).show();
            } else {
                DataBaseHandler dataBaseHandler = new DataBaseHandler(ShowmainListActivity.this);
                if (item.getTitle().equals("Protect List")) {
                    dataBaseHandler.add_protect_list(Title);
                } else {
                    dataBaseHandler.Delete_protect(Title);
                }
            }
        }
        if (id == R.id.Hide_List) {
            DataBaseHandler dataBaseHandler = new DataBaseHandler(ShowmainListActivity.this);
            if (item.getTitle().equals("Hide List")) {
                dataBaseHandler.add_hidden_list(Title);
            } else {
                dataBaseHandler.Delete_Hidden(Title);
            }
        }

        if (id == R.id.share_it) {
            ArrayList<String> list = new ArrayList<>();

            for (String itm : lst) {
                list.add(itm);
            }
            Share_it share_it = new Share_it(Title, list);
            Intent intent = share_it.getIntent();
            startActivity(Intent.createChooser(intent, "Send " + Title + " List"));
        }

        if (id == R.id.duplicate) {
            DataBaseHandler db = new DataBaseHandler(getApplicationContext());
            int i = 2;
            Title = Title + "(1)";
            while (db.TitlePresent(Title)) {
                Title = Title.substring(0, Title.length() - 3) + " (" + String.valueOf(i) + ")";
                i = i + 1;
            }
            ArrayList<String> temp = new ArrayList<>();

            for (String itm : lst) {
                temp.add(itm);
            }
            List_maker list_maker = new List_maker();
            list_maker.setList_title(Title);
            list_maker.setList(temp);

            db.add_list(list_maker);

            startActivity(new Intent(ShowmainListActivity.this, MainActivity.class));
            finish();
        }

        if (id == R.id.delete_main_list) {
            AlertDialog.Builder builder = new AlertDialog.Builder(ShowmainListActivity.this);
            View box = getLayoutInflater().inflate(R.layout.del_layout, null);
            builder.setView(box);
            final AlertDialog dialog = builder.create();
            dialog.show();

            Button yes = (Button) box.findViewById(R.id.del_yes_btn);
            Button no = (Button) box.findViewById(R.id.del_no_btn);

            yes.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {

                    DataBaseHandler db = new DataBaseHandler(getApplicationContext());
                    db.Delete_List(Title);
                    startActivity(new Intent(ShowmainListActivity.this, MainActivity.class));
                    finish();

                }
            });

            no.setOnClickListener(new Button.OnClickListener() {

                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


        }

        if (id == R.id.share_list_item) {
            ArrayList<String> list = new ArrayList<>();

            for (String itm : lst) {
                list.add(itm);
            }

            Share_it share_it = new Share_it(Title, list);
            Intent intent = share_it.getIntent();
            startActivity(Intent.createChooser(intent, "Send " + Title + " List"));

        }


        return super.onOptionsItemSelected(item);
    }

    public class MyUndoListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Cursor cur = data.Add_Delete_Item(Title, DELETED_ITEM);

            ListAdapter ad = new ArrayAdapter<String>(ShowmainListActivity.this, android.R.layout.simple_list_item_1, new String[0]);
            list.setAdapter(ad);

            ArrayList<String> list_m = new ArrayList<>();

            while (cur.moveToNext()) {
                String k = cur.getString(0);
                list_m.add(k);
            }

            lst = new String[list_m.size()];

            for (int i = 0; i < list_m.size(); i++) {
                lst[i] = list_m.get(i);
            }

            CustomAdapter customAdapter = new CustomAdapter();
            list.setAdapter(customAdapter);
        }
    }
}
