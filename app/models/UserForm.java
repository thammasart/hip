package models;

public class UserForm {
<<<<<<< HEAD
	public String username;
	public String password;
	
	public String valusernameate() {
	    if (User.authenticate(username, password) == null) {
	      return "Invalusername user or password";
	    }
	    return null;
	}
=======
    public String username;
    public String password;
    
    public String validate() {
        if (User.authenticate(username, password) == null) {
          return "Invalid user or password";
        }
        return null;
    }
>>>>>>> 09b91abf33f0d4e781fdadb2311fa8a67df887fe
}