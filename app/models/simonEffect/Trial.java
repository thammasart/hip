package models.simonEffect;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import models.ExperimentSchedule;
import models.TimeLog;

import play.db.ebean.*;
import play.libs.Json;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name = "simon_effect_trial")
public class Trial extends Model{
	@Id
	public long id;
	public QuestionType questionType;
	public double blinkTime = 0.5;
    public int noOfQuiz = 3;
    public double totalScore = 0;
    public int totalUser = 0;
    public double totalUsedTime = 0;

    @ManyToOne
    public ExperimentSchedule schedule;

    @OneToMany(cascade=CascadeType.REMOVE)
    @JsonManagedReference
    public List<Quiz> quizzes = new ArrayList<Quiz>();

    public Trial(ExperimentSchedule schedule, QuestionType questionType, double blinkTime){
    	this.schedule = schedule;
    	this.questionType = questionType;
    	this.blinkTime = blinkTime;
    }

    public static List<Trial> findInvolving(ExperimentSchedule ex){
        return find.where().eq("schedule", ex).findList();
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
    
    public static List<Trial> findAllTrial(int feature){

        if(feature == 1){ 
            return find.where().eq("question_type",QuestionType.ONEFEATURE).findList();
        }else if(feature == 2 ){ 
            return find.where().eq("question_type",QuestionType.TWOFEATURE).findList();
        }else{ 
            return find.all();
        }
    }
    
    public static List<String> getFeature(){
        List<String> expFeature = new ArrayList<String>();
        expFeature.add("1-Feature");
        expFeature.add("2-Feature");
        
        return expFeature;
    }


    @SuppressWarnings("unchecked")
	public static Finder<Long, Trial> find = new Finder(Long.class, Trial.class);

    public static Trial create(ExperimentSchedule experimentSchedule) {
        return new Trial(experimentSchedule, QuestionType.ONEFEATURE, 0.8);
    }

    public void generateQuiz() {
        for(int i = 0; i < noOfQuiz; i++){
            Quiz quiz = Quiz.create(this);
            quiz.save();
        }
    }
}
