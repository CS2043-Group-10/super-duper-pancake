public class AnsweredQuestion {

    private int position;
    private String question;
    private String userAnswer;
    private String realAnswer;
    private int examID;
    private int userID;

    AnsweredQuestion(int pos, String question, String userAnswer, String realAnswer, int user, int exam) {

        position = pos;
        this.question = question;
        this.userAnswer = userAnswer;
        this.realAnswer = realAnswer;
        userID = user;
        examID = exam;
    }


    public int getPosition() {
        return position;
    }

    public String getQuestion() {
        return question;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public String getRealAnswer() {
        return realAnswer;
    }


    public int getUserID() {
        return userID;
    }

    public int getExamID() {
        return examID;
    }







}
