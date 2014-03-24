package models.unitTest;

import models.signalDetection.*;
import play.test.WithApplication;
import org.junit.*;
import static org.junit.Assert.*;
import static play.test.Helpers.*;
import java.util.List;
import java.util.Date;
import java.util.Calendar;

public class SignalDetectionUnitTest extends WithApplication {

    @Before
    public void setUp(){
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));

    }

    @Test
    public void createQuestionNotNull(){
        assertNotNull(new Question());
    }

    @Test
    public void createQuestionWithParameter(){
        Question q = new Question('x','y');
        assertEquals('x',q.target);
        assertEquals('y',q.noise);
    }

    @Test
    public void createQuestionAndSaveComplete(){
        new Question('x','y').save();
        assertEquals(1,Question.find.findRowCount());
    }

    @Test
    public void saveAndRetrieveQuestionComplete(){
        new Question('x','y').save();
        Question q = Question.find.where().eq("id", new Long(1)).findUnique();
        assertEquals('x',q.target);
        assertEquals('y',q.noise);
    }

    @Test
    public void createQuizNotNull(){
        assertNotNull(new Quiz());
    }

    @Test
    public void createQuizWithParameter(){
        Quiz q = new Quiz(5,2);
        assertEquals(5,q.displayTime);
        assertEquals(2,q.noOfTarget);
    }

    @Test
    public void createQuizAndSaveComplete(){
        new Quiz(2,2).save();
        assertEquals(1,Quiz.find.findRowCount());
    }

    @Test
    public void saveAndRetrieveQuizComplete(){
        new Quiz(5,2).save();
        Quiz q = Quiz.find.byId(1L);
        assertEquals(5,q.displayTime);
        assertEquals(2,q.noOfTarget);
    }


    @Test
    public void quizCanAccessToQuestion(){
        Quiz quiz = new Quiz(5,2);
        Question q = new Question('x','y');
        quiz.question = q;
        q.save();
        quiz.save();
        Quiz quiz2 = Quiz.find.byId(1L);
        Question question = quiz2.question;
        assertEquals(q,question);
        assertEquals('x',question.target);
        assertEquals('y',question.noise);
    }

    @Test
    public void createQuizByMethonCcreate(){
        Question q = new Question('x','y');
        q.save();
        Quiz.create(5,2,q).save();
        Quiz quiz = Quiz.find.byId(1L);

        assertEquals(5,quiz.displayTime);
        assertEquals(2,quiz.noOfTarget);
        assertEquals(q,quiz.question);
    }

}
