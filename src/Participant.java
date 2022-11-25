import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Participant {

    private int userID;
    private int examID;


    Participant(int user, int exam) {

        userID = user;
        examID = exam;
    }

    public int getUserID() {
        return userID;
    }

    public int getExamID() {
        return examID;
    }

    public String getUserName(Connection con) {

        String userName = null;

        try {

            PreparedStatement pS = con.prepareStatement("SELECT user_name FROM USER WHERE user_id = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pS.setInt(1, getUserID());
            ResultSet rS = pS.executeQuery();
            rS.first();
            userName = rS.getString(1);


        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return userName;
    }

    public String getExamName(Connection con) {

        String examName = null;

        try {

            PreparedStatement pS = con.prepareStatement("SELECT exam_name FROM EXAM WHERE exam_id = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pS.setInt(1, getExamID());
            ResultSet rS = pS.executeQuery();
            rS.first();
            examName = rS.getString(1);


        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return examName;
    }




}
