package models.sternbergSearch;

import models.ExperimentSchedule;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name = "sternberg_search_trial")
public class Trial extends Model{
	@Id
	public long id;
	public int length;
	public double blinkTime;
	public int oneCharIsCorrect;
	public int oneCharIsInCorrect;
	public int twoCharIsCorrect;
	public int twoCharIsInCorrect;
    public QuestionType questionType;
    @ManyToOne
    public ExperimentSchedule schedule;
    @OneToMany
    public List<Quiz> quizzes = new ArrayList<Quiz>();

    public Trial(ExperimentSchedule schedule){
    	this.schedule = schedule;
    }

    public static Trial create(ExperimentSchedule schedule, int length, double blinkTime, int oneCharIsCorrect, int oneCharIsInCorrect, int twoCharIsCorrect, int twoCharIsInCorrect, QuestionType questionType){
    	Trial newTrial = new Trial(schedule);
    	newTrial.length = length;
    	newTrial.blinkTime = blinkTime;
    	newTrial.oneCharIsCorrect = oneCharIsCorrect;
    	newTrial.oneCharIsInCorrect = oneCharIsInCorrect;
    	newTrial.twoCharIsCorrect = twoCharIsCorrect;
    	newTrial.twoCharIsInCorrect = twoCharIsInCorrect;
    	newTrial.questionType = questionType;
    	return newTrial;
    }

    public static List<Trial> findInvolving(ExperimentSchedule ex){
        return find.where().eq("schedule", ex).findList();
    }

	@SuppressWarnings("unchecked")
	public static Finder<Long, Trial> find = new Finder(Long.class, Trial.class);
}