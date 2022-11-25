import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class User {

    private String user;


    public User (String userName) {

        user = userName;

    }


    public String getName() {
        return user;
    }

    public String getEmail(Connection con) {

        String email = null;

        try {

            PreparedStatement pS = con.prepareStatement("SELECT user_email FROM USER WHERE user_name = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pS.setString(1, user);
            ResultSet rS = pS.executeQuery();
            rS.first();
            email = rS.getString(1);


        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return email;
    }


    public int getID(Connection con) {

        int userID = 0;

        try {

            PreparedStatement pS = con.prepareStatement("SELECT user_id FROM USER WHERE user_name = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pS.setString(1, user);
            ResultSet rS = pS.executeQuery();
            rS.first();
            userID = rS.getInt(1);


        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return userID;
    }


    public int getInstitution(Connection con) {

        int instID = 0;

        try {

            PreparedStatement pS = con.prepareStatement("SELECT institution_id FROM USER WHERE user_name = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pS.setString(1, user);
            ResultSet rS = pS.executeQuery();
            rS.first();
            instID = rS.getInt(1);


        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return instID;
    }

    public int getInstitutionName(Connection con) {

        int instID = 0;

        try {

            PreparedStatement pS = con.prepareStatement("SELECT institution_name FROM USER NATURAL JOIN INSTITUTION WHERE user_name = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pS.setString(1, user);
            ResultSet rS = pS.executeQuery();
            rS.first();
            instID = rS.getInt(1);


        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return instID;
    }

    ///Views exams user is signed up for
    public ArrayList<Exam> viewExams(Connection con) {

        ArrayList<Exam> examList = new ArrayList<Exam>();

        Date date = Date.valueOf(LocalDate.now());

        int userID = getID(con);

        try {

            PreparedStatement pS2 = con.prepareStatement("SELECT exam_name, institution_id, exam_start, exam_end, exam_id FROM PARTICIPANT NATURAL JOIN EXAM where user_id = ? and participant_isreg = 1 and participant_complete = 0");
            pS2.setInt(1,userID);
            ResultSet rS2 = pS2.executeQuery();
            while (rS2.next()) {
                String examName = rS2.getString(1);
                int instID = rS2.getInt(2);
                Date sDate = rS2.getDate(3);
                Date eDate = rS2.getDate(4);
                int examID = rS2.getInt(5);
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


    ///Views all exams
    public ArrayList<Exam> viewExamsForRequest(Connection con) {

        ArrayList<Exam> examList = new ArrayList<Exam>();

        int instID = getInstitution(con);

        try {

            PreparedStatement pS2 = con.prepareStatement("SELECT exam_name, exam_start, exam_end, exam_id FROM EXAM WHERE institution_id = ?");
            pS2.setInt(1,instID);
            ResultSet rS2 = pS2.executeQuery();
            while (rS2.next()) {
                String examName = rS2.getString(1);
                Date sDate = rS2.getDate(2);
                Date eDate = rS2.getDate(3);
                int examID = rS2.getInt(4);
                Exam tempExam = new Exam(examName, instID, sDate, eDate);
                    examList.add(tempExam);
                }

        } catch (SQLException testing) {
            testing.printStackTrace();
            System.out.println("Failed to extract exams from database.");
        }

        return examList;
    }


    ///Views questions from an exam
    public ArrayList<Question> viewQuestions(Connection con, Exam exam) {

        ArrayList<Question> questionList = new ArrayList<Question>();

        int examID = exam.getID(con);

        try {

            PreparedStatement pS2 = con.prepareStatement("SELECT question_id, question_q, question_a, question_grade FROM QUESTION WHERE exam_id = ?");
            pS2.setInt(1, examID);
            ResultSet rS2 = pS2.executeQuery();
            while (rS2.next()) {
                int questionID = rS2.getInt(1);
                String question = rS2.getString(2);
                String answer= rS2.getString(3);
                int grade = rS2.getInt(4);
                Question tempQuestion = new Question(questionID, question, answer, grade);
                questionList.add(tempQuestion);
            }

        } catch (SQLException sqle2) {
            sqle2.printStackTrace();

        }

        return questionList;
    }

    public void submitAnswer(Connection con, String answer, Exam exam, Question question) {

        int userID = getID(con);
        int examID = exam.getID(con);
        int questionID = question.getPosition();

        try {
            PreparedStatement pS = con.prepareStatement("INSERT INTO PARTICIPANT_ANSWER VALUES(?, ?, ?, ?, null)");
            pS.setInt(1,userID);
            pS.setInt(2,examID);
            pS.setInt(3,questionID);
            pS.setString(4, answer);
            pS.execute();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    }

    public void finishExam(Connection con, Exam exam) {

        int userID = getID(con);
        int examID = exam.getID(con);

        try {
            PreparedStatement pS = con.prepareStatement("UPDATE PARTICIPANT SET participant_complete = 1 WHERE user_id = ? AND exam_id = ?");
            pS.setInt(1, userID);
            pS.setInt(2, examID);
            pS.execute();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    ///examRequest to be used with viewExamsForRequest, sends request to join exam
    public void examRequest(Connection con, int examID) {

        int userID = getID(con);

        try {

            PreparedStatement pS = con.prepareStatement("INSERT INTO PARTICIPANT VALUES (?, ?, 0, 0)");
            pS.setInt(1, userID);
            pS.setInt(2, examID);
         try {
            pS.execute();

        } catch (SQLIntegrityConstraintViolationException sqle) {

             System.out.println("You are already signed up for this exam");
         }

        } catch (SQLException sqle2) {
            sqle2.printStackTrace();
        }
        }


    public ArrayList<Grade> viewGrades(Connection con) {

        ArrayList<Grade> gradeList = new ArrayList<>();
        ArrayList<Exam> examList = new ArrayList<>();

        int userID = getID(con);

        try {

            PreparedStatement pS2 = con.prepareStatement("SELECT exam_name, institution_id, exam_start, exam_end FROM PARTICIPANT NATURAL JOIN EXAM where user_id = ? and participant_isreg = 1 and participant_complete = 1");
            pS2.setInt(1,userID);

            ResultSet rS2 = pS2.executeQuery();
            while (rS2.next()) {
                String examName = rS2.getString(1);
                int instID = rS2.getInt(2);
                Date sDate = rS2.getDate(3);
                Date eDate = rS2.getDate(4);
                Exam tempExam = new Exam(examName, instID, sDate, eDate);
                examList.add(tempExam);
            }

            double score = 0;
            int total = 0;
            for (int i = 0; i < examList.size(); i++) {

                int examID = examList.get(i).getID(con);

                PreparedStatement pS = con.prepareStatement("SELECT participant_answer_grade, question_grade FROM PARTICIPANT_ANSWER NATURAL JOIN QUESTION WHERE user_id = ? and exam_id = ?");
                pS.setInt(1, userID);
                pS.setInt(2, examID);
                ResultSet rS = pS.executeQuery();
                while (rS.next()) {
                score += rS.getDouble(1);
                total += rS.getInt(2);

                }
                Grade tempGrade = new Grade(score, total,examList.get(i));
                gradeList.add(tempGrade);
                score = 0;
                total = 0;
            }


            } catch (SQLException sqle) {
                    sqle.printStackTrace();
        }

        return gradeList;
    }







    }





