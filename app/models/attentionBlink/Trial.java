package models.attentionBlink;

import play.db.ebean.Model;
import javax.persistence.*;

import models.ExperimentSchedule;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="attention_blink_trial")
public class Trial extends Model{
    @Id
    public long id;
    public int lenght;
    public int blinkTime;
    public String charactorType;

    public static final int TOTAL_QUESTION = 3;

    @ManyToOne
    public ExperimentSchedule schedule;
    @OneToMany
    public List<TimeLog> timeLogs = new ArrayList<TimeLog>();
    @OneToMany
    public List<Quiz> quizzes = new ArrayList<Quiz>();

    public Trial(){

    }

    public static Trial create(ExperimentSchedule experimentSchedule){
        Trial trial = new Trial();
        trial.schedule = experimentSchedule;
        return trial;
    }

    public static Trial findById(long id){
        return find.byId(new Long(id));
    }

    public static List<Trial> findInvolving(ExperimentSchedule ex){
        return find.where().eq("schedule", ex).findList();
    }

    @SuppressWarnings("unchecked")
    public static Model.Finder<Long,Trial> find = new Finder(Long.class, Trial.class);
}
