package models.magicNumber7;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import play.db.ebean.Model.Finder;

@Entity
@Table (name = "magic_number_7_question")
public class Question extends Model{
    @Id
    public long id;
    public String memorySet;
    public QuestionType questionType;
    @OneToMany
    public List<Quiz> quizzes = new ArrayList<Quiz>();

    public Question(String memorySet, QuestionType questionType) {
    	this.memorySet = memorySet;
    	this.questionType = questionType;
    }

	@SuppressWarnings("unchecked")
	public static Finder<Long, Question> find = new Finder(Long.class, Question.class);
}