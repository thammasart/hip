package models.garnerInterference;

import models.AnswerResult;
import models.ExperimentSchedule;
import models.TimeLog;
import models.User;
import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="garner_interference_answer")
public class Answer extends Model implements AnswerResult{
    @Id
    public long id;
    public boolean answer;
    public double usedTime;
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

    public static Answer create(User user, Quiz quiz, boolean answer, double usedTime, boolean isCorrect){
        Answer newAnswer = new Answer(user, quiz);
        newAnswer.answer = answer;
        newAnswer.usedTime = usedTime;
        newAnswer.isCorrect = isCorrect;
        return newAnswer;
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
    public ExperimentSchedule getExperimentSchedule(){
        return this.quiz.trial.schedule;
    }
    public long getTrialId(){
        return this.quiz.trial.id;
    }
    public String getParameterType(){
        if (this.quiz.questionType == QuestionType.BOTH)
            return "2 Dimension";
        else if(this.quiz.questionType == QuestionType.COLOR)
            return "1 Dimension";
        else if (this.quiz.questionType == QuestionType.SIZE)
            return "1 Dimension";
        else return "Null";
    }
    public User getUser(){
        return this.user;
    }
    public long getQuestionId(){
        return this.quiz.question.id;
    }
    public long getQuizId(){
        return this.quiz.id;
    }
    public String getIsCorrect(){
        return String.valueOf(this.isCorrect);
    }
    public double getUsedTime(){
        return this.usedTime;
    }
    public TimeLog getTimeLog(){
        return TimeLog.findByUserAndTrialId(this.user,new Long(this.quiz.trial.id),this.quiz.trial.schedule);
    }
    @SuppressWarnings("unchecked")
    public static Finder<Long, Answer> find = new Finder(Long.class, Answer.class);

}
