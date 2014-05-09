package models.visualSearch;

import models.ExperimentSchedule;

import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="visual_search_trial")
public class Trial extends Model{
    @Id
    public long id;
    @ManyToOne
    public ExperimentSchedule schedule;
    @OneToMany
    public List<Quiz> quizzes = new ArrayList<Quiz>();

    public Trial(ExperimentSchedule schedule){
    	this.schedule = schedule;
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Trial> find = new Finder(Long.class, Trial.class);

}
