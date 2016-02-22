package me.khrystal.sample;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ListActivity extends AppCompatActivity {

    int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        type = getIntent().getIntExtra("ListType",0);
        initFragment();
    }

    private void initFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (type==4){
            SwipRefreshListFragment fragment = new SwipRefreshListFragment();
            transaction.add(R.id.fragment_container,fragment);
            transaction.commit();
        }else {
            ListFragment fragment = new ListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("ListType", type);
            fragment.setArguments(bundle);
            transaction.add(R.id.fragment_container, fragment);
            transaction.commit();
        }
    }
}
