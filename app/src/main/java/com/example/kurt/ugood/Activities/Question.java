package com.example.kurt.ugood.Activities;

public class Question {
    private String Question;
    private String Option1;
    private String Option2;
    private String Option3;

    public Question() {}

    public Question(String question, String option1, String option2, String option3) {
        this.Question = question;
        this.Option1 = option1;
        this.Option2 = option2;
        this.Option3 = option3;
        //this.options[3] = option4;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        this.Question = question;
    }

    public String getOption1() {return Option1; }
    public String getOption2() {return Option2; }
    public String getOption3() {return Option3; }

//    public void setOption(int index, String option1) {
//        this.options[index] = option1;
//    }
}
