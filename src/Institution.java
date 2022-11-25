import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Institution {

    private String name;


    public Institution(String instName) {

        name = instName;

    }

    public String getName() {
        return name;
    }

    public int getID(Connection con) {

        int instID = 0;

        try {

            PreparedStatement pS = con.prepareStatement("SELECT institution_id FROM INSTITUTION WHERE institution_name = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pS.setString(1, name);
            ResultSet rS = pS.executeQuery();
            rS.first();
            instID = rS.getInt(1);


        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return instID;
    }

    public ArrayList<User> getUsers(Connection con) {

        ArrayList<User> userList = new ArrayList<>();
        int instID = getID(con);

        try {

            PreparedStatement pS = con.prepareStatement("SELECT user_name FROM USER WHERE institution_id = ?");
            pS.setInt(1,instID);
            ResultSet rS = pS.executeQuery();
            while (rS.next()) {
                String userName = rS.getString(1);
                User tempUser =  new User(userName);
               userList.add(tempUser);

            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return userList;
    }




    public ArrayList<Participant> getUnregisteredUsersForExam(Connection con, Exam exam) {

        ArrayList<Participant> unRegUserList = new ArrayList<>();
        int examID = exam.getID(con);

        try {

            PreparedStatement pS = con.prepareStatement("SELECT user_id FROM PARTICIPANT WHERE exam_id = ? AND participant_isreg = false");
            pS.setInt(1,examID);
            ResultSet rS = pS.executeQuery();
            while (rS.next()) {
                int userID = rS.getInt(1);
                Participant tempParticipant =  new Participant(userID, examID);
                unRegUserList.add(tempParticipant);

            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return unRegUserList;
    }

    public void registerUserForExam(Connection con, Participant participant) {

        try {

            PreparedStatement pS = con.prepareStatement("UPDATE PARTICIPANT SET participant_isreg = true WHERE user_id = ? AND exam_id = ?");
            pS.setInt(1, participant.getUserID());
            pS.setInt(2, participant.getExamID());

            try {
                pS.execute();
            } catch (SQLIntegrityConstraintViolationException sqle) {
                System.out.println("User already registered");
            }


        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }


    public ArrayList<Exam> getFinishedExams(Connection con) {

        ArrayList<Exam> examList = new ArrayList<>();
        int instID = getID(con);

        try {

            PreparedStatement pS = con.prepareStatement("SELECT exam_id, exam_name, exam_start, exam_end FROM EXAM WHERE SYSDATE() > exam_end AND institution_id = ?");
            pS.setInt(1, instID);
            ResultSet rS = pS.executeQuery();
            while (rS.next()) {
                int examID = rS.getInt(1);
                String examName = rS.getString(2);
                Date sDate = rS.getDate(3);
                Date eDate = rS.getDate(4);
                Exam temp = new Exam(examName, instID, sDate, eDate);
                examList.add(temp);

            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();

        }

        return examList;
    }

    public ArrayList<Participant> viewExamParticipants(Connection con, Exam exam) {

        ArrayList<Participant> participantList = new ArrayList<>();
        int examID = exam.getID(con);

        try {

            PreparedStatement pS = con.prepareStatement("SELECT user_id FROM PARTICIPANT WHERE exam_id = ? AND participant_complete = 1");
            pS.setInt(1, examID);
            ResultSet rS = pS.executeQuery();
            while (rS.next()) {
                int userID = rS.getInt(1);
                Participant temp = new Participant(userID, examID);
                participantList.add(temp);

            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return participantList;
    }



    public ArrayList<AnsweredQuestion> getWrittenExam(Connection con, Participant participant) {

        ArrayList<AnsweredQuestion> answeredList = new ArrayList<>();

        int userID = participant.getUserID();
        int examID = participant.getExamID();

        try {

            PreparedStatement pS = con.prepareStatement("SELECT question_id, question_q, question_a, participant_answer_a FROM QUESTION NATURAL JOIN PARTICIPANT_ANSWER WHERE user_id = ? AND exam_id = ?");
            pS.setInt(1, userID);
            pS.setInt(2,examID);
            ResultSet rS = pS.executeQuery();
            while (rS.next()) {
                int questionID = rS.getInt(1);
                String question = rS.getString(2);
                String answer = rS.getString(3);
                String userAnswer = rS.getString(4);
                AnsweredQuestion temp = new AnsweredQuestion(questionID, question, answer, userAnswer, userID, examID);
                answeredList.add(temp);
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }


        return answeredList;
    }


    public void submitGrade(Connection con, AnsweredQuestion aQ, double score) {

        try {
            PreparedStatement pS = con.prepareStatement("UPDATE PARTICIPANT_ANSWER SET participant_answer_grade = ? WHERE user_id = ? AND exam_id = ? AND question_id = ?");
            pS.setDouble(1, score);
            pS.setInt(2, aQ.getUserID());
            pS.setInt(3, aQ.getExamID());
            pS.setInt(4, aQ.getPosition());
            pS.execute();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public ArrayList<Exam> viewActiveExams(Connection con) {

        ArrayList<Exam> examList = new ArrayList<Exam>();

        Date date = Date.valueOf(LocalDate.now());

        int instID = getID(con);

        try {

            PreparedStatement pS2 = con.prepareStatement("SELECT exam_name, exam_start, exam_end FROM EXAM where institution_id = ?");
            pS2.setInt(1,instID);
            ResultSet rS2 = pS2.executeQuery();
            while (rS2.next()) {
                String examName = rS2.getString(1);
                Date sDate = rS2.getDate(2);
                Date eDate = rS2.getDate(3);
                Exam tempExam = new Exam(examName, instID, sDate, eDate);

                if (sDate.compareTo(date) <= 0 && date.compareTo(eDate) <= 0) {
                    examList.add(tempExam);
                }
            }
        } catch (SQLException testing) {
            testing.printStackTrace();
            System.out.println("Failed to extract exams from database.");
        }

        return examList;

    }


    public ArrayList<Exam> viewAllExams(Connection con) {

        ArrayList<Exam> examList = new ArrayList<Exam>();

        Date date = Date.valueOf(LocalDate.now());

        int userID = getID(con);

        try {

            PreparedStatement pS2 = con.prepareStatement("SELECT exam_name, institution_id, exam_start, exam_end FROM EXAM");
            pS2.setInt(1, userID);
            ResultSet rS2 = pS2.executeQuery();
            while (rS2.next()) {
                String examName = rS2.getString(1);
                int instID = rS2.getInt(2);
                Date sDate = rS2.getDate(3);
                Date eDate = rS2.getDate(4);
                Exam tempExam = new Exam(examName, instID, sDate, eDate);
                examList.add(tempExam);

            }
        } catch (SQLException testing) {
            testing.printStackTrace();
            System.out.println("Failed to extract exams from database.");
        }

        return examList;
    }





    public void createExam(String path, String fileName, String examName, Date startDate, Date endDate, Connection con){


        path = path.replace("\\", "\\\\");
        path = path.concat("\\");



        Integer questionNumber = null;
        String questionDescription = null;
        String answer = null;
        Integer score = null;
        int instID = getID(con);

        Exam exam = new Exam(examName, instID, startDate, endDate);



        try{
            try(Scanner fileScanner = new Scanner(new File(path + fileName))){

                insertExam(examName, startDate, endDate, con);

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
            nsee.printStackTrace();
        }


    }

    public void insertExam(String examName, Date startDate, Date endDate, Connection con){

        int instID = getID(con);

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

    public void insertQuestion(Connection con, String examName, String questionDescription, String questionAnswer, Integer score){

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









}
