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






}
