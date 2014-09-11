package models.stroopEffect;

import models.AnswerResult;
import models.ExperimentSchedule;
import models.TimeLog;
import models.User;

import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table (name="stroop_answer")
public class Answer extends Model implements AnswerResult{

    @Id
    public long id;
    @Column(length=20)
    public String answer;
    public double usedTime;
    public boolean isCorrect;

    @ManyToOne
    public User user;
    @ManyToOne
    public Quiz quiz;

    public Answer(String answer,double usedTime){
        this.answer = answer;
        this.usedTime = usedTime;
    }

    public static Answer create(String answer, double usedTime,User user, Quiz quiz){
        Answer newAnswer = new Answer(answer,usedTime);
        newAnswer.user = user;
        newAnswer.quiz = quiz;
        newAnswer.isCorrect = quiz.question.inkColor.equalsIgnoreCase(answer);
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
        for(Answer ans : answers) {
            if(ans.isCorrect) {
                totalScore++;
            }
        }
        return totalScore;
    }
    public ExperimentSchedule getExperimentScheduleObject(){
        return this.quiz.trial.schedule;
    }
    public long getTrialIdLong(){
        return this.quiz.trial.id;
    }
    public String getParameterType(){
        String type = "";
        if (this.quiz.question.questionType == QuestionType.ENGLISH)
            return "English ";
        else if (this.quiz.question.questionType == QuestionType.THAI)
            return "Thai ";
        else if (this.quiz.question.questionType == QuestionType.SHAPE)
            return "Shape ";
        if (this.quiz.question.isMatch()){
            type = type + "Match";
        }
        else type = type + "Not-Match";

        return type;
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
    public static Finder<Long,Answer> find = new Finder(Long.class,Answer.class);

}
