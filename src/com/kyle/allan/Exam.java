package com.kyle.allan;

import java.sql.Date;
import java.util.ArrayList;

public class Exam {

    ArrayList<Question> examQuestions = new ArrayList<>();
    String examName;
    int institutionID;
    Date startDate;
    Date endDate;

    Exam(String examName,int institutionID,Date startDate,Date endDate){

        this.examName = examName;
        this.institutionID = institutionID;
        this.startDate = startDate;
        this.endDate = endDate;

    }

    public String getExamName() {
        return examName;
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
