package models.simonEffect;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import play.db.ebean.Model.Finder;

@Entity
@Table (name = "simon_effect_quiz")
public class Quiz extends Model{
	@Id
	public long id;
	public String position;

	@ManyToOne
    public Trial trial;
    @ManyToOne
    public Question question;
    @OneToMany
    public List<Answer> answers = new ArrayList<Answer>();

    public Quiz(Trial trial, Question question,String position ){
    	this.trial = trial;
    	this.question = question;
    	this.position = position;
    }

	@SuppressWarnings("unchecked")
	public static Finder<Long, Quiz> find = new Finder(Long.class, Quiz.class);
}