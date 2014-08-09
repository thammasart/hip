package models.simonEffect;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table (name = "simon_effect_quiz")
public class Quiz extends Model{

    public static final String[] positions = {"up", "down", "right", "left"};

	@Id
	public long id;
	public String position;

    @ManyToOne
    @JsonBackReference
    public Trial trial;
    @ManyToOne
    public Question question;
    @OneToMany(cascade=CascadeType.REMOVE, mappedBy = "quiz")
    private List<Answer> answers = new ArrayList<Answer>();

    public List<Answer> findAnswers(){
        return answers;
    }

    public Quiz(Trial trial, Question question,String position ){
    	this.trial = trial;
    	this.question = question;
    	this.position = position;
    }

	@SuppressWarnings("unchecked")
	public static Finder<Long, Quiz> find = new Finder(Long.class, Quiz.class);

    public Quiz() {

    }

    public static Quiz create(Trial trial) {
        Quiz quiz = new Quiz();
        quiz.trial = trial;
        quiz.position = randomPosition();
        quiz.question = Question.findQuestionByType(trial.questionType);
        return  quiz;
    }

    public static String randomPosition() {
        Random random = new Random();
        return positions[random.nextInt(positions.length)];

    }
}
