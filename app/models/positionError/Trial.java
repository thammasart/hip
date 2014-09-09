package models.positionError;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import models.TimeLog;
import models.TrialStatus;

import play.db.ebean.*;
import javax.persistence.*;
import models.TimeLog;
import models.ExperimentSchedule;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table (name="position_error_trial")
public class Trial extends Model{
	@Id
	public long id;

	public double flashSpeed;
	public double delayTime;
	public QuestionType questionType;
	public double totalScore = 0;
    public double totalUsedTime = 0;
    public int totalUser = 0;

    public int numberOfQuiz = 3;
    public TrialStatus status = TrialStatus.CLOSE;

	@ManyToOne
    public ExperimentSchedule schedule;
    @OneToMany(cascade=CascadeType.REMOVE, mappedBy = "trial")
    @JsonManagedReference
    public List<Quiz> quizzes;

	public Trial(double flashSpeed, double delayTime, QuestionType questionType){
		this.flashSpeed = flashSpeed;
		this.delayTime = delayTime;
		this.questionType = questionType;
	}

	public static Trial create(double flashSpeed, double delayTime, QuestionType questionType, 
								ExperimentSchedule schedule){
		Trial newTrial = new Trial(flashSpeed, delayTime, questionType);
		newTrial.schedule = schedule;
		return newTrial;
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
    
    public static Trial create(ExperimentSchedule schedule){
        Trial newTrial = new Trial(0.05,0.1,QuestionType.ENGLISH);
        newTrial.schedule = schedule;
        return newTrial;
    }

    public void generateQuiz(){
        for(int i = 0; i < this.numberOfQuiz; i++){
            Quiz.create(this).save();
        }
    }

    public static List<Trial> findInvolving(ExperimentSchedule ex){
        return find.where().eq("schedule", ex).findList();
    }

	@SuppressWarnings("unchecked")
    public static Model.Finder<Long,Trial> find = new Finder(Long.class, Trial.class);




}
