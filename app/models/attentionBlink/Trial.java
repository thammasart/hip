package models.attentionBlink;

import models.ExperimentSchedule;
import models.TimeLog;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table (name = "attention_blink_trial")
public class Trial extends Model{
	@Id
	public long id;
    public QuestionType questionType = QuestionType.ENGLISH;
    public int numberOfQuiz = 3;
    public int totalUser = 0;
    public double totalScore = 0;
    public double totalUsedTime = 0;

    @ManyToOne
    @JsonManagedReference("attentionBlinkBackRef")
    public ExperimentSchedule schedule;

    @OneToMany(cascade=CascadeType.REMOVE)
    @JsonManagedReference
    public List<Quiz> quizzes = new ArrayList<Quiz>();
        
    //Constructor
    public Trial(){}

	public Trial(QuestionType questionType) {
            this.questionType = questionType;
	}

    public void updateResult(){
        this.totalScore = 0;
        this.totalUsedTime = 0;
        for(Quiz q:quizzes){
            this.totalScore += Answer.calculateTotalScore(q.answers);
            this.totalUsedTime += Answer.calculateTotalUsedTime(q.answers);
        }
        this.totalUser = TimeLog.calaulateTotalUserTakeExp(schedule,id);
    }

    public static Trial create(ExperimentSchedule ex){
        Trial trial = new Trial();
        trial.schedule = ex;
        return trial;
    }

    public static Trial create(ExperimentSchedule ex, QuestionType questionType,int numberOfQuiz) {
        Trial trial = new Trial(questionType);
        trial.schedule = ex; 
        trial.numberOfQuiz = numberOfQuiz;
        return trial;
    }

    public static List<Trial> findInvolving(ExperimentSchedule ex){
        return find.where().eq("schedule", ex).findList();
    }

    public void generateNewQuestions(){
        for(Quiz quiz : quizzes){
            Question question = Question.generateQuestion(questionType, quiz.length, quiz.numberOfTarget, quiz.isCorrect);
            quiz.question = question;
            quiz.update();
        }
    }

    public void changeQuestionType(String questionType){
        if(questionType.equals("ENGLISH")){
            this.questionType = QuestionType.ENGLISH;
        }else if(questionType.equals("THAI")){
            this.questionType = QuestionType.THAI;
        }
        else if(questionType.equals("NUMBER")){
            this.questionType = QuestionType.NUMBER;
        }
    }
    public void generateQuiz(){
        for(int i = 0; i < this.numberOfQuiz; i++){
            Quiz.create(this).save();
        }
    }

	@SuppressWarnings("unchecked")
	public static Finder<Long, Trial> find = new Finder(Long.class, Trial.class);

}
