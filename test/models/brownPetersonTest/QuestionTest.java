import play.test.WithApplication;
import org.junit.*;
import static org.junit.Assert.*;
import static play.test.Helpers.*;
import models.brownPeterson.Question;
import java.util.List;

public class QuestionTest extends WithApplication{
	@Before
	public void setUp(){
		start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
	}

	@Test
	public void createQuestion(){
        Question q = new Question("hello","world","!!!");
        assertNotNull(q);
    }

    @Test
    public void saveAndQuery(){
    	new Question("hello","world","!!!").save();
    	assertEquals(1,Question.find.findRowCount());
    }
    @Test
    public void saveAndQueryFourQuestion(){
    	new Question("hello","world","!!!").save();
    	new Question("hello","world","!!!").save();
    	new Question("hello","world","!!!").save();
    	new Question("hello","world","!!!").save();
    	assertEquals(4,Question.find.findRowCount());

    }

    @Test
    public void recieveQuestionCorrectly(){
    	new Question("hello","world","!!!").save();
    	Question q = Question.find.where().eq("id", new Long(1)).findUnique();
    	assertEquals("hello",q.firstWord);
    	assertEquals("world",q.secondWord);
    	assertEquals("!!!",q.thirdWord);
    }

    @Test
    public void recieveQuestionById(){
    	new Question("hello","world","!!!").save();
        Question q = Question.findQuestionById(1);
    	assertEquals("hello",q.firstWord);
    	assertEquals("world",q.secondWord);
    	assertEquals("!!!",q.thirdWord);
    }    

    @Test
    public void recieveQuestionListByThree(){
    	new Question("hello1","world1","!!!1").save();
    	new Question("hello2","world2","!!!2").save();
    	new Question("hello3","world3","!!!3").save();
    	new Question("hello4","world4","!!!4").save();
    	new Question("hello5","world5","!!!5").save();

    	List<Question> questionList = Question.getQuestionListBy(3);
    	assertEquals(3,questionList.size());
    }

    @Test
    public void recieveQuestionListByFive(){
    	new Question("hello1","world1","!!!1").save();
    	new Question("hello2","world2","!!!2").save();
    	new Question("hello3","world3","!!!3").save();
    	new Question("hello4","world4","!!!4").save();
    	new Question("hello5","world5","!!!5").save();

    	List<Question> questionList = Question.getQuestionListBy(5);
    	assertEquals(5,questionList.size());
    }

    @Test
    public void recieveQuestionByAmountThreeNotNull(){
     	new Question("hello1","world1","!!!1").save();
    	new Question("hello2","world2","!!!2").save();
    	new Question("hello3","world3","!!!3").save();
    	new Question("hello4","world4","!!!4").save();
    	new Question("hello5","world5","!!!5").save();
    	
     	List<Question> questionList = Question.getQuestionListBy(3);
   		assertNotNull(questionList.get(0));
   		assertNotNull(questionList.get(1));
   		assertNotNull(questionList.get(2));
    }

    @Test
    public void recieveQuestionByAmountByfiveNotNull(){
     	new Question("hello1","world1","!!!1").save();
    	new Question("hello2","world2","!!!2").save();
    	new Question("hello3","world3","!!!3").save();
    	new Question("hello4","world4","!!!4").save();
    	new Question("hello5","world5","!!!5").save();
    	
     	List<Question> questionList = Question.getQuestionListBy(5);
   		assertNotNull(questionList.get(0));
   		assertNotNull(questionList.get(1));
   		assertNotNull(questionList.get(2));
   		assertNotNull(questionList.get(3));
   		assertNotNull(questionList.get(4));
    }

    @Test
    public void questionMemberInListShouldNotSame() {
    	new Question("hello1","world1","!!!1").save();
    	new Question("hello2","world2","!!!2").save();
    	new Question("hello3","world3","!!!3").save();
    	new Question("hello4","world4","!!!4").save();
    	new Question("hello5","world5","!!!5").save();

    	List<Question> questionList = Question.getQuestionListBy(3);
    	assertNotEquals(questionList.get(0).id, questionList.get(1).id);
    	assertNotEquals(questionList.get(0).id, questionList.get(2).id);
    	assertNotEquals(questionList.get(2).id, questionList.get(1).id);

    }

    @Ignore @Test
    public void randomTwoQuestionListShouldNotSame(){
    	new Question("hello1","world1","!!!1").save();
    	new Question("hello2","world2","!!!2").save();
    	new Question("hello3","world3","!!!3").save();
    	new Question("hello4","world4","!!!4").save();
    	new Question("hello5","world5","!!!5").save();

    	List<Question> questionList1 = Question.getQuestionListBy(2);
    	List<Question> questionList2 = Question.getQuestionListBy(2);

    	assertNotEquals(questionList1, questionList2);
    	assertNotEquals(questionList1, questionList2);
    }

    @Test
    public void amountOverflow() {
    	new Question("hello1","world1","!!!1").save();
    	new Question("hello2","world2","!!!2").save();
    	new Question("hello3","world3","!!!3").save();
    	new Question("hello4","world4","!!!4").save();
    	new Question("hello5","world5","!!!5").save();

    	List<Question> questionList = Question.getQuestionListBy(10);

    	assertNull(questionList);
    }
}
