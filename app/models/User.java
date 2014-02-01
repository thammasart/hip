package models;

import javax.persistence.*;
import play.db.ebean.*;
import com.avaje.ebean.*;

@Entity
public class User extends Model {
	@Id
	public String username;
	public String password;

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public static User authenticate(String username, String password) {
		return find.where().eq("username", username).eq("password", password).findUnique();
	}

	public static Finder<String, User> find = new Finder<String,User>(String.class, User.class);
}