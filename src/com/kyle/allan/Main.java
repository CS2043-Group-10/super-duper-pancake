package com.kyle.allan;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;



public class Main {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Filename?");
        String fileName = scan.nextLine();
        System.out.println("Path?");
        String path = scan.nextLine();
        readTextFile(path, fileName);


    }

    static void readTextFile(String path, String fileName){

        Integer questionNumber = null;
        String questionDescription = null;
        String answer = null;
        Exam exam = new Exam();



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
            System.out.println("fnfe exception");
            fnfe.printStackTrace();
        }
        catch(NoSuchElementException nsee){
            System.out.println("nsee ecxeption");
        }
        //System.out.println(questionNumber + " " + questionDescription + " " + answer);
        exam.printExam();



    }

}

