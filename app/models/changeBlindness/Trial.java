package models.changeBlindness;

import models.TimeLog;

import models.ExperimentSchedule;
import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="change_blindness_trial")
public class Trial extends Model{
    @Id
    public long id;
    @ManyToOne
    public ExperimentSchedule schedule;
    @OneToMany(cascade=CascadeType.REMOVE)
    public List<Quiz> quizzes = new ArrayList<Quiz>();
    public double totalScore = 0;
    public double totalUsedTime = 0;
    public int totalUser = 0;

    public Trial(ExperimentSchedule schedule){
    	this.schedule = schedule;
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

    public static List<Trial> findInvolving(ExperimentSchedule ex){
        return find.where().eq("schedule", ex).findList();
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Trial> find = new Finder(Long.class, Trial.class);

}
