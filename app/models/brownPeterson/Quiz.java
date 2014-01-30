package models.brownPeterson;

import javax.persistence.*;
import play.db.ebean.*;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Quiz extends Model{
	@Id
	public long id;
	public int countdown = 100;
	public int flashTime =5;

	@ManyToOne
	public Trial trial;

	@ManyToOne
	public Question question;

	public Quiz(int countdown, int flashTime){
		this.countdown = countdown;
		this.flashTime = flashTime;
	}

	public static Quiz create(int countdown, int flashTime, Trial trial, Question question){
		Quiz quiz = new Quiz(countdown, flashTime);
		quiz.trial = trial;
		quiz.question = question;
		return quiz;
	}

	public static List<Quiz> findInvolving(Trial trial){
		return find.where().eq("trial", trial).findList();
	}

	public static Finder<Long, Quiz> find = new Finder(Long.class, Quiz.class);
}