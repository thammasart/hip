package models.brownPeterson;

import models.*;
import models.TimeLog;
import models.TrialStatus;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table (name="brown_peterson_trial")
public class Trial extends Model {

	public static final String WORD = "word";
	public static final String NON_SENSE = "nonsense";
	public static final String ENGLISH = "english";
	public static final String THAI = "thai";
	public double totalScore = 0;
    public double totalUsedTime = 0;
    public int totalUser = 0;
    public TrialStatus status = TrialStatus.CLOSE;

	@Id
	public long id;
	@Column(nullable=false, length=20)
	public String trigramType = WORD;
	@Column(nullable=false, length=20)
	public String trigramLanguage = ENGLISH;

	public static final int TOTAL_QUESTION = 3;

	@ManyToOne
	public ExperimentSchedule schedule;

    @OneToMany(cascade=CascadeType.REMOVE)
    @JsonManagedReference
    public List<Quiz> quizzes = new ArrayList<Quiz>();
        
	public static Trial create(ExperimentSchedule experimentSchedule){
		Trial trial = new Trial();
		trial.schedule = experimentSchedule;
		return trial;
	}

    public Trial(){}

	public static Trial findById(int id) {
		return find.byId(new Long(id));
	}

	public static List<Trial> findInvolving(ExperimentSchedule ex){
		return find.where().eq("schedule", ex).findList();
	}

    public void updateResult(){
        this.totalScore = 0;
        this.totalUsedTime = 0;
        for(Quiz q:quizzes){
            this.totalScore += Answer.calculateTotalScore(q.findAnswer());
            this.totalUsedTime += Answer.calculateTotalUsedTime(q.findAnswer());
        }
        this.totalUser = TimeLog.calaulateTotalUserTakeExp(schedule,id);
        this.totalScore /=totalUser;
        this.totalUsedTime /=totalUser;
        this.update();
    }

	public int calculateAverageScore(){
		int totalScore = 0;
		for(Quiz quiz : this.quizzes){
			totalScore += Answer.calculateTotalScore(quiz.findAnswer());
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

	public void generateQuiz(){
		List<Question> questions 
			= Question.find.where().eq("trigramType", trigramType).eq("trigramLanguage", trigramLanguage).findList();
		for(int i = 0; i < TOTAL_QUESTION; i++){
			Quiz.create(Quiz.DEFAULT_INITCOUNTDOWN, 
				Quiz.DEFAULT_FLASHTIME, this, Question.randomNewQuestion(questions)).save();
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
