package models.unitTest;

import models.garnerInterference.*;
import models.ExperimentSchedule;
import models.ExperimentType;
import models.User;
import play.test.WithApplication;
import org.junit.*;
import static org.junit.Assert.*;
import static play.test.Helpers.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;

public class GarnerInterferenceUnitTest extends WithApplication {

    @Before
    public void setUp(){
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date lastYearDate = calendar.getTime();
        calendar.add(Calendar.YEAR, +2);
        Date nextYearDate = calendar.getTime();
        new ExperimentSchedule("Garner Interference Experiment", 3, lastYearDate, nextYearDate, ExperimentType.GARNERINTERFERENCE).save();
        new User("test","test").save();
    }

    @Test
    public void createTrialShouldNotNull(){
        ExperimentSchedule exp = ExperimentSchedule.find.byId(1L);
        Trial trial = new Trial(exp);
        assertNotNull(trial);
        assertEquals(exp, trial.schedule);
    }

    @Test
    public void queryTrialShouldCorrect(){
        ExperimentSchedule exp = ExperimentSchedule.find.byId(1L);
        new Trial(exp).save();
        Trial trial = Trial.find.byId(1L);
        assertNotNull(trial);
        assertEquals(exp, trial.schedule);
    }

    @Test
    public void createQuestionShouldNotNull(){
        Question question = new Question();
        assertNotNull(question);
    }

    @Test
    public void queryQuestionShouldCorrect(){
        new Question().save();
        Question question = Question.find.byId(1L);
        assertNotNull(question);
    }

    @Test
    public void createQuizShouldNotNull(){
        ExperimentSchedule exp = ExperimentSchedule.find.byId(1L);
        new Trial(exp).save();
        Trial trial = Trial.find.byId(1L);
        new Question().save();
        Question question = Question.find.byId(1L);
        Quiz quiz = new Quiz(trial, question);
        assertNotNull(quiz);
        assertEquals(trial, quiz.trial);
        assertEquals(question, quiz.question);
    }

    @Test
    public void queryQuizShouldCorrect(){
        ExperimentSchedule exp = ExperimentSchedule.find.byId(1L);
        new Trial(exp).save();
        Trial trial = Trial.find.byId(1L);
        new Question().save();
        Question question = Question.find.byId(1L);
        new Quiz(trial, question).save();
        Quiz quiz = Quiz.find.byId(1L);
        assertNotNull(quiz);
        assertEquals(trial, quiz.trial);
        assertEquals(question, quiz.question);
    }

    @Test
    public void createAnswerShouldNotNull(){
        ExperimentSchedule exp = ExperimentSchedule.find.byId(1L);
        new Trial(exp).save();
        Trial trial = Trial.find.byId(1L);
        new Question().save();
        Question question = Question.find.byId(1L);
        new Quiz(trial, question).save();
        Quiz quiz = Quiz.find.byId(1L);
        User user = User.find.byId("test");
        Answer answer = new Answer(user, quiz);
        assertNotNull(answer);
        assertEquals(user, answer.user);
        assertEquals(quiz, answer.quiz);
    }

    @Test
    public void queryAnswerShouldCorrect(){
        ExperimentSchedule exp = ExperimentSchedule.find.byId(1L);
        new Trial(exp).save();
        Trial trial = Trial.find.byId(1L);
        new Question().save();
        Question question = Question.find.byId(1L);
        new Quiz(trial, question).save();
        Quiz quiz = Quiz.find.byId(1L);
        User user = User.find.byId("test");
        new Answer(user, quiz).save();
        Answer answer = Answer.find.byId(1L);
        assertNotNull(answer);
        assertEquals(user, answer.user);
        assertEquals(quiz, answer.quiz);
    }

    @Test
    public void getListAnswerFromUserAndTrialShouldCorrect(){
        ExperimentSchedule exp = ExperimentSchedule.find.byId(1L);
        new Trial(exp).save();
        Trial trial = Trial.find.byId(1L);
        new Question().save();
        new Question().save();
        new Question().save();
        Question question1 = Question.find.byId(1L);
        Question question2 = Question.find.byId(2L);
        Question question3 = Question.find.byId(3L);
        new Quiz(trial, question1).save();
        new Quiz(trial, question2).save();
        new Quiz(trial, question3).save();
        Quiz quiz1 = Quiz.find.byId(1L);
        Quiz quiz2 = Quiz.find.byId(2L);
        Quiz quiz3 = Quiz.find.byId(3L);
        User user = User.find.byId("test");
        new Answer(user, quiz1).save();
        new Answer(user, quiz2).save();
        new Answer(user, quiz3).save();
        List<Answer> answers = new ArrayList<Answer>();
        answers = Answer.findInvolving(user, trial.quizzes);
        assertNotNull(answers);
        assertEquals(Answer.find.byId(1L), answers.get(0));
        assertEquals(Answer.find.byId(2L), answers.get(1));
        assertEquals(Answer.find.byId(3L), answers.get(2));
    }
}