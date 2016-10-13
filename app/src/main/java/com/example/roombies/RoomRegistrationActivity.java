package com.example.roombies;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Base64;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RoomRegistrationActivity extends AppCompatActivity {
    public String ElectricityResponse,WaterResponse,AcResponse,WifiResponse,BalconyResponse;
    TextView mResult;
    EditText Name,Age,Gender,Profession,Address,City,Phone;
    EditText RoomSize,Sharing,WashRoom,Kitchen,Rent,Email;
    Button UpdateRegistrationDetails;
    final int CAMERA_CAPTURE = 1;
    private Uri picUri;
    final int PIC_CROP = 2;
    private Uri picUri1;
    private Bitmap Uri1;
    private Bitmap Uri2;
    int flag=0;
    //Electricity CheckBox;
    CheckBox ElectricityYes,ElectricityNo,WaterYes,WaterNo,AcYes,AcNo,WifiYes,WifiNo,BalconyYes,BalconyNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_registration);
        //-------------------PERSONAL DETAILS----------------------------
        Name=(EditText) findViewById(R.id.etName);
        Age=(EditText)findViewById(R.id.etAge);
        Gender=(EditText)findViewById(R.id.etGender);
        Profession=(EditText) findViewById(R.id.etProfession);
        Phone=(EditText)findViewById(R.id.etPhone);
        Email=(EditText)findViewById(R.id.etEmail);
        //--------------------ROOM DETAILS-------------------------------
        RoomSize=(EditText)findViewById(R.id.etRoomSize);
        Sharing=(EditText)findViewById(R.id.etSharing);
        WashRoom=(EditText)findViewById(R.id.etWashRoom);
        Kitchen=(EditText)findViewById(R.id.etKitchen);
        //----------------Electricity wala checkbox--------------------
        ElectricityYes=(CheckBox) findViewById(R.id.etElectricityYes);
        ElectricityNo=(CheckBox)findViewById(R.id.etElectricityNo);
        WaterYes=(CheckBox)findViewById(R.id.etWaterYes);
        WaterNo=(CheckBox)findViewById(R.id.etWaterNo);
        WifiYes=(CheckBox)findViewById(R.id.etWifiYes);
        WifiNo=(CheckBox)findViewById(R.id.etWifiNo);
        AcYes=(CheckBox)findViewById(R.id.etAcYes);
        AcNo=(CheckBox)findViewById(R.id.etAcNo);
        BalconyYes=(CheckBox)findViewById(R.id.etBalconyYes);
        BalconyNo=(CheckBox)findViewById(R.id.etBalconyNo);
        Address=(EditText) findViewById(R.id.etAddress);
        City=(EditText) findViewById(R.id.etCity);

        //State=(EditText)findViewById(R.id.etState);
        Rent=(EditText) findViewById(R.id.etRent);
        mResult=(TextView) findViewById(R.id.tv_result);
        Button room_capture = (Button)findViewById(R.id.room_capture);
        Button captureBtn = (Button)findViewById(R.id.capture_btn);
        UpdateRegistrationDetails=(Button) findViewById(R.id.etUpdateDetailsButton);


        //----------------------------Checking for Electricity--------------------------
        ElectricityYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ElectricityResponse="Yes";
            }
        });
        ElectricityNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ElectricityResponse="No";
            }
        });
        //---------------------------Checking for Water---------------------------------
        WaterYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WaterResponse="Yes";
            }
        });
        WaterNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WaterResponse="No";
            }
        });
        //-------------------------Checking for Ac------------------------------------
        AcYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AcResponse="Yes";
            }
        });
        AcNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AcResponse="No";
            }
        });
        //------------------------Checking for Wifi----------------------------------
        WifiYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WifiResponse="Yes";
            }
        });
        WifiNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WifiResponse="No";
            }
        });
        //---------------------checking for Balcony
        BalconyYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BalconyResponse="Yes";
            }
        });
        BalconyNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BalconyResponse="No";
            }
        });
        room_capture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.room_capture) {
                    try {
                        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        flag=1;
                        startActivityForResult(captureIntent, CAMERA_CAPTURE);
                    }
                    catch(ActivityNotFoundException anfe){
                        String errorMessage = "Your device doesn't support capturing images!";
                        Toast toast = Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });

        captureBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.capture_btn) {
                    try {
                        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        flag=2;
                        startActivityForResult(captureIntent, CAMERA_CAPTURE);
                    }
                    catch(ActivityNotFoundException anfe){
                        String errorMessage = "Your device doesn't support capturing images!";
                        Toast toast = Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });

        //--------------------------Button Click-------------------------------------
        UpdateRegistrationDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new postRoomDetails().execute("http://192.168.43.75:5000/saveRoomDetails");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if(requestCode == CAMERA_CAPTURE){
                if(flag==1) {
                    picUri = data.getData();
                    performCrop1();
                }
                else if(flag==2) {
                    picUri1 = data.getData();
                    performCrop2();
                }
            }
            else if(requestCode == PIC_CROP){
                Bundle extras = data.getExtras();
                Bitmap thePic = extras.getParcelable("data");
                ImageView picView = (ImageView) findViewById(R.id.picture);
                picView.setImageBitmap(thePic);
                if(flag==1)
                    Uri1=thePic;
                else Uri2=thePic;
            }
        }
    }

    private void performCrop1(){
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 4096);
            cropIntent.putExtra("outputY", 4096);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            String errorMessage = "Your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private void performCrop2(){
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri1, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 4096);
            cropIntent.putExtra("outputY", 4096);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException anfe){
            String errorMessage = "Your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    class postRoomDetails extends AsyncTask<String,Void,String>{
        ProgressDialog progressDialog;
        //---------------------------------------------------------------
        final String NameToSend=Name.getText().toString();
        final int AgeToSend=Integer.parseInt(Age.getText().toString());
        final String GenderToSend=Gender.getText().toString();
        final String ProfessionToSend=Profession.getText().toString();
        final String PhoneToSend=Phone.getText().toString();
        final String EmailToSend=Email.getText().toString();
        //----------------------------------------------------------------
        //------------------------ROOM DETAILS TO SEND--------------------
        final int RoomSizeToSend=Integer.parseInt(RoomSize.getText().toString());
        final int SharingToSend=Integer.parseInt(Sharing.getText().toString());
        final String WashRoomToSend=WashRoom.getText().toString();
        final String KitchenToSend=Kitchen.getText().toString();

        final String AddressToSend=Address.getText().toString();
        final String CityToSend=City.getText().toString();
        final int RentToSend=Integer.parseInt(Rent.getText().toString());
        //final int Rent=Integer.parseInt(etRet.getText().toString());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(RoomRegistrationActivity.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return postRoomData(params[0]);
            } catch (IOException ex) {
                return "Network Error!!";
            } catch (JSONException ex) {
                return "Data Invalid !";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("resultKyaAaya",result.toString());
            if(result.contains("true")){
                //Log.d("Successfully login !! ",result);
                Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_LONG).show();
                Intent i=new Intent(RoomRegistrationActivity.this,Navigation.class);
                i.putExtra("email",EmailToSend);
                startActivity(i);
                mResult.setText("room Register ho gya :"+result);
                //alert_call(result);
            }
            else
            {
                //Log.d("Failed login !! ",result);
                mResult.setText("room Register nhi hua : "+result);
                Toast.makeText(getApplicationContext(),"Try Again", Toast.LENGTH_LONG).show();
                Intent mainintentback=new Intent(RoomRegistrationActivity.this,RoomRegistrationActivity.class);
                startActivity(mainintentback);

            }
            if(progressDialog!=null)
                progressDialog.dismiss();
        }
        private String postRoomData(String urlPath) throws IOException,JSONException{
            StringBuilder result =new StringBuilder();
            BufferedWriter bufferedWriter=null;
            BufferedReader bufferedReader=null;
            StringBuilder builder;
            Bitmap Pic1ToSend=Uri1;
            Bitmap Pic2ToSend=Uri2;
            try{
                JSONObject dataToSend=new JSONObject();
                //---------------------------------------------------------------
                dataToSend.put("Name",NameToSend);
                dataToSend.put("Address",AddressToSend);
                dataToSend.put("City",CityToSend);
                dataToSend.put("Age",AgeToSend);
                dataToSend.put("Gender",GenderToSend);
                dataToSend.put("Profession",ProfessionToSend);
                dataToSend.put("Phone",PhoneToSend);
                dataToSend.put("email",EmailToSend);
                //---------------------------------------------------------------
                //-------------------Room Details--------------------------------
                dataToSend.put("RoomSize",RoomSizeToSend);
                dataToSend.put("Sharing",SharingToSend);
                dataToSend.put("WashRoom",WashRoomToSend);
                dataToSend.put("Kitchen",KitchenToSend);
                dataToSend.put("Expected Rent",RentToSend);
                //---------------------------------------------------------------
                dataToSend.put("Electricity",ElectricityResponse);
                dataToSend.put("Water",WaterResponse);
                dataToSend.put("Ac",AcResponse);
                dataToSend.put("Wifi",WifiResponse);
                dataToSend.put("Balcony",BalconyResponse);
                //dataToSend.put("state",StateToSend);


         //       dataToSend.put("Room Photo",temp);
          //      dataToSend.put("Profile Photo",Pic2ToSend.toString());
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
}
