package com.example.kurt.ugood.diagnostic.Classes;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.kurt.ugood.R;

import java.util.List;

public class QuestionAdapter extends ArrayAdapter<Question> {

    private Activity context;
    private List<Question> questionList;

    public QuestionAdapter(Activity context, List<Question> questionList){
        super(context, R.layout.diagnostic_activity, questionList);
        this.context = context;
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View view = inflater.inflate(R.layout.diagnostic_question_list, parent, true);

        TextView question = view.findViewById((R.id.questionText));

        RadioButton option1 = view.findViewById((R.id.radio_button1));
        RadioButton option2 = view.findViewById((R.id.radio_button2));
        RadioButton option3 = view.findViewById((R.id.radio_button3));

        Question quest = questionList.get(position);

        question.setText((quest.getQuestion()));

        option1.setText(quest.getOption1());
        option2.setText(quest.getOption2());
        option3.setText(quest.getOption3());

        return view;
    }
}
