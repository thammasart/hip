package models.signalDetection;

import models.AnswerResult;
import models.ExperimentSchedule;
import models.TimeLog;
import models.User;
import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="signal_detection_answer")
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

	public Answer(boolean answer,double usedTime){
		this.answer = answer;
		this.usedTime = usedTime;
	}

	public static Answer create(boolean answer, double usedTime, User user, Quiz quiz){
		Answer newAnswer = new Answer(answer,usedTime);
		newAnswer.user = user;
		newAnswer.quiz = quiz;
		boolean answerOfQuiz = false;
		if(quiz.noOfTarget > 0){
			answerOfQuiz = true;
		}

		if(answer == answerOfQuiz){
			newAnswer.isCorrect = true;
		}
		else{
			newAnswer.isCorrect = false;
		}
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
    public static Finder<Long, Answer> find = new Finder(Long.class,Answer.class);

} 