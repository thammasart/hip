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
    @OneToMany
    public List<Question> questions = new ArrayList<Question>();

    public Quiz(Trial trial){
    	this.trial = trial;
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Quiz> find = new Finder(Long.class, Quiz.class);

}
