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
import android.widget.TextView;

import com.example.kurt.ugood.login.LoginActivity;
import com.example.kurt.ugood.main.MainActivity;
import com.example.kurt.ugood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText username, email, password, passConfirm;
    private TextView errorMessage;
    private Button confirmButton;

    private FirebaseAuth fbAuth;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SignupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignupFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
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
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.login_fragment_signup, container, false);

        fbAuth = FirebaseAuth.getInstance();

        username = view.findViewById(R.id.usernameText);
        email = view.findViewById(R.id.emailText);
        password = view.findViewById(R.id.passText);
        passConfirm = view.findViewById(R.id.passConfirmText);
        errorMessage = view.findViewById(R.id.errorMessage);


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
        if (username.getText() != null && username.getText().toString().length() == 0 ||
                email.getText() != null && email.getText().toString().length() == 0 ||
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

                    String userId = fbAuth.getCurrentUser().getUid();
                    DatabaseReference currentUserDB = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                    Map newPost = new HashMap();
                    newPost.put("name", username.getText().toString());

                    currentUserDB.setValue(newPost);

                    Intent intent = new Intent(getActivity(), MainActivity.class);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
