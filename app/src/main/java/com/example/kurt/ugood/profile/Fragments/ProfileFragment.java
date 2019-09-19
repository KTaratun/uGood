package com.example.kurt.ugood.profile.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kurt.ugood.R;
import com.example.kurt.ugood.classes.User;
import com.example.kurt.ugood.firebase.GlideApp;
import com.example.kurt.ugood.profile.ProfileActivity;
import com.example.kurt.ugood.profile.SettingsActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "ProfileFragment";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView userName, favoriteQuote, calenderDays, longestStreak, totalDays;
    CircleImageView ProfilePic;
    Button back, settings, favorites;
    FirebaseAuth fbAuth;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Grabbing user info
        User currentUser = User.retreiveUserObjectFromUserDefaults(getContext());
        // currentUser.UpdateUserInfo();
        userName.setText(currentUser.getUsername());
        Log.i(TAG, "onActivityCreated:  + username" + currentUser.getUsername());
        GlideApp.with(getContext() /* context */)
                .load(currentUser.getProfilePic()).centerInside()
                .into(ProfilePic);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        userName = (TextView) view.findViewById(R.id.userNameTextProfile);
        favoriteQuote = (TextView) view.findViewById(R.id.favoriteQuote);
        ProfilePic = view.findViewById(R.id.profileActivityPic);
        // Inflate the layout for this fragment
        return view;
    }

    //MARK TOOL BAR STUFF
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //if you want to only add something to this specific fragments tool bar u add menu item here
        inflater.inflate(R.menu.profile_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //How to get touch event from added menu item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_Toolbar_Button:
                getActivity().getSupportFragmentManager().executePendingTransactions();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.profileContainer,
                        new SettingsFragment()).addToBackStack("SettingsFragment").commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        //return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Grabbing user info
        User currentUser = User.retreiveUserObjectFromUserDefaults(getContext());
       // currentUser.UpdateUserInfo();
        userName.setText(currentUser.getUsername());
        Log.i(TAG, "onActivityCreated:  + username" + currentUser.getUsername());
        GlideApp.with(getContext() /* context */)
                .load(currentUser.getProfilePic()).centerInside()
                .into(ProfilePic);

    }



    @Override
    public void onDetach() {
        super.onDetach();
       // mListener = null;
    }

    //TODO SAVE URL TO DATABASE ONCE ORINIGAL IMAGE IS UPLOADED. THEN WE CAN JUST GET THE HOLE USER OBJECT SAVE IT TO USERDEFAULTS AND YOULL BE ABLE TO ACCESS THE URL ANYWHERE
    public void LoadImageFromFirebase()
    {
        // Reference to an image file in Cloud Storage
        String curUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Users").child(curUserID).child("Profile Pic");

        Task<Uri> newTask = storageReference.getDownloadUrl();
        newTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {


                // Download directly from StorageReference using Glide
                // (See MyAppGlideModule for Loader registration)
                GlideApp.with(getActivity().getApplicationContext() /* context */)
                        .load(uri)
                        .into(ProfilePic);
            }
        });
    }
}
