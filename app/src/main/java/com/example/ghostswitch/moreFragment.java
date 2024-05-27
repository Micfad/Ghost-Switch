package com.example.ghostswitch;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class moreFragment extends Fragment {

    private OnCloseFragmentListener onCloseFragmentListener;

    ImageView morecloseBtn;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);

        morecloseBtn = view.findViewById(R.id.more_back_img_click);
        morecloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCloseButtonClick();
            }
        });

        return view;
    }

    public interface OnCloseFragmentListener {
        void onCloseFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCloseFragmentListener) {
            onCloseFragmentListener = (OnCloseFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnCloseFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onCloseFragmentListener = null;
    }

    // Other fragment code...

    private void handleCloseButtonClick() {
        if (onCloseFragmentListener != null) {
            onCloseFragmentListener.onCloseFragment();
        }
    }
}
