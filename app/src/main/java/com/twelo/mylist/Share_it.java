package com.twelo.mylist;


import android.content.Intent;

import java.util.ArrayList;

public class Share_it {

    Intent intent = new Intent();

    Share_it(String Title, ArrayList<String> lst) {
        String send_matter = "";
        send_matter = send_matter + Title + "\n\n";

        for (int i = 0; i < lst.size(); i++) {
            send_matter = send_matter + "  " + lst.get(i).toString() + "\n";
        }
        send_matter = send_matter + "\n\t\t\t\t-BY My List App";


        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, send_matter);
        intent.setType("text/plain");
    }

    public Intent getIntent() {
        return intent;
    }
}
