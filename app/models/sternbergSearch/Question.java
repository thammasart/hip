package models.sternbergSearch;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import play.db.ebean.Model.Finder;

@Entity
@Table (name = "sternberg_search_question")
public class Question extends Model{
    @Id
    public long id;
    public String question;
    public QuestionType questionType;
    @OneToOne
    public Quiz quiz;

    public Question(String question, QuestionType questionType) {
    	this.question = question;
    	this.questionType = questionType;
    }

	@SuppressWarnings("unchecked")
	public static Finder<Long, Question> find = new Finder(Long.class, Question.class);
}