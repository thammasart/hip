package models.attentionBlink;
import models.User;

import play.db.ebean.*;
import javax.persistence.*;

@Entity
@Table (name = "attention_blink_answer")
public class Answer extends Model{

        @Id
        public long id;
        public double usedTime;
        public boolean answer; 
        public boolean isCorrect;

        @ManyToOne
        public User user;
        @ManyToOne 
        public Quiz quiz;

	public Answer(boolean answer,double usedTime) {
            this.answer = answer;
            this.usedTime = usedTime;
	}

	@SuppressWarnings("unchecked")
	public static Finder<Long,Answer> find = new Finder(Long.class,Answer.class);
}
