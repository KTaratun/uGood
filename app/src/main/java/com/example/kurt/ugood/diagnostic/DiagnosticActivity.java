package com.example.kurt.ugood.diagnostic;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.kurt.ugood.diagnostic.Classes.Question;
import com.example.kurt.ugood.diagnostic.Classes.QuestionAdapter;
import com.example.kurt.ugood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DiagnosticActivity extends AppCompatActivity {

    DatabaseReference databaseQuestions;
    List<Question> questionList;

    ListView questionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diagnostic_activity);

        questionView = findViewById(R.id.questionList);
        questionList = new ArrayList<>();

        databaseQuestions = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference databaseQuestionsref = databaseQuestions.child("Questions");

        databaseQuestionsref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        questionList.clear();

                        for(DataSnapshot questionSnapshot : dataSnapshot.getChildren()){
                            Question quest = questionSnapshot.getValue(Question.class);

                            questionList.add(quest);
                        }

                        QuestionAdapter adapter = new QuestionAdapter(DiagnosticActivity.this, questionList);
                        questionView.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
