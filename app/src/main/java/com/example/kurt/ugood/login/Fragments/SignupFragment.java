package com.example.kurt.ugood.login.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kurt.ugood.classes.Interfaces.ProfilePicResultHandler;
import com.example.kurt.ugood.classes.User;
import com.example.kurt.ugood.login.Activities.AdditionalInfoActivity;
import com.example.kurt.ugood.R;
import com.example.kurt.ugood.main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class SignupFragment extends Fragment implements View.OnClickListener{
    private EditText email, password, passConfirm, username;
    private TextView errorMessage;
    private Button confirmButton;
    private ProgressBar loadingProgress;

    private FirebaseAuth fbAuth;
    private OnFragmentInteractionListener mListener;

    public SignupFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.login_fragment_signup, container, false);

        fbAuth = FirebaseAuth.getInstance();

        email = view.findViewById(R.id.emailText);
        password = view.findViewById(R.id.passText);
        passConfirm = view.findViewById(R.id.passConfirmText);
        errorMessage = view.findViewById(R.id.errorMessage);
        loadingProgress = view.findViewById(R.id.progressBar);
        loadingProgress.setVisibility(View.INVISIBLE);
        username = view.findViewById(R.id.usernameText);

        confirmButton = view.findViewById(R.id.confirmButton);

        confirmButton.setOnClickListener(this);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        //Checking for empty data
        if(email.getText().toString().trim().isEmpty() ||
                username.getText().toString().trim().isEmpty() ||
                password.getText().toString().trim().isEmpty() ||
                passConfirm.getText().toString().trim().isEmpty()
                ){
            createToast(getString(R.string.errorMessageNEEDTOFILL));
            //TO me this wasnt enough to grab users attention
            //errorMessage.setText();
            return;
        }

        if(!password.getText().toString().equals(passConfirm.getText().toString())){
            //errorMessage.setText(getString(R.string.errorMessagePASSNEEDTOMATCH));
            createToast(getString(R.string.errorMessagePASSNEEDTOMATCH));
            return;
        }

        if(isValidEmail(email.getText().toString())){
            createToast("Email is not correctly formatted");
            return;
        }

        Loading(true);
        final User newUser = new User(username.getText().toString(), email.getText().toString());
        newUser.RegisterUser(password.getText().toString()).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    newUser.setUserID(FirebaseAuth.getInstance().getUid());
                    newUser.CreateProfilePic(new ProfilePicResultHandler<Uri>() {
                        @Override
                        public void onSuccess(Uri data) {
                            newUser.setProfilePic(data.toString());
                            newUser.WriteNewUser().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Loading(false);
                                    //if (getContext() == null)

                                    newUser.saveObjectInUserDefaults(getContext());
                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }

                        @Override
                        public void onFailure(Exception e) {

                        }
                    });


                }

                if (!task.isSuccessful()){
                    if (task.getException() != null){
                        Loading(false);
                        createToast(((FirebaseAuthException) task.getException()).getMessage());
                    }
                }
            }
        });

    }

    private void Loading(Boolean Show){
        if(Show){
            loadingProgress.setVisibility(View.VISIBLE);
            confirmButton.setVisibility(View.INVISIBLE);
        }else{
            loadingProgress.setVisibility(View.INVISIBLE);
            confirmButton.setVisibility(View.VISIBLE);
        }

    }

    private void createToast(String text){
        Toast.makeText(getContext() , text, Toast.LENGTH_SHORT).show();
    }

    //Built in REGEX Check
    private boolean isValidEmail(CharSequence target){
        if(TextUtils.isEmpty(target)){
            return false;
        }else{
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    //TODO delete old method
    private void RegisterUser()
    {
        String emailFinal = email.getText().toString().trim();
        String passFinal = password.getText().toString().trim();

        fbAuth.createUserWithEmailAndPassword(emailFinal, passFinal)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    errorMessage.setText(getString(R.string.regSuccess));



                    Intent intent = new Intent(getActivity(), AdditionalInfoActivity.class);
                    startActivity(intent);
                }
                else
                {
                    try {
                        throw task.getException();
                    } catch(FirebaseAuthWeakPasswordException e) {
                        errorMessage.setText(getString(R.string.errorMessageWEAKPASSWORD));
                    } catch(FirebaseAuthInvalidCredentialsException e) {
                        errorMessage.setText(getString(R.string.errorMessageINVALIDEMAILORPASSWORD));
                    } catch(FirebaseAuthUserCollisionException e) {
                        errorMessage.setText(getString(R.string.errorMessageUSEREXISTS));
                    } catch(Exception e) {
                        Log.e("ERROR: ", e.getMessage());
                    }
                }
            }
        });
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
