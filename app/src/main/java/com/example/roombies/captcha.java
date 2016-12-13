package com.example.roombies;

        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.graphics.drawable.Drawable;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
        import android.widget.Toast;
        import com.cooltechworks.views.ScratchImageView;

public class captcha extends AppCompatActivity {

    Button button;
    RadioButton giraffe,penguine,elephant,kangroo,tiger;
    RadioGroup radioGroup;
    ScratchImageView scratchImageView;
    int xx;
    String email;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captcha);

        Bundle data=getIntent().getExtras();
        email=data.getString("email");

        button = (Button) findViewById(R.id.button);
        scratchImageView = (ScratchImageView) findViewById(R.id.scratchView1);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        giraffe = (RadioButton) findViewById(R.id.Giraffe);
        penguine = (RadioButton) findViewById(R.id.Penguin);
        elephant = (RadioButton) findViewById(R.id.Elephant);
        kangroo = (RadioButton) findViewById(R.id.Kangroo);
        tiger = (RadioButton) findViewById(R.id.Tiger);
        int Max=5;
        int Min=1;
        xx= Min + (int)(Math.random() * ((Max - Min) + 1));
        if(xx==1)
        {
            scratchImageView.setImageResource(R.drawable.elephants);
        }
        else if(xx==2)
        {
            scratchImageView.setImageResource(R.drawable.giraffe);
        }
        else if(xx==3)
        {
            scratchImageView.setImageResource(R.drawable.kangaroo);
        }
        else if(xx==4)
        {
            scratchImageView.setImageResource(R.drawable.penguine);
        }
        else
        {
            scratchImageView.setImageResource(R.drawable.tiger);
        }

        scratchImageView.setRevealListener(new ScratchImageView.IRevealListener() {
            @Override
            public void onRevealed(ScratchImageView scratchImageView) {

                //Don't Write Any Code Here
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!scratchImageView.isRevealed()) {

                    Toast.makeText(captcha.this, "Please Scratch The Whole Image First", Toast.LENGTH_LONG).show();

                } else {

                    int id=radioGroup.getCheckedRadioButtonId();
                    if(id==R.id.Elephant && xx==1)
                    {
                        Toast.makeText(captcha.this, "You Have Entered Correct Captcha Code.", Toast.LENGTH_LONG).show();

                        SharedPreferences sp=getApplicationContext().getSharedPreferences("Login",0);
                        SharedPreferences.Editor spe=sp.edit();
                        spe.putInt("Login",1);
                        spe.commit();
                        SharedPreferences spp=getApplicationContext().getSharedPreferences("Email",0);
                        SharedPreferences.Editor sppe=spp.edit();
                        sppe.putString("Login",email);
                        sppe.commit();
                        Intent i=new Intent(getApplicationContext(),Main2Activity.class);
                        i.putExtra("email",email);
                        startActivity(i);
                        finish();
                    }
                    else if(id==R.id.Giraffe && xx==2)
                    {
                        Toast.makeText(captcha.this, "You Have Entered Correct Captcha Code.", Toast.LENGTH_LONG).show();
                        SharedPreferences sp=getApplicationContext().getSharedPreferences("Login",0);
                        SharedPreferences.Editor spe=sp.edit();
                        spe.putInt("Login",1);
                        spe.commit();
                        SharedPreferences spp=getApplicationContext().getSharedPreferences("Email",0);
                        SharedPreferences.Editor sppe=spp.edit();
                        sppe.putString("Login",email);
                        sppe.commit();

                        Intent i=new Intent(getApplicationContext(),Main2Activity.class);
                        i.putExtra("email",email);
                        startActivity(i);
                        finish();
                    }
                    else if(id==R.id.Kangroo && xx==3)
                    {
                        Toast.makeText(captcha.this, "You Have Entered Correct Captcha Code. ", Toast.LENGTH_LONG).show();
                        SharedPreferences sp=getApplicationContext().getSharedPreferences("Login",0);
                        SharedPreferences.Editor spe=sp.edit();
                        spe.putInt("Login",1);
                        spe.commit();
                        SharedPreferences spp=getApplicationContext().getSharedPreferences("Email",0);
                        SharedPreferences.Editor sppe=spp.edit();
                        sppe.putString("Login",email);
                        sppe.commit();
                        Intent i=new Intent(getApplicationContext(),Main2Activity.class);
                        i.putExtra("email",email);
                        startActivity(i);
                        finish();
                    }
                    else if(id==R.id.Penguin && xx==4)
                    {
                        Toast.makeText(captcha.this, "You Have Entered Correct Captcha Code.", Toast.LENGTH_LONG).show();
                        SharedPreferences sp=getApplicationContext().getSharedPreferences("Login",0);
                        SharedPreferences.Editor spe=sp.edit();
                        spe.putInt("Login",1);
                        spe.commit();
                        SharedPreferences spp=getApplicationContext().getSharedPreferences("Email",0);
                        SharedPreferences.Editor sppe=spp.edit();
                        sppe.putString("Login",email);
                        sppe.commit();
                        Intent i=new Intent(getApplicationContext(),Main2Activity.class);
                        i.putExtra("email",email);
                        startActivity(i);
                        finish();
                    }
                    else if(id==R.id.Tiger && xx==5)
                    {
                        Toast.makeText(captcha.this, "You Have Entered Correct Captcha Code. ", Toast.LENGTH_LONG).show();
                        SharedPreferences sp=getApplicationContext().getSharedPreferences("Login",0);
                        SharedPreferences.Editor spe=sp.edit();
                        spe.putInt("Login",1);
                        spe.commit();
                        SharedPreferences spp=getApplicationContext().getSharedPreferences("Email",0);
                        SharedPreferences.Editor sppe=spp.edit();
                        sppe.putString("Login",email);
                        sppe.commit();
                        Intent i=new Intent(getApplicationContext(),Main2Activity.class);
                        i.putExtra("email",email);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(captcha.this, "Wrong Captcha Entered.", Toast.LENGTH_LONG).show();
                        Intent i=new Intent(getApplicationContext(),LogIn.class);
                        startActivity(i);
                    }


                    /*switch (radioGroup.getCheckedRadioButtonId()) {

                        case R.id.Lollipop:
                            Toast.makeText(MainActivity.this, "Wrong Captcha Entered.", Toast.LENGTH_LONG).show();
                            break;

                        case R.id.Marshmallow:
                            Toast.makeText(MainActivity.this, "Wrong Captcha Entered.", Toast.LENGTH_LONG).show();
                            break;

                        case R.id.KitKat:
                            Toast.makeText(MainActivity.this, "You Have Entered Correct Captcha Code.", Toast.LENGTH_LONG).show();
                            break;
                    }*/
                }
            }
        });

    }
}