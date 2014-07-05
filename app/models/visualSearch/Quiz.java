package models.visualSearch;

import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="visual_search_quiz")
public class Quiz extends Model{
    @Id
    public long id;
    public int positionXofTarget;
    public int positionYofTarget;
    @ManyToOne
    public Trial trial;
    @OneToOne
    public Question question;

    public Quiz(Trial trial){
    	this.trial = trial;
    }

    public static List<Quiz> findInvolving(Trial trial){
        return Quiz.find.where().eq("trial", trial).findList();
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Quiz> find = new Finder(Long.class, Quiz.class);

}
