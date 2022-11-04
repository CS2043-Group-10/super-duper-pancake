package com.kyle.allan;

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
                System.out.println("Do you want this exam to be open?\nType \"true\" for yes:\nType \"false\" for no:");
                boolean isOpen = scan.nextBoolean();
                String garbage = scan.nextLine();
                System.out.println("What is the filename?");
                String fileName = scan.nextLine();
                System.out.println("What is the path?");
                String path = scan.nextLine();
                path = path.replace("\\","\\\\");
                path = path.concat("\\");
                createExam(path, fileName, examName, instID, isOpen,con);


            }

        }catch(SQLException s){
            System.out.println("sql exception");
        }

    }

    static void createExam(String path, String fileName, String examName, int instID, boolean isOpen, Connection con){

        Integer questionNumber = null;
        String questionDescription = null;
        String answer = null;
        Exam exam = new Exam(examName, instID, isOpen);



            try{
                try(Scanner fileScanner = new Scanner(new File(path + fileName))){

                    insertExam(examName,instID,isOpen,con);

                    while(fileScanner.hasNextLine()){
                        Scanner lineScanner = new Scanner(fileScanner.nextLine());
                        lineScanner.useDelimiter("<>");
                        questionNumber = lineScanner.nextInt();
                        questionDescription = lineScanner.next();
                        answer = lineScanner.next();
                        Question question = new Question(questionNumber,questionDescription,answer);
                        exam.addToExam(question);
                        insertQuestion(con,examName,questionDescription,answer);

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

    static void insertExam(String examName, int instID, boolean isOpen, Connection con){

        try{

            PreparedStatement PrepStat = con.prepareStatement("INSERT into EXAM values (default, ?, ?, ?)");
            PrepStat.setString(1,examName);
            PrepStat.setInt(2,instID);
            PrepStat.setBoolean(3,isOpen);
            PrepStat.execute();

        }catch(SQLException s){
            System.out.println("sql exception");
            s.printStackTrace();
        }


    }

    static void insertQuestion(Connection con, String examName, String questionDescription, String questionAnswer){

        try{



            PreparedStatement PrepStatement  = con.prepareStatement("SELECT exam_id from EXAM where exam_name = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            PrepStatement.setString(1,examName);
            ResultSet ResSet = PrepStatement.executeQuery();
            ResSet.first();
            int exam_id = ResSet.getInt(1);


            PreparedStatement PrepStat = con.prepareStatement("INSERT into QUESTION values(1, ?, ?, ?, 10)");
            PrepStat.setInt(1,exam_id);
            PrepStat.setString(2,questionDescription);
            PrepStat.setString(3,questionAnswer);
            PrepStat.executeUpdate();


        }catch(SQLException s){
            System.out.println("sql exception");
            s.printStackTrace();
        }

    }

}

