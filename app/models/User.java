package models;

import models.brownPeterson.TimeLog;
import models.brownPeterson.Answer;
import java.util.List;
import java.util.ArrayList;
import play.db.ebean.*;
import javax.persistence.*;
@Entity
public class User extends Model{

	@Id
<<<<<<< HEAD
	public String username;
=======
	@Column(length=20)
	public String username;
	@Column(nullable=false, length=20)
>>>>>>> 09b91abf33f0d4e781fdadb2311fa8a67df887fe
	public String password;
	public UserRole status = UserRole.STUDENT;

	@OneToMany
	List<Answer> answers = new ArrayList<Answer>();
	@OneToMany
	List<TimeLog> timeLogs = new ArrayList<TimeLog>();

	public User(String username,String password){
		this.username = username;
		this.password =password;
	}

<<<<<<< HEAD
	public User(String username, String password) {
		this.username = username;
		this.password = password;
=======
	public static List<User> getAllUser(){
		return find.all();
>>>>>>> 09b91abf33f0d4e781fdadb2311fa8a67df887fe
	}

	public static User authenticate(String username, String password) {
		return find.where().eq("username", username).eq("password", password).findUnique();
	}

	@SuppressWarnings("unchecked")
	public static Finder<String,User> find = new Finder(String.class,User.class);
}