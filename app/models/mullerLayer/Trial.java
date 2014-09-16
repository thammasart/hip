package models.mullerLayer;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import models.ExperimentSchedule;
import models.TimeLog;
import models.TrialStatus;

import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="muller_layer_trial")
public class Trial extends Model{
    @Id
    public long id;
    public int noOfQuiz = 3;
    public double totalScore = 0;
    public double totalUsedTime = 0;
    public int totalUser = 0;
    public int displayTime = 10;

    @ManyToOne
    public ExperimentSchedule schedule;
    @OneToMany(mappedBy = "trial",cascade=CascadeType.REMOVE)
    @JsonManagedReference("trial")
    public List<Quiz> quizzes = new ArrayList<Quiz>();


    public Trial(ExperimentSchedule schedule){
    	this.schedule = schedule;
    }

    public void updateResult(){
        this.totalScore = 0;
        this.totalUsedTime = 0;
        for(Quiz q:quizzes){
            this.totalScore += Answer.calculateTotalScore(q.findAnswers());
            this.totalUsedTime += Answer.calculateTotalUsedTime(q.findAnswers());
        }
        this.totalUser = TimeLog.calaulateTotalUserTakeExp(schedule,id);
        this.totalScore /=totalUser;
        this.totalUsedTime /=totalUser;
        this.update();
    }
    
    public static List<Trial> findInvolving(ExperimentSchedule ex){
        return find.where().eq("schedule", ex).findList();
    }
    @SuppressWarnings("unchecked")
    public static Finder<Long, Trial> find = new Finder(Long.class, Trial.class);

    public static Trial create(ExperimentSchedule experimentSchedule) {
        return new Trial(experimentSchedule);
    }

    public void generateQuiz() {
        for(int i =0; i < noOfQuiz; i++){
            Quiz quiz = Quiz.create(this);
            quiz.save();
        }
    }
}
