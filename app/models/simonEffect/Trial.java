package models.simonEffect;

import com.fasterxml.jackson.databind.JsonNode;
import models.ExperimentSchedule;

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
	public double blinkTime;
    public int noOfQuiz = 3;

	@ManyToOne
    public ExperimentSchedule schedule;
    @OneToMany
    public List<Quiz> quizzes = new ArrayList<Quiz>();

    public Trial(ExperimentSchedule schedule, QuestionType questionType, double blinkTime){
    	this.schedule = schedule;
    	this.questionType = questionType;
    	this.blinkTime = blinkTime;
    }

    public static List<Trial> findInvolving(ExperimentSchedule ex){
        return find.where().eq("schedule", ex).findList();
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

    public String getTrialJson(){
        JsonNode json = Json.toJson(this);
        return Json.stringify(json);
    }
}
