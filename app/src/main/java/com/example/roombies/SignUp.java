package com.example.roombies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class SignUp extends AppCompatActivity {
    ImageButton FindRoom,FindRoommate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        FindRoom=(ImageButton) findViewById(R.id.FillRoomMateDetails);
        FindRoommate=(ImageButton) findViewById(R.id.FillRoomDetails);
        FindRoommate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent FindRMate=new Intent(getApplicationContext(),RoomRegistrationActivity.class);
                startActivity(FindRMate);
            }
        });
        FindRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent FindR=new Intent(getApplicationContext(),RoomMateRegistrationActivity.class);
                startActivity(FindR);
            }
        });

    }

}
