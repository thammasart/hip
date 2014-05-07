package models.simonEffect;

import models.ExperimentSchedule;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name = "simon_effect_trial")
public class Trial extends Model{
	@Id
	public long id;
	public QuestionType questionType;
	public double blinkTime;

	@ManyToOne
    public ExperimentSchedule schedule;
    @OneToMany
    public List<Quiz> quizzes = new ArrayList<Quiz>();

    public Trial(ExperimentSchedule schedule, QuestionType questionType, double blinkTime){
    	this.schedule = schedule;
    	this.questionType = questionType;
    	this.blinkTime = blinkTime;
    }

    @SuppressWarnings("unchecked")
	public static Finder<Long, Trial> find = new Finder(Long.class, Trial.class);
}