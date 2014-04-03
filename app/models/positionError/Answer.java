package models.positionError;

import models.User;
import play.db.ebean.*;
import javax.persistence.*;
import models.TimeLog;
import models.ExperimentSchedule;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="position_error_answer")
public class Answer extends Model{
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

	public static double calculateTotalUsedTime(List<Answer> answers){
		double totalUsedTime = 0.0;
		for(Answer ans : answers){
			totalUsedTime += ans.usedTime;
		}
		return totalUsedTime;
	}

	@SuppressWarnings("unchecked")
    public static Model.Finder<Long,Answer> find = new Finder(Long.class, Answer.class);

}