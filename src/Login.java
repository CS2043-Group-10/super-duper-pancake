import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class Login {

    public Login (){

    }

    public boolean processLogin(int loginType, String userName, String passWord, Connection con) {


        try {
            switch (loginType) {

                case 1 -> {

                    CallableStatement validateUser = con.prepareCall("{ call validate_user (?, ?) }");
                    validateUser.setString(1, userName);
                    validateUser.registerOutParameter(2, Types.VARCHAR);
                    validateUser.executeUpdate();
                    String outParamVU = validateUser.getString(2);
                    if (Objects.equals(passWord, outParamVU)) {
                        return true;
                    }
                }
                case 2 -> {

                    CallableStatement validateInstitution = con.prepareCall("{ call validate_institution (?, ?) }");
                    validateInstitution.setString(1, userName);
                    validateInstitution.registerOutParameter(2, Types.VARCHAR);
                    validateInstitution.executeUpdate();
                    String outParamVI = validateInstitution.getString(2);
                    if (Objects.equals(passWord, outParamVI)) {
                        return true;
                    }

                }

                case 3 -> {
                    CallableStatement validateAdmin = con.prepareCall("{ call validate_admin (?, ?) }");
                    validateAdmin.setString(1, userName);
                    validateAdmin.registerOutParameter(2, Types.VARCHAR);
                    validateAdmin.executeUpdate();
                    String outParamVA = validateAdmin.getString(2);
                    if (Objects.equals(passWord, outParamVA)) {
                        return true;
                    }
                }
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return false;
    }

    public void createUser(Connection con, String userName, String userPass, String userEmail, int instID) {
        try {
            PreparedStatement pS = con.prepareStatement("INSERT INTO USER VALUES (default, ?, ?, ?, false, null, ?)");
            pS.setString(1, userName);
            pS.setString(2, userPass);
            pS.setString(3, userEmail);
            pS.setInt(4, instID);

            try {
                pS.execute();
            } catch (SQLIntegrityConstraintViolationException sqli) {
                System.out.println("Username taken.");
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








}

