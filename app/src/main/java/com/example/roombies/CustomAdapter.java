package com.example.roombies;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomAdapter extends BaseAdapter {
    ArrayList<String> name;
    ArrayList<String> place;
    ArrayList<String> city;
    ArrayList<Bitmap> img;
    ArrayList<Bitmap> dp;
    ArrayList<Integer> price;
    LayoutInflater inflater;
    public ArrayList<PersonDetails> personDetailsList=new ArrayList<PersonDetails>();
    public ArrayList<PersonDetails> arraylist=new ArrayList<PersonDetails>();
    Context c;

  public CustomAdapter(Context context,ArrayList<PersonDetails> personDetailsList)
    {
        this.personDetailsList=personDetailsList;
        this.c=context;
        this.inflater = LayoutInflater.from(c);
        this.arraylist.addAll(personDetailsList);
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
    public Object getItem(int position) {
        return personDetailsList.get(position);
    }

    @Override
    public int getCount() {
        return personDetailsList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.custom_adapter, null);
            // Locate the TextViews in listview_item.xml

            holder.name=(TextView)convertView.findViewById(R.id.name);
            holder.place=(TextView) convertView.findViewById(R.id.place);
            holder.description=(TextView) convertView.findViewById(R.id.description);
            holder.img=(ImageView)convertView.findViewById(R.id.imageView2);
            holder.dp=(ImageView)convertView.findViewById(R.id.profile_photo);
            holder.price=(TextView)convertView.findViewById(R.id.price);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.img.setImageBitmap(personDetailsList.get(position).getImg());
        holder.name.setText(personDetailsList.get(position).getName());
        holder.place.setText(personDetailsList.get(position).getPlace());
        holder.description.setText(personDetailsList.get(position).getCity());
        holder.dp.setImageBitmap(personDetailsList.get(position).getDp());
        holder.price.setText("INR "+personDetailsList.get(position).getPrice().toString());

        return convertView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        personDetailsList.clear();
        if (charText.length() == 0) {
            personDetailsList.addAll(arraylist);
        }
        else
        {
            for (PersonDetails pd : arraylist)
            {
                if (pd.getCity().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    personDetailsList.add(pd);
                }
            }
        }
        notifyDataSetChanged();
    }
    public ArrayList<PersonDetails> loc(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        personDetailsList.clear();
        if (charText.length() == 0) {
            personDetailsList.addAll(arraylist);
        }
        else
        {
            for (PersonDetails pd : arraylist)
            {
                if (pd.getCity().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    personDetailsList.add(pd);
                }
            }
        }
        return personDetailsList;
    }
}
