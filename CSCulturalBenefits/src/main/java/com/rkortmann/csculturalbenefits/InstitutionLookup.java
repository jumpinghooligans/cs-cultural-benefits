package com.rkortmann.csculturalbenefits;

import android.content.ContentValues;
import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

/**
 * Created by Ryan on 7/8/13.
 */
public class InstitutionLookup extends AsyncTask<String, Void, JSONObject> {

    public InstitutionHandler dbHandler;
    public MainActivity.ParticipatingVendorsFragment fragment;

    public InstitutionLookup(InstitutionHandler dbHandler, MainActivity.ParticipatingVendorsFragment fragment) {
        this.dbHandler = dbHandler;
        this.fragment = fragment;
    }

    protected JSONObject doInBackground(String... urls) {
        DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
        HttpGet httpGet = new HttpGet(urls[0]);

        httpGet.setHeader("Content-type", "application/json");

        InputStream inputStream = null;
        JSONObject json = null;

        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            inputStream = entity.getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while((line = reader.readLine()) != null) {
                sb.append(line);
            }

            json = new JSONObject(sb.toString());

        } catch(Exception e) {
            System.out.println("JSON EXCEPTION: " + e);
        }

        return json;
    }

    protected void onPostExecute(JSONObject result) {
        Iterator keys = result.keys();

        JSONObject filteredResult = new JSONObject();

        JSONArray institutions = new JSONArray();

        while(keys.hasNext()) {
            String key = (String) keys.next();

            if(!key.substring(0, 4).equals("jcr:")) {
                System.out.println("Filtering: " + key);

                try {
                    institutions.put(result.getJSONObject(key));
                } catch (Exception e) {
                    System.out.println("FILTER: " + e);
                }
            }
        }

        try {
            filteredResult.put("institutions", institutions);
        } catch (Exception e) {
            System.out.println("CONSOLIDATE: " + e);
        }

        //get details
        new GooglePlacesLookup(dbHandler, fragment, filteredResult).execute();
    }
}

