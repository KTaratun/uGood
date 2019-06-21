package com.example.kurt.ugood.profile;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.kurt.ugood.R;

public class SettingsActivity extends AppCompatActivity {

    private TextView changeUser, changePhoto;
    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_settings_activity);

        changeUser = (TextView) findViewById(R.id.change_username);
        changePhoto = (TextView) findViewById(R.id.change_photo);

        changeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment changeName = new ChangeUsernameFragment();
                changeName.show(getFragmentManager(), "ChangeUsernameFragment");
            }
        });

        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment changeName = new ChangeUserPictureFragment();
                changeName.show(getFragmentManager(), "ChangeUserPictureFragment");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            //TODO: action
            Log.i("dfdf", "onActivityResult: ");
        }
    }
}
