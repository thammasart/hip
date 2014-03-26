package models.brownPeterson;

import play.db.ebean.*;
import play.db.ebean.Model.Finder;
import javax.persistence.*;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

@Entity
@Table (name="brown_peterson_question")
public class Question extends Model {
	@Id
	public Long id;
	@Column(nullable=false, length=20)
	public String firstWord;
	@Column(nullable=false, length=20)
	public String secondWord;
	@Column(nullable=false, length=20)
	public String thirdWord;
	public String trigramType = Trial.WORD;
	public String trigramLanguage = Trial.ENGLISH;	

	public Question (String firstWord, String secondWord,String thirdWord){
		this.firstWord = firstWord;
		this.secondWord = secondWord;
		this.thirdWord = thirdWord;
	}

	public static Question findQuestionById(int id) {
		return find.byId(new Long(id));
	}

	public static List<Question> getQuestionListBy(int amount){
		List<Question> allQuestion = new ArrayList<Question>();
        List<Question> questionList = new ArrayList<Question>();
        allQuestion = find.all();
        if(allQuestion.size() >= amount){
	        Collections.shuffle(allQuestion);
			for(int i = 0;i < amount;i++){
	        	questionList.add(allQuestion.get(i));
			}
			return questionList;
		}
		return null;
	}

	public static List<Question> findInvolving(List<Quiz> quizzes){
		List<Question> questions = new ArrayList<Question>();
		for(Quiz quiz : quizzes){
			questions.add(quiz.question);
		}
		return questions;
	}

	public String toString(){
		return firstWord + ", " + secondWord + ", " + thirdWord;
	}
    
    @SuppressWarnings("unchecked")
	public static Finder<Long,Question> find = new Finder(Long.class,Question.class);

}
