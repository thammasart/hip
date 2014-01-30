package models;

public class UserForm {
	public String id;
	public String password;
	
	public String validate() {
	    if (User.authenticate(id, password) == null) {
	      return "Invalid user or password";
	    }
	    return null;
	}
}