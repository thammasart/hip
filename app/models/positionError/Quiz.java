package models.positionError;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
@Table (name="position_error_quiz")
public class Quiz extends Model{
	@Id
	public long id;

	public int length = 7;

	@ManyToOne
	public Question question;
    @ManyToOne
    public Trial trial;

    public Quiz(){}

    public Quiz(int length,Question question, Trial trial){
    	this.length = length;
    	this.question = question;
    	this.trial = trial;
    }

    public static Quiz create(int length, Question question, Trial trial){
    	Quiz newQuiz = new Quiz(length, question, trial);
    	return newQuiz;
    }
    public static Quiz create(Trial trial){
    	Quiz newQuiz = new Quiz();
        Question question = Question.generateQuestion(trial.questionType, newQuiz.length);
        question.save();
        newQuiz.question = question;
        newQuiz.trial = trial;
    	return newQuiz;
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Quiz> find = new Finder(Long.class,Quiz.class);

}