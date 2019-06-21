package com.example.kurt.ugood.profile;

import android.os.Bundle;
import android.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.kurt.ugood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ChangeUsernameFragment extends DialogFragment {

    Button cancel, confirm;

    public ChangeUsernameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.profile_change_username_fragment, container, false);

        cancel = (Button) view.findViewById(R.id.cancel_button);
        confirm = (Button) view.findViewById(R.id.confirm_button);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText newName = view.findViewById(R.id.text_field);
                if (newName.getText() == null)
                    return;

                FirebaseAuth fbAuth = FirebaseAuth.getInstance();

                String userId = fbAuth.getCurrentUser().getUid();
                DatabaseReference currentUserDB = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                Map newPost = new HashMap();
                newPost.put("name", newName.getText().toString());

                currentUserDB.setValue(newPost);

                getDialog().dismiss();
            }
        });

        return view;
    }

}
