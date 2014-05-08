package models.mullerLayer;

import models.User;

import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="muller_layer_answer")
public class Answer extends Model{
    @Id
    public long id;
    @ManyToOne
    public User user;
    @ManyToOne 
    public Quiz quiz;

    public Answer(User user, Quiz quiz){
        this.user = user;
        this.quiz = quiz;
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Answer> find = new Finder(Long.class,Answer.class);

}
