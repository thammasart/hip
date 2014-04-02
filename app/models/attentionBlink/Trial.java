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
    public QuestionType questionType;        
    public int numberOfQuiz;

    @ManyToOne
    public ExperimentSchedule schedule;

    @OneToMany
    public List<Quiz> quizzes = new ArrayList<Quiz>();
        
    //Constructor
	public Trial(QuestionType questionType) {
            this.questionType = questionType;
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

	@SuppressWarnings("unchecked")
	public static Finder<Long, Trial> find = new Finder(Long.class, Trial.class);

}
