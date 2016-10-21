package ua.gvv.sunshine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setContentView(R.layout.fragment_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main, new MainFragment())
                    .commit();
        }


    }

    public static class MainFragment extends Fragment {

        public MainFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            ArrayList<String> fakeDate = new ArrayList<String>(Arrays.asList(new String[] {
                    "Today - light cloud, no precipitation - 0/+8",
                    "Tomorrow - cloudy, clear at times, no precipitation - +3/+9",
                    "Sunday - overcast, no precipitation - +5/+8",
                    "Monday - cloudy, clear at times, no precipitation - +3/+6",
                    "Tuesday - light cloud, no precipitation - -1/+5",
                    "Wednesday - clear, no precipitation - 1/+5",
                    "Thursday - cloud, no precipitation - -1/+6"
            }));
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, fakeDate);

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            ListView listView = (ListView)rootView.findViewById(R.id.listview_forecast);
            listView.setAdapter(adapter);
            
            return rootView;
        }
    }
}
