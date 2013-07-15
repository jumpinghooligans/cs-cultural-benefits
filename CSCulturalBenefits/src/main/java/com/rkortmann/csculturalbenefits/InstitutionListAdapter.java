package com.rkortmann.csculturalbenefits;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ryan on 7/11/13.
 */
public class InstitutionListAdapter extends BaseAdapter {
    private Activity activity;
    public ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater = null;

    public InstitutionListAdapter(Activity activity, ArrayList<HashMap<String, String>> data) {
        this.activity = activity;
        this.data = data;

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        if(view==null)
            vi = inflater.inflate(R.layout.institution_row, null);

        TextView name = (TextView)vi.findViewById(R.id.institution_name);
        //TextView categories = (TextView)vi.findViewById(R.id.institution_categories);

        HashMap<String, String> institution = new HashMap<String, String>();
        institution = data.get(i);

        name.setText(institution.get("name"));
        //categories.setText(institution.get("categories"));

        return vi;
    }
}
