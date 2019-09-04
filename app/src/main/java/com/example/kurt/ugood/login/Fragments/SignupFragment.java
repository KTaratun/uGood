package com.example.kurt.ugood.login.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.kurt.ugood.login.Activities.AdditionalInfoActivity;
import com.example.kurt.ugood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class SignupFragment extends Fragment implements View.OnClickListener{
    private EditText email, password, passConfirm;
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
        if (email.getText() != null && email.getText().toString().length() == 0 ||
                password.getText() != null && password.getText().toString().length() == 0)
            errorMessage.setText(getString(R.string.errorMessageNEEDTOFILL));
        else if (!password.getText().toString().equals(passConfirm.getText().toString()))
            errorMessage.setText(getString(R.string.errorMessagePASSNEEDTOMATCH));
        else
            RegisterUser();
    }

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

                    loadingProgress.setVisibility(View.VISIBLE);
                    confirmButton.setVisibility(View.INVISIBLE);

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
