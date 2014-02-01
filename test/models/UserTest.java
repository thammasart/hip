import play.test.WithApplication;
import org.junit.*;
import java.util.List;
import static org.junit.Assert.*;
import static play.test.Helpers.*;
import models.User;
import models.UserRole;
import static org.hamcrest.CoreMatchers.instanceOf;
import javax.persistence.PersistenceException;



public class UserTest extends WithApplication{
	@Before
	public void setUp(){
		start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
	}

	@Test
	public void createUserInfo(){

		User user = new User("123","secret");
		assertNotNull(user);
	}

	@Test
	public void saveUserInfo(){

		new User("123","Secret").save();

		new User("124","Secret").save();

		assertEquals(2,User.find.findRowCount());

	}
    
        @Test
        public void recieveUserInfoCorrectly(){
            new User("122","Secret").save();

            User user = User.find.where().eq("username","122").findUnique();
            assertEquals("122",user.username);
            assertEquals("Secret",user.password);

        }

        @Test 
        public void statusShouldBeStudentByDefault(){

        	new User("123","Secret").save();

        	User user = User.find.where().eq("username","123").findUnique();
     		assertNotNull(user);
        	assertEquals(user.status,UserRole.STUDENT);

        }

        @Test
        public void passwordShouldBeChangeCorrectly(){
        	new User("100", "secret").save();
        	User user = User.find.where().eq("username", "100").findUnique();
        	user.setPassword("new_password");
        	user.save();
        	User clone = User.find.where().eq("username", "100").findUnique();
        	assertEquals(user.password, clone.password);
        }

        @Test
        public void getAllUserSizeShouldBeCorrect(){
        	new User("1", "password").save();
        	new User("2", "password").save();
        	new User("3", "password").save();
        	new User("4", "password").save();
        	new User("5", "password").save();
        	new User("6", "password").save();
        	List<User> users = User.getAllUser();
        	assertEquals(6, users.size());
        }

        @Test (expected = PersistenceException.class)
        public void userIdShouldNotDuplicate(){
        	new User("100","1234").save();
        	new User("100","1234").save();
        	
        }

}

















