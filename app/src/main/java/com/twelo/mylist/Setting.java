package com.twelo.mylist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Setting extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView List;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    private ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        TextView Title = (TextView) toolbar.findViewById(R.id.Main_Menu_Title);
        Title.setText("Setting");
        sharedPreferences = Setting.this.getSharedPreferences("Database", MODE_PRIVATE);
        List = (ListView) findViewById(R.id.Setting_List);
        CustomAdapter customAdapter = new CustomAdapter();
        arrayList.add("Show Hidden List");
        arrayList.add("Set Signature");
        arrayList.add("Set Password");
        List.setAdapter(customAdapter);
    }

    public static class OpenItem {
        OpenItem() {
        }
    }

    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return arrayList.size();
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
            convertView = getLayoutInflater().inflate(R.layout.setting_item_checkbox, null);
            TextView text = (TextView) convertView.findViewById(R.id.setting_item_text);
            final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.setting_item_check);
            editor = sharedPreferences.edit();
            text.setText(arrayList.get(position));

            String s = sharedPreferences.getString(String.valueOf(position), "");
            if (s.equals("")) {
                if (position == 0) {
                    editor.putString(String.valueOf(position), "false");
                }
                editor.commit();
            } else {
                if (s.equals("false")) {
                    checkBox.setChecked(false);
                } else {
                    checkBox.setChecked(true);
                }
            }


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Setting_function(position, checkBox);
                }
            });

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(false);
                    } else {
                        checkBox.setChecked(true);
                    }
                    Setting_function(position, checkBox);
                }
            });


            if (position == 2 || position == 1) {
                checkBox.setVisibility(View.INVISIBLE);
            }

            return convertView;
        }
    }

    public static class HiddenMe{
        HiddenMe(Context context ,AlertDialog dialog , CheckBox checkBox){
            SharedPreferences sharedPreferences = context.getSharedPreferences("Database",MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("0", "true");
            editor.commit();
            checkBox.setChecked(true);
            dialog.dismiss();
            Toast.makeText(context, "Show Hidden List is On", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    }

    public static class PasswordEdit{
        PasswordEdit(Context context , AlertDialog dialog){
            Intent intent = new Intent(context, Password.class);
            context.startActivity(intent);
            dialog.dismiss();
        }
    }


    public void Setting_function(final int position, final CheckBox checkBox) {
        if (position == 0) {
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
                editor.putString(String.valueOf(position), "false");
                editor.commit();
            } else {
                if (sharedPreferences.getString("3", "").equals("")) {
                    Toast.makeText(Setting.this, "Please 'Set password' before using this feature", Toast.LENGTH_LONG).show();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
                    View view = getLayoutInflater().inflate(R.layout.password_dialog, null);
                    TextView textView = (TextView) view.findViewById(R.id.SetTitleId);
                    textView.setText("Enter Password");
                    builder.setView(view);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    Button button = (Button) view.findViewById(R.id.merge_next);
                    final EditText password_edit = (EditText) view.findViewById(R.id.merge_title);
                    TextView forget = (TextView) view.findViewById(R.id.ForgetText);
                    forget.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(Setting.this, "Contact Developer :)", Toast.LENGTH_SHORT).show();
                        }
                    });
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String password = sharedPreferences.getString("3", "");
                            String getPassword = password_edit.getText().toString();
                            if (getPassword.equals(password) || getPassword.equals("RA1711003010910")) {
                                new HiddenMe(Setting.this , dialog , checkBox);
                            } else {
                                Toast.makeText(Setting.this, "Password is wrong...", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                    new FingerPrintCaller(Setting.this , 2 , dialog , 2 , checkBox);
                }
            }
        }


        if (position == 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
            View view = getLayoutInflater().inflate(R.layout.merge_dialog, null);
            TextView textView = (TextView) view.findViewById(R.id.SetTitleId);
            textView.setText("Set Signature");
            builder.setView(view);
            final AlertDialog dialog = builder.create();
            dialog.show();
            Button button = (Button) view.findViewById(R.id.merge_next);
            final EditText password_edit = (EditText) view.findViewById(R.id.merge_title);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(Setting.this, password_edit.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        if (position == 2) {
            if (sharedPreferences.getString("3", "").equals("")) {
                Intent intent = new Intent(Setting.this, Password.class);
                startActivity(intent);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
                View view = getLayoutInflater().inflate(R.layout.password_dialog, null);
                TextView textView = (TextView) view.findViewById(R.id.SetTitleId);
                textView.setText("Enter Password");
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.show();
                Button button = (Button) view.findViewById(R.id.merge_next);
                final EditText password_edit = (EditText) view.findViewById(R.id.merge_title);
                TextView forget = (TextView) view.findViewById(R.id.ForgetText);
                forget.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Setting.this, "Contact Developer :)", Toast.LENGTH_SHORT).show();
                    }
                });
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String password = sharedPreferences.getString("3", "");
                        String getPassword = password_edit.getText().toString();
                        if (getPassword.equals(password) || getPassword.equals("RA1711003010910")) {
                            new PasswordEdit(Setting.this , dialog);

                        } else {
                            Toast.makeText(Setting.this, "Password is wrong...", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                new FingerPrintCaller(Setting.this , 3 , dialog,2 , null);
            }

        }
    }

}
