package com.example.roombies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaCodec;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Debug;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements AsyncResponse {
    TextView mResult;
    EditText email,password;
    Button signup,b;
    int Min=1000;
    int Max=9999;
    //editTextEmail = (EditText) findViewById(R.id.email);

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    // private EditText editTextEmail=(EditText) findViewById(R.id.email);
    private GoogleApiClient client;
    //EditText email;
    private TextView info,etotp;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    int otp=Min + (int)(Math.random() * ((Max - Min) + 1));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();


        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        setContentView(R.layout.activity_main);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        signup = (Button) findViewById(R.id.SignUp);
        mResult=(TextView) findViewById(R.id.tv_result1);

        loginButton = (LoginButton) findViewById(R.id.login_button);
            b=(Button)findViewById(R.id.Loginbutton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,LogIn.class);
                startActivity(i);
            //    finish();
            }

        });
        SharedPreferences sp=getApplicationContext().getSharedPreferences("Login",0);
        int value=sp.getInt("Login",0);


        if(value==1)
        {
            Intent i=new Intent(MainActivity.this,Main2Activity.class);
            startActivity(i);
        //    finish();

        }
        final int flag=0;
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateEmail(email.getText().toString())) {
                    email.setError("Invalid Email");
                    email.requestFocus();

                } else if (!validatePassword(password.getText().toString())) {
                    password.setError("Invalid Password");
                    password.requestFocus();

                } else {
                    //192.168.43.75
                    //new sendNotification().execute("http://192.168.43.228:5000/notify");
                    new postDataTask().execute("http://192.168.43.227:5000/register");
                    //new postDataTask().execute("http://192.168.0.104:1000/api/status");

                }

            }
        });


        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email", "user_birthday","user_location","user_hometown","user_photos"));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(final LoginResult loginResult) {
                Toast.makeText(getApplicationContext(),loginResult.getAccessToken().getToken(),Toast.LENGTH_SHORT).show();

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Intent intent=new Intent(getApplicationContext(),SignUp.class);
                        try {
                            Log.d("values",object.toString());
                            intent.putExtra("email",object.getString("email"));
                            intent.putExtra("name",object.getString("name"));
                            intent.putExtra("gender",object.getString("gender"));
                            intent.putExtra("id",object.getString("id"));

                            JSONObject location=object.getJSONObject("location");
                            JSONObject location2=location.getJSONObject("location");

                            intent.putExtra("city",location2.getString("city"));
                            intent.putExtra("street",location2.getString("street"));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        startActivity(intent);
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,gender,birthday,email,picture,location{location},verified");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this,"Login attempt canceled.",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {

                Toast.makeText(MainActivity.this,"Login attempt failed.",Toast.LENGTH_SHORT).show();
            }
        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent data){
        callbackManager.onActivityResult(requestCode, resultCode, data);
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


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.roombies/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.roombies/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    /* public void onButtonClick(View v)
     {
         Button button=(Button) v;
         startActivity(new Intent(getApplicationContext(),SignUp.class));

     }*/
    //alert box after successful registration
    /*void alert_call(String res){
        if(res.equals("201")) {
            Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_LONG).show();
            Intent mainintent=new Intent(MainActivity.this,SignUp.class);
            startActivity(mainintent);
        }
        else{
            Toast.makeText(getApplicationContext(),"Try Again", Toast.LENGTH_LONG).show();
            Intent mainintentback=new Intent(MainActivity.this,MainActivity.class);
            startActivity(mainintentback);
        }
    }*/

    @Override
    public void inputotp() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialogbox, null);
        dialog.setView(dialogView);
        dialog.show();
        etotp=(EditText) dialogView.findViewById(R.id.etotp);
        final String email1=email.getText().toString();
        final String password1=password.getText().toString();
        final String password2=md5(password1);

        Button submitOtp = (Button) dialogView.findViewById(R.id.submitotp);
        Button cancelOtp = (Button) dialogView.findViewById(R.id.cancelotp);
        submitOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String enterotp=(etotp.getText().toString());

                //Toast.makeText(MainActivity.this, "otp is:"+enterotp, Toast.LENGTH_LONG).show();
                new GetOtpVerify().execute("http://192.168.43.227:5000/verifyotp?email="+email1+"&password="+password2+"&otp="+enterotp);
                //startActivity(new Intent(MainActivity.this,SignUp.class));
            }
        });
        cancelOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"You Have Entered Incorrect Otp", Toast.LENGTH_LONG).show();
              startActivity(new Intent(MainActivity.this,MainActivity.class));
            }
        });
    }

    //password encryption using md5
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
    //adding data to database
    class postDataTask extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;
        final String email1=email.getText().toString();
        final String password2=password.getText().toString();
        final String password1=md5(password2);
        final String otpdb=Integer.toString(otp);
        //final int age=Integer.parseInt(etAge.getText().toString());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog=new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
            //progressDialog.dismiss();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                return postData(params[0]);
            } catch (IOException ex) {
                return "Network Error!!";
            } catch (JSONException ex) {
                return "Data Invalid !";
            }
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //mResult.setText(result);
            if(progressDialog!=null)
            {
                progressDialog.dismiss();
            }
            if(result.contains("true"))
            {
               /* Log.d("key",result);
                Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_LONG).show();
                Intent mainintent=new Intent(MainActivity.this,SignUp.class);
                startActivity(mainintent);
                mResult.setText("Register ho gya :"+result);
                //alert_call(result);*/


                String mailData = email.getText().toString().trim();
                String subject = "Sign Up Verification from Roombies";
                String message = "Thank you for registering in Roombies we will notifiy you as soon as we find a match as per your requirements\n" +
                        "Stay Connected. Happy living\nYour one time password is "+otp+" verify it to continue.";

                SendMail sm = new SendMail(MainActivity.this, mailData, subject, message,MainActivity.this);
                sm.del = MainActivity.this;
                sm.execute();
            }
            else
            {
                //Log.d("Failed login !! ",result);
                mResult.setText("Register nhi hua : "+result);
                Toast.makeText(getApplicationContext(),"Try Again", Toast.LENGTH_LONG).show();
                Intent mainintentback=new Intent(MainActivity.this,MainActivity.class);
                startActivity(mainintentback);


            }
            //if(progressDialog!=null)
               // progressDialog.dismiss();
            //alert_call(result);
        }

        private String postData(String urlPath) throws IOException,JSONException{
            StringBuilder result =new StringBuilder();
            BufferedWriter bufferedWriter=null;
            BufferedReader bufferedReader=null;
            StringBuilder builder;
            try{
                JSONObject dataToSend=new JSONObject();
                dataToSend.put("email",email1);
                dataToSend.put("password",password1);
                dataToSend.put("otp",otpdb);
                //building connection to the server
                URL url=new URL(urlPath);
                HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.connect();

                //Writing data to server
                OutputStream outputStream=urlConnection.getOutputStream();
                bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(dataToSend.toString());
                bufferedWriter.flush();

                //read data response from server
                InputStream inputStream=urlConnection.getInputStream();
                bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line=bufferedReader.readLine())!=null){
                    result.append(line).append("\n");
                }
                builder = new StringBuilder();
                builder.append(urlConnection.getResponseCode());
                Log.d("not in finally",result.toString());
            }finally {
                if(bufferedReader!=null)
                    bufferedReader.close();
                if(bufferedWriter!=null)
                    bufferedWriter.close();
                Log.d("in finally",result.toString());
            }
            return result.toString();
        }
    }
    class sendNotification extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;
        final String message = "hello";
        final String deviceid = FirebaseInstanceId.getInstance().getToken();


        //final int age=Integer.parseInt(etAge.getText().toString());
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog=new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
            //progressDialog.dismiss();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                return notify(params[0]);
            } catch (IOException ex) {
                return "Network Error!!";
            } catch (JSONException ex) {
                return "Data Invalid !";
            }
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //mResult.setText(result);
            if(progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }
            if(result.contains("true"))
            {
               /* Log.d("key",result);
                Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_LONG).show();
                Intent mainintent=new Intent(MainActivity.this,SignUp.class);
                startActivity(mainintent);
                mResult.setText("Register ho gya :"+result);
                //alert_call(result);*/



            }
            else
            {

                mResult.setText("Register nhi hua : "+result);
                Toast.makeText(getApplicationContext(),"Try Again", Toast.LENGTH_LONG).show();
                Intent mainintentback=new Intent(MainActivity.this,MainActivity.class);
                startActivity(mainintentback);


            }
            //if(progressDialog!=null)
            // progressDialog.dismiss();
            //alert_call(result);
        }

        private String notify(String urlPath) throws IOException,JSONException{
            StringBuilder result =new StringBuilder();
            BufferedWriter bufferedWriter=null;
            BufferedReader bufferedReader=null;
            StringBuilder builder;
            try{
                JSONObject dataToSend=new JSONObject();
                dataToSend.put("message",message);
                Log.d("Failed login !! ",deviceid);
                dataToSend.put("deviceid",deviceid);
                //building connection to the server
                URL url=new URL(urlPath);
                HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Type","application/json");
                urlConnection.connect();

                //Writing data to server
                OutputStream outputStream=urlConnection.getOutputStream();
                bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(dataToSend.toString());
                bufferedWriter.flush();

                //read data response from server
                InputStream inputStream=urlConnection.getInputStream();
                bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line=bufferedReader.readLine())!=null){
                    result.append(line).append("\n");
                }
                builder = new StringBuilder();
                builder.append(urlConnection.getResponseCode());
                Log.d("not in finally",result.toString());
            }finally {
                if(bufferedReader!=null)
                    bufferedReader.close();
                if(bufferedWriter!=null)
                    bufferedWriter.close();
                Log.d("in finally",result.toString());
            }
            return result.toString();
        }
    }
    class GetOtpVerify extends AsyncTask<String,Void,String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Please Wait While We Verify You!!");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                return OtpVerify(params[0]);
            }catch(IOException ex){
                return "Network Error!!";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result.contains("true"))
            {
                new sendNotification().execute("http://192.168.43.227:5000/notify");

                Intent otpVerify=new Intent(MainActivity.this,SignUp.class);
                startActivity(otpVerify);
         //       finish();
            }
            else{
                Intent otpverifyWrong=new Intent(MainActivity.this,MainActivity.class);
                startActivity(otpverifyWrong);
            }
            if(progressDialog!=null)
                progressDialog.dismiss();
        }
        private String OtpVerify(String urlPath) throws IOException{
            StringBuilder result=new StringBuilder();
            BufferedReader bufferedReader=null;
            StringBuilder builder;
            try{
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
                while((line=bufferedReader.readLine())!=null)
                {
                    result.append(line).append("\n");
                }
                builder=new StringBuilder();
                builder.append(urlConnection.getResponseCode());
            }finally{
                if(bufferedReader!=null)
                    bufferedReader.close();
            }
            return result.toString();
        }
    }
}

