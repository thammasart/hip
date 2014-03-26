package models.brownPeterson;

import models.*;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="brown_peterson_trial")
public class Trial extends Model {

	public static final String WORD = "word";
	public static final String NON_SENSE = "nonsense";
	public static final String ENGLISH = "english";
	public static final String THAI = "thai";

	@Id
	public long id;
	@Column(nullable=false, length=20)
	public String trigramType = WORD;
	@Column(nullable=false, length=20)
	public String trigramLanguage = ENGLISH;

	public static final int TOTAL_QUESTION = 3;

	@ManyToOne
	public ExperimentSchedule schedule;

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
		//return totalScore/this.calculateTotalUser();
        return 1;
	}

	public void randomNewQuestions(){
		List<Question> questions 
			= Question.find.where().eq("trigramType", trigramType).eq("trigramLanguage", trigramLanguage).findList();
		for(Quiz quiz : quizzes){
			quiz.randomToNewQuestion(questions);
		}	
	}

/*
	public int calculateTotalUser(){
		return this.timeLogs.size();
	}
*/
	@SuppressWarnings("unchecked")
	public static Finder<Long, Trial> find = new Finder(Long.class, Trial.class);

}
