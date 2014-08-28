package models.sternbergSearch;

import models.AnswerResult;
import models.ExperimentSchedule;
import models.TimeLog;
import models.User;
import models.sternbergSearch.*;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name = "sternberg_search_answer")
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

    public static Answer create(User user, Quiz quiz, boolean answer, double usedTime, boolean isCorrect){
        Answer newAnswer = new Answer(user, quiz);
        newAnswer.answer = answer;
        newAnswer.usedTime = usedTime;
        newAnswer.isCorrect = isCorrect;
        return newAnswer;
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
    public ExperimentSchedule getExperimentSchedule(){
        return this.quiz.trial.schedule;
    }
    public long getTrialId(){
        return this.quiz.trial.id;
    }
    public String getParameterType(){
        List<Quiz> quizzes = this.quiz.trial.quizzes;
        for (int i =0;i<quizzes.size();i++){
            Quiz q = quizzes.get(i);
            if (q.questionChar.length() > 1)
                return "Parallel Search";
        }
        return "Exhaustive Search";
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
        return TimeLog.findByUserAndTrialId(this.user,new Long(this.quiz.trial.id));
    }
	@SuppressWarnings("unchecked")
	public static Finder<Long,Answer> find = new Finder(Long.class,Answer.class);
}
