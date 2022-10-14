package com.kyle.allan;

import java.io.*;
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

    static void readTextFile(String path, String fileName)throws NoSuchElementException{

        try{
            try(Scanner scanner = new Scanner(new File(path + fileName))){

                while(scanner.hasNextLine()){
                    String question = scanner.nextLine();
                    String answer = scanner.nextLine();
                    if(scanner.nextLine() == "$"){
                        scanner.skip("$");
                        String garbageAfter = scanner.nextLine();
                    }
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

    }

}

