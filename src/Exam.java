import java.sql.*;
import java.util.ArrayList;

public class Exam {

    private String name;
    private int inst;
    private Date sDate;
    private Date eDate;
    ArrayList<Question> examQuestions = new ArrayList<>();

    Exam(String examName, int institutionID, Date startDate, Date endDate) {

        name = examName;
        inst = institutionID;
        sDate = startDate;
        eDate = endDate;
    }

    public int getID(Connection con) {

        int examID = 0;

        try {

            PreparedStatement pS = con.prepareStatement("SELECT exam_id FROM EXAM WHERE exam_name = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pS.setString(1, name);
            ResultSet rS = pS.executeQuery();
            rS.first();
            examID = rS.getInt(1);


        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return examID;
    }

    public String getName() {
        return name;
    }

    public Date getStartDate(){
        return sDate;

    }

    public Date getEndDate(){
        return eDate;

    }

    public int getInstitution() {
        return inst;
    }

    public void addToExam(Question question) {
        examQuestions.add(question);
    }

    public ArrayList<Question> getQuestions(Connection con) {


        ArrayList<Question> questionList = new ArrayList<Question>();

        int examID = getID(con);

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






}
