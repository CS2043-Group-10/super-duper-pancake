package com.kyle.allan;

import java.util.ArrayList;

public class Exam {

    ArrayList<Question> examQuestions = new ArrayList<>();
    String name;
    boolean isOpen;



    public void addToExam(Question question){
        examQuestions.add(question);
    }

    public void printExam(){
        for(int i = 0; i < examQuestions.size(); i++){
            System.out.println(examQuestions.get(i).toString());
        }
    }


}
