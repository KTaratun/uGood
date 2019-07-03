package com.example.kurt.ugood.diagnostic.Classes;

public class Question {
    private String Question;
    private String Option1;
    private String Option2;
    private String Option3;
    private String Option4;
    private String Option5;

    public Question() {}

    public Question(String question, String option1, String option2, String option3, String option4, String option5) {
        this.Question = question;
        this.Option1 = option1;
        this.Option2 = option2;
        this.Option3 = option3;
        this.Option4 = option4;
        this.Option5 = option5;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) { this.Question = question; }
    public void setOption1(String option) { this.Option1 = option; }
    public void setOption2(String option) { this.Option2 = option; }
    public void setOption3(String option) { this.Option3 = option; }
    public void setOption4(String option) { this.Option4 = option; }
    public void setOption5(String option) { this.Option5 = option; }
    public void setItAll(String question, String option1, String option2, String option3, String option4, String option5)
    {this.Question = question; this.Option1 = option1; this.Option2 = option2; this.Option3 = option3; this.Option4 = option4; this.Option5 = option5;}

    public String getOption1() {return Option1; }
    public String getOption2() {return Option2; }
    public String getOption3() {return Option3; }
    public String getOption4() {return Option4; }
    public String getOption5() {return Option5; }
}
