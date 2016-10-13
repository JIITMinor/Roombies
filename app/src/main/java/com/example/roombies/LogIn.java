package com.example.roombies;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogIn extends AppCompatActivity {
    TextView mResult;
    public Button b;
    TextView roombutton;
    EditText useremail;
    public String md5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        b=(Button)findViewById(R.id.SignUpbutton);
        roombutton=(TextView) findViewById(R.id.etRegisterRoom);
        roombutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RoomButtonIntent=new Intent(LogIn.this,RoomRegistrationActivity.class);
                startActivity(RoomButtonIntent);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
@Override
    public void onClick(View view) {
        Intent i=new Intent(LogIn.this,MainActivity.class);
        startActivity(i);
    }


});

        useremail = (EditText) findViewById(R.id.email);
        final EditText userpassword = (EditText) findViewById(R.id.password);
        final Button login = (Button) findViewById(R.id.LogIn);
        mResult=(TextView) findViewById(R.id.tv_result2);
        if (login != null) {
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!validateEmail(useremail.getText().toString())) {
                        useremail.setError("Invalid Email");
                        useremail.requestFocus();

                    } else if (!validatePassword(userpassword.getText().toString())) {
                        userpassword.setError("Invalid Password");
                        userpassword.requestFocus();

                    } else {
                        final String email_to_check=useremail.getText().toString();
                        final String password_to_check=userpassword.getText().toString();
                        final String password_encrypted_to_check=md5(password_to_check);
                        Log.d("encrypted password",password_encrypted_to_check);
                        Log.d("email to check",email_to_check);
                        new GetDataTask().execute("http://192.168.43.75:5000/login?email="+email_to_check+"&password="+password_encrypted_to_check);
                    }

                }
            });
        }
    }
    private boolean validatePassword(String password) {
        if (password != null && password.length() >= 8) {
            return true;
        } else {
            return false;
        }
    }

    private boolean validateEmail(String email) {
        String emailpattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(emailpattern);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();

    }

    class GetDataTask extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(LogIn.this);
            progressDialog.setMessage("Loading data...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return getData(params[0]);
            } catch (IOException ex) {
                return "Network Error...";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
           // Log.d("result kya aaya",result);
           if(result.contains("true")){
               //Log.d("Successfully login !! ",result);
               mResult.setText("Login ho gya :"+result);
               Intent i=new Intent(getApplicationContext(),Find.class);
               startActivity(i);
           }
            else
           {
               //Log.d("Failed login !! ",result);
               mResult.setText("Login nhi hua : "+result);

           }
            System.out.println("here is life!!");
            Log.d("vsh","idea");
          //  mResult.setText("Login: "+result);
            //Log.d("Mresult",mResult.toString());
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
        private String getData(String urlPath) throws IOException{
            StringBuilder result=new StringBuilder();
            BufferedReader bufferedReader=null;
            StringBuilder builder;
            try {
                URL url=new URL(urlPath);
                HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.connect();

                InputStream inputStream=urlConnection.getInputStream();
                bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line=bufferedReader.readLine())!=null) {
                    result.append(line).append("\n");
                }
                builder = new StringBuilder();
                builder.append(urlConnection.getResponseCode());
                //Log.d("naksh",builder.toString());
            } finally{
                if(bufferedReader !=null){
                    bufferedReader.close();
                }
            }
            //Log.d("result from login",result.toString());
            //Log.d("mresult",mResult.toString());
            return result.toString();
        }
    }

}
