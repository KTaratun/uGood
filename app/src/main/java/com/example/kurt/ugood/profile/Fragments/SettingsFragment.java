package com.example.kurt.ugood.profile.Fragments;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;

import com.example.kurt.ugood.login.Activities.LoginActivity;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.kurt.ugood.R;
import com.example.kurt.ugood.classes.User;
import com.example.kurt.ugood.profile.SettingsActivity;
import com.example.kurt.ugood.utilities.ChangeUserPictureFragment;
import com.google.android.gms.tasks.Continuation;
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


public class SettingsFragment extends PreferenceFragmentCompat {
    private static final String TAG = "SettingsFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static final int PICK_IMAGE = 11223;
    private StorageReference storageReference;
    private Uri filePath;

    User currentUser;
    //UI
    EditTextPreference userNameET;
    //private OnFragmentInteractionListener mListener;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, "settings");
        userNameET = (EditTextPreference) getPreferenceScreen().findPreference("change_username");
        userNameET.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newName) {
                if (newName.toString().trim().isEmpty()){
                    return false;
                    //todo create toast to tell user that there username was not successful
                }

                currentUser.setUsername(newName.toString());
                currentUser.UpdateUserInfo().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            currentUser.saveObjectInUserDefaults(getContext());
                        }else{

                        }
                    }
                });

                return true;
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

    }



    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        switch (preference.getKey()){
            case "change_username":
                //EXAMPLE OF HOW TO RETREIVE sharedPReference data
                //When using EditTextPreferences what ever the user submitted gets saved in sharedPreferences under the key used to create EditText Preference
                SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext().getApplicationContext());
                String name = mSharedPreferences.getString("change_username", "");
                Log.i(TAG, "onPreferenceTreeClick: " + name);
                break;
            case "change_profile_pic":


                AlertDialog.Builder newPicDialog = new AlertDialog.Builder(getContext()).setTitle("Change Profile Picture");
                newPicDialog.setItems(new CharSequence[]
                                {"Take Photo", "Choose From Gallery", "Cancel"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                switch (which) {
                                    case 0:
                                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                        intent.setType("image/*");
                                        //Was having trouble here because i used getactivity().startActivityForResult
                                        // ///this will make the activities  "onActivtiyResult" to go off instead of fragments
                                        startActivityForResult(intent, PICK_IMAGE);


                                        break;
                                    case 1:
                                        //Toast.makeText(context, "clicked 2", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 2:
                                        //Toast.makeText(context, "clicked 3", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 3:
                                        //Toast.makeText(context, "clicked 4", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });
                newPicDialog.show();

//                //Changing to alert dialog alot easier to use instead of creating your own UI
//                DialogFragment changeName = new ChangeUserPictureFragment();
//                changeName.show(getActivity().getFragmentManager(), "ChangeUserPictureFragment");
                break;
            case "sign_out":
                //Signing out of firebase
                FirebaseAuth fbAuth = FirebaseAuth.getInstance();
                fbAuth.signOut();

                //clearing shared preferences
                SharedPreferences prefs = getContext().getSharedPreferences(
                        "com.example.kurt.ugood", Context.MODE_PRIVATE);
                prefs.edit().clear().commit();


                Intent signOutIntent = new Intent(getContext(), LoginActivity.class);
                //adding flags to make sure that all current activities are closed
                signOutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                signOutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(signOutIntent); //Go back to home page
                getActivity().finish();

                break;
            default:

        }

        return super.onPreferenceTreeClick(preference);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            filePath = data.getData();

            if(filePath != null)
            {
                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Uploading...");
                progressDialog.show();

                String curUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                storageReference = FirebaseStorage.getInstance().getReference();
                final StorageReference userProfilePicStorageLocation = storageReference.child(curUserID);
                final UploadTask uploadTask = userProfilePicStorageLocation.putFile(filePath);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> urltask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                    @Override
                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                        if(!task.isSuccessful()){
                                            throw task.getException();
                                        }
                                        return userProfilePicStorageLocation.getDownloadUrl();
                                    }
                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if(task.isSuccessful()){
                                            Uri downloadUri = task.getResult();
                                            currentUser.setProfilePic(downloadUri.toString());
                                            currentUser.saveObjectInUserDefaults(getContext());
                                            currentUser.UpdateUserInfo();
                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        currentUser = User.retreiveUserObjectFromUserDefaults(getContext());


    }


}
