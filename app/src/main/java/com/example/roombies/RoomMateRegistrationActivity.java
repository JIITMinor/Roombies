package com.example.roombies;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionProvider;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RoomMateRegistrationActivity extends AppCompatActivity {
    TextView mResult;
    public String PetsResponse, AlcoholicResponse, VisitorsAllowedResponse;
    EditText Name, Age, Gender, Profession, Phone, Address, City, Email;
    EditText GenderRoomMate, AgeRoomMate, Nationality, ProfessionRoomMate, WorkingHours;
    CheckBox PetsAllowedYes, PetsAllowedNo, AlcoholicYes, AlcoholicNo, VisitorsAllowedYes, VisitorsAllowedNo;
    Button UpdateDetailsRoomMate;

     final int CAMERA_CAPTURE = 1;
     private Uri picUri;
     final int PIC_CROP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_mate_registration);
        Button captureBtn = (Button)findViewById(R.id.capture_btn);
        Name = (EditText) findViewById(R.id.etName);
        Age = (EditText) findViewById(R.id.etAge);
        Gender = (EditText) findViewById(R.id.etGender);
        Profession = (EditText) findViewById(R.id.etProfession);
        Phone = (EditText) findViewById(R.id.etPhone);
        Address = (EditText) findViewById(R.id.etAddress);
        City = (EditText) findViewById(R.id.etCity);
        mResult = (TextView) findViewById(R.id.tv_result);
        //-----------------------room mate details----------------------------
        GenderRoomMate = (EditText) findViewById(R.id.etGenderRoomMate);
        AgeRoomMate = (EditText) findViewById(R.id.etAgeRoomMate);
        Nationality = (EditText) findViewById(R.id.etNationality);
        ProfessionRoomMate = (EditText) findViewById(R.id.etProfessionRoomMate);
        WorkingHours = (EditText) findViewById(R.id.etWorkingHours);
        Email = (EditText) findViewById(R.id.etEmail);
        //--------------------- CHECKBOX DETAILS------------------------------
        PetsAllowedYes = (CheckBox) findViewById(R.id.etPetsAllowedYes);
        PetsAllowedNo = (CheckBox) findViewById(R.id.etPetsAllowedNo);
        AlcoholicYes = (CheckBox) findViewById(R.id.etAlcoholicYes);
        AlcoholicNo = (CheckBox) findViewById(R.id.etAlcoholicNo);
        VisitorsAllowedYes = (CheckBox) findViewById(R.id.etVisitorsAllowedYes);
        VisitorsAllowedNo = (CheckBox) findViewById(R.id.etVisitorsAllowedNo);
        UpdateDetailsRoomMate = (Button) findViewById(R.id.etUpdateDetailsRoomMate);
        //-------------------------------PETS ALLOWED RESPONSE---------------------
        PetsAllowedYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PetsResponse = "Yes";
            }
        });
        PetsAllowedNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PetsResponse = "No";
            }
        });
        //---------------------------ALOCHOLIC RESPONSE---------------------------
        AlcoholicYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlcoholicResponse = "Yes";
            }
        });
        AlcoholicNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlcoholicResponse = "No";
            }
        });
        //-----------------------VISITORS RESPONSE-------------------------------
        VisitorsAllowedYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisitorsAllowedResponse = "Yes";
            }
        });
        VisitorsAllowedNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VisitorsAllowedResponse = "No";
            }
        });
        captureBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.capture_btn) {
                    try {
                        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(captureIntent,CAMERA_CAPTURE);
                    }
                    catch(ActivityNotFoundException anfe){
                        String errorMessage = "Your device doesn't support capturing images!";
                        Toast toast = Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                }
            }
        });
        UpdateDetailsRoomMate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new postRoomMateDetails().execute("http://192.168.43.75:5000/saveRoomMateDetails");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if(requestCode == CAMERA_CAPTURE){
                    picUri = data.getData();
                    performCrop1();

            }
            else if(requestCode == PIC_CROP){
                Bundle extras = data.getExtras();
                Bitmap thePic = extras.getParcelable("data");
                ImageView picView = (ImageView) findViewById(R.id.picture);
                picView.setImageBitmap(thePic);
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

        class postRoomMateDetails extends AsyncTask<String, Void, String> {
            ProgressDialog progressDialog;
            final String NameToSend = Name.getText().toString();
            final int AgeToSend = Integer.parseInt(Age.getText().toString());
            final String GenderToSend = Gender.getText().toString();
            final String ProfessionToSend = Profession.getText().toString();
            final String PhoneToSend = Phone.getText().toString();
            final String AddressToSend = Address.getText().toString();
            final String CityToSend = City.getText().toString();
            final String EmailToSend = Email.getText().toString();
            //-------------------------ROOM MATE DETAILS---------------------
            final int AgeRoomMateToSend = Integer.parseInt(AgeRoomMate.getText().toString());
            final String GenderRoomMateToSend = GenderRoomMate.getText().toString();
            final String NationalityToSend = Nationality.getText().toString();
            final String ProfessionRoomMateToSend = ProfessionRoomMate.getText().toString();
            final String WorkingHoursRoomMateToSend = WorkingHours.getText().toString();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new ProgressDialog(RoomMateRegistrationActivity.this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    return postRoomMate(params[0]);
                } catch (IOException ex) {
                    return "Network Error!!";
                } catch (JSONException ex) {
                    return "Data Invalid !";
                }
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Log.d("resultKyaAaya", result.toString());
                if (result.contains("true")) {
                    //Log.d("Successfully login !! ",result);
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(RoomMateRegistrationActivity.this, Navigation.class);
                    i.putExtra("email", EmailToSend);
                    startActivity(i);
                    mResult.setText("room Register ho gya :" + result);
                    //alert_call(result);
                } else {
                    //Log.d("Failed login !! ",result);
                    mResult.setText("room Register nhi hua : " + result);
                    Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();
                    Intent mainintentback = new Intent(RoomMateRegistrationActivity.this, RoomMateRegistrationActivity.class);
                    startActivity(mainintentback);

                }
                if (progressDialog != null)
                    progressDialog.dismiss();
            }

            private String postRoomMate(String urlPath) throws IOException, JSONException {
                StringBuilder result = new StringBuilder();
                BufferedWriter bufferedWriter = null;
                BufferedReader bufferedReader = null;
                StringBuilder builder;
                try {
                    JSONObject dataToSend = new JSONObject();
                    dataToSend.put("Name", NameToSend);
                    dataToSend.put("Age", AgeToSend);
                    dataToSend.put("Gender", GenderToSend);
                    dataToSend.put("Profession", ProfessionToSend);
                    dataToSend.put("Phone", PhoneToSend);
                    dataToSend.put("Address", AddressToSend);
                    dataToSend.put("City", CityToSend);
                    dataToSend.put("email", EmailToSend);
                    //--------------------------------ROOM MATE DETAILS---------------------
                    dataToSend.put("GenderRoomMate", GenderRoomMateToSend);
                    dataToSend.put("AgeRoomMate", AgeRoomMateToSend);
                    dataToSend.put("NationalityRoomMate", NationalityToSend);
                    dataToSend.put("ProfessionRoomMate", ProfessionRoomMateToSend);
                    dataToSend.put("WorkingHoursRoomMate", WorkingHoursRoomMateToSend);
                    //-------------------------------CheckBoxes Reponses--------------------
                    dataToSend.put("Pets Allowed", PetsResponse);
                    dataToSend.put("Alcoholic", AlcoholicResponse);
                    dataToSend.put("visitors Allowed", VisitorsAllowedResponse);
                    URL url = new URL(urlPath);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setReadTimeout(10000);
                    urlConnection.setConnectTimeout(10000);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoInput(true);
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.connect();

                    //Writing data to server
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
                    builder = new StringBuilder();
                    builder.append(urlConnection.getResponseCode());
                    Log.d("not in finally", result.toString());
                } finally {
                    if (bufferedReader != null)
                        bufferedReader.close();
                    if (bufferedWriter != null)
                        bufferedWriter.close();
                    Log.d("in finally", result.toString());
                }
                return result.toString();
            }
        }


    }

