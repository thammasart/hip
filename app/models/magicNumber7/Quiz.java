package models.magicNumber7;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import play.db.ebean.Model.Finder;

@Entity
@Table (name = "magic_number_7_quiz")
public class Quiz extends Model{
	@Id
	public long id;
	public double displayTime;
	public int chunkSize;
    public int length;
	@ManyToOne
	public Trial trial;
	@ManyToOne(cascade=CascadeType.REMOVE)
	public Question question;
    @OneToMany(cascade=CascadeType.REMOVE)
    public List<Answer> answers = new ArrayList<Answer>();

	public Quiz(Trial trial, Question question){
		this.trial = trial;
		this.question = question;
	}

    public Quiz(){}

	public static Quiz create(Trial trial, Question question, double displayTime, int chunkSize, int length){
		Quiz newQuiz = new Quiz(trial, question);
		newQuiz.displayTime = displayTime;
		newQuiz.chunkSize = chunkSize;
        newQuiz.length = length;
		return newQuiz;
	}

    public static Quiz create(Trial trial){
        Quiz quiz = new Quiz();
        quiz.length = 9;
        quiz.displayTime = 3.0;
        quiz.chunkSize = 3;
        Question question = Question.generateQuestion(trial.questionType, quiz.length);
        question.save();
        quiz.question = question;
        quiz.trial = trial;
        return quiz;
    }

	@SuppressWarnings("unchecked")
	public static Finder<Long, Quiz> find = new Finder(Long.class, Quiz.class);
}