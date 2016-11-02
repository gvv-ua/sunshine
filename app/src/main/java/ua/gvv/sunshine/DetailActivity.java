package ua.gvv.sunshine;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
    public static class DetailFragment extends Fragment {
        private ShareActionProvider shareActionProvider;

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
            if ((intent != null) && (intent.hasExtra(intent.EXTRA_TEXT))) {
                String message = intent.getStringExtra(intent.EXTRA_TEXT);
                TextView textViewview = (TextView) view.findViewById(R.id.detail_text_view);
                textViewview.setText(message);
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
            if (shareActionProvider != null) {
                Intent intent = getActivity().getIntent();
                if ((intent != null) && (intent.hasExtra(intent.EXTRA_TEXT))) {
                    String message = intent.getStringExtra(intent.EXTRA_TEXT);
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareActionProvider.setShareIntent(shareIntent.setType("text/plain").putExtra(shareIntent.EXTRA_TEXT, message + "#SunshineApp"));
                }
            }
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

    }
}
