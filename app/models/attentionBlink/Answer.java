package models.attentionBlink;

import models.User;

import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="attention_blink_answer")
public class Answer extends Model{

    @Id
    public long id;
    @Column(length=20)
    public boolean isCorrect;
    public double usedTime;

    @ManyToOne
    public User user;
    @ManyToOne
    public Quiz quiz;

    public Answer(boolean isCorrect,double usedTime){
        this.answer = answer;
        this.usedTime = usedTime;
    }

    public static Answer create(boolean isCorrect, double usedTime,User user, Quiz quiz){
        Answer newAnswer = new Answer(isCorrect, usedTime);
        newAnswer.user = user;
        newAnswer.quiz = quiz;
        return newAnswer;
    }

    public static List<Answer> findInvolving(User user, List<Quiz> quizzes){
        List<Answer> answers = new ArrayList<Answer>();
        for(Quiz quiz:quizzes){
            answers.add(find.where().eq("user" ,user).eq("quiz",quiz).findUnique());
        }
        return answers;
    }

    public static double calculateTotalUsedTime(List<Answer> answers) {
        double totalUsedTime = 0.0;
        for(Answer ans : answers) {
            totalUsedTime += ans.usedTime;
        }
        return totalUsedTime;
    }

    public static int calculateTotalScore(List<Answer> answers) {
        int totalScore = 0;
        return totalScore;
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long,Answer> find = new Finder(Long.class,Answer.class);

}
