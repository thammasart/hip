package models.signalDetection;

import play.db.ebean.Model;
import javax.persistence.*;
import play.db.ebean.Model.Finder;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table (name="signal_detection_quiz")
public class Quiz extends Model{
	@Id
    public long id;

    public double displayTime;
    public int noOfTarget = 1;
    public int length = 10;
    @OneToOne(cascade=CascadeType.REMOVE)
	public Question question;
    @ManyToOne
    @JsonBackReference
    public Trial trial;
    @OneToMany(cascade=CascadeType.REMOVE)
    private List<Answer> answers = new ArrayList<Answer>();

    public List<Answer> findAnswers(){
        return answers;
    }

    public Quiz(){
    	this.displayTime = 0;
    	this.noOfTarget = 0;
    }

    public Quiz(double displayTime,int noOfTarget,int length){
    	this.displayTime = displayTime;
    	this.noOfTarget = noOfTarget;
        this.length = length;
    }

    public static Quiz create(double displayTime,int noOfTarget,int length,Trial trial,Question question){
    	if(displayTime > 1.0){
    		displayTime = 1.0;
    	}
    	else if(displayTime < 0.5){
    		displayTime = 0.5;
    	}
    	Quiz quiz = new Quiz(displayTime,noOfTarget,length);
    	quiz.question = question;
    	quiz.trial = trial;
    	return quiz;
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Quiz> find = new Finder(Long.class,Quiz.class);
}
