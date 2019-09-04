package com.example.kurt.ugood.login.Activities;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kurt.ugood.R;
import com.example.kurt.ugood.firebase.GlideApp;
import com.example.kurt.ugood.main.MainActivity;
import com.example.kurt.ugood.utilities.ChangeUserPictureFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AdditionalInfoActivity extends AppCompatActivity {

    private ImageView profilePic;
    private Button confirm;
    private TextView errorMessage;
    private FirebaseAuth fbAuth;
    private FirebaseFirestore ff = FirebaseFirestore.getInstance();
    private DialogFragment dialog;
    private Spinner dropdown;

    public static final int PICK_IMAGE = 1;
    private StorageReference storageReference;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_info);

        fbAuth = FirebaseAuth.getInstance();

        errorMessage = findViewById(R.id.additonalInfo_errorMessage);

        storageReference = FirebaseStorage.getInstance().getReference().child("Users");

        profilePic = findViewById(R.id.info_profilePic);

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new ChangeUserPictureFragment();
                dialog.show(getFragmentManager(), "ChangeUserPictureFragment");
            }
        });

        //get the spinner from the xml.
        dropdown = findViewById(R.id.spinner1);
        //create a list of items for the spinner.
        String[] items = new String[]{"Edison", "Morristown", "Metuchen"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        confirm = findViewById(R.id.info_confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmSelections();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            filePath = data.getData();

            if(filePath != null)
            {
                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                String curUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                StorageReference FBStorageLocation = storageReference.child(curUserID).child("Profile Pic");
                FBStorageLocation.putFile(filePath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss();
                                Toast.makeText(AdditionalInfoActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                                LoadImageFromFirebase();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(AdditionalInfoActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                        .getTotalByteCount());
                                progressDialog.setMessage("Uploaded "+(int)progress+"%");
                            }
                        });
            }
        }
    }

    public void LoadImageFromFirebase()
    {
        // Reference to an image file in Cloud Storage
        String curUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Users").child(curUserID).child("Profile Pic");

        Task<Uri> newTask = storageReference.getDownloadUrl();
        newTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageView other = findViewById(R.id.info_profilePic);

                GlideApp.with(getApplicationContext() /* context */)
                        .load(uri)
                        .into(other);
            }
        });
    }

    private void ConfirmSelections()
    {
        EditText username = findViewById(R.id.info_usernameText);

        if (username.getText().toString().length() == 0)
        {
            errorMessage.setText(getString(R.string.errorMessageNEEDTOFILL));
            return;
        }

        String userId = fbAuth.getCurrentUser().getUid();

        Map newPost = new HashMap();

        newPost.put("name", username.getText().toString());
        newPost.put("school", dropdown.getSelectedItem().toString());

        ff.collection("Users").document(userId).set(newPost)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e){
                        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                        Log.d("error", e.toString());
                    }
                });

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
