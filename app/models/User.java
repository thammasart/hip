package models;

import javax.persistence.*;
import play.db.ebean.*;
import com.avaje.ebean.*;

@Entity
public class User extends Model {
	@Id
	public String id;
	public String password;

	public User(String id, String password) {
		this.id = id;
		this.password = password;
	}

	public static User authenticate(String id, String password) {
		return find.where().eq("id", id).eq("password", password).findUnique();
	}

	public static Finder<String, User> find = new Finder<String,User>(String.class, User.class);
}