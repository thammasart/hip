package models.unitTest;

import models.visualSearch.*;
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

public class VisualSearchUnitTest extends WithApplication {

    @Before
    public void setUp(){
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date lastYearDate = calendar.getTime();
        calendar.add(Calendar.YEAR, +2);
        Date nextYearDate = calendar.getTime();
        new ExperimentSchedule("Visual Search Experiment", 3, lastYearDate, nextYearDate, ExperimentType.VISUALSEARCH).save();
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
        Quiz quiz = new Quiz();
        assertNotNull(quiz);
    }

    @Test
    public void queryQuizShouldCorrect(){
        new Quiz().save();
        Quiz quiz = Quiz.find.byId(1L);
        assertNotNull(quiz);
    }
}