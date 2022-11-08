package com.kyle.allan;


public class Question {

    Integer questionNumber;
    String questionDescription;
    String answer;
    Integer score;


    Question(Integer questionNumber, String questionDescription, String answer, Integer score){

        this.questionNumber = questionNumber;
        this.questionDescription = questionDescription;
        this.answer = answer;
        this.score = score;

    }

    @Override
    public String toString() {
        return "Question{" +
                "questionNumber=" + questionNumber +
                ", questionDescription='" + questionDescription + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }

    public Integer getQuestionNumber() {
        return questionNumber;
    }

    public String getQuestionDescription() {
        return questionDescription;
    }

    public String getAnswer() {
        return answer;
    }
}
