package com.example.roombies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.icu.text.UnicodeSetSpanner;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.andtinder.model.CardModel;
import com.andtinder.model.Orientations;
import com.andtinder.view.CardContainer;
import com.andtinder.view.SimpleCardStackAdapter;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class looking_for_mates extends AppCompatActivity{
    String NatureLooking,EatingLooking,SmokingLooking,AlcoholLooking,SleepLooking;
    private CardContainer mCardContainer;
    String Name,Email;
    public ArrayList<Bitmap> dp=new ArrayList<Bitmap>();
    public ArrayList<CardModel> Cards=new ArrayList<CardModel>();
    public ArrayList<String> Room=new ArrayList<String>();
    public ArrayList<String> ArrayResult=new ArrayList<String>();
    public SimpleCardStackAdapter adapter;
    String p;
    SimpleCardStackAdapter simpleCardStackAdapter;
    String usermail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getIntent().getExtras();
        setContentView(R.layout.activity_looking_for_mates);
        NatureLooking=bundle.getString("Nature");
        AlcoholLooking=bundle.getString("Alcohol");
        SmokingLooking=bundle.getString("Smoking");
        EatingLooking=bundle.getString("Eating");
        SleepLooking=bundle.getString("Sleep");
        System.out.println("naturelooking "+NatureLooking);
        System.out.println("Alcoholic "+AlcoholLooking);
        System.out.println("smoking looking "+SmokingLooking);
        System.out.println("eating looking "+EatingLooking);
        System.out.println("SLeep looking "+SleepLooking);

        mCardContainer = (CardContainer) findViewById(R.id.layoutview);
        mCardContainer.setOrientation(Orientations.Orientation.Ordered);
        mCardContainer.setOrientation(Orientations.Orientation.Disordered);
        CardModel card = new CardModel("Title1", "Description goes here", getResources().getDrawable(R.drawable.picture1));
        card.setTitle("hello");
        adapter = new SimpleCardStackAdapter(this);
        adapter.add(card);

        SharedPreferences pref = getSharedPreferences("MY_PREF",MODE_PRIVATE);
        usermail = pref.getString("email","none");
        System.out.println("usermail is: "+usermail);
        new FindMatesClass().execute("http://192.168.43.228:5000/findMates?nature="+NatureLooking+"&sleep="+SleepLooking+"&eat="+EatingLooking+"&alcohol="+AlcoholLooking+"&smoke="+SmokingLooking);
        System.out.println("last");
    }


    private class FindMatesClass extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        private static final String TAG ="" ;
        JSONObject mFactJSON;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(looking_for_mates.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return getProfiles(params[0]);
            } catch (IOException ex) {
                Toast.makeText(getApplicationContext(),"Network Error...", Toast.LENGTH_LONG).show();
                return "Network Error!!";
            }
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (progressDialog != null)
                progressDialog.dismiss();
            if(result.contains("false")){
                System.out.println("result is false!!");
                System.out.println(result);
            }
            else{
                int c=0;
                System.out.println("result is: "+result);

                //YAHAN KUCCH KAAM KARNA HAI
                ArrayResult.clear();
                for(int i=0;i<result.length();i++)
                {
                    if(result.charAt(i)==',')
                    {
                        p="";
                    }
                    else
                    {
                        if(result.charAt(i)=='"')
                        {
                            c++;
                        }
                        else if(result.charAt(i)!='"' && result.charAt(i)!='[' && result.charAt(i)!=']' && result.charAt(i)!=' ')
                        {
                            p+=result.charAt(i);
                        }
                        if(c!=0 && (c%2)==0)
                        {
                            if(ArrayResult.size()==0)
                            {
                                String pero=p.substring(4);
                                ArrayResult.add(pero);
                            }
                            else {
                                ArrayResult.add(p);
                            }
                            c=0;
                        }
                    }
                }
                System.out.println("array result is:");
                System.out.println(ArrayResult);
                for(String one :ArrayResult) {
                    System.out.println(one);
                    new GetRoommatesClass().execute("http://192.168.43.227:5000/getroommates?email=" + one);

                }



            }
        }

        private String getProfiles(String urlPath) throws IOException {
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

                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line).append("\n");
                }

                json = result.toString();
                System.out.println("after fetch");
                //System.out.println(result);

            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            return result.toString();
        }
    }


    private class GetRoommatesClass extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        private static final String TAG ="" ;
        JSONObject mFactJSON;
        String json = "";
        JSONObject jobj;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(looking_for_mates.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return getroommates(params[0]);
            } catch (IOException ex) {
                Toast.makeText(getApplicationContext(),"Network Error...", Toast.LENGTH_LONG).show();
                return "Network Error!!";
            }
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (progressDialog != null)
                progressDialog.dismiss();
            if(result.contains("false")){
                System.out.println("result is false!!");
                System.out.println(result);
            }
            else{
                int c=0;
                System.out.println("*result is: "+result);
                System.out.println("after fetch");
                try {
                    JSONObject jobj  = new JSONObject(result); // json
                    Email = jobj.optString("Email");
                    Name = jobj.optString("Name");
                    String pic2=jobj.optString("profilePhoto");
                    byte [] encodeByte2= Base64.decode(pic2,Base64.DEFAULT);
                    Bitmap bitmap2= BitmapFactory.decodeByteArray(encodeByte2, 0, encodeByte2.length);
                    CardModel card = new CardModel(Name, Email, bitmap2);

                    card.setOnClickListener(new CardModel.OnClickListener() {
                        @Override
                        public void OnClickListener() {
                            Intent ProfileIntent=new Intent(looking_for_mates.this,other_user.class);
                            ProfileIntent.putExtra("Email",Email);
                            startActivity(ProfileIntent);

                            Log.i("Swipeable Cards","I am pressing the card");
                        }
                    });
                    adapter.add(card);
                    mCardContainer.setAdapter(adapter);
                    System.out.println("at the moment cards size :"+Cards.size());

                }
                catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }
            }
        }

        private String getroommates(String urlPath) throws IOException {

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

                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line).append("\n");
                }

            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }
            return result.toString();
        }
    }
}
