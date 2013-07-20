package com.rkortmann.csculturalbenefits;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends FragmentActivity {

    static final String INSTITUTION_JSON_URL = "http://rkortmann.com/csculturalbenefits/-1.json";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        System.out.println("onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume");
    }


    public void moreInformation(View v) {
        Intent mi = new Intent(Intent.ACTION_VIEW, Uri.parse("http://csintra.net/corporate_responsibility/corporatecitizenship/en/americas/culturalbenefits.html"));
        startActivity(mi);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_refresh:
                InstitutionHandler ih = new InstitutionHandler(this, null, null, 5);
                ParticipatingVendorsFragment pv = (ParticipatingVendorsFragment) mSectionsPagerAdapter.pv;

                new InstitutionLookup(ih, pv).execute(INSTITUTION_JSON_URL);
                return true;
            case R.id.action_about:
                Toast.makeText(this, "About Under Construction", 2000).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private Fragment home;
        private Fragment pv;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);

            home = new HomepageFragment();
            pv = new ParticipatingVendorsFragment();
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a HomepageFragment (defined as a static inner class
            // below) with the page number as its lone argument.

            switch (position) {
                case 0:
                    return home;
                case 1:
                    return pv;
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }
    }

    static class HomepageFragment extends Fragment {

        public static final String ARG_SECTION_NUMBER = "section_number";

        public HomepageFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_home, container, false);
            return rootView;
        }
    }

    static class ParticipatingVendorsFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private ListView lv;
        private InstitutionHandler ih;
        private InstitutionAdapter adapter;
        private Cursor cursor;
        //private SimpleCursorAdapter cursorAdapter;
        private InstitutionListAdapter cursorAdapter;

        public ParticipatingVendorsFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_participating_institutions, container, false);

            lv = (ListView) rootView.findViewById(R.id.institutionList);
            ih = new InstitutionHandler(getActivity(), null, null, 5);

            //new InstitutionLookup(ih, this).execute(INSTITUTION_JSON_URL);

            adapter = new InstitutionAdapter(getActivity());

            cursor = adapter.queryInstitutions();
            ArrayList<HashMap<String, String>> data = cursorToList(cursor);

            //cursorAdapter = new SimpleCursorAdapter(getActivity(), android.R.layout.two_line_list_item, cursor, new String[] { ih.COLUMN_NAME, ih.COLUMN_ADDRESS_STREET }, new int[] { android.R.id.text1, android.R.id.text2 });
            cursorAdapter = new InstitutionListAdapter(getActivity(), data);

            System.out.println("ONCREATE: " + cursor.getCount());

            lv.setAdapter(cursorAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity(), InstitutionDetail.class);

                    cursor.moveToPosition(i);

                    intent.putExtra("institution_id", cursor.getInt(cursor.getColumnIndex(ih.COLUMN_ID)));

                    startActivity(intent);
                }
            });

            return rootView;
        }

        public void updateInstitutionListWithJSON(JSONObject json) {
            System.out.println("UPDATE LISTVIEW");

            cursor = adapter.queryInstitutions();
            cursorAdapter.data = cursorToList(cursor);
            cursorAdapter.notifyDataSetChanged();

            Toast.makeText(getActivity(), "Institutions Updated...", 2000).show();
        }

        public ArrayList<HashMap<String, String>> cursorToList(Cursor cursor) {

            ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> temp = null;

            //cursor.moveToFirst();
            while(cursor.moveToNext()) {
                temp = new HashMap<String, String>();

                temp.put("name", cursor.getString(1));
                temp.put("categories", cursor.getString(2));

                data.add(temp);
            }

            return data;
        }
    }

}
