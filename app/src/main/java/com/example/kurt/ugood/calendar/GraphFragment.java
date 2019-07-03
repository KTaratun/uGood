package com.example.kurt.ugood.calendar;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kurt.ugood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.GregorianCalendar;
import java.text.DateFormat;
import java.util.Map;

import javax.annotation.Nullable;

public class GraphFragment extends Fragment implements View.OnClickListener {

    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    final LocalDate loDate = LocalDate.now();

    private java.util.Calendar date;
    private java.util.Calendar otherDate;
    private DateFormat df;
    private Map<String, String> fbDates;
    private GraphView graph;

    private int pos = 0;
    private FirebaseAuth fbAuth = FirebaseAuth.getInstance();
    private ImageButton left, right;
    private Button week, month, year;
    private TextView type;
    private String lastButton;
    private DocumentReference dR;
    private CollectionReference cR;
    private String userId = fbAuth.getCurrentUser().getUid();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calendar_graph_fragment, container, false);

        fbDates = new HashMap<>();
        type = (TextView) view.findViewById(R.id.week_month_year);
        graph = (GraphView) view.findViewById(R.id.graph);

        df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        date = (GregorianCalendar) GregorianCalendar.getInstance();
        lastButton = getString(R.string.week);
        GetWeek();

        SetUpButtons(view);

        // Inflate the layout for this fragment
        return view;
    }

    private void CreateMood(FirebaseAuth FBAuth)
    {
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference quoteRef;

        if (FBAuth.getCurrentUser() != null)
        {
            quoteRef = DBRef.child("Users").child(FBAuth.getCurrentUser().getUid()).child("Records");

            quoteRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String[] minusYear = loDate.toString().split("-");
                    String noYear = minusYear[1] + "-" + minusYear[2];
                    int numMoods = 0;
                    int totalMood = 0;

                    // Keep in mind that data and date are too completely different things
                    DataSnapshot dateSnapshot = dataSnapshot.child(noYear);

                    for (DataSnapshot yearSnapShot : dateSnapshot.getChildren()) {
                        DataSnapshot ds = yearSnapShot.child("Date");
                        String ySs = ds.getValue().toString();
                        String loDos = loDate.toString();
                        if (ySs.equals(loDos)) {
                            numMoods++;
                            totalMood += Integer.parseInt(yearSnapShot.child("Mood").getValue().toString());
                        }

                    }
                }

                @Override
                public void onCancelled (@NonNull DatabaseError databaseError){

                }
            });
        }
    }

    private void GetWeekGraph()
    {
        date = (GregorianCalendar) GregorianCalendar.getInstance();
        date.set(Calendar.DAY_OF_WEEK, 1);
        date.add(Calendar.WEEK_OF_YEAR, pos);
        String[] dates = new String[7];

        otherDate = (Calendar)date.clone();
        otherDate.add(Calendar.WEEK_OF_MONTH, 1);

        String currDay;
        List<DataPoint> data = new ArrayList<DataPoint>();

        for (int n = 0; n < 7; n++) {

            currDay = df.format(date.getTime());
            dates[n] = new SimpleDateFormat("MM").format(date.getTime()) + "/\n" +
                    new SimpleDateFormat("dd").format(date.getTime()) + "/\n" +
                    new SimpleDateFormat("yy").format(date.getTime());
            if (fbDates.containsKey(currDay))
            {
                DataPoint dat = new DataPoint(n, Float.parseFloat(fbDates.get(currDay)));
                data.add(dat);
            }

            date.add(GregorianCalendar.DATE, 1);
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(data.toArray(new DataPoint[0]));

        //String[] dates = new String[] {"Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat", ""};
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph, dates, new String[]{"0", "1", "2", "3", "4", "5"});


        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graph.removeAllSeries();
        graph.addSeries(series);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(6);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(5);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setNumVerticalLabels(6);
        graph.getGridLabelRenderer().setNumHorizontalLabels(7);
        graph.getGridLabelRenderer().setHumanRounding(true);
        graph.getGridLabelRenderer().setLabelHorizontalHeight(160);
    }

    private void GetMonthGraph()
    {
        date = (GregorianCalendar) GregorianCalendar.getInstance();
        // For some reason getting this string "q" helps things
        String q = df.format(date.getTime());
        date.set(Calendar.DAY_OF_MONTH, 1);
        q = df.format(date.getTime());
        date.set(Calendar.DAY_OF_WEEK, 1);
        q = df.format(date.getTime());
        date.add(Calendar.MONTH, pos);

        String[] dates = new String[fbDates.size()];

        List<DataPoint> data = new ArrayList<DataPoint>();

        String currweek;

        for (int n = 0; n < fbDates.size(); n++) {

            otherDate = (Calendar)date.clone();
            otherDate.add(Calendar.WEEK_OF_YEAR, 1);
            otherDate.add(Calendar.DAY_OF_YEAR, -1);
            dates[n] = new SimpleDateFormat("MM").format(date.getTime()) + "/\n" +
                    new SimpleDateFormat("dd").format(date.getTime()) + "-\n" +
                    new SimpleDateFormat("MM").format(otherDate.getTime()) + "/\n" +
                    new SimpleDateFormat("dd").format(otherDate.getTime());

            currweek = df.format(date.getTime()) + "=" + df.format(otherDate.getTime());
            if (fbDates.containsKey(currweek))
            {
                DataPoint dat = new DataPoint(n, Float.parseFloat(fbDates.get(currweek)));
                data.add(dat);
            }

            date.add(Calendar.WEEK_OF_YEAR, 1);

        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(data.toArray(new DataPoint[0]));

        //String[] dates = new String[] {"Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat", ""};
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph, dates, new String[]{"0", "1", "2", "3", "4", "5"});


        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graph.removeAllSeries();
        graph.addSeries(series);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(fbDates.size() - 1);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(5);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setNumVerticalLabels(6);
        graph.getGridLabelRenderer().setNumHorizontalLabels(fbDates.size());
        graph.getGridLabelRenderer().setHumanRounding(true);
        graph.getGridLabelRenderer().setLabelHorizontalHeight(210);
    }

    private void GetYearGraph()
    {
        date = (GregorianCalendar) GregorianCalendar.getInstance();
        date.set(Calendar.DAY_OF_YEAR, date.getActualMinimum(Calendar.DAY_OF_YEAR));
        date.add(Calendar.YEAR, pos);

        String currMonth;

        String[] dates = new String[12];

        List<DataPoint> data = new ArrayList<DataPoint>();

        for (int i = 0; i < 12; i++)
        {
            dates[i] = new SimpleDateFormat("MMM").format(date.getTime());

            currMonth = new SimpleDateFormat("MMMM").format(date.getTime());
            if (fbDates.containsKey(currMonth))
            {
                DataPoint dat = new DataPoint(i, Float.parseFloat(fbDates.get(currMonth)));
                data.add(dat);
            }

            date.add(Calendar.MONTH, 1);
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(data.toArray(new DataPoint[0]));

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph, dates, new String[]{"0", "1", "2", "3", "4", "5"});


        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graph.removeAllSeries();
        graph.addSeries(series);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(11);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(5);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setNumVerticalLabels(6);
        graph.getGridLabelRenderer().setNumHorizontalLabels(12);
        graph.getGridLabelRenderer().setHumanRounding(true);
        graph.getGridLabelRenderer().setLabelHorizontalHeight(80);
    }

    private void GetWeek()
    {
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference quoteRef;

        if (fbAuth.getCurrentUser() != null)
        {
            date.set(Calendar.DAY_OF_WEEK, 1);
            date.add(Calendar.WEEK_OF_YEAR, pos);
            fbDates.clear();
            String datStr = df.format(date.getTime());
            otherDate = (Calendar)date.clone();
            otherDate.add(Calendar.WEEK_OF_MONTH, 1);
            otherDate.add(Calendar.DAY_OF_MONTH, -1);
            String otherStr = df.format(otherDate.getTime());
            String week = datStr + "=" + otherStr;

            dR = FirebaseFirestore.getInstance().collection("Users").document(userId)
                    .collection("Mood-Weeks").document(week);

            int weekNum = date.get(Calendar.WEEK_OF_MONTH);
            int otherWeekNum = otherDate.get(Calendar.WEEK_OF_MONTH);
            if (weekNum != otherWeekNum)
                type.setText("WEEK " + weekNum + "/" + otherWeekNum);
            else
                type.setText("WEEK " + weekNum);

            dR.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Toast.makeText(getActivity(), "Error while loading!", Toast.LENGTH_SHORT);
                        Log.d("tag", e.toString());
                        return;
                    }

                    if (documentSnapshot.exists()){
                        for(Map.Entry moodData : documentSnapshot.getData().entrySet())
                        {
                            if (moodData.getKey().equals("Mood"))
                                continue;

                            fbDates.put(moodData.getKey().toString(), moodData.getValue().toString());
                        }
                    }
                    GetWeekGraph();
                }
            });
        }
    }

    private void GetMonth()
    {
        if (fbAuth.getCurrentUser() != null)
        {
            fbDates.clear();
            date.add(Calendar.MONTH, pos);

            cR = FirebaseFirestore.getInstance().collection("Users").document(userId)
                    .collection("Mood-Weeks");

            type.setText(new SimpleDateFormat("MMMM").format(date.getTime()));

            cR.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                        Collections.reverse(myListOfDocuments);
                        Iterator<DocumentSnapshot> it = myListOfDocuments.iterator();

                        DocumentSnapshot currDoc;
                        while (true)
                        {
                            if (it.hasNext())
                                currDoc = it.next();
                            else
                                break;

                            String[] id = currDoc.getId().split("=");
                            String beginningDayMonth = id[0].split("-")[1];
                            String beginningDayYear = id[0].split("-")[0];
                            String endingDayMonth = id[1].split("-")[1];
                            String endingDayYear = id[1].split("-")[0];
                            String currMonth = new SimpleDateFormat("MM").format(date.getTime());
                            String currYear = new SimpleDateFormat("yyyy").format(date.getTime());

                            if (beginningDayMonth.equals(currMonth) ||
                                    endingDayMonth.equals(currMonth))
                                fbDates.put(currDoc.getId(), currDoc.getData().get("Mood").toString());
                            else if (Integer.parseInt(beginningDayMonth) < Integer.parseInt(currMonth) ||
                                    Integer.parseInt(beginningDayYear) != Integer.parseInt(currYear) ||
                                    Integer.parseInt(endingDayMonth) < Integer.parseInt(currMonth) ||
                                    Integer.parseInt(endingDayYear) != Integer.parseInt(currYear))
                                break;
                        }

                        GetMonthGraph();
                    }
                }
            });
        }
    }

    private void GetYear()
    {
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference quoteRef;

        if (fbAuth.getCurrentUser() != null)
        {
            date.set(Calendar.DAY_OF_YEAR, date.getActualMinimum(Calendar.DAY_OF_YEAR));
            date.add(Calendar.YEAR, pos);
            fbDates.clear();
            String datStr = df.format(date.getTime());
            String year = datStr.split("-")[0];

            dR = FirebaseFirestore.getInstance().collection("Users").document(userId)
                    .collection("Mood-Months").document(year);

            type.setText(new SimpleDateFormat("YYYY").format(date.getTime()));

            dR.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Toast.makeText(getActivity(), "Error while loading!", Toast.LENGTH_SHORT);
                        Log.d("tag", e.toString());
                        return;
                    }

                    if (documentSnapshot.exists()){
                        for(Map.Entry moodData : documentSnapshot.getData().entrySet())
                            fbDates.put(moodData.getKey().toString(), moodData.getValue().toString());
                    }
                    GetYearGraph();
                }
            });
        }
    }

    private void SetUpButtons(View view)
    {
        left = (ImageButton) view.findViewById(R.id.left_button);
        left.setOnClickListener(this);

        right = (ImageButton) view.findViewById(R.id.right_button);
        right.setOnClickListener(this);

        week = (Button) view.findViewById(R.id.week_display);
        week.setOnClickListener(this);

        month = (Button) view.findViewById(R.id.month_display);
        month.setOnClickListener(this);

        year = (Button) view.findViewById(R.id.year_display);
        year.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == left)
        {
            pos--;
            date = (GregorianCalendar) GregorianCalendar.getInstance();

            if (lastButton.equals("Week"))
                GetWeek();
            else if (lastButton.equals("Month"))
                GetMonth();
            else if (lastButton.equals("Year"))
                GetYear();
        }
        else if (v == right)
        {
            pos++;
            date = (GregorianCalendar) GregorianCalendar.getInstance();

            if (lastButton.equals("Week"))
                GetWeek();
            else if (lastButton.equals("Month"))
                GetMonth();
            else if (lastButton.equals("Year"))
                GetYear();
        }
        else if (v == week)
        {
            date = (GregorianCalendar) GregorianCalendar.getInstance();
            pos = 0;
            lastButton = getString(R.string.week);
            type.setText(getString(R.string.week));
            GetWeek();
        }
        else if (v == month)
        {
            date = (GregorianCalendar) GregorianCalendar.getInstance();
            pos = 0;
            lastButton = getString(R.string.month);
            type.setText(getString(R.string.month));
            GetMonth();
        }
        else if (v == year)
        {
            date = (GregorianCalendar) GregorianCalendar.getInstance();
            pos = 0;
            lastButton = getString(R.string.year);
            type.setText(getString(R.string.year));
            GetYear();
        }
    }
}
