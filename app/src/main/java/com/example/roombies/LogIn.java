package com.example.roombies;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class LogIn extends AppCompatActivity implements AsyncResponse{
    TextView mResult,etotp;
    public Button b,forget;
    TextView roombutton;
    EditText useremail;
    int Min=1000;
    int Max=9999;
    int otp=Min + (int)(Math.random() * ((Max - Min) + 1));
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
        useremail = (EditText) findViewById(R.id.email);
        if( getIntent().getBooleanExtra("Exit me", false)){
            finish();
            return; // add this to prevent from doing unnecessary stuffs
        }


        roombutton=(TextView) findViewById(R.id.etRegisterRoom);
        roombutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RoomButtonIntent=new Intent(LogIn.this,RoomRegistrationActivity.class);
                startActivity(RoomButtonIntent);
            }
        });
        forget=(Button)findViewById(R.id.forgotbutton) ;
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateEmail(useremail.getText().toString())) {
                    useremail.setError("Invalid Email");
                    useremail.requestFocus();

                }
                 else{
                String mailData = useremail.getText().toString().trim();
                String subject = "Roombies Password reset";
                String message = "Looks like you'd like to change your Roombies password.\n" + " Please enter the otp: "
                        + otp + " to do so.\n" + "Please disregard this e-mail if you did not request a password reset.\n" +
                        "Cheers,\n" +
                        "The Roombies Team";

                Mail sm = new Mail(LogIn.this, mailData, subject, message, LogIn.this);
                //sm.del = LogIn.this;
                sm.execute();
            }
               // Intent it=new Intent(LogIn.this,Forgetpassword.class);
                //startActivity(it);
            }
        });



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
                        SharedPreferences.Editor editor = getSharedPreferences("MY_PREF",MODE_PRIVATE).edit();
                        editor.putString("email",email_to_check);
                        editor.commit();

                        new GetDataTask().execute("http://192.168.43.227:5000/login?email="+email_to_check+"&password="+password_encrypted_to_check);
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
               SharedPreferences sp=getApplicationContext().getSharedPreferences("Login",0);
               SharedPreferences.Editor spe=sp.edit();
               final String email_to_check=useremail.getText().toString();
               spe.putString("Email",email_to_check);
               Intent i=new Intent(getApplicationContext(),captcha.class);
               i.putExtra("email",useremail.getText().toString());
               startActivity(i);
               finish();
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
    @Override
    public void inputotp() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(LogIn.this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialogbox, null);
        dialog.setView(dialogView);
        dialog.show();
        etotp=(EditText) dialogView.findViewById(R.id.etotp);
        final String email1=useremail.getText().toString();
        System.out.println("email is:"+useremail);
        /*final String password1=password.getText().toString();
        final String password2=md5(password1);

        Button submitOtp = (Button) dialogView.findViewById(R.id.submitotp);
        Button cancelOtp = (Button) dialogView.findViewById(R.id.cancelotp);
        submitOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String enterotp=(etotp.getText().toString());

                //Toast.makeText(MainActivity.this, "otp is:"+enterotp, Toast.LENGTH_LONG).show();
                new GetOtpVerify().execute("http://192.168.43.228:5000/verifyotp?email="+email1+"&password="+password2+"&otp="+enterotp);
                //startActivity(new Intent(MainActivity.this,SignUp.class));
            }
        });
        cancelOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"You Have Entered Incorrect Otp", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this,MainActivity.class));
            }
        });*/
    }

}
class Mail extends AsyncTask<Void,Void,Void> {
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
    public Mail(Context context, String email, String subject, String message,AsyncResponse asyncResponse){
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
        //Toast.makeText(LogIn.this, "Wrong Captcha Entered.", Toast.LENGTH_LONG).show();
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
            mm.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(email));
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


