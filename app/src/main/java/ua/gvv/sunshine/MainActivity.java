package ua.gvv.sunshine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;


public class MainActivity extends AppCompatActivity {
    private static final String ACTIVITY_LIFE_CYCLE_TAG = "ACTIVITY_LIFE_CYCLE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(ACTIVITY_LIFE_CYCLE_TAG, "onCreate");
        setContentView(R.layout.activity_main);
        //setContentView(R.layout.fragment_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main, new ForecastFragment())
                    .commit();
        }
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        super.addContentView(view, params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(ACTIVITY_LIFE_CYCLE_TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(ACTIVITY_LIFE_CYCLE_TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(ACTIVITY_LIFE_CYCLE_TAG, "onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(ACTIVITY_LIFE_CYCLE_TAG, "onStop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(ACTIVITY_LIFE_CYCLE_TAG, "onStart");
    }
}
