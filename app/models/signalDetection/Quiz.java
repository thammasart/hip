package models.signalDetection;

import play.db.ebean.Model;
import javax.persistence.*;

@Entity
@Table (name="signal_detection_quiz")
public class Quiz extends Model{
	@Id
    public long id;

    public double displayTime;
    public int noOfTarget;

    @ManyToOne
	public Question question;
    @ManyToOne
    public Trial trial;

    public Quiz(){
    	this.displayTime = 0;
    	this.noOfTarget = 0;
    }

    public Quiz(double displayTime,int noOfTarget){
    	this.displayTime = displayTime;
    	this.noOfTarget = noOfTarget;
    }

    public static Quiz create(double displayTime,int noOfTarget,Trial trial,Question question){
    	Quiz quiz = new Quiz(displayTime,noOfTarget);
    	quiz.question = question;
    	quiz.trial = trial;
    	return quiz;
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Quiz> find = new Finder(Long.class,Quiz.class);
}