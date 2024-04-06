package com.example.ghostswitch;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


//added
import android.os.Handler;
import android.os.Looper;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import android.os.AsyncTask;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ghostswitch.otherClass.MyDBHelper;


public class Url extends Fragment {

    // Define a Handler
    private final Handler handler = new Handler(Looper.getMainLooper());
    private TextView url_errorrMsg, DeviceType, nextxt;
    private EditText urlEdt;
    private ConstraintLayout nextbtn;
    private ProgressBar progbar;



    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       // return inflater.inflate(R.layout.fragment_url, container, false);
        final View view = inflater.inflate(R.layout.fragment_url, container, false);


        // Initialize UI elements
        url_errorrMsg = view.findViewById(R.id.number_error);
        urlEdt = view.findViewById(R.id.Urleditext);
        nextbtn =view.findViewById(R.id.Nextbtn);
        DeviceType = view.findViewById(R.id.device_type);
        progbar = view.findViewById(R.id.IPprogressBar);
        nextxt = view.findViewById(R.id.ip_next_txt);

        urlEdt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                url_errorrMsg.setVisibility(View.GONE);
                url_errorrMsg.setText("Taking too long Please check the IP and wifi you are connected to");
                progbar.setVisibility(View.GONE);
                nextxt.setVisibility(View.VISIBLE);

            }

        });


        // Execute network request in a background thread
        // Set OnClickListener on the "Next" button
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if editext is empty
                if(urlEdt.getText().toString().isEmpty()){
                    url_errorrMsg.setVisibility(View.VISIBLE);
                    url_errorrMsg.setText("please enter your device IP address");
                    // Use a Handler to hide the error message after 3 seconds
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Hide the error message after 3 seconds
                            url_errorrMsg.setVisibility(View.GONE);
                        }
                    }, 3000); // 3000 milliseconds = 3 seconds

                }else {
                    String ipAddress = urlEdt.getText().toString();
                    progbar.setVisibility(View.VISIBLE);
                    nextxt.setVisibility(View.GONE);
                    // Execute AsyncTask to fetch "node" text from ESP server
                    new FetchNodeTextTask().execute(ipAddress);
                    // wait for DeviceType txt to change
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (DeviceType.getText().toString().equals("IP Address")) {
                                url_errorrMsg.setVisibility(View.VISIBLE);
                                url_errorrMsg.setText("Taking too long Please check the IP and wifi you are connected to");
                                progbar.setVisibility(View.GONE);
                                nextxt.setVisibility(View.VISIBLE);


                            }

                        }
                    }, 3000); // 3000 milliseconds = 3 seconds

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (DeviceType.getText().toString().equals("IP Address")) {
                                progbar.setVisibility(View.GONE);
                                nextxt.setVisibility(View.VISIBLE);

                            }

                        }
                    }, 3000); // 3000 milliseconds = 3 seconds





                }




            }
        });



        return view;










    }


    private class FetchNodeTextTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String ipAddress = params[0];
            String nodeText = null;

            try {
                // Construct URL with the given IP address
                URL url = new URL("http://" + ipAddress + "/");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                // Set request properties
                urlConnection.setRequestMethod("GET");

                // Set custom User-Agent header to identify the app
                urlConnection.setRequestProperty("User-Agent", "Ghost-Switch");

                // Read response
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                // Close streams
                reader.close();
                inputStream.close();

                // Get "node" text from response
                nodeText = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return nodeText;
        }


        @Override
        protected void onPostExecute(String nodeText) {

            // Check if nodeText is not null, indicating successful connection
            if (nodeText != null) {

                // Update TextView with "node" text
                DeviceType.setText(nodeText);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (nodeText.equals("node")) {
                            // Navigate to another fragment
                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.urlcontainer, new Node_ssid_forms())
                                    .commit();
                            // Call a function to save the IP address in the database
                            saveIpAddressToDatabase();

                        }else {
                            if (nodeText.equals("home")) {
                                    // Navigate to another fragment
                                    //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.urlcontainer, new Node_ssid_forms()).commit();

                                    // Call a function to save the IP address in a diffrnt table in the database
                                    //saveIpAddressToDatabase();
                                }

                             }


                    }
                }, 200); // 3000 milliseconds = 3 seconds


            }else{
                url_errorrMsg.setVisibility(View.VISIBLE);
                url_errorrMsg.setText("The IP adress you entered is wrong.");

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Hide the error message after 3 seconds
                        url_errorrMsg.setVisibility(View.GONE);
                        progbar.setVisibility(View.GONE);
                        nextxt.setVisibility(View.VISIBLE);

                    }
                }, 3000); // 3000 milliseconds = 3 seconds


            }


        }

        private void saveIpAddressToDatabase() {
            // Get the IP address from the EditText
            String ipAddress = urlEdt.getText().toString();

            // Open or create the database
            MyDBHelper dbHelper = new MyDBHelper(getActivity());
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // Create a ContentValues object to store the data
            ContentValues values = new ContentValues();
            values.put(MyDBHelper.COLUMN_IP_ADDRESS, ipAddress);

            // Insert the data into the database
            long newRowId = db.insert(MyDBHelper.TABLE_IP_ADDRESS, null, values);

            // Check if the data was inserted successfully
            if (newRowId != -1) {
                // Data inserted successfully
                // You can perform any additional actions here if needed
            } else {
                // Failed to insert data
                // Handle the error accordingly
            }

            // Close the database connection
            db.close();
        }

        private void saveOncIpAddressToDatabase() {
            // Get the IP address from the EditText
            String ipAddress = urlEdt.getText().toString();

            // Open or create the database
            MyDBHelper dbHelper = new MyDBHelper(getActivity());
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            try {
                // Create a ContentValues object to store the data
                ContentValues values = new ContentValues();
                values.put(MyDBHelper.COLUMN_HOME_IP_ADDRESS, ipAddress);

                // Insert the data into the TABLE_IP_ADDRESS table
                long newRowId = db.insert(MyDBHelper.TABLE_IP_ADDRESS, null, values);

                // Check if the data was inserted successfully
                if (newRowId != -1) {
                    // Data inserted successfully
                    // You can perform any additional actions here if needed
                } else {
                    // Failed to insert data
                    // Handle the error accordingly
                }
            } catch (Exception e) {
                // Handle any exceptions that might occur during the insertion process
                e.printStackTrace();
            } finally {
                // Close the database connection
                db.close();
            }
        }





    }






}