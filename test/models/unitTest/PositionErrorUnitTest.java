package models.unitTest;

import models.positionError.*;
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

public class PositionErrorUnitTest extends WithApplication {

    @Before
    public void setUp(){
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date lastYearDate = calendar.getTime();
        calendar.add(Calendar.YEAR, +2);
        Date nextYearDate = calendar.getTime();
        new ExperimentSchedule("Experiment 1", 3, lastYearDate,
         		nextYearDate, ExperimentType.POSITIONERROR).save();
        new User("123","Secret").save();

    }

    @Test
    public void createTrialNotNull(){
        assertNotNull(new Trial(0.0, 0.0, QuestionType.THAI));
    }

    @Test
    public void createTrialWithParameter(){
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
        Trial trial = Trial.create(0.5,0.6,QuestionType.THAI,ex);
        assertEquals(0.5, trial.flashSpeed, 0.001);
        assertEquals(0.6, trial.delayTime, 0.001);
        assertEquals(QuestionType.THAI, trial.questionType);
    }

    @Test
    public void createTrialAndSaveComplete(){
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
        Trial trial = Trial.create(0.7,0.8,QuestionType.NUMBER,ex);
    	trial.save();
    	assertEquals(1,Trial.find.findRowCount());
    	assertEquals(trial, Trial.find.byId(1L));
    }

    @Test
    public void createQuestionNotNull(){
        assertNotNull(new Question("12345", QuestionType.NUMBER));
    }

    @Test
    public void createQuestionWithParameter(){
    	Question question = new Question("12345",QuestionType.NUMBER);
        assertEquals("12345", question.memorySet);
        assertEquals(QuestionType.NUMBER, question.questionType);
    }

    @Test
    public void createQuestionAndSaveComplete(){
    	Question question  = Question.create("หกสวยนร",QuestionType.THAI);
    	question.save();
    	assertEquals(1,Question.find.findRowCount());
    	assertEquals(question, Question.find.byId(1L));
    }

    @Test
    public void createQuizNotNull(){
    	Question.create("12345",QuestionType.NUMBER).save();
        Question q = Question.find.byId(1L);
        ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
        Trial.create(0.7,0.8,QuestionType.NUMBER,ex).save();
        Trial trial = Trial.find.byId(1L);
        assertNotNull(new Quiz(5,q,trial));
    }

    @Test
    public void createQuizWithParameter(){
    	Question.create("12345",QuestionType.NUMBER).save();
        Question q = Question.find.byId(1L);
        ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
        Trial.create(0.7,0.8,QuestionType.NUMBER,ex).save();
        Trial trial = Trial.find.byId(1L);
    	Quiz quiz = new Quiz(12,q,trial);
        assertEquals(12,quiz.length);
    }

    @Test
    public void createQuizAndSaveComplete(){
    	Question.create("12345",QuestionType.NUMBER).save();
        Question q = Question.find.byId(1L);
        ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
        Trial.create(0.7,0.8,QuestionType.NUMBER,ex).save();
        Trial trial = Trial.find.byId(1L);
    	Quiz quiz  = Quiz.create(7,q,trial);
    	quiz.save();
    	assertEquals(1,Quiz.find.findRowCount());
    	assertEquals(quiz, Quiz.find.byId(1L));
    }

    @Test
    public void questionShouldFindQuizzesCorrectly(){
        Question.create("12345",QuestionType.NUMBER).save();
        Question q = Question.find.byId(1L);
        ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
        Trial.create(0.7,0.8,QuestionType.NUMBER,ex).save();
        Trial trial = Trial.find.byId(1L);

        Quiz.create(5,q,trial).save();

        List<Quiz> quizzes = Question.find.byId(1L).quizzes;

        assertEquals(1,quizzes.size());
        Quiz quiz = Quiz.find.byId(1L);
        assertEquals(quiz, quizzes.get(0));
    }

    @Test
    public void trialShouldFindQuizzesCorrectly(){
        Question.create("12345",QuestionType.NUMBER).save();
        Question q = Question.find.byId(1L);
        Question.create("67890",QuestionType.NUMBER).save();
        Question q2 = Question.find.byId(2L);
        ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
        Trial.create(0.7,0.8,QuestionType.NUMBER,ex).save();
        Trial trial = Trial.find.byId(1L);

        Quiz.create(5,q,trial).save();
        Quiz.create(5,q2,trial).save();

        List<Quiz> quizzes = Trial.find.byId(1L).quizzes;

        assertEquals(2,quizzes.size());
        Quiz quiz = Quiz.find.byId(1L);
        assertEquals(quiz, quizzes.get(0));
        quiz = Quiz.find.byId(2L);
        assertEquals(quiz, quizzes.get(1));
    }

    @Test
    public void createAnswerNotNull(){
        assertNotNull(new Answer("12345", 5.4321));
    }

    @Test
    public void createAnswerWithParameterAndSaveComplete(){
    	User user = User.find.byId("123");
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
        Trial trial = Trial.create(0.5,0.6,QuestionType.NUMBER,ex);
        trial.save();

        Question.create("12345",QuestionType.NUMBER).save();
        Question q = Question.find.byId(1L);
        Question.create("67890",QuestionType.NUMBER).save();
        Question q2 = Question.find.byId(2L);
        Question.create("32908",QuestionType.NUMBER).save();
        Question q3 = Question.find.byId(3L);

        Quiz.create(5,q,trial).save();
        Quiz.create(5,q2,trial).save();
        Quiz.create(5,q3,trial).save();

        Answer.create("12345",1.2345,user,Quiz.find.byId(1L)).save();
        Answer.create("67890",6.7890,user,Quiz.find.byId(2L)).save();
        Answer.create("23098",2.3098,user,Quiz.find.byId(3L)).save();

        assertEquals(3,Answer.find.findRowCount());
        Answer answer1 = Answer.find.byId(1L);
        assertEquals("12345", answer1.answer);
        assertEquals(1.2345,answer1.usedTime,0.0001);
        assertEquals(user, answer1.user);
        assertEquals(Quiz.find.byId(1L), answer1.quiz);
    }

    @Test
    public void calculateUsedTimeComplete(){
    	User user = User.find.byId("123");
    	ExperimentSchedule ex = ExperimentSchedule.find.byId(1L);
        Trial trial = Trial.create(0.5,0.6,QuestionType.NUMBER,ex);
        trial.save();

        Question.create("12345",QuestionType.NUMBER).save();
        Question q = Question.find.byId(1L);
        Question.create("67890",QuestionType.NUMBER).save();
        Question q2 = Question.find.byId(2L);
        Question.create("32908",QuestionType.NUMBER).save();
        Question q3 = Question.find.byId(3L);

        Quiz.create(5,q,trial).save();
        Quiz.create(5,q2,trial).save();
        Quiz.create(5,q3,trial).save();

        Answer.create("12345",12.5,user,Quiz.find.byId(1L)).save();
        Answer.create("67890",9.7,user,Quiz.find.byId(2L)).save();
        Answer.create("23098",10.4,user,Quiz.find.byId(3L)).save();

        List<Answer> answers = Answer.find.where().eq("user",user).findList();

        assertEquals(32.6, Answer.calculateTotalUsedTime(answers),0.0001);
    }

}