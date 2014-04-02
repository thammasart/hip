package models.attentionBlink;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import play.db.ebean.Model.Finder;

@Entity
@Table (name = "attention_blink_quiz")
public class Quiz extends Model{
        
        @Id
        public long id;
        public int length = 13;
        public int numberOfTarget = 2;
        public double blinkTime = 0.1;
        public boolean isCorrect = true;

        @ManyToOne
        public Trial trial;

        @ManyToOne
        public Question question;

        @OneToMany
        public List<Answer> answers = new ArrayList<Answer>();

	@SuppressWarnings("unchecked")
	public static Finder<Long, Quiz> find = new Finder(Long.class, Quiz.class);
}
