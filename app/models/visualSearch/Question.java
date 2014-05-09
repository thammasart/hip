package models.visualSearch;

import play.db.ebean.Model;
import javax.persistence.*;

@Entity
@Table (name="visual_search_trial")
public class Question extends Model{
    @Id
    public long id;

    public Question(){
    	
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Question> find = new Finder(Long.class, Question.class);

}
