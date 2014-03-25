package models.signalDetection;

import play.db.ebean.Model;
import javax.persistence.*;
import models.TimeLog;

import models.ExperimentSchedule;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="signal_trial")
public class Trial extends Model{
    @Id
    public long id;

    @ManyToOne
    public ExperimentSchedule schedule;

    @OneToMany
    public List<Quiz> quizzes;

    public Trial(){}

    public static Trial create(ExperimentSchedule experimentSchedule){
        Trial trial = new Trial();
        trial.schedule = experimentSchedule;
        return trial;
    }

    public static List<Trial> findInvolving(ExperimentSchedule ex){
        return find.where().eq("schedule", ex).findList();
    }

    @SuppressWarnings("unchecked")
    public static Model.Finder<Long,Trial> find = new Finder(Long.class, Trial.class);
}
