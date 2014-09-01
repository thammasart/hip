package models.attentionBlink;
import models.*;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table (name = "attention_blink_answer")
public class Answer extends Model implements AnswerResult{

        @Id
        public long id;
        public double usedTime;
        public boolean answer; 
        public boolean isCorrect;

        @ManyToOne
        public User user;
        @ManyToOne 
        public Quiz quiz;

	public Answer(boolean answer,double usedTime) {
            this.answer = answer;
            this.usedTime = usedTime;
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
        QuestionType qt = this.quiz.question.questionType;
        if (qt == QuestionType.ENGLISH)
            return "English";
        else if (qt == QuestionType.THAI)
            return "Thai";
        else if (qt == QuestionType.NUMBER)
            return "Number";
        else
            return "Null";
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
        //return TimeLog.findByUserAndTrialId(this.user,new Long(this.quiz.trial.id));
    }
	@SuppressWarnings("unchecked")
	public static Finder<Long,Answer> find = new Finder(Long.class,Answer.class);
}
