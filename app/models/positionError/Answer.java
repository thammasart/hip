package models.positionError;

import models.AnswerResult;
import models.User;
import play.db.ebean.*;
import javax.persistence.*;
import models.TimeLog;
import models.ExperimentSchedule;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="position_error_answer")
public class Answer extends Model implements AnswerResult{
	@Id
	public long id;

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

	public static Answer create(String answer, double usedTime, User user, Quiz quiz){
		Answer newAnswer = new Answer(answer,usedTime);
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

    public static int calculateTotalScore(List<Answer> answers){
        int totalScore = 0;
        for(Answer ans : answers){
            if(ans.isCorrect) totalScore++;
        }
        return totalScore;
    }

	public static double calculateTotalUsedTime(List<Answer> answers){
		double totalUsedTime = 0.0;
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
        if (this.quiz.trial.questionType == QuestionType.ENGLISH)
            return "English";
        else if (this.quiz.trial.questionType == QuestionType.THAI)
            return "Thai";
        else if (this.quiz.trial.questionType == QuestionType.NUMBER)
            return "Number";
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
        return TimeLog.findByUserAndTrialId(this.user,new Long(this.quiz.trial.id));
    }
	@SuppressWarnings("unchecked")
    public static Model.Finder<Long,Answer> find = new Finder(Long.class, Answer.class);

}