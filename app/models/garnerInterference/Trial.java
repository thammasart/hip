package models.garnerInterference;

import models.ExperimentSchedule;
import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="garner_interference_trial")
public class Trial extends Model{
    @Id
    public long id;
    public double lengthBigSquare;
    public double lengthSmallSquare;
    public String color;
    public Feature feature; // one feature or two feature
    @ManyToOne
    public Color colorDark;
    @ManyToOne
    public Color colorLight;
    @ManyToOne
    public ExperimentSchedule schedule;
    @OneToMany(cascade=CascadeType.REMOVE)
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
        return null;
    }

    public void generateQuiz() {

    }
}
