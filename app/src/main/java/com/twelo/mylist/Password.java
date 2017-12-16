package com.twelo.mylist;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Password extends AppCompatActivity {
    SharedPreferences shared;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.setting_toolbar);
        TextView Title = (TextView) toolbar.findViewById(R.id.Main_Menu_Title);
        Title.setText("Set Password");

        shared = Password.this.getSharedPreferences("Database",MODE_PRIVATE);

        final EditText pas1 = (EditText)findViewById(R.id.password1);
        final EditText pas2 = (EditText)findViewById(R.id.password2);
        Button next = (Button)findViewById(R.id.password_next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass1 = pas1.getText().toString();
                String pass2 = pas2.getText().toString();

                if (pass1.equals(pass2)){
                    if (pass1.length()>=4){
                    SharedPreferences.Editor edit = shared.edit();
                    edit.putString("3",pass1);
                    edit.commit();
                    Toast.makeText(Password.this, "Password Set", Toast.LENGTH_SHORT).show();
                    finish();}else {
                        Toast.makeText(Password.this, "Password Length greater than 4 character ", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(Password.this, "Password dosen't matched ", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
