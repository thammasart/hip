package models.simonEffect;

import models.User;
import models.sternbergSearch.*;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name = "simon_effect_answer")
public class Answer extends Model{
    @Id
    public long id;
    public String answer;
    public double usedTime;
    public boolean isCorrect;
    @ManyToOne
    public User user;
    @ManyToOne 
    public Quiz quiz;

    public Answer(User user, Quiz quiz, String ans, double usedTime){
    	this.user = user;
    	this.quiz = quiz;
    	this.answer = ans;
    	this.usedTime = usedTime;
    	this.isCorrect = answer.equals(quiz.question.direction);
    }

    @SuppressWarnings("unchecked")
	public static Finder<Long,Answer> find = new Finder(Long.class,Answer.class);

}
