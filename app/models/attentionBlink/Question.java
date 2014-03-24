package models.attentionBlink;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
@Table (name = "attention_blink_question")
public class Question extends Model{
	@Id
	public long id;
        @Column(nullable=false, length=2)
	public String letter;
        @Column(nullable=false, length=20)
        public String set;
        @Column(nullable=false, length=2)
        public String correctAnswer;
        public QuestionType questionType;

	public Question(String letter, String set, String correctAnswer, QuestionType questionType) {
            this.letter = letter;
            this.set = set;
            this.correctAnswer = correctAnswer;
            this.questionType = questionType; 
	}
        
        @SuppressWarnings("unchecked")
        public static Finder<Long, Question> find = new Finder(Long.class, Question.class);
}
