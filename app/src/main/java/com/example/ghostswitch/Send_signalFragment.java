package com.example.ghostswitch;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.ghostswitch.otherClass.AudioPlayer;
import com.example.ghostswitch.otherClass.DatabaseUtil;
import com.example.ghostswitch.otherClass.FragmentUtils;


public class Send_signalFragment extends Fragment {

    TextView feedback;


    private static final String ESP_IP_ADDRESS = "192.168.4.1"; // ESP32 IP address should be fetched from db
    private View view;

    // Interface for communication with other fragments/activity
    public interface SignalListener {
        void onSignalFailure(String message);
    }

    private SignalListener signalListener;

    // Method to set the SignalListener
    public void setSignalListener(SignalListener listener) {
        this.signalListener = listener;
    }




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_send_signal, container, false);
        feedback = view.findViewById(R.id.feedbk);



        /*
        use this fragment to recieve
        signal data to be sent to the ghost home device
        and use the ui for http error  messages.
         */



        // Retrieve the speech text from arguments
        Bundle args = getArguments();
        if (args != null) {
            String speechText = args.getString("signal_key");

            if(speechText.toString().contains("light") && speechText.toString().contains("on")){
                // If text contains light and on proceed to send lay1_on signal via HTTP
                String textToSend = "lay1_on";
                new SendRequestTask().execute(textToSend);

            }

            else if(speechText.toString().contains("light") && speechText.toString().contains("off")){
                // If text contains light and off proceed to send lay1_on signal via HTTP
                String textToSend = "lay1_off";
                new SendRequestTask().execute(textToSend);

            }


        }

        return view;

    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer resources when fragment is destroyed
        AudioPlayer.releaseMediaPlayer();
    }



    private class SendRequestTask extends AsyncTask<String, Void, Integer> {

        private static final int CONNECTION_TIMEOUT = 3000; // 3 seconds


        @Override
        protected Integer doInBackground(String... params) {
            try {
                String action = params[0];
                long startTime = System.currentTimeMillis();

                //retrieve ipaddress from database
                //String homeIpAddress = DatabaseUtil.getIpAddressFromDatabase(getContext());
                //URL url = new URL(homeIpAddress);
                URL url = new URL("http://" + ESP_IP_ADDRESS + "/endpoint?action=" + action);



                // Open connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                // Set connection timeout
                urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
                urlConnection.setRequestProperty("Content-Type", "text/plain");

                // Get response code
                int responseCode = urlConnection.getResponseCode();

                // Calculate elapsed time
                long elapsedTime = System.currentTimeMillis() - startTime;

                // Close connection
                urlConnection.disconnect();

                // If elapsed time exceeds timeout, return -2 to indicate timeout
                if (elapsedTime > CONNECTION_TIMEOUT) {
                    return -1;
                }

                // Return response code
                return responseCode;
            } catch (IOException e) {
                Log.e("HTTP", "Error sending request: " + e.getMessage());
                return -2; // Return -1 to indicate an error
            }
        }






        @Override
        protected void onPostExecute(Integer responseCode) {
            super.onPostExecute(responseCode);
            // Handle response code
            if (responseCode == HttpURLConnection.HTTP_OK) {
                //delete this
                feedback.setText("OK");

                // Retrieve the location text from arguments. this is so this fragment can know where the data comes from.
                Bundle args = getArguments();
                String location = args.getString("from");
                if ("VC".equals(location)) {
                    FragmentUtils.navigateToFragment(requireActivity(), R.id.vcfrag_container, new VCFragment());

                } else if ("Switch".equals(location)) {
                    FragmentUtils.navigateToFragment(requireActivity(), R.id.vcfrag_container, new SwitchesFragment());
                }

            } else if (responseCode == -2) {
                // Timeout occurred
                String message = "Can't reach";
                feedback.setText(message);
                AudioPlayer.playAudioError(requireContext());
                /*
                delete if works fine
                  new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AudioPlayer.playAudioTwo(requireContext());
                    }
                }, 3000); // 3000 milliseconds = 3 seconds


                 */

                //to send message to switches fragment. his may not be needed tho
                if (signalListener != null) {
                    signalListener.onSignalFailure(message);
                }

            } else {
                // Request failed
                String message = "Request failed, response code: " + responseCode;
                feedback.setText(message);
                AudioPlayer.playAudioError(requireContext());
                 /*
                delete if works fine
                  new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AudioPlayer.playAudioTwo(requireContext());
                    }
                }, 3000); // 3000 milliseconds = 3 seconds


                 */
            }
        }
    }
}