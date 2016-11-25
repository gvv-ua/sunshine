package ua.gvv.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import ua.gvv.sunshine.data.WeatherContract;

/**
 * Created by gvv on 29.10.16.
 */
public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private ForecastAdapter adapter;
    private SharedPreferences sharedPref;
    private final static int CURSOR_LOADER_ID = 100;

    private static final String[] FORECAST_COLUMNS = {
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.LocationEntry.COLUMN_COORD_LAT,
            WeatherContract.LocationEntry.COLUMN_COORD_LONG
    };


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
            getActivity().startActivity(intent);
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
        new FetchWeatherTask(getActivity()).execute(
                sharedPref.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_def_value)),
                sharedPref.getString(getString(R.string.pref_units_key), getString(R.string.pref_unit_def_value))
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Cursor cur = getActivity().getContentResolver().query(weatherForLocationUri, null, null, null, sortOrder);

        adapter = new ForecastAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String msg = adapter.getItem(position);
//                Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_TEXT, msg);
//                startActivity(intent);
//            }
//        });

        return rootView;
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = null;
        if (id == CURSOR_LOADER_ID) {
            String locationSetting = Utility.getPreferredLocation(getActivity());

            // Sort order:  Ascending, by date.
            String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
            Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(locationSetting, System.currentTimeMillis());
            cursorLoader = new CursorLoader(getActivity(),
                    weatherForLocationUri,
                    FORECAST_COLUMNS,
                    null,
                    null,
                    sortOrder);
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
    }
}
