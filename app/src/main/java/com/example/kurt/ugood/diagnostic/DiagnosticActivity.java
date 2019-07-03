package com.example.kurt.ugood.diagnostic;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.kurt.ugood.diagnostic.Classes.Question;
import com.example.kurt.ugood.diagnostic.Classes.QuestionAdapter;
import com.example.kurt.ugood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DiagnosticActivity extends AppCompatActivity {

    DatabaseReference databaseQuestions;
    List<Question> questionList;
    Button backButton;

    ListView questionView;
    private CollectionReference cR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diagnostic_activity);

        questionView = findViewById(R.id.questionList);
        questionList = new ArrayList<>();

        databaseQuestions = FirebaseDatabase.getInstance().getReference();

        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        cR = FirebaseFirestore.getInstance().collection("Questions");

        cR.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    questionList.clear();

                    List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                    Collections.reverse(myListOfDocuments);
                    Random rand = new Random();
                    DocumentSnapshot randDoc = myListOfDocuments.get(rand.nextInt(myListOfDocuments.size()));

                    Question quest = new Question(randDoc.get("Question").toString(), randDoc.get("Option1").toString(),
                            randDoc.get("Option2").toString(), randDoc.get("Option3").toString(),
                            randDoc.get("Option4").toString(), randDoc.get("Option5").toString());

                    questionList.add(quest);
                    }

                QuestionAdapter adapter = new QuestionAdapter(DiagnosticActivity.this, questionList);
                questionView.setAdapter(adapter);
            }
        });
    }
}
