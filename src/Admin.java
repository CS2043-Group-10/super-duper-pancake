import java.sql.*;
import java.util.ArrayList;

public class Admin {

    private String name;

    public Admin(String userName) {

        name = userName;

    }

    public String getName() {
        return name;
    }


    public int getID(Connection con) {

        int adID = 0;

        try {

            PreparedStatement pS = con.prepareStatement("SELECT admin_id FROM ADMIN WHERE admin_name = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            pS.setString(1, name);
            ResultSet rS = pS.executeQuery();
            rS.first();
            adID = rS.getInt(1);


        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return adID;
    }




    public void createInstitution(Connection con, String instName, String instPass) {

        try {

            PreparedStatement pS = con.prepareStatement("INSERT INTO INSTITUTION VALUES(default, ?, ?, ?)");
            pS.setString(1, instName);
            pS.setString(2, instPass);
            pS.setInt(3, getID(con));

            try {

                pS.execute();

            } catch (SQLIntegrityConstraintViolationException sqle) {
                System.out.println("Institution of given name already exists.");

            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }


    }


    public ArrayList<User> getUnregisteredUsersForInstitution(Connection con, Institution inst) {

        ArrayList<User> unRegUserList = new ArrayList<>();
        int instID = inst.getID(con);

        try {

            PreparedStatement pS = con.prepareStatement("SELECT user_name FROM USER WHERE institution_id = ? AND user_isreg = false");
            pS.setInt(1,instID);
            ResultSet rS = pS.executeQuery();
            while (rS.next()) {
                String userName = rS.getString(1);
                User tempUser =  new User(userName);
                unRegUserList.add(tempUser);

            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return unRegUserList;
    }

    public void registerUser(Connection con, User user) {

        try {

            PreparedStatement pS = con.prepareStatement("UPDATE USER SET admin_id = ?, user_isreg = true WHERE user_name = ?");
            pS.setInt(1, getID(con));
            pS.setString(2, user.getName());

            try {
                pS.execute();
            } catch (SQLIntegrityConstraintViolationException sqle) {
                System.out.println("User already registered");
            }


        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    }

    public ArrayList<Institution> viewInstitutions(Connection con) {

        ArrayList<Institution> instList = new ArrayList<>();

        try {
            PreparedStatement pS = con.prepareStatement("SELECT institution_name FROM INSTITUTION ORDER BY institution_id ASC");
            ResultSet rS = pS.executeQuery();
            while (rS.next()) {
                String name = rS.getString(1);
                Institution tempInst = new Institution(name);
                instList.add(tempInst);
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }



        return instList;
    }

    public ArrayList<User> getUsers(Connection con) {

        ArrayList<User> userList = new ArrayList<>();
        int instID = getID(con);

        try {

            PreparedStatement pS = con.prepareStatement("SELECT user_name FROM USER");
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








}
