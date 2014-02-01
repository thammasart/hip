package models;

public class UserForm {
	public String username;
	public String password;
	
	public String valusernameate() {
	    if (User.authenticate(username, password) == null) {
	      return "Invalusername user or password";
	    }
	    return null;
	}
}