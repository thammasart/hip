package models.sternbergSearch;

import models.User;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name = "sternberg_search_answer")
public class Answer extends Model{

    @Id
    public long id;
    public boolean answer;
    public double usedTime;
    public boolean isCorrect;
    @ManyToOne
    public User user;
    @ManyToOne 
    public Quiz quiz;

    public Answer(User user, Quiz quiz){
        this.user = user;
        this.quiz = quiz;
    }

    public static Answer create(User user, Quiz quiz, boolean answer, double usedTime, boolean isCorrect){
        Answer newAnswer = new Answer(user, quiz);
        newAnswer.answer = answer;
        newAnswer.usedTime = usedTime;
        newAnswer.isCorrect = isCorrect;
        return newAnswer;
    }

	@SuppressWarnings("unchecked")
	public static Finder<Long,Answer> find = new Finder(Long.class,Answer.class);
}
