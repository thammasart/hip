package models.attentionBlink;
import models.ExperimentSchedule;

import play.db.ebean.*;
import javax.persistence.*;

import java.util.List;
import java.util.ArrayList;

@Entity
@Table (name = "attention_blink_trial")
public class Trial extends Model{
	@Id
	public long id;
        public int length;
        public double blinkTime;
        public QuestionType questionType;        

        @ManyToOne
        public ExperimentSchedule schedule;

        @OneToMany
        public List<Quiz> quizzes = new ArrayList<Quiz>();

	public Trial(int length , double blinkTime ,QuestionType questionType) {
            this.length = length;
            this.blinkTime = blinkTime;
            this.questionType = questionType;
	}

	@SuppressWarnings("unchecked")
	public static Finder<Long, Trial> find = new Finder(Long.class, Trial.class);

}
