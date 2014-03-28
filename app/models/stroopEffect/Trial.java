package models.stroopEffect;

import play.db.ebean.Model;
import javax.persistence.*;

import models.ExperimentSchedule;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="stroop_trial")
public class Trial extends Model{
    @Id
    public long id;
    @Column(nullable=false, length=2)
    public long appearTime;
    @Column(nullable=false, length=20)
    public QuestionType questionType = QuestionType.ENGLISH;

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

    public Trial(){
        appearTime = 0;
        questionType = QuestionType.ENGLISH;
    }

    public Trial(long appearTime,QuestionType type){
        this.appearTime = appearTime;
        this.questionType = type;
    }

    public static Trial findById(long id){
        return find.byId(new Long(id));
    }

    public static List<Trial> findInvolving(ExperimentSchedule ex){
        return find.where().eq("schedule", ex).findList();
    }

    public void setQuestionType(String type){
        if(type.equals(QuestionType.ENGLISH.toString())){
            this.questionType = QuestionType.ENGLISH;
        }else if(type.equals(QuestionType.THAI.toString())){
            this.questionType = QuestionType.THAI;
        }else if(type.equals(QuestionType.SHAPE.toString())){
            this.questionType = QuestionType.SHAPE;
        }
        update();
    }

    public void randomNewQuestions(){
        List<Question> questions = Question.find.where().eq("questionType", questionType).findList();
        for(Quiz quiz : quizzes){
            quiz.randomToNewQuestion(questions);
        }
    }

    public void generateQuiz(){
        List<Question> questions = Question.find.where().eq("questionType", questionType).findList();
        for(int i = 0; i < TOTAL_QUESTION; i++){
            Quiz.create(this, Question.randomNewQuestion(questions)).save();
        }
    }

    @SuppressWarnings("unchecked")
    public static Model.Finder<Long,Trial> find = new Finder(Long.class, Trial.class);
}
