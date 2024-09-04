package com.example.ghostswitch.fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.speech.RecognizerIntent;

import android.widget.Button;
import android.widget.TextView;


import com.example.ghostswitch.R;
import com.example.ghostswitch.Send_signalFragment;
import com.example.ghostswitch.otherClass.AudioPlayer;
import com.example.ghostswitch.otherClass.FragmentUtils;

import java.util.ArrayList;


public class VCFragment extends Fragment {

    private static final int SPEECH_REQUEST_CODE = 0;

    Button startRecognitionButton;
    TextView speech_Text;
    private View view;
    //private MediaPlayer mediaPlayer; // Declare MediaPlayer instance

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_v_c, container, false);

        startRecognitionButton = view.findViewById(R.id.start_recognition_button);
        speech_Text = view.findViewById(R.id.speech_text);

        AudioPlayer.releaseMediaPlayer();

        startRecognitionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecognition();
            }
        });
        return view;
    }

    private void startRecognition() {
        // Start voice recognition
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Release MediaPlayer resources when fragment is destroyed
        AudioPlayer.releaseMediaPlayer();
    }



    private void speechtxt(String speech) {

            String from = "VC";

            // Create a Bundle and set the recognized speech
            Bundle bundle = new Bundle();
            bundle.putString("signal_key", speech);
            bundle.putString("from", from);


            // Create the fragment instance and set arguments
            Send_signalFragment fragment2 = new Send_signalFragment();
            fragment2.setArguments(bundle);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.vcfrag_container, fragment2);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }, 3000); // 3000 milliseconds = 3 seconds


    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




        if (requestCode == SPEECH_REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            // Retrieve the recognized speech
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && !matches.isEmpty()) {

                // Check if any of the recognized phrases contain "light on" or "on"
                for (String match : matches) {
                    if(match.toLowerCase().contains("on") && match.toLowerCase().contains("off")){
                        speech_Text.setText("you said on and off at the same time");
                        AudioPlayer.playAudioError(requireContext());
                    }
                    else {
                            if (match.toLowerCase().contains("off")) {
                            //get all switches and devices strings saved in db then compare which. if its a light
                            speech_Text.setText("Okay off");
                            speechtxt(match); // Pass the recognized speech to speechtxt() method
                                AudioPlayer.playAudioTwo(requireContext());
                            //playAudiotwo(); // Play audio when "light on" or "on" is recognized
                            return; // Exit the loop once a match is found
                        }
                            if (match.toLowerCase().contains("on")) {
                                //get all switches and devices strings saved in db then compare which. if its a light
                                speech_Text.setText("Okay on");
                                speechtxt(match); // Pass the recognized speech to speechtxt() method
                                //playAudioone();
                                AudioPlayer.playAudioOne(requireContext()); // Play audio when "light on" or "on" is recognized
                                return; // Exit the loop once a match is found
                            }

                        
                         }


                }
                // If no match containing "light on" or "on" is found, set the TextView to the first recognized text
                speech_Text.setText(matches.get(0));
            }
        }
    }



}
