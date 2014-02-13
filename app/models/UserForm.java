package models;

public class UserForm {

    public String username;
    public String password;
    
    public String validate() {
        if (User.authenticate(username, password) == null) {
          errors.add(new ValidationError("unauthenticate", "Incorrect Username/Password"));
        }
        return null;
    }
}