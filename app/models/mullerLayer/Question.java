package models.mullerLayer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import play.db.ebean.Model;
import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="muller_layer_question")
public class Question extends Model{
    @Id
    public long id;
    public LineType line1;
    public LineType line2;
    public LineType line3;
    public LineType line4;
    public LineType line5;
    @OneToMany(mappedBy = "question")
    @JsonBackReference
    public List<Quiz> quizzes = new ArrayList<Quiz>();

    public Question(){}

    @SuppressWarnings("unchecked")
    public static Finder<Long, Question> find = new Finder(Long.class,Question.class);

    public static Question generateQuestion() {
        Question question = new Question();
        List<LineType> lines = new ArrayList<LineType>();
        for(LineType line : LineType.values()){
            lines.add(line);
        }
        Collections.shuffle(lines);
        question.line1 = lines.get(0);
        question.line2 = lines.get(1);
        question.line3 = lines.get(2);
        question.line4 = lines.get(3);
        question.line5 = lines.get(4);

        question.save();
        return question;
    }
}
