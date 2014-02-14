package models;

import models.brownPeterson.TimeLog;
import models.brownPeterson.Answer;
import java.util.List;
import java.util.ArrayList;
import play.db.ebean.*;
import javax.persistence.*;
import java.util.Date;
import play.data.format.Formats;
@Entity
public class User extends Model{

	@Id
	@Column(length=20)
	public String username;
	@Column(nullable=false, length=20)
	public String password;
	public UserRole status = UserRole.STUDENT;
    public String firstName;
    public String lastName;
    public String gender;
    @Formats.DateTime(pattern="dd/mm/yyyy")
    public Date   birthDate;
    public String section;
    public String semester;
    public int year = 1;
    public String eMail;


	@OneToMany
	List<Answer> answers = new ArrayList<Answer>();
	@OneToMany
	List<TimeLog> timeLogs = new ArrayList<TimeLog>();

	public User(String username, String password) {
		this.username = username;
		this.password = password;
        this.firstName = "";
        this.lastName = "";
        this.gender = "";
        this.section = "";
        this.semester = "";
        this.year = 1;
        this.eMail = "";
	}

	public static List<User> getAllUser() {
		return find.all();
	}

	public static User authenticate(String username, String password) {
		return find.where().eq("username", username).eq("password", password).findUnique();
	}

	@SuppressWarnings("unchecked")
	public static Finder<String,User> find = new Finder(String.class,User.class);
}
