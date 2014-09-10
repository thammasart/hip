package models.changeBlindness;

import models.AnswerResult;
import models.User;
import models.ExperimentSchedule;
import models.TimeLog;
import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="change_blindness_answer")
public class Answer extends Model implements AnswerResult{
    @Id
    public long id;
    public double usedTime;
    public double positionOfChangeX;
    public double positionOfChangeY;
    public boolean isCorrect;
    @ManyToOne
    public User user;
    @ManyToOne 
    public Quiz quiz;

    public Answer(User user, Quiz quiz){
    	this.user = user;
    	this.quiz = quiz;
    }

    public static List<Answer> findInvolving(User user, List<Quiz> quizzes){
        List<Answer> answers = new ArrayList<Answer>();
        for(Quiz quiz:quizzes){
            answers.add(find.where().eq("user" ,user).eq("quiz",quiz).findUnique());
        }
        return answers;
    }

    public static int calculateTotalScore(List<Answer> answers){
        int totalScore = 0;
        for(Answer ans : answers){
            if(ans.isCorrect) totalScore++;
        }
        return totalScore;
    }

    public static double calculateTotalUsedTime(List<Answer> answers){
        double totalUsedTime = 0;
        for(Answer ans : answers){
            totalUsedTime += ans.usedTime;
        }
        return totalUsedTime;
    }
    public ExperimentSchedule getExperimentScheduleObject(){
        return this.quiz.trial.schedule;
    }
    public long getTrialIdLong(){
        return this.quiz.trial.id;
    }
    public String getParameterType(){
        return "------";
    }
    public User getUserObject(){
        return this.user;
    }
    public long getQuestionIdLong(){
        return this.quiz.question.id;
    }
    public long getQuizIdLong(){
        return this.quiz.id;
    }
    public String getIsCorrectString(){
        return String.valueOf(this.isCorrect);
    }
    public double getUsedTimeDouble(){
        return this.usedTime;
    }
    public TimeLog getTimeLogObject(){
        return TimeLog.findByUserAndTrialId(this.user,new Long(this.quiz.trial.id),this.quiz.trial.schedule);
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Answer> find = new Finder(Long.class, Answer.class);

}
