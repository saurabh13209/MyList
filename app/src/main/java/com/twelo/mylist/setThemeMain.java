package com.twelo.mylist;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;

class setThemeMain {
    public static int Background;
    public static Drawable ListTheme;
    public static Drawable NumTheme;
    public static int Text;
    public static boolean isdark = false;

    SharedPreferences sharedPreferences;

    setThemeMain(Context context) {
        sharedPreferences = context.getSharedPreferences("Database", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("Theme", "").equals("true")) {
            isdark = true;
            ListTheme = context.getDrawable(R.drawable.main_list_background_dark);
            NumTheme = context.getDrawable(R.drawable.item_number_dark);
            Text = context.getResources().getColor(R.color.DarkThemeText);
            Background = context.getResources().getColor(R.color.DarkThemeBackground);

        } else {
            isdark = false;
            Background = context.getResources().getColor(R.color.MainList_Back);
            NumTheme = context.getDrawable(R.drawable.item_number);
            ListTheme = context.getDrawable(R.drawable.main_list_background);
            Text = context.getResources().getColor(R.color.DarkThemeBackground);

        }
    }
}
