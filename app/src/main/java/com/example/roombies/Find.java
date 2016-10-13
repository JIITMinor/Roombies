package com.example.roombies;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Base64;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.app.ListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Find extends AppCompatActivity {

    public ArrayList<String> Name=new ArrayList<String>();
    public ArrayList<String> Address=new ArrayList<String>();
    public ArrayList<String> City=new ArrayList<String>();
    public ArrayList<Integer> Rent=new ArrayList<Integer>();
    public int imgs[]={};
    public int dps[]={};
    TextView mResult;
    public TextView textView5;
    TextView textView6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_find);
        //  mResult=(TextView) findViewById(R.id.mResult);

        new getRoomDetails().execute("http://192.168.43.75:5000/getRoomDetails");

        int img[]= {R.mipmap.room2,R.mipmap.room3,R.mipmap.room4,R.mipmap.room5};
        this.imgs=img;
        int dp[]= {R.mipmap.img1,R.mipmap.img2,R.mipmap.img3,R.mipmap.img4};
        this.dps=dp;
        //  final int dp[]={R.mipmap.img,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};



    }

    class getRoomDetails extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Find.this);
            progressDialog.setMessage("Loading data...");
            progressDialog.show();
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

           /* if (result.contains("true")) {

                mResult.setText("data fetched:" + result);
            } else {

                mResult.setText("data not fetched: " + result);

            }*/
            System.out.println("here is life!!");
            Log.d("vsh", "idea");
            ListView listView = (ListView) findViewById(R.id.listView);

            CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), Name, Address, City ,Rent,imgs,dps);
            listView.setAdapter(customAdapter);

            //  mResult.setText("Login: "+result);
            //Log.d("Mresult",mResult.toString());
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
                //    urlConnection.setReadTimeout(10000);
                //    urlConnection.setConnectTimeout(10000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                System.out.println("after connect");

            /*   InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line).append("\n");
                }


                System.out.println("after fetch");

                builder = new StringBuilder();
                builder.append(urlConnection.getResponseCode());
                System.out.println("after builder!");;

            }

            finally {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            }

            return result.toString();
        }*/
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
                    Name.add(jobj.optString("Name"));
                    Address.add(jobj.optString("Address"));
                    City.add(jobj.optString("City"));
                    Rent.add(Integer.parseInt(jobj.optString("Expected Rent")));

                    //  Log.e("msg",bitmap.toString());
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            return result.toString();
        }
    }
}