package models.attentionBlink;
import models.ExperimentSchedule;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name = "attention_blink_trial")
public class Trial extends Model{
	@Id
	public long id;
        public int length;
        public double blinkTime;
        public QuestionType questionType;        
        public int numberOfQuiz;

        @ManyToOne
        public ExperimentSchedule schedule;

        @OneToMany
        public List<Quiz> quizzes = new ArrayList<Quiz>();
        
        //Constructor
	public Trial(int length , double blinkTime ,QuestionType questionType) {
            this.length = length;
            this.blinkTime = blinkTime;
            this.questionType = questionType;
	}

    public static Trial create(ExperimentSchedule ex, int length, double blinkTime, QuestionType questionType,int numberOfQuiz) {
        Trial trial = new Trial(length,blinkTime,questionType);
        trial.schedule = ex; 
        trial.numberOfQuiz = numberOfQuiz;

        return trial;
    }

    public static List<Trial> findInvolving(ExperimentSchedule ex){
        return find.where().eq("schedule", ex).findList();
    }

	@SuppressWarnings("unchecked")
	public static Finder<Long, Trial> find = new Finder(Long.class, Trial.class);

}
