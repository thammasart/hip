package models.garnerInterference;

import play.db.ebean.Model;
import javax.persistence.*;

@Entity
@Table (name="garner_interference_quiz")
public class Quiz extends Model{
    @Id
    public long id;
    public QuestionType questionType; // color or size or both
    @ManyToOne
    public Trial trial;
    @ManyToOne
    public Question question;

    public Quiz(Trial trial, Question question){
    	this.trial = trial;
    	this.question = question;
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Quiz> find = new Finder(Long.class, Quiz.class);

}
