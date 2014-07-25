package models;

import models.brownPeterson.Answer;
import java.util.List;
import java.util.ArrayList;
import play.db.ebean.*;
import javax.persistence.*;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import play.data.format.Formats;

@Entity
public class User extends Model{

	@Id
	@Column(length=20)
	public String username;
	@Column(nullable=false, length=20)
	public String password;
	public UserRole status = UserRole.STUDENT;
    public String firstName ="";
    public String lastName ="";
    public String gender ="";
    @Formats.DateTime(pattern="dd/MM/yyyy")
    public Date   birthDate;
    public String section;
    public String semester;
    public String academicYear;
    public int year = 1;
    public String eMail ="";
    public String faculty;
    public String department;


	@OneToMany
	List<Answer> answers = new ArrayList<Answer>();
	@OneToMany
	List<TimeLog> timeLogs = new ArrayList<TimeLog>();

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }


	public static List<User> getAllUser() {
		return find.all();
	}

	public static User authenticate(String username, String password) {
        User user = find.where().eq("username", username).eq("password", password).findUnique();
		if( user != null && user.status == UserRole.DELETED){
            return null;
        }
        return user;
	}

    public int getAge(){
        if (birthDate != null){
            Date d = new Date();
            long diff = d.getTime() - birthDate.getTime();
            long days = TimeUnit.MILLISECONDS.toDays(diff);
            long years = days/365;
            return (int)years;
        }
        else
            return 0;
    }

	@SuppressWarnings("unchecked")
	public static Finder<String,User> find = new Finder(String.class,User.class);
}
