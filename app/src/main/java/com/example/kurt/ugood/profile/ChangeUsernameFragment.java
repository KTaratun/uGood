package com.example.kurt.ugood.profile;

import android.os.Bundle;
import android.app.DialogFragment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kurt.ugood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

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
                FirebaseFirestore ff = FirebaseFirestore.getInstance();

                Map newPost = new HashMap();
                newPost.put("name", newName.getText().toString());

                ff.collection("Users").document(userId).set(newPost)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e){
                                Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                                Log.d("error", e.toString());
                            }
                        });

                getDialog().dismiss();
            }
        });

        return view;
    }

}
