package models.brownPeterson;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

@Entity
public class Question extends Model {
	@Id
	public Long id;
	public String firstWord;
	public String secondWord;
	public String thirdWord;

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
        
	public static Finder<Long,Question> find = new Finder(Long.class,Question.class);

}
