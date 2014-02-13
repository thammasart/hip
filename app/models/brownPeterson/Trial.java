package models.brownPeterson;

import models.*;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="brown_peterson_trial")
public class Trial extends Model {
	@Id
	public long id;
	@Column(nullable=false, length=20)
	public String trigramType = "word";
	@Column(nullable=false, length=20)
	public String trigramLanguage = "english";

	public static final int TOTAL_QUESTION = 3;

	@ManyToOne
	public ExperimentSchedule schedule;
	@OneToMany
	public List<TimeLog> timeLogs = new ArrayList<TimeLog>();
    @OneToMany
    public List<Quiz> quizzes = new ArrayList<Quiz>();
        
	public static Trial create(ExperimentSchedule experimentSchedule){
		Trial trial = new Trial();
		trial.schedule = experimentSchedule;
		return trial;
	}

	public static Trial findById(int id) {
		return find.byId(new Long(id));
	}

	public static List<Trial> findInvolving(ExperimentSchedule ex){
		return find.where().eq("schedule", ex).findList();
	}

	public int calculateAverageScore(){
		int totalScore = 0;
		for(Quiz quiz : this.quizzes){
			totalScore += Answer.calculateTotalScore(quiz.answers);
		}
		return totalScore;
	}

	public int calculateTotalUser(){
		List<Answer> answers = new ArrayList<Answer>();
		for(Quiz quiz : this.quizzes){
			answers.addAll(quiz.answers);
		}
		return 1;
	}

	@SuppressWarnings("unchecked")
	public static Finder<Long, Trial> find = new Finder(Long.class, Trial.class);

}
