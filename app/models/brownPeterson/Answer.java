package models.brownPeterson;
import models.*;
import play.db.ebean.*;
import javax.persistence.*;

@Entity
public class Answer extends Model{

	public String firstWord;
	public String secondWord;
	public String thirdWord;
	public double time;
	public int countdownResult;

	@ManyToOne
	public User user;
	@ManyToOne
	public Quiz quiz;

	public Answer(String firstWord,String secondWord,String thirdWord,double time,int countdownResult, User user, Quiz quiz){
		this.firstWord = firstWord;
		this.secondWord = secondWord;
		this.thirdWord = thirdWord;
		this.time = time;
		this.countdownResult = countdownResult;
		this.user = user;
		this.quiz = quiz;
	}

}