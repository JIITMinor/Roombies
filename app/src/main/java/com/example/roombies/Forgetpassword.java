package com.example.roombies;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import java.util.Date;

public class Forgetpassword extends AppCompatActivity {

    EditText Password,ConfirmPassword;
    Button SubmitButton,CancelButton;
    String Email;
    int Min=1000;
    int Max=9999;
    int otp=Min + (int)(Math.random() * ((Max - Min) + 1));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getIntent().getExtras();
        Email=bundle.getString("email");
        System.out.println("email is:"+Email);
        setContentView(R.layout.activity_forgetpassword);
        Password=(EditText)findViewById(R.id.etPassword1);
        ConfirmPassword=(EditText)findViewById(R.id.etConfirmPassword1);
        SubmitButton=(Button)findViewById(R.id.etSubmitButton);
        CancelButton=(Button)findViewById(R.id.etCancelButton);
        SubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PutDataTask().execute("http://192.168.43.227:5000/UpdatePassword");
            }
        });
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent BackIntent=new Intent(Forgetpassword.this,LogIn.class);
                startActivity(BackIntent);
            }
        });
    }
    public String md5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i)
            {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
    class PutDataTask extends AsyncTask<String,Void,String>{
        ProgressDialog progressDialog;
        final String password1=Password.getText().toString();
        final String password2=md5(password1);
        final String otpdb=Integer.toString(otp);
        final String ConfirmPassword1=ConfirmPassword.getText().toString();
        final String ConfirmPassword2=md5(ConfirmPassword1);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(Forgetpassword.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
            //progressDialog.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return PutData(params[0]);
            } catch (IOException ex) {
                Toast.makeText(getApplicationContext(),"Network Error...", Toast.LENGTH_LONG).show();
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
            if(result.contains("true")) {
                Intent ProfileIntent=new Intent(Forgetpassword.this,captcha.class);
                startActivity(ProfileIntent);
            }
            else{
                Toast.makeText(getApplicationContext(),"Try Again", Toast.LENGTH_LONG).show();
            }
        }
        private String PutData(String urlPath) throws IOException,JSONException{
            StringBuilder result=new StringBuilder();
            BufferedWriter bufferedWriter=null;
            BufferedReader bufferedReader=null;
            try {
                //Creating Data TO update
                JSONObject dataToSend = new JSONObject();
                //Check here
                dataToSend.put("email",Email);
                dataToSend.put("password", password2);
                dataToSend.put("otp",otpdb);
                //Connecting To The Server
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                //Now Writing Data To Server
                OutputStream outputStream = urlConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(dataToSend.toString());
                bufferedWriter.flush();

                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line).append("\n");
                }
            }finally {
                if(bufferedReader!=null)
                    bufferedReader.close();
                if(bufferedWriter!=null)
                    bufferedWriter.close();
            }
            return result.toString();
        }
    }

}
