package com.kyle.allan;


public class Question {

    Integer questionNumber;
    String questionDescription;
    String answer;


    Question(Integer questionNumber, String questionDescription, String answer){

        this.questionNumber = questionNumber;
        this.questionDescription = questionDescription;
        this.answer = answer;

    }

    @Override
    public String toString() {
        return "Question{" +
                "questionNumber=" + questionNumber +
                ", questionDescription='" + questionDescription + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
