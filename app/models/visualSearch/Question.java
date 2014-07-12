package models.visualSearch;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.util.List;
import java.util.Random;

@Entity
@Table (name="visual_search_question")

public class Question extends Model{
    @Id
    public long id;
    @Column(columnDefinition = "TEXT")
    public String sharps;

    public Question(){
    }

    public static List<Question> findInvolving(Quiz quiz){
        return Question.find.where().eq("quiz", quiz).findList();
    }

    public String toStringJson(){
        return Json.stringify(Json.toJson(this));
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Question> find = new Finder(Long.class, Question.class);

}
