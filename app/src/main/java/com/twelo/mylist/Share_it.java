package com.twelo.mylist;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class Share_it {

    Intent intent = new Intent();

    Share_it(String Title, ArrayList<String> lst , Context context) {
        String send_matter = "*";
        send_matter = send_matter + Title + "*\n";

        for (int i = 0; i < lst.size(); i++) {
            send_matter = send_matter + "  " + lst.get(i).toString().substring(0,1).toUpperCase()+lst.get(i).substring(1).toLowerCase() + "\n";
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("Database",Context.MODE_PRIVATE);
        String s = sharedPreferences.getString("1","");
        if (s.equals("")){
            s = "By MyList Apk";
        }
        send_matter = send_matter + "\n\t\t\t\t-"+s;

        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, send_matter);
        intent.setType("text/plain");
    }

    public Intent getIntent() {
        return intent;
    }
}
