package models.mullerLayer;

import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="muller_layer_trial")
public class Trial extends Model{
    @Id
    public long id;

    /*@OneToMany
    public List<Quiz> quizzes = new ArrayList<Quiz>();
*/
    public Trial(){}

    @SuppressWarnings("unchecked")
    public static Finder<Long, Trial> find = new Finder(Long.class, Trial.class);

}
