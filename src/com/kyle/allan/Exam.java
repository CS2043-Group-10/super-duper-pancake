package com.kyle.allan;

import java.util.ArrayList;

public class Exam {

    ArrayList<Question> examQuestions = new ArrayList<>();
    String examName;
    boolean isOpen;
    int institutionID;

    Exam(String examName,int institutionID){

        this.examName = examName;
        this.institutionID = institutionID;

    }

    public String getExamName() {
        return examName;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public int getInstitutionID() {
        return institutionID;
    }

    public void addToExam(Question question){
        examQuestions.add(question);
    }

    public void printExam(){
        for(int i = 0; i < examQuestions.size(); i++){
            System.out.println(examQuestions.get(i).toString());
        }
    }


}
