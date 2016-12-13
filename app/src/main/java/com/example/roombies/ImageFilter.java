package com.example.roombies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import android.widget.ViewSwitcher.ViewFactory;
import android.app.ActionBar.LayoutParams;

public class ImageFilter extends AppCompatActivity {

    private ShakeDetector shakeDetector;
    private ImageSwitcher imageSwitcher;
    private Button button;
    final int i[]={0};
    Drawable drawable1,drawable2,drawable3;
    private BitmapDrawable bd;
    private Bitmap bmp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_filter);

        final Bitmap bitmap=this.getIntent().getParcelableExtra("photo");
        final Drawable drawable=new BitmapDrawable(bitmap);


        Toast.makeText(getApplicationContext(),"shake phone to apply filter",Toast.LENGTH_LONG).show();
        shakeDetector = new ShakeDetector(this);
        button=(Button)findViewById(R.id.button);
        imageSwitcher=(ImageSwitcher)findViewById(R.id.imageSwitcher1);
        imageSwitcher.setFactory(new ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                myView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
                return myView;
            }
        });

        imageSwitcher.setImageDrawable(drawable);
        i[0]++;

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();

                if(i[0]==1)
                {
                    intent.putExtra("returnPhoto",bitmap);
                }
                else if(i[0]==2)
                {
                    bd=(BitmapDrawable)drawable1;
                    bmp=bd.getBitmap();
                    intent.putExtra("returnPhoto",bmp);
                }
                else if(i[0]==3)
                {
                    bd=(BitmapDrawable)drawable2;
                    bmp=bd.getBitmap();
                    intent.putExtra("returnPhoto",bmp);
                }
                else if(i[0]==0)
                {
                    bd=(BitmapDrawable)drawable3;
                    bmp=bd.getBitmap();
                    intent.putExtra("returnPhoto",bmp);
                }
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        shakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            public void onShake() {
                final Vibrator vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(100);
                if(i[0]==0)
                {
                    imageSwitcher.setImageDrawable(drawable);
                    i[0]++;
                }
                else if(i[0]==1)
                {
                    drawable1=bright(imageSwitcher,bitmap);
                    i[0]++;
                }
                else if(i[0]==2)
                {
                    drawable2=gray(imageSwitcher,bitmap);
                    i[0]++;
                }
                else if(i[0]==3)
                {
                    drawable3=gama(imageSwitcher,bitmap);
                    i[0]=0;
                }
            }
        });

    }

    public Drawable gray(ImageSwitcher view,Bitmap bmp) {
        Bitmap operation = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), bmp.getConfig());
        for (int i = 0; i < bmp.getWidth(); i++) {
            for (int j = 0; j < bmp.getHeight(); j++) {
                int p = bmp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);

                r = r -5;
                g = g -5;
                b = b -5;
                alpha = alpha -5;
                operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));

            }
        }
        Drawable drawable = new BitmapDrawable(operation);
        view.setImageDrawable(drawable);
        return drawable;
    }

    public Drawable bright(ImageSwitcher view,Bitmap bmp){
        Bitmap operation= Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(),bmp.getConfig());

        for(int i=0; i<bmp.getWidth(); i++){
            for(int j=0; j<bmp.getHeight(); j++){
                int p = bmp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);
                r = 100 +  r;
                g =  100 + g;
                b = 100  + b;
                alpha = 100 + alpha;
                operation.setPixel(i, j, Color.argb(alpha, r, g, b));
            }
        }
        Drawable drawable=new BitmapDrawable(operation);
        view.setImageDrawable(drawable);
        return drawable;
    }

    public Drawable gama(ImageSwitcher view,Bitmap bmp) {
        Bitmap operation = Bitmap.createBitmap(bmp.getWidth(),bmp.getHeight(),bmp.getConfig());

        for(int i=0; i<bmp.getWidth(); i++){
            for(int j=0; j<bmp.getHeight(); j++){
                int p = bmp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);
                int alpha = Color.alpha(p);
                g =  0;
                b =  0;
                alpha=0;
                operation.setPixel(i, j, Color.argb(Color.alpha(p), r, g, b));
            }
        }
        Drawable drawable=new BitmapDrawable(operation);
        view.setImageDrawable(drawable);
        return drawable;
    }

    @Override
    public void onResume()
    {
        shakeDetector.resume();
        super.onResume();
    }
    @Override
    public void onPause()
    {
        shakeDetector.pause();
        super.onPause();
    }
}
