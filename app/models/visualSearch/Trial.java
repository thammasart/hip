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
    public ShapeType target = ShapeType.CIRCLE_BLUE;
    public int dense;
    public boolean sqaureBlue;
    public boolean sqaureGreen;
    public boolean sqaureRed;
    public boolean circleGreen;
    public boolean circleRed;
    public FrameSize frameSize;
    @ManyToOne
    public ExperimentSchedule schedule;
    @OneToMany
    public List<Quiz> quizzes = new ArrayList<Quiz>();

    public Trial(ExperimentSchedule schedule){
    	this.schedule = schedule;
    }

    public static Trial findById(long id){
        return find.byId(new Long(id));
    }

    public static List<Trial> findInvolving(ExperimentSchedule ex){
        return find.where().eq("schedule", ex).findList();
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Trial> find = new Finder(Long.class, Trial.class);

}
