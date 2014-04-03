package models.positionError;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name="position_error_question")
public class Question extends Model{
	@Id
	public long id;

	public String memorySet;
	public QuestionType questionType;

	@OneToMany
    public List<Quiz> quizzes;

	public Question(String memorySet, QuestionType questionType){
		this.memorySet = memorySet;
		this.questionType = questionType;
	}

	public static Question create(String memorySet, QuestionType questionType){
		Question newQuestion = new Question(memorySet,questionType);
		return newQuestion;
	}

	@SuppressWarnings("unchecked")
    public static Model.Finder<Long,Question> find = new Finder(Long.class, Question.class);
}