//sending mail class
class SendMail extends AsyncTask<Void,Void,Void> {
    public AsyncResponse del=null;
    //Declaring Variables
    private Context context;
    private Session session;

    //Information to send email
    private String email;
    private String subject;
    private String message;

    //Progressdialog to show while sending email
    private ProgressDialog progressDialog;

    //Class Constructor
    public SendMail(Context context, String email, String subject, String message,AsyncResponse asyncResponse){
        //Initializing variables
        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.del = asyncResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Showing progress dialog while sending email
        progressDialog = ProgressDialog.show(context,"Sending Confirmation","Please wait...",false,false);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Dismissing the progress dialog
        progressDialog.dismiss();
        del.inputotp();

        //Showing a success messagetreu

    }

    @Override
    protected Void doInBackground(Void... params) {
        //Creating properties
        Properties props = new Properties();

        //Configuring properties for gmail
        //If you are not using gmail you may need to change the values
        /*props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");*/

        props.setProperty("mail.smtp.host", "smtp.gmail.com");
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.port", "465");
        props.setProperty("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "true");
        props.put("mail.store.protocol", "pop3");
        props.put("mail.transport.protocol", "smtp");

        //Creating a new session
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    //Authenticating the password
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(config.EMAIL,config.PASSWORD);
                    }
                });

        try {
            //Creating MimeMessage object
            MimeMessage mm = new MimeMessage(session);

            //Setting sender address
            mm.setFrom(new InternetAddress(config.EMAIL));
            //Adding receiver
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            //Adding subject
            mm.setSubject(subject);
            //Adding message
            mm.setText(message);

            //Sending email
            Transport.send(mm);

        } catch (Exception e) {
            Log.d("catched",""+e);
            e.printStackTrace();
        }
        return null;
    }
}

