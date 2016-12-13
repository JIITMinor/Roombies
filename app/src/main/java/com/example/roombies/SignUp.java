package com.example.roombies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class SignUp extends AppCompatActivity {
    ImageButton FindRoom,FindRoommate;
    String email,name,gender,id,street,city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final Bundle data=getIntent().getExtras();

        if(data!=null)
        {
            name=data.getString("name");
            gender=data.getString("gender");
            email=data.getString("email");
            id=data.getString("id");
            city=data.getString("city");
            street=data.getString("street");
        }
        FindRoom=(ImageButton) findViewById(R.id.FillRoomMateDetails);
        FindRoommate=(ImageButton) findViewById(R.id.FillRoomDetails);
        FindRoommate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent FindRMate=new Intent(getApplicationContext(),RoomRegistrationActivity.class);
               if(data!=null)
                {
                    FindRMate.putExtra("email",email);
                    FindRMate.putExtra("name",name);
                    FindRMate.putExtra("gender",gender);
                    FindRMate.putExtra("id",id);
                    FindRMate.putExtra("city",city);
                    FindRMate.putExtra("street",street);
                }

                startActivity(FindRMate);
            }
        });
        FindRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent FindR=new Intent(getApplicationContext(),RoomMateRegistrationActivity.class);
                if(data!=null)
                {
                    FindR.putExtra("email",email);
                    FindR.putExtra("name",name);
                    FindR.putExtra("gender",gender);
                    FindR.putExtra("id",id);
                    FindR.putExtra("city",city);
                    FindR.putExtra("street",street);
                }
                startActivity(FindR);
            }
        });

    }

}
