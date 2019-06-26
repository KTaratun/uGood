package com.example.kurt.ugood.calendar;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kurt.ugood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.GregorianCalendar;
import java.text.DateFormat;
import java.util.Map;

public class GraphActivity extends AppCompatActivity {

    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    final LocalDate loDate = LocalDate.now();

    private java.util.Calendar date;
    private java.util.Calendar otherDate;
    DateFormat df;
    Map<String, String> fbDates;
    GraphView graph;

    int pos = 0;
    FirebaseAuth fbAuth;
    ImageButton left, right;
    Button week, month, year;
    TextView type;
    String lastButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_graph_activity);

        fbDates = new HashMap<>();
        type = (TextView) findViewById(R.id.week_month_year);
        graph = (GraphView) findViewById(R.id.graph);

        fbAuth = FirebaseAuth.getInstance();

        df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        date = (GregorianCalendar) GregorianCalendar.getInstance();
        lastButton = getString(R.string.week);
        GetWeek();

        SetUpButtons();
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
                DataPoint dat = new DataPoint(n, Integer.parseInt(fbDates.get(currDay)));
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

        String[] dates = new String[6];

        List<DataPoint> data = new ArrayList<DataPoint>();

        String currweek;

        for (int n = 0; n < 6; n++) {

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
        graph.getViewport().setMaxX(5);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(5);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setNumVerticalLabels(6);
        graph.getGridLabelRenderer().setNumHorizontalLabels(6);
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
            String q = df.format(date.getTime());
            date.add(Calendar.WEEK_OF_YEAR, pos);
            q = df.format(date.getTime());
            fbDates.clear();
            String datStr = df.format(date.getTime());
            Calendar otherMonth = (Calendar)date.clone();
            otherMonth.add(Calendar.WEEK_OF_MONTH, 1);
            otherMonth.add(Calendar.DAY_OF_MONTH, -1);
            String otherStr = df.format(otherMonth.getTime());
            String year = datStr.split("-")[0];
            final String month = new SimpleDateFormat("MMMM").format(date.getTime());
            String week = datStr + "=" + otherStr;

            quoteRef = DBRef.child("Users").child(fbAuth.getCurrentUser().getUid()).child("Moods")
            .child(year).child(month).child(week);

            int weekNum = date.get(Calendar.WEEK_OF_MONTH);
            int otherWeekNum = otherMonth.get(Calendar.WEEK_OF_MONTH);
            if (weekNum != otherWeekNum)
                type.setText("WEEK " + weekNum + "/" + otherWeekNum);
            else
                type.setText("WEEK " + weekNum);

            quoteRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot moodSnapshot : dataSnapshot.getChildren())
                    {
                        if (moodSnapshot.getKey().equals("Mood"))
                            continue;

                        fbDates.put(moodSnapshot.getKey().toString(), moodSnapshot.getValue().toString());
                    }
                    GetWeekGraph();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void GetMonth()
    {
        DatabaseReference DBRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference quoteRef;

        if (fbAuth.getCurrentUser() != null)
        {
            fbDates.clear();
            date.add(Calendar.MONTH, pos);
            String datStr = df.format(date.getTime());
            String year = datStr.split("-")[0];
            final String month = new SimpleDateFormat("MMMM").format(date.getTime());

            quoteRef = DBRef.child("Users").child(fbAuth.getCurrentUser().getUid()).child("Moods")
                    .child(year).child(month);

            type.setText(new SimpleDateFormat("MMMM").format(date.getTime()));

            quoteRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot moodSnapshot : dataSnapshot.getChildren())
                    {
                        if (moodSnapshot.getKey().equals("Mood"))
                            continue;
                        fbDates.put(moodSnapshot.getKey(), moodSnapshot.child("Mood").getValue().toString());
                    }

                    GetMonthGraph();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

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

            quoteRef = DBRef.child("Users").child(fbAuth.getCurrentUser().getUid()).child("Moods")
                    .child(year);

            type.setText(new SimpleDateFormat("YYYY").format(date.getTime()));

            quoteRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot moodSnapshot : dataSnapshot.getChildren())
                        fbDates.put(moodSnapshot.getKey(), moodSnapshot.child("Mood").getValue().toString());

                    GetYearGraph();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void SetUpButtons()
    {
        left = (ImageButton) findViewById(R.id.left_button);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos--;
                date = (GregorianCalendar) GregorianCalendar.getInstance();

                if (lastButton.equals("Week"))
                    GetWeek();
                else if (lastButton.equals("Month"))
                    GetMonth();
                else if (lastButton.equals("Year"))
                    GetYear();
            }
        });

        right = (ImageButton) findViewById(R.id.right_button);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pos++;
                date = (GregorianCalendar) GregorianCalendar.getInstance();

                if (lastButton.equals("Week"))
                    GetWeek();
                else if (lastButton.equals("Month"))
                    GetMonth();
                else if (lastButton.equals("Year"))
                    GetYear();
            }
        });

        week = (Button) findViewById(R.id.week_display);
        week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = (GregorianCalendar) GregorianCalendar.getInstance();
                pos = 0;
                lastButton = getString(R.string.week);
                type.setText(getString(R.string.week));
                GetWeek();
            }
        });

        month = (Button) findViewById(R.id.month_display);
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = (GregorianCalendar) GregorianCalendar.getInstance();
                pos = 0;
                lastButton = getString(R.string.month);
                type.setText(getString(R.string.month));
                GetMonth();
            }
        });

        year = (Button) findViewById(R.id.year_display);
        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = (GregorianCalendar) GregorianCalendar.getInstance();
                pos = 0;
                lastButton = getString(R.string.year);
                type.setText(getString(R.string.year));
                GetYear();
            }
        });
    }
}
