package com.rkortmann.csculturalbenefits;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

/**
 * Created by Ryan on 6/26/13.
 */
public class InstitutionDetail extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_institution_detail);

        int institutionId = getIntent().getIntExtra("institution_id", -1);

        InstitutionAdapter adapter = new InstitutionAdapter(this);
        Institution institution = adapter.getInstitution(institutionId);

        TextView name = (TextView) findViewById(R.id.name);
        name.setText(institution.getName());

        TextView addressStreet = (TextView) findViewById(R.id.addressStreet);
        addressStreet.setText(institution.getAddressStreet());
        TextView addressCityStateZip = (TextView) findViewById(R.id.addressCityStateZip);
        addressCityStateZip.setText(institution.getAddressCity() + ", " + institution.getAddressState() + " " + institution.getAddressZip());

        TextView phone = (TextView) findViewById(R.id.phone);
        phone.setText(institution.getPhone());

        TextView url = (TextView) findViewById(R.id.url);
        final String urlText = institution.getUrl();
        url.setText(urlText);
        url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent link = new Intent(Intent.ACTION_VIEW, Uri.parse(urlText));
                startActivity(link);
            }
        }
        );

        TextView description = (TextView) findViewById(R.id.description);
        description.setText(Html.fromHtml(institution.getDescriptionString("<br /><br />")));
    }
}