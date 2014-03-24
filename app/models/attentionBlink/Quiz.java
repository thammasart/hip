package models.attentionBlink;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name = "attention_blink_quiz")
public class Quiz extends Model{
        
        @Id
        public long id;

        @ManyToOne
        public Trial trial;

        @ManyToOne
        public Question question;

        @OneToMany
        public List<Answer> answers = new ArrayList<Answer>();

	public Quiz() {

	}

	@SuppressWarnings("unchecked")
	public static Finder<Long, Quiz> find = new Finder(Long.class, Quiz.class);
}
