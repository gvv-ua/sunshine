package ua.gvv.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by gvv on 29.10.16.
 */
public class ForecastFragment extends Fragment {
    private ArrayAdapter<String> adapter;
    private SharedPreferences sharedPref;



    public ForecastFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                updateWeather();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            case R.id.action_view_on_map:
                viewLoactionOnMap();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void viewLoactionOnMap() {
        if (sharedPref == null) {
            sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        }
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme("geo")
                .authority("0,0")
                .appendQueryParameter("q", sharedPref.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_def_value)))
                .build();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriBuilder.toString()));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            getContext().startActivity(intent);
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();

    }

    private void updateWeather() {
        if (sharedPref == null) {
            sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        }
        new FetchWeatherTask(getActivity(), adapter).execute(
                sharedPref.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_def_value)),
                sharedPref.getString(getString(R.string.pref_units_key), getString(R.string.pref_unit_def_value))
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, new ArrayList());


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String msg = adapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_TEXT, msg);
                startActivity(intent);
            }
        });

        return rootView;
    }

}
