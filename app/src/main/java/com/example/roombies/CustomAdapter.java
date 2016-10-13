package com.example.roombies;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<String>{
    ArrayList<String> name;
    ArrayList<String> place;
    ArrayList<String> city;
    int img[]={};
    int dp[]={};
    ArrayList<Integer> price;
    Context c;

    public CustomAdapter(Context context,ArrayList<String> name,ArrayList<String> place,ArrayList<String> city,ArrayList<Integer> price,int img[],int dp[])
    {
        super(context,R.layout.custom_adapter,name);

        this.name=name;
        this.place=place;
        this.city=city;
        this.img=img;
        this.dp=dp;
        this.price=price;
        this.c=context;
    }

    public class ViewHolder{
        TextView name;
        TextView place;
        TextView description;
        ImageView img;
        ImageView dp;
        TextView price;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.custom_adapter,null);
        }

        final ViewHolder holder =new ViewHolder();

        holder.name=(TextView)convertView.findViewById(R.id.name);
        holder.place=(TextView) convertView.findViewById(R.id.place);
        holder.description=(TextView) convertView.findViewById(R.id.description);
        holder.img=(ImageView)convertView.findViewById(R.id.imageView2);
        holder.dp=(ImageView)convertView.findViewById(R.id.profile_photo);
        holder.price=(TextView)convertView.findViewById(R.id.price);

        holder.img.setImageResource(img[position]);
        holder.name.setText(name.get(position));
        holder.place.setText(place.get(position));
        holder.description.setText(city.get(position));
        holder.dp.setImageResource(dp[position]);
        holder.price.setText("INR "+price.get(position).toString());

        return convertView;
    }
}
