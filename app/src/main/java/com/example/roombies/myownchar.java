package com.example.roombies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link myownchar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class myownchar extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String NatureResponse,EatingResponse,SleepResponse,AlcoholResponse,SmokingResponse;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String usermail;


    private OnFragmentInteractionListener mListener;

    public myownchar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment myownchar.
     */
    // TODO: Rename and change types and number of parameters
    public static myownchar newInstance(String param1, String param2) {
        myownchar fragment = new myownchar();
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
        SharedPreferences pref = this.getActivity().getSharedPreferences("MY_PREF",Context.MODE_PRIVATE);
        usermail = pref.getString("email","none");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_myownchar, container, false);
        CheckBox NatureYes=(CheckBox) v.findViewById(R.id.etnatureyes);
        CheckBox NatureNo=(CheckBox)v.findViewById(R.id.etnatureno);
        CheckBox EatingYes=(CheckBox)v.findViewById(R.id.eteatingyes);
        CheckBox EatingNo=(CheckBox)v.findViewById(R.id.eteatingno);
        CheckBox SleepingYes=(CheckBox)v.findViewById(R.id.etsleepyes);
        CheckBox SleepingNo=(CheckBox)v.findViewById(R.id.etsleepno);
        CheckBox AlcoholYes=(CheckBox)v.findViewById(R.id.etalcoholyes);
        CheckBox AlcoholNo=(CheckBox)v.findViewById(R.id.etalcoholno);
        CheckBox SmokingYes=(CheckBox)v.findViewById(R.id.etsmokeyes);
        CheckBox SmokingNo=(CheckBox)v.findViewById(R.id.etsmokeno);
        Button etSearchButton = (Button)v.findViewById(R.id.etSearch);
        NatureYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NatureResponse="Yes";
            }
        });
        NatureNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NatureResponse="No";
            }
        });
        EatingYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EatingResponse="Yes";
            }
        });
        EatingNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EatingResponse="No";
            }
        });
        SleepingYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SleepResponse="Yes";
            }
        });
        SleepingNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SleepResponse="No";
            }
        });
        AlcoholYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlcoholResponse="Yes";
            }
        });
        AlcoholNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlcoholResponse="No";
            }
        });
        SmokingYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmokingResponse="Yes";
            }
        });
        SmokingNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmokingResponse="No";
            }
        });
        etSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PostOwnCharactersticsDetailsClass().execute("http://192.168.43.228:5000/savecharacterstics");
            }
        });
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
    class PostOwnCharactersticsDetailsClass extends AsyncTask<String,Void,String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return PostOwnCharacterticsDetails(params[0]);
            } catch (IOException ex) {
                return "Network Error!!";
            } catch (JSONException ex) {
                return "Data Invalid !";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(progressDialog!=null)
                progressDialog.dismiss();
            if(result.contains("true"))
            {

                //means successfully done
                //Intent NextActivity=new Intent(myownchar.this,looking_for_mates.class);
                // startActivity(NextActivity);
            }
            else{
                //not done
                //Toast.makeText(getApplicationContext(),"Unable to Update Details",Toast.LENGTH_LONG);
                //Intent YahiActivity=new Intent(mychar.this,mychar.class);
                //startActivity(YahiActivity);
            }
        }
        private String PostOwnCharacterticsDetails(String urlPath) throws IOException, JSONException {
            BufferedWriter bufferedWriter=null;
            BufferedReader bufferedReader=null;
            StringBuilder result=new StringBuilder();
            StringBuilder builder;
            try{
                JSONObject dataToSend=new JSONObject();
                dataToSend.put("usermail",usermail);
                dataToSend.put("Nature",NatureResponse);
                dataToSend.put("Sleep",SleepResponse);
                dataToSend.put("Eating",EatingResponse);
                dataToSend.put("Smoking",SmokingResponse);
                dataToSend.put("Alcohol",AlcoholResponse);

                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.connect();

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
                System.out.println(line);
                builder = new StringBuilder();
                builder.append(urlConnection.getResponseCode());
                Log.d("not in finally", result.toString());
            }finally{
                if(bufferedReader!=null)
                    bufferedReader.close();
                if(bufferedWriter!=null)
                    bufferedWriter.close();
            }


            return result.toString();
        }
    }

}
