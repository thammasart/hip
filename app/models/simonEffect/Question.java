package models.simonEffect;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import play.db.ebean.Model.Finder;

@Entity
@Table (name = "simon_effect_question")
public class Question extends Model{
    @Id
    public long id;
    public String color;
    public char character;
    public String direction;
    @OneToMany
    public List<Quiz> quizzes;

    public Question(String color,char character , String direction) {
    	this.color = color;
    	this.character = character;
    	this.direction = direction;
    }

	@SuppressWarnings("unchecked")
	public static Finder<Long, Question> find = new Finder(Long.class, Question.class);
}