package models.sternbergSearch;

import play.db.ebean.*;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

import play.db.ebean.Model.Finder;

@Entity
@Table(name = "sternberg_search_quiz")
public class Quiz extends Model {
    @Id
    public long id;
    public String questionChar;
    public boolean isTrue;
    @ManyToOne
    public Trial trial;
    @ManyToOne
    public Question question;
    @OneToMany
    public List<Answer> answers = new ArrayList<Answer>();


    public Quiz(){}
    public Quiz(Trial trial, Question question) {
        this.trial = trial;
        this.question = question;
    }

    public static Quiz create(Trial trial, Question question, String questionWord, boolean isTrue) {
        Quiz newQuiz = new Quiz(trial, question);
        newQuiz.questionChar = questionWord;
        newQuiz.isTrue = isTrue;
        return newQuiz;
    }

    public static Quiz create(Trial trial){
        Quiz newQuiz = new Quiz();
        Question question = Question.generateQuestion(trial.questionType, trial.length);
        question.save();
        newQuiz.question = question;
        newQuiz.trial = trial;
        return newQuiz;
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Quiz> find = new Finder(Long.class, Quiz.class);
}