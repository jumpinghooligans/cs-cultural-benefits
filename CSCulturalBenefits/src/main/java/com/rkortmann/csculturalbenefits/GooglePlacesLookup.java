package com.rkortmann.csculturalbenefits;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

/**
 * Created by Ryan on 7/10/13.
 */
public class GooglePlacesLookup extends AsyncTask<Void, Void, JSONObject> {

    public InstitutionHandler dbHandler;
    public MainActivity.ParticipatingVendorsFragment fragment;
    public JSONObject institutionList;

    private final String PLACES_API_KEY = "AIzaSyBeg1K9pr0lAjnGSHBAGRd5dslwwfsQZmk";

    public GooglePlacesLookup(InstitutionHandler dbHandler, MainActivity.ParticipatingVendorsFragment fragment, JSONObject institutionList) {
        this.dbHandler = dbHandler;
        this.fragment = fragment;
        this.institutionList = institutionList;

        System.out.println("GooglePlacesLookup instantiated.");
    }

    protected JSONObject doInBackground(Void... params) {
        DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());

        HttpGet httpGet = null;
        HttpResponse response = null;
        HttpEntity entity = null;

        InputStream inputStream = null;
        BufferedReader reader = null;
        StringBuilder sb = null;

        try {
            JSONArray institutions = institutionList.getJSONArray("institutions");

            for(int i = 0; i < institutions.length(); i++) {
                try {

                    //GET GOOGLE SEARCH REFERENCE
                    String institutionName = institutions.getJSONObject(i).getJSONObject("jcr:content").getString("jcr:title");

                    httpGet = new HttpGet("https://maps.googleapis.com/maps/api/place/textsearch/json?sensor=true&key=" + PLACES_API_KEY + "&query=" + URLEncoder.encode(institutionName, "ISO-8859-1"));
                    httpGet.setHeader("Content-type", "application/json");

                    response = httpClient.execute(httpGet);
                    entity = response.getEntity();

                    inputStream = entity.getContent();

                    reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    sb = new StringBuilder();

                    String line = null;
                    while((line = reader.readLine()) != null) {
                        sb.append(line);
                    }

                    System.out.println("GETTING REFERENCE: " + new JSONObject(sb.toString()).getString("status"));

                    //GET GOOGLE PLACE DETAILS
                    String reference = new JSONObject(sb.toString()).getJSONArray("results").getJSONObject(0).getString("reference");

                    httpGet = new HttpGet("https://maps.googleapis.com/maps/api/place/details/json?sensor=true&key=" + PLACES_API_KEY + "&reference=" + reference);
                    httpGet.setHeader("Content-type", "application/json");

                    response = httpClient.execute(httpGet);
                    entity = response.getEntity();

                    inputStream = entity.getContent();

                    reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    sb = new StringBuilder();

                    line = null;
                    while((line = reader.readLine()) != null) {
                        sb.append(line);
                    }

                    System.out.println("GETTING DETAILS: " + new JSONObject(sb.toString()).getString("status"));

                    institutions.getJSONObject(i).put("gp:data", new JSONObject(sb.toString()).getJSONObject("result"));
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }

            institutionList.remove("institutions");
            institutionList.put("institutions", institutions);

        } catch(Exception e) {
            e.printStackTrace();
        }

        return institutionList;
    }

    protected void onPostExecute(JSONObject result) {
        //System.out.println("TEST: " + result.getJSONArray("institutions").getJSONObject(i).getJSONObject("jcr:content").getString("jcr:title"));
        //update db
        dbHandler.updateDBWithJSON(result);
        //update listview
        fragment.updateInstitutionListWithJSON(result);
    }
}
