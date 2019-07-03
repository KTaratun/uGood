package com.example.kurt.ugood.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.kurt.ugood.R;
import com.example.kurt.ugood.diagnostic.DiagnosticActivity;
import com.example.kurt.ugood.firebase.FirebaseFunctions;
import com.example.kurt.ugood.profile.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private TextView name;
    private FirebaseAuth fbAuth;
    private Button diagnosticButton;
    private ConstraintLayout profileBarButton;
    private FirebaseFirestore ff = FirebaseFirestore.getInstance();

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment_home, container, false);

        name = view.findViewById(R.id.username);
        fbAuth = FirebaseAuth.getInstance();
        FirebaseFunctions.GetUserName(fbAuth, name);

        diagnosticButton = view.findViewById(R.id.button_diagnostic);
        diagnosticButton.setOnClickListener(this);

        // Profile Bar button
        profileBarButton = view.findViewById(R.id.status_bar);
        profileBarButton.setOnClickListener(this);

        // Inflate the layout for this fragment
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
        if (v == diagnosticButton)
        {
            Intent intent = new Intent(getActivity(), DiagnosticActivity.class);
            startActivity(intent);
        }
        else if (v == profileBarButton)
        {
            Intent intent = new Intent(getActivity(), ProfileActivity.class);
            startActivity(intent);
        }
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
