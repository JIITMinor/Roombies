package com.example.roombies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.content.res.ConfigurationHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class mychar extends AppCompatActivity {
    String NatureResponse,EatingResponse,SleepResponse,AlcoholResponse,SmokingResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //NOT DONE YET REST API NOT DONE
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mychar);
        Button SearchButton = (Button)findViewById(R.id.Search);
        CheckBox NatureYes=(CheckBox) findViewById(R.id.natureyes);
        CheckBox NatureNo=(CheckBox)findViewById(R.id.natureno);
        CheckBox EatingYes=(CheckBox)findViewById(R.id.eatingyes);
        final CheckBox EatingNo=(CheckBox)findViewById(R.id.eatingno);
        CheckBox SleepingYes=(CheckBox)findViewById(R.id.sleepyes);
        CheckBox SleepingNo=(CheckBox)findViewById(R.id.sleepno);
        CheckBox AlcoholYes=(CheckBox)findViewById(R.id.alcoholyes);
        CheckBox AlcoholNo=(CheckBox)findViewById(R.id.alcoholno);
        CheckBox SmokingYes=(CheckBox)findViewById(R.id.smokeyes);
        CheckBox SmokingNo=(CheckBox)findViewById(R.id.smokeno);
        NatureYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NatureResponse="Yes";
            }
        });
        NatureNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NatureResponse="No";
            }
        });
        EatingYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EatingResponse="Yes";
            }
        });
        EatingNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EatingResponse="No";
            }
        });
        SleepingYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SleepResponse="Yes";
            }
        });
        SleepingNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SleepResponse="No";
            }
        });
        AlcoholYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlcoholResponse="Yes";
            }
        });
        AlcoholNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlcoholResponse="No";
            }
        });
        SmokingYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmokingResponse="Yes";
            }
        });
        SmokingNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmokingResponse="No";
            }
        });
        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new PostCharactersticsDetailsClass().execute("http://192.168.43.227:5000/savecharacterstics");
                Intent GoToLookingForMates=new Intent(mychar.this,looking_for_mates.class);
                GoToLookingForMates.putExtra("Nature",NatureResponse);
                GoToLookingForMates.putExtra("Alcohol",AlcoholResponse);
                GoToLookingForMates.putExtra("Eating",EatingResponse);
                GoToLookingForMates.putExtra("Sleep",SleepResponse);
                GoToLookingForMates.putExtra("Smoking",SmokingResponse);
                startActivity(GoToLookingForMates);
            }
        });
    }
    /*class PostCharactersticsDetailsClass extends AsyncTask<String,Void,String>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mychar.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return PostCharacterticsDetails(params[0]);
            } catch (IOException ex) {
                return "Network Error!!";
            } catch (JSONException ex) {
                return "Data Invalid !";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(progressDialog!=null)
                progressDialog.dismiss();
            if(result.contains("true"))
            {
                //means successfully done
                Intent NextActivity=new Intent(mychar.this,looking_for_mates.class);
                startActivity(NextActivity);
            }
            else{
                //not done
                Toast.makeText(getApplicationContext(),"Unable to Update Details",Toast.LENGTH_LONG);
                Intent YahiActivity=new Intent(mychar.this,mychar.class);
                startActivity(YahiActivity);
            }
        }
        private String PostCharacterticsDetails(String urlPath) throws IOException, JSONException {
            BufferedWriter bufferedWriter=null;
            BufferedReader bufferedReader=null;
            StringBuilder result=new StringBuilder();
            StringBuilder builder;
            try{
                JSONObject dataToSend=new JSONObject();
                dataToSend.put("Nature",NatureResponse);
                dataToSend.put("Sleep",SleepResponse);
                dataToSend.put("Eating",EatingResponse);
                dataToSend.put("Smoking",SmokingResponse);
                dataToSend.put("Alcohol",AlcoholResponse);

                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                OutputStream outputStream = urlConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(dataToSend.toString());
                bufferedWriter.flush();

                //read data response from server
                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                System.out.println(line);
                builder = new StringBuilder();
                builder.append(urlConnection.getResponseCode());
                Log.d("not in finally", result.toString());
            }finally{
                if(bufferedReader!=null)
                    bufferedReader.close();
                if(bufferedWriter!=null)
                    bufferedWriter.close();
            }


            return result.toString();
        }
    }*/

}
