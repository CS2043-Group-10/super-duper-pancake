public class Question {

    String question;
    int position;
    String answer;
    int score;

    Question(int pos, String q, String a, int score) {

        question = q;
        position = pos;
        answer = a;
        this.score = score;
    }

    public String getQuestion() {
        return question;
    }

    public int getPosition() {
        return position;
    }

    public String getAnswer(){
        return answer;
    }

    public int getScore() {
        return score;
    }



}