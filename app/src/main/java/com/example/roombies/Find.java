package com.example.roombies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.app.ListActivity;
import android.widget.Toast;

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
import java.util.Locale;

public class Find extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    public ArrayList<String> Name=new ArrayList<String>();
    public ArrayList<String> Address=new ArrayList<String>();
    public ArrayList<String> City=new ArrayList<String>();
    public ArrayList<Integer> Rent=new ArrayList<Integer>();
    public ArrayList<Bitmap> img=new ArrayList<Bitmap>();
    public ArrayList<Bitmap> dp=new ArrayList<Bitmap>();
    ArrayList<PersonDetails> arrayList=new ArrayList<PersonDetails>();
    ArrayList<PersonDetails> details=new ArrayList<PersonDetails>();
    CustomAdapter customAdapter;
    public static ArrayList<String> Locations=new ArrayList<String>();
    TextView mResult;
    public TextView textView5;
    TextView textView6;
    String usermail,usercity,useraddress,userlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_find);
        //  mResult=(TextView) findViewById(R.id.mResult);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MY_PREF", Context.MODE_PRIVATE);
        usermail = pref.getString("email","none");
        Toast.makeText(getApplicationContext(),"email: "+usermail, Toast.LENGTH_LONG).show();

        //usermail="chhabra.simran02@gmail.com";
        String[] sortOptions={"Sort By-","Rent-low to high","Rent-high to low"};

        Spinner sort= (Spinner)findViewById(R.id.sort);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,sortOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sort.setAdapter(adapter);

        new getRoomDetails().execute("http://192.168.43.227:5000/getRoomDetails");

        sort.setOnItemSelectedListener(this);

        final EditText search=(EditText)findViewById(R.id.inputSearch);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String text = search.getText().toString().toLowerCase(Locale.getDefault());
                customAdapter.filter(text);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ImageButton mapButton=(ImageButton) findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = search.getText().toString().toLowerCase(Locale.getDefault());
                details=customAdapter.loc(text);

                new getLocation().execute("http://192.168.43.227:5000/getlocation?email="+usermail);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        //String item = parent.getItemAtPosition(position).toString();
        //Toast.makeText(parent.getContext(), "Selected: " + position, Toast.LENGTH_LONG).show();
        if(position==1)
        {
            new sortByRent().execute("http://192.168.43.227:5000/sortByRent");
        }
        if(position==2)
        {
            new sortByRentDesc().execute("http://192.168.43.227:5000/sortByRentDesc");
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class getLocation extends AsyncTask<String, Void, String> {
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
            System.out.println("here is life!!");
            Log.d("vsh", "idea");

            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            for(int i=0;i<details.size();i++)
            {
                String loc=details.get(i).getPlace()+" "+details.get(i).getCity();
                Locations.add(loc);
            }
            Intent i=new Intent(getApplicationContext(),MapsActivity1.class);
          //  i.putExtra("locations",Locations);
            i.putExtra("userlocation",userlocation);


            startActivity(i);

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
                urlConnection.setReadTimeout(20000);
                urlConnection.setConnectTimeout(20000);
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
                JSONObject jObject = new JSONObject(json); // json
                useraddress = jObject.optString("Address");
                usercity = jObject.optString("City");
                userlocation = useraddress + " " + usercity;
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            return result.toString();
        }
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
            System.out.println("here is life!!");
            Log.d("vsh", "idea");
            ListView listView = (ListView) findViewById(R.id.listView);
            for(int i=0;i<Name.size();i++)
            {
                PersonDetails pd=new PersonDetails(Name.get(i),Address.get(i),City.get(i),Rent.get(i),img.get(i),dp.get(i));
                arrayList.add(pd);
            }

            //customAdapter = new CustomAdapter(getApplicationContext(), Name, Address, City ,Rent,img,dp);
            //Log.e("msg",arrayList.get(0).getName());
            customAdapter = new CustomAdapter(getApplicationContext(),arrayList);
            listView.setAdapter(customAdapter);

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
                urlConnection.setReadTimeout(20000);
                urlConnection.setConnectTimeout(20000);
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
                    Name.add(jobj.optString("Name"));
                    Address.add(jobj.optString("Address"));
                    City.add(jobj.optString("City"));
                    Rent.add(Integer.parseInt(jobj.optString("Expected Rent")));

                    String pic1=jobj.optString("roomPhoto");
                    byte [] encodeByte1=Base64.decode(pic1,Base64.DEFAULT);
                    Bitmap bitmap1=BitmapFactory.decodeByteArray(encodeByte1, 0, encodeByte1.length);
                    img.add(bitmap1);

                    String pic2=jobj.optString("profilePhoto");
                    byte [] encodeByte2=Base64.decode(pic2,Base64.DEFAULT);
                    Bitmap bitmap2=BitmapFactory.decodeByteArray(encodeByte2, 0, encodeByte2.length);
                    dp.add(bitmap2);
                    //Log.e("msg",Name.get(0));
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            return result.toString();
        }
    }

    class sortByRent extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Find.this);
            progressDialog.setMessage("Loading data...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ListView listView = (ListView) findViewById(R.id.listView);

            arrayList.clear();
            for(int i=0;i<Name.size();i++)
            {
                PersonDetails pd=new PersonDetails(Name.get(i),Address.get(i),City.get(i),Rent.get(i),img.get(i),dp.get(i));
                arrayList.add(pd);
            }
            CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(),arrayList );
            listView.setAdapter(customAdapter);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return sortByRent(params[0]);
            } catch (IOException ex) {
                return "Network Error...";
            }
        }

        private String sortByRent(String urlPath) throws IOException {
            String json = "";
            JSONObject jobj;

            StringBuilder result = new StringBuilder();
            BufferedReader bufferedReader = null;
            StringBuilder builder;
            try {

                System.out.println("before connect");
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(20000);
                urlConnection.setConnectTimeout(20000);
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
            try {
                System.out.println("adding");
                JSONArray jsonArray=new JSONArray(json);
                Name.clear();
                Address.clear();
                City.clear();
                Rent.clear();
                img.clear();
                dp.clear();

                for(int i=0;i<jsonArray.length();i++) {
                    jobj = jsonArray.getJSONObject(i);
                    // Storing  JSON item in a Variable

                    Name.add(jobj.optString("Name"));
                    Address.add(jobj.optString("Address"));
                    City.add(jobj.optString("City"));
                    Rent.add(Integer.parseInt(jobj.optString("Expected Rent")));

                    String pic1=jobj.optString("roomPhoto");
                    byte [] encodeByte1=Base64.decode(pic1,Base64.DEFAULT);
                    Bitmap bitmap1=BitmapFactory.decodeByteArray(encodeByte1, 0, encodeByte1.length);
                    img.add(bitmap1);

                    String pic2=jobj.optString("profilePhoto");
                    byte [] encodeByte2=Base64.decode(pic2,Base64.DEFAULT);
                    Bitmap bitmap2=BitmapFactory.decodeByteArray(encodeByte2, 0, encodeByte2.length);
                    dp.add(bitmap2);
                    //  Log.e("msg",bitmap.toString());
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }

            return result.toString();
        }
    }

    class sortByRentDesc extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Find.this);
            progressDialog.setMessage("Loading data...");
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            ListView listView = (ListView) findViewById(R.id.listView);

            arrayList.clear();
            for(int i=0;i<Name.size();i++)
            {
                PersonDetails pd=new PersonDetails(Name.get(i),Address.get(i),City.get(i),Rent.get(i),img.get(i),dp.get(i));
                arrayList.add(pd);
            }
            CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), arrayList);
            listView.setAdapter(customAdapter);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return sortByRent(params[0]);
            } catch (IOException ex) {
                return "Network Error...";
            }
        }

        private String sortByRent(String urlPath) throws IOException {
            String json = "";
            JSONObject jobj;

            StringBuilder result = new StringBuilder();
            BufferedReader bufferedReader = null;
            StringBuilder builder;
            try {

                System.out.println("before connect");
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(20000);
                urlConnection.setConnectTimeout(20000);
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
            try {
                System.out.println("adding");
                JSONArray jsonArray=new JSONArray(json);
                Name.clear();
                Address.clear();
                City.clear();
                Rent.clear();
                img.clear();
                dp.clear();

                for(int i=0;i<jsonArray.length();i++) {
                    jobj = jsonArray.getJSONObject(i);
                    // Storing  JSON item in a Variable

                    Name.add(jobj.optString("Name"));
                    Address.add(jobj.optString("Address"));
                    City.add(jobj.optString("City"));
                    Rent.add(Integer.parseInt(jobj.optString("Expected Rent")));

                    String pic1=jobj.optString("roomPhoto");
                    byte [] encodeByte1=Base64.decode(pic1,Base64.DEFAULT);
                    Bitmap bitmap1=BitmapFactory.decodeByteArray(encodeByte1, 0, encodeByte1.length);
                    img.add(bitmap1);

                    String pic2=jobj.optString("profilePhoto");
                    byte [] encodeByte2=Base64.decode(pic2,Base64.DEFAULT);
                    Bitmap bitmap2=BitmapFactory.decodeByteArray(encodeByte2, 0, encodeByte2.length);
                    dp.add(bitmap2);
                    //  Log.e("msg",bitmap.toString());
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }

            return result.toString();
        }
    }

}