package models.sternbergSearch;

import models.ExperimentSchedule;
import models.TimeLog;
import models.TrialStatus;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table (name = "sternberg_search_trial")
public class Trial extends Model{
	@Id
	public long id;
	public int length;
    public double totalScore = 0;
    public double totalUsedTime = 0;
    public int totalUser = 0;
	public double blinkTime;
	public int oneCharIsCorrect = 1;
	public int oneCharIsInCorrect = 0;
	public int twoCharIsCorrect = 0;
	public int twoCharIsInCorrect = 0;
    public QuestionType questionType = QuestionType.ENGLISH;
    
    @ManyToOne
    public ExperimentSchedule schedule;
    @OneToMany(cascade=CascadeType.REMOVE, mappedBy = "trial")
    @JsonManagedReference
    public List<Quiz> quizzes = new ArrayList<Quiz>();

    public Trial(ExperimentSchedule schedule){
    	this.schedule = schedule;
    }

    public static Trial create(ExperimentSchedule schedule){
        Trial trial = new Trial(schedule);
        trial.length = 3;
        trial.blinkTime = 0.3;
        return trial;

    }

    public void updateResult(){
        this.totalScore = 0;
        this.totalUsedTime = 0;
        for(Quiz q:quizzes){
            this.totalScore += Answer.calculateTotalScore(q.findAnswers());
            this.totalUsedTime += Answer.calculateTotalUsedTime(q.findAnswers());
        }
        this.totalUser = TimeLog.calaulateTotalUserTakeExp(schedule,id);
        this.totalScore /=totalUser;
        this.totalUsedTime /=totalUser;
        this.update();
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

    public void generateQuiz() {
        Quiz.create(this).save();
    }
}
