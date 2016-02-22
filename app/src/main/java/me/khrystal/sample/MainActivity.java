package me.khrystal.sample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void listOnClick(View view) {
        Intent intent = new Intent(this,ListActivity.class);
        intent.putExtra("ListType",0);
        startActivity(intent);
    }

    public void gridOnClick(View view) {
        Intent intent = new Intent(this,ListActivity.class);
        intent.putExtra("ListType",1);
        startActivity(intent);
    }

    public void listWithHeaderOnClick(View view) {
        Intent intent = new Intent(this,ListActivity.class);
        intent.putExtra("ListType",2);
        startActivity(intent);
    }

    public void gridWithHeaderOnClick(View view) {
        Intent intent = new Intent(this,ListActivity.class);
        intent.putExtra("ListType",3);
        startActivity(intent);
    }
}
