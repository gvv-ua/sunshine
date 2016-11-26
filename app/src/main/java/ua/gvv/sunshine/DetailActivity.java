package ua.gvv.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailActivity extends ActionBarActivity {
    private final static int DETAIL_LOADER_ID = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new DetailFragment())
                    .commit();
        }
    }

    /**
     * A detail fragment containing a simple view.
     */
    public static class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
        private ShareActionProvider shareActionProvider;
        private Uri detailUri;
        private String detailMsg = null;

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            return rootView;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            Intent intent = getActivity().getIntent();
            if (intent != null) {
                String message = intent.getDataString();
                if (message != null) {
                    detailUri = Uri.parse(message);
                }
            }
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            // Inflate the menu; this adds items to the action bar if it is present.
            inflater.inflate(R.menu.detail, menu);

            // Locate MenuItem with ShareActionProvider
            MenuItem item = menu.findItem(R.id.action_share);

            // Fetch and store ShareActionProvider
            shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
            if ((shareActionProvider != null) && (detailMsg != null)) {
                shareActionProvider.setShareIntent(createShareForecastIntent());
            }
        }

        private Intent createShareForecastIntent() {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain").putExtra(shareIntent.EXTRA_TEXT, detailMsg + "#SunshineApp");
            return shareIntent;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            getLoaderManager().initLoader(DETAIL_LOADER_ID, null, this);
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            CursorLoader cursorLoader = null;
            if (id == DETAIL_LOADER_ID) {
                cursorLoader = new CursorLoader(getActivity(),
                        detailUri,
                        Utility.getForecastColumns(),
                        null,
                        null,
                        null);
            }
            return cursorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data.moveToFirst()) {
                boolean isMetric = Utility.isMetric(getActivity());
                detailMsg = Utility.formatDate(data.getLong(Utility.COL_WEATHER_DATE)) + " - " +
                        data.getString(Utility.COL_WEATHER_DESC) + " - " +
                        Utility.formatTemperature(data.getDouble(Utility.COL_WEATHER_MAX_TEMP), isMetric) +  "/" +
                        Utility.formatTemperature(data.getDouble(Utility.COL_WEATHER_MIN_TEMP), isMetric);
                TextView textViewview = (TextView) getActivity().findViewById(R.id.detail_text_view);
                textViewview.setText(detailMsg);

                // If onCreateOptionsMenu has already happened, we need to update the share intent now.
                if (shareActionProvider != null) {
                    shareActionProvider.setShareIntent(createShareForecastIntent());
                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }
}
