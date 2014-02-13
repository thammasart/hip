package models.stroopEffect;

import models.User;
import play.test.WithApplication;
import org.junit.*;
import static org.junit.Assert.*;
import static play.test.Helpers.*;

public class AnswerTest extends WithApplication{

    @Before
    public void setUp(){
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
    }

    @Test
    public void createAnswer(){
        Answer answer = new Answer("BLACK",10);
        assertNotNull(answer);
    }

    @Test
    public void createAnswerWithProperty(){
        Answer answer = new Answer("BLACK",10.0);
        assertEquals("BLACK",answer.answer);
    }

    @Test
    public void saveAndQuery(){
        new Answer("BLACK",10.0).save();
        assertNotNull(Answer.find.byId(new Long(1)));
    }

    @Test
    public void createAndConnectWithQuizAndUser(){
        Quiz q = new Quiz();
        q.save();
        new User("s550","1234").save();
        User u = User.find.where().eq("username","s550").findUnique();
        Answer ans = Answer.create("BLACK",10,u,q);
        ans.save();
        assertEquals(q.id,ans.quiz.id);
        assertEquals(u.username,ans.user.username);
    }


}
