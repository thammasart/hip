package models.signalDetection;

import play.db.ebean.Model;
import javax.persistence.*;
import models.TimeLog;
import models.ExperimentSchedule;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="signal_detection_trial")
public class Trial extends Model{
    @Id
    public long id;
    @ManyToOne
    public ExperimentSchedule schedule;
    @OneToMany
    public List<Quiz> quizzes;

    public static final int TOTAL_QUESTION = 3;
    public Trial(){}

    public static Trial create(ExperimentSchedule experimentSchedule){
        Trial trial = new Trial();
        trial.schedule = experimentSchedule;
        return trial;
    }

    public static List<Trial> findInvolving(ExperimentSchedule ex){
        return find.where().eq("schedule", ex).findList();
    }

    public void generateQuiz(){
        for(int i = 0; i < TOTAL_QUESTION; i++){
            Question question = new Question('Y', 'X');
            question.save();
            Quiz.create(0.1, 1,15, this, question).save();
        }
    }

    @SuppressWarnings("unchecked")
    public static Model.Finder<Long,Trial> find = new Finder(Long.class, Trial.class);
}
