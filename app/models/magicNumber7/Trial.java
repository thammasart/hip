package models.magicNumber7;

import models.ExperimentSchedule;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name = "magic_number_7_trial")
public class Trial extends Model{
	@Id
	public long id;
    public QuestionType questionType;
    @ManyToOne
    public ExperimentSchedule schedule;
    @OneToMany(cascade=CascadeType.REMOVE)
    public List<Quiz> quizzes = new ArrayList<Quiz>();

    public int numberOfQuiz = 3;

    public Trial(ExperimentSchedule schedule){
    	this.schedule = schedule;
    }

    public static Trial create(ExperimentSchedule schedule, QuestionType questionType){
    	Trial newTrial = new Trial(schedule);
    	newTrial.questionType = questionType;
    	return newTrial;
    }

    public static Trial create(ExperimentSchedule schedule){
        Trial newTrial = new Trial(schedule);
        newTrial.questionType = QuestionType.ENGLISH;
        return newTrial;
    }

    public static List<Trial> findInvolving(ExperimentSchedule ex){
        return find.where().eq("schedule", ex).findList();
    }

    public void generateQuiz(){
        for(int i = 0; i < this.numberOfQuiz; i++){
            Quiz.create(this).save();
        }
    }



	@SuppressWarnings("unchecked")
	public static Finder<Long, Trial> find = new Finder(Long.class, Trial.class);
}