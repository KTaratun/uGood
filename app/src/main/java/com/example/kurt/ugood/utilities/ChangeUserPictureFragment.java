package com.example.kurt.ugood.utilities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kurt.ugood.R;
import com.example.kurt.ugood.firebase.GlideApp;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ChangeUserPictureFragment extends DialogFragment {
    public static final int PICK_IMAGE = 1;

    private Button cancel;
    private TextView TtakePhoto, fromGallery;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.profile_change_user_picture_fragment, container, false);

        cancel = (Button) v.findViewById(R.id.cancel_button);
        fromGallery = (TextView) v.findViewById(R.id.from_gallery_button);

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

        return v;
    }
}
