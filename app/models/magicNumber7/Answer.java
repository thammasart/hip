package models.magicNumber7;

import models.User;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name = "magic_number_7_answer")
public class Answer extends Model{

    @Id
    public long id;
    public String answer;
    public double usedTime;
    public int score;
    @ManyToOne
    public User user;
    @ManyToOne 
    public Quiz quiz;

    public Answer(User user, Quiz quiz){
        this.user = user;
        this.quiz = quiz;
    }

    public static Answer create(User user, Quiz quiz, String answer, double usedTime, int score){
        Answer newAnswer = new Answer(user, quiz);
        newAnswer.answer = answer;
        newAnswer.usedTime = usedTime;
        newAnswer.score = score;
        return newAnswer;
    }

    public static List<Answer> findInvolving(User user, List<Quiz> quizzes){
        List<Answer> answers = new ArrayList<Answer>();
        for(Quiz quiz:quizzes){
            answers.add(find.where().eq("user" ,user).eq("quiz",quiz).findUnique());
        }
        return answers;
    }

    public static double calculateTotalUsedTime(List<Answer> answers){
        double totalUsedTime = 0;
        for(Answer ans : answers){
            totalUsedTime += ans.usedTime;
        }
        return totalUsedTime;
    }

	@SuppressWarnings("unchecked")
	public static Finder<Long,Answer> find = new Finder(Long.class,Answer.class);
}
