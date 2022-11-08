package com.kyle.allan;

import java.sql.Date;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;


public class Main {

    public static void main(String[] args) {

        try{
            String connectionString = "jdbc:mysql://47.54.75.83:3306/oems";
            Connection con = DriverManager.getConnection(connectionString, "test", "password");


            if(con != null){

                Scanner scan = new Scanner(System.in);
                int instID = 1;
                System.out.println("What is the exam name?");
                String examName = scan.nextLine();
                System.out.println("When does this exam start? (yyyy-mm-dd)");
                Date examStartDate = Date.valueOf(scan.nextLine());
                System.out.println("When does this exam end? (yyyy-mm-dd)");
                Date examEndDate = Date.valueOf(scan.nextLine());
                System.out.println("What is the filename?");
                String fileName = scan.nextLine();
                System.out.println("What is the path?");
                String path = scan.nextLine();
                path = path.replace("\\","\\\\");
                path = path.concat("\\");
                createExam(path, fileName, examName, instID, examStartDate, examEndDate, con);


                System.out.println("Do you want to add a grade for an exam? If so type \"true\": If no type \"false\":");
                boolean addAGrade = scan.nextBoolean();
                String garbage = scan.nextLine();
                if(addAGrade == true ){
                    System.out.println("What is the question id?");
                    int questionID = scan.nextInt();
                    garbage = scan.nextLine();
                    System.out.println("Enter the grade to be added:");
                    double grade = scan.nextDouble();
                    garbage = scan.nextLine();
                    System.out.println("Was the answer correct? Type \"Right\" for correct:\nType \"Wrong\" for incorrect:");
                    String answerOutcome = scan.nextLine();

                    PreparedStatement PrepStat = con.prepareStatement("Select exam_id from EXAM where exam_name = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    PrepStat.setString(1,examName);
                    ResultSet ResSet = PrepStat.executeQuery();
                    ResSet.first();
                    int exam_id = ResSet.getInt(1);

                    addGradeForQuestion(con,1,3,questionID,grade,answerOutcome);
                }




            }

        }catch(SQLException s){
            System.out.println("sql exception");
        }

    }

    static void createExam(String path, String fileName, String examName, int instID, Date startDate, Date endDate,Connection con){

        Integer questionNumber = null;
        String questionDescription = null;
        String answer = null;
        Integer score = null;
        Exam exam = new Exam(examName, instID, startDate, endDate);



            try{
                try(Scanner fileScanner = new Scanner(new File(path + fileName))){

                    insertExam(examName,instID, startDate, endDate, con);

                    while(fileScanner.hasNextLine()){
                        Scanner lineScanner = new Scanner(fileScanner.nextLine());
                        lineScanner.useDelimiter("<>");
                        questionNumber = lineScanner.nextInt();
                        questionDescription = lineScanner.next();
                        answer = lineScanner.next();
                        score = lineScanner.nextInt();

                        Question question = new Question(questionNumber,questionDescription,answer,score);
                        exam.addToExam(question);
                        insertQuestion(con,examName,questionDescription,answer,score);

                    }

                }

            }
            catch(FileNotFoundException fnfe){
                System.out.println("File could not be found.");
            }
            catch(NoSuchElementException nsee){
                System.out.println("nsee exception");
            }
            exam.printExam();

    }

    static void insertExam(String examName, int instID, Date startDate, Date endDate, Connection con){

        try{

            PreparedStatement PrepStat = con.prepareStatement("INSERT into EXAM values (default, ?, ?, ?, ?)");
            PrepStat.setString(1,examName);
            PrepStat.setInt(2,instID);
            PrepStat.setDate(3,startDate);
            PrepStat.setDate(4,endDate);
            PrepStat.execute();

        }catch(SQLException s){
            System.out.println("sql exception");
            s.printStackTrace();
        }


    }

    static void insertQuestion(Connection con, String examName, String questionDescription, String questionAnswer, Integer score){

        try{



            PreparedStatement PrepStatement  = con.prepareStatement("SELECT exam_id from EXAM where exam_name = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            PrepStatement.setString(1,examName);
            ResultSet ResSet = PrepStatement.executeQuery();
            ResSet.first();
            int exam_id = ResSet.getInt(1);


            PreparedStatement PrepStat = con.prepareStatement("INSERT into QUESTION values(1, ?, ?, ?, ?)");
            PrepStat.setInt(1,exam_id);
            PrepStat.setString(2,questionDescription);
            PrepStat.setString(3,questionAnswer);
            PrepStat.setInt(4,score);
            PrepStat.executeUpdate();


        }catch(SQLException s){
            System.out.println("sql exception");
            s.printStackTrace();
        }

    }

    static void addGradeForQuestion(Connection con, int userID, int examID, int questionID, double score, String theirAnswer){

        try{

            PreparedStatement PrepStat = con.prepareStatement("INSERT into participant_answer values(?, ?, ?, ?, ?)");
            PrepStat.setInt(1,userID);
            PrepStat.setInt(2,examID);
            PrepStat.setInt(3,questionID);
            PrepStat.setString(4,theirAnswer);
            PrepStat.setDouble(5,score);
            PrepStat.executeUpdate();


        }catch(SQLException s){
        System.out.println("sql exception");
        s.printStackTrace();
    }





    }

}

