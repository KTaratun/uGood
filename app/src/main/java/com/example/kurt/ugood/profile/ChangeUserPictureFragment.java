package com.example.kurt.ugood.profile;

import android.content.Intent;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kurt.ugood.R;

public class ChangeUserPictureFragment extends DialogFragment {
    public static final int PICK_IMAGE = 1;

    Button cancel;
    TextView TtakePhoto, fromGallery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.profile_change_user_picture_fragment, container, false);

        cancel = (Button) view.findViewById(R.id.cancel_button);
        fromGallery = (TextView) view.findViewById(R.id.from_gallery_button);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        fromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                getActivity().startActivityForResult(intent, PICK_IMAGE);

                getDialog().dismiss();
            }
        });

        return view;
    }

}
