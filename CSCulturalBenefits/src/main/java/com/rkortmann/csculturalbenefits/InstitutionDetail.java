package com.rkortmann.csculturalbenefits;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Ryan on 6/26/13.
 */
public class InstitutionDetail extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institution_detail);

        TextView tv = (TextView) findViewById(R.id.InstitutionDetailName);

        tv.setText(getIntent().getStringExtra("name"));
    }
}