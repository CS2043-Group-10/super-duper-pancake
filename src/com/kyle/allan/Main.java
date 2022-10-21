package com.kyle.allan;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;


public class Main {

    public static void main(String[] args) {




            Scanner scan = new Scanner(System.in);
            System.out.println("What is the exam name?");
            String examName = scan.nextLine();
            System.out.println("Do you want this exam to be open?\nType \"true\" for yes:\nType \"false\" for no:");
            boolean isOpen = scan.nextBoolean();
            System.out.println("Filename?");
            String fileName = scan.nextLine();
            System.out.println("Path?");
            String path = scan.nextLine();
            path = path.replace("\\","\\\\");
            path = path.concat("\\");
            createExam(path, fileName);







    }

    static void createExam(String path, String fileName){

        Integer questionNumber = null;
        String questionDescription = null;
        String answer = null;
        Exam exam = new Exam();

        try{
            String connectionString = "jdbc:mysql://47.54.75.83:3306/oems";
            Connection con = DriverManager.getConnection(connectionString, "test", "password");
            Statement command = con.createStatement();


            try{
                try(Scanner fileScanner = new Scanner(new File(path + fileName))){


                    while(fileScanner.hasNextLine()){
                        Scanner lineScanner = new Scanner(fileScanner.nextLine());
                        lineScanner.useDelimiter("<>");
                        questionNumber = lineScanner.nextInt();
                        questionDescription = lineScanner.next();
                        answer = lineScanner.next();
                        Question question = new Question(questionNumber,questionDescription,answer);
                        exam.addToExam(question);

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



        }catch(SQLException s){
            System.out.println("sql exception");
        }



    }

}

