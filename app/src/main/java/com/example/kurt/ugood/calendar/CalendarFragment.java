package com.example.kurt.ugood.calendar;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kurt.ugood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

public class CalendarFragment extends Fragment implements View.OnClickListener{

    public GregorianCalendar cal_month, cal_month_copy;
    private HwAdapter hwAdapter;
    private TextView tv_month;
    private FirebaseAuth FBAuth;
    private Button graph;
    private CollectionReference cR;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_fragment, container, false);

        FBAuth = FirebaseAuth.getInstance();

        graph = (Button) view.findViewById(R.id.graphButton);
        graph.setOnClickListener(this);

        GetRecords(FBAuth);

        // Inflate the layout for this fragment
        return view;
    }

    private void GetRecords(FirebaseAuth FBAuth)
    {
        if (FBAuth.getCurrentUser() != null)
        {
            String userId = FBAuth.getCurrentUser().getUid();
            cR = FirebaseFirestore.getInstance().collection("Users").document(userId)
                    .collection("Records");

            cR.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                        Collections.reverse(myListOfDocuments);
                        Iterator<DocumentSnapshot> it = myListOfDocuments.iterator();

                        // the month
                        DocumentSnapshot currDoc;
                        while (true) {
                            if (it.hasNext())
                                currDoc = it.next();
                            else
                                break;

                            HomeCollection homC = new HomeCollection();
                            homC.setContent(currDoc.get("Content").toString());
                            homC.setDate(currDoc.get("Date").toString());
                            homC.setMood(currDoc.get("Mood").toString());
                            homC.setTitle(currDoc.get("Title").toString());

                            String[] dateKeyElements = homC.getDate().split("-");
                            String dateKey = dateKeyElements[1] + "-" + dateKeyElements[2];

                            if (!HomeCollection.date_collection_arr.containsKey(dateKey))
                                HomeCollection.date_collection_arr.put(dateKey, new ArrayList<HomeCollection>());

                            HomeCollection.date_collection_arr.get(dateKey).add(homC);
                        }
                    }
                    ContinueCreate();
                }
            });
        }
    }

    private void ContinueCreate()
    {
        cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
        cal_month_copy = (GregorianCalendar) cal_month.clone();
        hwAdapter = new HwAdapter(getActivity(), cal_month,HomeCollection.date_collection_arr);

        tv_month = (TextView) getActivity().findViewById(R.id.tv_month);
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));

        ImageButton previous = (ImageButton) getActivity().findViewById(R.id.ib_prev);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cal_month.get(GregorianCalendar.MONTH) == 4&&cal_month.get(GregorianCalendar.YEAR)==2017) {
                    //cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
                    Toast.makeText(getActivity(), "Event Detail is available for current session only.", Toast.LENGTH_SHORT).show();
                }
                else {
                    setPreviousMonth();
                    refreshCalendar();
                }


            }
        });

        ImageButton next = (ImageButton) getActivity().findViewById(R.id.Ib_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cal_month.get(GregorianCalendar.MONTH) == 5&&cal_month.get(GregorianCalendar.YEAR)==2018) {
                    //cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1), cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
                    Toast.makeText(getActivity(), "Event Detail is available for current session only.", Toast.LENGTH_SHORT).show();
                }
                else {
                    setNextMonth();
                    refreshCalendar();
                }
            }
        });
        GridView gridview = (GridView) getActivity().findViewById(R.id.gv_calendar);
        gridview.setAdapter(hwAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String selectedGridDate = HwAdapter.day_string.get(position);
                ((HwAdapter) parent.getAdapter()).getPositionList(selectedGridDate, getActivity());
            }

        });
    }

    protected void setNextMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMaximum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1), cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) + 1);
        }
    }

    protected void setPreviousMonth() {
        if (cal_month.get(GregorianCalendar.MONTH) == cal_month.getActualMinimum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1), cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH, cal_month.get(GregorianCalendar.MONTH) - 1);
        }
    }

    public void refreshCalendar() {
        hwAdapter.refreshDays();
        hwAdapter.notifyDataSetChanged();
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
    }


    @Override
    public void onClick(View v) {

        if (v == graph)
        {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new GraphFragment()).commit();
        }

    }
}
