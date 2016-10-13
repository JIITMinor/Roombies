package com.example.roombies;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompatBase;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProfileF extends Fragment {


    private NotificationCompatBase.Action intent;

    public ProfileF() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new getRoomDetails().execute("http://192.168.43.75:5000/getRoomDetails");
        return inflater.inflate(R.layout.profile_f, container, false);

    }

    public NotificationCompatBase.Action getIntent() {
        return intent;
    }

    class getRoomDetails extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String name,email,phone,city,age,prof;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*progressDialog = new ProgressDialog();
            progressDialog.setMessage("Loading data...");
            progressDialog.show();*/
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return getRoomData(params[0]);
            } catch (IOException ex) {
                return "Network Error...";
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            System.out.println("here is life!!");
            Log.d("vsh", "idea");

            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }

        private String getRoomData(String urlPath) throws IOException {
            String json = "";
            JSONObject jobj;

            StringBuilder result = new StringBuilder();
            BufferedReader bufferedReader = null;
            StringBuilder builder;
            try {

                System.out.println("before connect");
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                System.out.println("after connect");

                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line).append("\n");
                }

                json = result.toString();

                System.out.println("after fetch");

            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }


            try {
                JSONArray jsonArray=new JSONArray(json);
                for(int i=0;i<jsonArray.length();i++) {
                    jobj = jsonArray.getJSONObject(i);
                    // Storing  JSON item in a Variable
                    String email1=jobj.optString("email");
                    if(email1==email)
                    {
                        name =jobj.optString("email");
                        phone=jobj.optString("Phone");
                        city=jobj.optString("City");
                        age=jobj.optString("Age");
                        prof=jobj.optString("Profession");
                        TextView t = (TextView) getView().findViewById(R.id.user_profile_name);
                        t= (TextView) getView().findViewById(R.id.user_profile_name);
                        t.setText(name);
                        t = (TextView) getView().findViewById(R.id.user_profile_short_bio);
                        t.setText(age);
                        t = (TextView) getView().findViewById(R.id.user_city);
                        t.setText(city);
                        t = (TextView) getView().findViewById(R.id.user_phone);
                        t.setText(phone);
                        t = (TextView) getView().findViewById(R.id.user_prof);
                        t.setText(prof);



                    }
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            return result.toString();
        }
    }


}
