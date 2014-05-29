package models.mullerLayer;

import models.ExperimentSchedule;

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

    @ManyToOne
    public ExperimentSchedule schedule;
    @OneToMany
    public List<Quiz> quizzes = new ArrayList<Quiz>();

    public Trial(ExperimentSchedule schedule){
    	this.schedule = schedule;
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
