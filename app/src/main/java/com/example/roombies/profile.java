package com.example.roombies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link profile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String name,phone,age,city,gender,prof;
    View v;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String usermail;

    private OnFragmentInteractionListener mListener;

    public profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profile.
     */
    // TODO: Rename and change types and number of parameters
    public static profile newInstance(String param1, String param2) {
        profile fragment = new profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences pref = this.getActivity().getSharedPreferences("MY_PREF",Context.MODE_PRIVATE);
        usermail = pref.getString("email","none");
        Toast.makeText(getActivity(),"email: "+usermail, Toast.LENGTH_LONG).show();

        new GetProfileClass().execute("http://192.168.43.227:5000/getprofile?email="+usermail);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    private class GetProfileClass extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        private static final String TAG ="" ;
        JSONObject jobj;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String urlPath = params[0];
                String json = "";
                JSONObject jobj;
                StringBuilder result = new StringBuilder();
                BufferedReader bufferedReader = null;
                StringBuilder builder;
                try {
                    System.out.println("before connect");
                    URL url = new URL(urlPath);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    //    urlConnection.setReadTimeout(10000);
                    //    urlConnection.setConnectTimeout(10000);
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.connect();

                    System.out.println("after connect");

                    InputStream inputStream = urlConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        result.append(line).append("\n");
                    }

                    json = result.toString();
                    System.out.println("after fetch");
                    //System.out.println(result);

                } catch (Exception e) {
                    Log.e("Buffer Error", "Error converting result " + e.toString());
                }

                return result.toString();
            } catch (Exception ex) {
                Toast.makeText(getActivity(),"Network Error...", Toast.LENGTH_LONG).show();
                return "Network Error!!";
            }
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (progressDialog != null)
                progressDialog.dismiss();
            if(result.contains("false")){
                System.out.println("result is false!!");
                System.out.println(result);
            }
            else{
                int c=0;
                System.out.println("result is: "+result);
                try {
                    JSONObject jObject  = new JSONObject(result); // json
                    name = jObject.getString("Name"); // get the name from data.
                    age = jObject.getString("Age");
                    prof = jObject.getString("Profession");
                    gender = jObject.getString("Gender");
                    phone = jObject.getString("Phone");
                    city = jObject.getString("City");
                    System.out.println(name+' '+age+' '+gender+' '+prof+' '+phone+' '+city);
                    String pic2=jObject.optString("profilePhoto");
                    byte [] encodeByte2= Base64.decode(pic2,Base64.DEFAULT);
                    Bitmap bitmap2= BitmapFactory.decodeByteArray(encodeByte2, 0, encodeByte2.length);


                    TextView name1 = (TextView)v.findViewById(R.id.profname);
                    name1.setText("Name : "+name);
                    TextView email1 = (TextView)v.findViewById(R.id.profmail);
                    email1.setText("Email : "+usermail);
                    TextView age1 = (TextView)v.findViewById(R.id.profage);
                    age1.setText("Age : "+age);
                    TextView gender1 = (TextView)v.findViewById(R.id.profgender);
                    gender1.setText("Gender : " +gender);
                    TextView prof1 = (TextView)v.findViewById(R.id.profprof);
                    prof1.setText("Profession : "+prof);
                    TextView phone1 = (TextView)v.findViewById(R.id.profphone);
                    phone1.setText("Phone : "+phone);

                    CircleImageView circleImageView = (CircleImageView) v.findViewById(R.id.user_profile_photo);

                   // ImageButton photo1 = (ImageButton)getView().findViewById(R.id.user_profile_photo);

                    BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap2);
                    circleImageView.setImageDrawable(bitmapDrawable);
                    //photo1.setBackgroundDrawable(bitmapDrawable);

                } catch (JSONException e) {
                    Log.e("JSON Parser", "Error parsing data " + e.toString());
                }
            }
        }

        private String getProfile(String urlPath) throws IOException {
            String json = "";
            JSONObject jobj;
            StringBuilder result = new StringBuilder();
            BufferedReader bufferedReader = null;
            StringBuilder builder;
            try {
                System.out.println("before connect");
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //    urlConnection.setReadTimeout(10000);
                //    urlConnection.setConnectTimeout(10000);
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

                System.out.println("after connect");

                InputStream inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line).append("\n");
                }

                json = result.toString();
                System.out.println("after fetch");
                //System.out.println(result);

            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            return result.toString();
        }
    }
}
