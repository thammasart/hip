package models.visualSearch;

import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.util.List;

@Entity
@Table (name="visual_search_question")
public class Question extends Model{
    @Id
    public long id;
    public ShapeType shapeType;
    public int positionX;
    public int positionY;
    public String sharps;

    public Question(){
    }

    public static List<Question> findInvolving(Quiz quiz){
        return Question.find.where().eq("quiz", quiz).findList();
    }

    public String getQustionJson(){
        return Json.stringify(Json.toJson(this));
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Question> find = new Finder(Long.class, Question.class);

}
