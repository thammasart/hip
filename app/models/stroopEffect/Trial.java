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
    public QuestionType questionType;
    @Column(nullable=false)
    public boolean colorMatch;

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
        questionType = QuestionType.THAI;
        colorMatch = true;
    }

    public Trial(long appearTime,QuestionType type, boolean colorMatch){
        this.appearTime = appearTime;
        this.questionType = type;
        this.colorMatch = colorMatch;
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
