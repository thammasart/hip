package models.unitTest;

import models.changeBlindness.*;
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

public class ChangeBlindnessUnitTest extends WithApplication {

    @Before
    public void setUp(){
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date lastYearDate = calendar.getTime();
        calendar.add(Calendar.YEAR, +2);
        Date nextYearDate = calendar.getTime();
        new ExperimentSchedule("Change Blindness Experiment", 3, lastYearDate, nextYearDate, ExperimentType.VISUALSEARCH).save();
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
    public void createQuestionShouldWithParameterCorrect(){
        Question question = new Question("path/of/pic1.jpg", "path/of/pic2.jpg",10 ,20);
        assertEquals("path/of/pic1.jpg", question.pathOfPic1);
        assertEquals("path/of/pic2.jpg", question.pathOfPic2);
        assertEquals(10, question.positionOfChangeX);
        assertEquals(20, question.positionOfChangeY);
    }

    @Test
    public void queryQuestionShouldCorrect(){
        new Question("path/of/pic1.jpg", "path/of/pic2.jpg",10 ,20).save();
        Question question = Question.find.byId(1L);
        assertNotNull(question);
        assertEquals("path/of/pic1.jpg", question.pathOfPic1);
        assertEquals("path/of/pic2.jpg", question.pathOfPic2);
        assertEquals(10, question.positionOfChangeX);
        assertEquals(20, question.positionOfChangeY);
    }

    @Test
    public void createQuizShouldNotNull(){
        ExperimentSchedule exp = ExperimentSchedule.find.byId(1L);
        new Trial(exp).save();
        Trial trial = Trial.find.byId(1L);
        Question question = new Question("path/of/pic1.jpg", "path/of/pic2.jpg",10 ,20);
        Quiz quiz = new Quiz(trial, 15, question);

        assertNotNull(quiz);
        assertEquals(trial, quiz.trial);
        assertEquals(15, quiz.displayTime);
        assertEquals(question, quiz.question);
    }

    @Test
    public void queryQuizShouldCorrect(){
        ExperimentSchedule exp = ExperimentSchedule.find.byId(1L);
        new Trial(exp).save();
        Trial trial = Trial.find.byId(1L);
        new Question("path/of/pic1.jpg", "path/of/pic2.jpg",10 ,20).save();
        Question question = Question.find.byId(1L);
        new Quiz(trial, 120, question).save();
        Quiz quiz = Quiz.find.byId(1L);
        assertNotNull(quiz);
        assertEquals(trial, quiz.trial);
        assertEquals(120, quiz.displayTime);
        assertEquals(question, quiz.question);
    }

    @Test
    public void createAnswerShouldNotNull(){
        ExperimentSchedule exp = ExperimentSchedule.find.byId(1L);
        new Trial(exp).save();
        Trial trial = Trial.find.byId(1L);
        new Question("path/of/pic1.jpg", "path/of/pic2.jpg",10 ,20).save();
        Question question = Question.find.byId(1L);
        new Quiz(trial, 60, question).save();
        Quiz quiz = Quiz.find.byId(1L);
        User user = User.find.byId("test");
        Answer answer = new Answer(user, quiz);
        answer.positionOfChangeX = 10;
        answer.positionOfChangeY = 20;
        answer.usedTime = 10.5;
        answer.isCorrect = true;
        assertNotNull(answer);
        assertEquals(user, answer.user);
        assertEquals(quiz, answer.quiz);
        assertEquals(10, answer.positionOfChangeX);
        assertEquals(20, answer.positionOfChangeY);
        assertEquals(10.5, answer.usedTime,0.001);
        assertTrue(answer.isCorrect);
    }

    @Test
    public void queryAnswerShouldCorrect(){
        ExperimentSchedule exp = ExperimentSchedule.find.byId(1L);
        new Trial(exp).save();
        Trial trial = Trial.find.byId(1L);
        new Question("path/of/pic1.jpg", "path/of/pic2.jpg",10 ,20).save();
        Question question = Question.find.byId(1L);
        new Quiz(trial, 60, question).save();
        Quiz quiz = Quiz.find.byId(1L);
        User user = User.find.byId("test");
        Answer temp = new Answer(user, quiz);
        temp.positionOfChangeX = 10;
        temp.positionOfChangeY = 20;
        temp.usedTime = 10.5;
        temp.isCorrect = true;
        temp.save();

        Answer answer = Answer.find.byId(1L);
        assertNotNull(answer);
        assertEquals(user, answer.user);
        assertEquals(quiz, answer.quiz);
        assertEquals(10, answer.positionOfChangeX);
        assertEquals(20, answer.positionOfChangeY);
        assertEquals(10.5, answer.usedTime,0.001);
        assertTrue(answer.isCorrect);
    }

    @Test
    public void getListAnswerFromUserAndTrialShouldCorrect(){
        ExperimentSchedule exp = ExperimentSchedule.find.byId(1L);
        new Trial(exp).save();
        Trial trial = Trial.find.byId(1L);
        new Question("path/of/pic1.jpg", "path/of/pic2.jpg",10 ,20).save();
        new Question("path/of/pic3.jpg", "path/of/pic4.jpg",30 ,40).save();
        new Question("path/of/pic5.jpg", "path/of/pic6.jpg",100 ,200).save();
        Question question1 = Question.find.byId(1L);
        Question question2 = Question.find.byId(2L);
        Question question3 = Question.find.byId(3L);
        new Quiz(trial, 60, question1).save();
        new Quiz(trial, 120, question2).save();
        new Quiz(trial, 180, question3).save();
        Quiz quiz1 = Quiz.find.byId(1L);
        Quiz quiz2 = Quiz.find.byId(2L);
        Quiz quiz3 = Quiz.find.byId(3L);
        User user = User.find.byId("test");
        Answer temp1 = new Answer(user, quiz1);
        temp1.positionOfChangeX = 10;
        temp1.positionOfChangeY = 20;
        temp1.isCorrect = true;
        temp1.usedTime = 10.5;
        temp1.save();
        Answer temp2 = new Answer(user, quiz2);
        temp2.positionOfChangeX = 20;
        temp2.positionOfChangeX = 30;
        temp2.isCorrect = false;
        temp2.usedTime = 39.5;
        temp2.save();
        Answer temp3 = new Answer(user, quiz3);
        temp3.positionOfChangeX = 100;
        temp3.positionOfChangeX = 200;
        temp3.usedTime = 2.05;
        temp3.isCorrect = true;
        temp3.save();
        List<Answer> answers = new ArrayList<Answer>();
        answers = Answer.findInvolving(user, trial.quizzes);
        assertNotNull(answers);
        assertEquals(Answer.find.byId(1L), answers.get(0));
        assertEquals(Answer.find.byId(2L), answers.get(1));
        assertEquals(Answer.find.byId(3L), answers.get(2));
    }
}