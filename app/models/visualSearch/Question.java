package models.visualSearch;

import play.db.ebean.Model;
import javax.persistence.*;

@Entity
@Table (name="visual_search_question")
public class Question extends Model{
    @Id
    public long id;
    public ShapeType shapeType;
    public int positionX;
    public int positionY;
    @ManyToOne
    public Quiz quiz;

    public Question(Quiz quiz){
    	this.quiz = quiz;
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Question> find = new Finder(Long.class, Question.class);

}
