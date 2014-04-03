package models.positionError;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
@Table (name="position_error_quiz")
public class Quiz extends Model{
	@Id
	public long id;

	public int length;

	@ManyToOne
	public Question question;
    @ManyToOne
    public Trial trial;

    public Quiz(int length,Question question, Trial trial){
    	this.length = length;
    	this.question = question;
    	this.trial = trial;
    }

    public static Quiz create(int length, Question question, Trial trial){
    	Quiz newQuiz = new Quiz(length, question, trial);
    	return newQuiz;
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Quiz> find = new Finder(Long.class,Quiz.class);

}