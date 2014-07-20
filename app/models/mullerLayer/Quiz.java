package models.mullerLayer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import play.db.ebean.Model;
import javax.persistence.*;

@Entity
@Table (name="muller_layer_Quiz")
public class Quiz extends Model{
    @Id
    public long id;
    public int noOfChoice;
    public int differChoice;
    public LenghtType lenghtType;
    public int differLenght;
    @ManyToOne
    @JsonBackReference
    public Trial trial;
    @ManyToOne
    @JsonBackReference
    public Question question;

    public Quiz(Trial trial, Question question){
    	this.trial = trial;
    	this.question = question;
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Quiz> find = new Finder(Long.class,Quiz.class);

    public Quiz() {

    }

    public static Quiz create(Trial trial) {
        Quiz quiz = new Quiz();
        quiz.trial = trial;
        quiz.noOfChoice = 3;
        quiz.differChoice = 2;
        quiz.differLenght = 2;
        quiz.lenghtType = LenghtType.MIDDLE;
        quiz.question = Question.generateQuestion();
        return quiz;
    }
}
