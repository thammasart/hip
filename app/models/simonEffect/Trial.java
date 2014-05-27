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