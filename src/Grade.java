public class Grade {

    double points;
    int total;
    Exam exam;

    Grade(double score, int outOf, Exam exam) {

        points = score;
        total = outOf;
        this.exam = exam;
    }

    public String getName(){
       String name = exam.getName();
       return name;
    }

    public double getPoints() {
        return points;
    }

    public int getTotal() {
        return total;

    }





}
