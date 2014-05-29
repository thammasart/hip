package models.garnerInterference;

import play.db.ebean.Model;
import javax.persistence.*;

@Entity
@Table (name="garner_interference_question")
public class Question extends Model{
    @Id
    public long id;
    public String colorPic;
    public String sizePic;
    public String colorQuestion;
    public String sizeQuestion;
    public boolean colorMatch;
    public boolean sizeMatch;
    
    public Question(){
    	
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Question> find = new Finder(Long.class, Question.class);

}
