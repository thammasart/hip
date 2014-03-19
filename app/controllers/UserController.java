
package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.data.DynamicForm;
import views.html.*;
import models.*;
import views.html.admin.*;
import java.util.List;
import java.util.ArrayList;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;


public class UserController extends Controller {
    
    @Security.Authenticated(Secured.class)
    public static Result editProfile() {
        User user = User.find.byId(session().get("username"));

        if(user == null){
            return redirect(routes.Application.index());
        }

        return ok(user_profile.render(user));
    }

    @Security.Authenticated(Secured.class)
    public static Result saveUserProfile(){
        Form<User> userForm = Form.form(User.class).bindFromRequest();
        User oldUser = User.find.byId(session().get("username"));
        User newUser = userForm.get();
        oldUser.firstName = newUser.firstName;
        oldUser.lastName = newUser.lastName;
        oldUser.gender = newUser.gender;
        oldUser.birthDate = newUser.birthDate;
        oldUser.year = newUser.year;
        oldUser.eMail = newUser.eMail;
        //่้try catch
        oldUser.update();
        flash("success","Submit User Profile Complete!!!");
        return ok(user_profile.render(oldUser));
    }

    @Security.Authenticated(Secured.class)
    public static Result changeUserPassword(){
        User user = User.find.byId(session().get("username"));
        return ok(password.render(user));
    }

    @Security.Authenticated(Secured.class)
    public static Result saveUserPassword(){
        DynamicForm requestData = Form.form().bindFromRequest();
        User user = User.find.byId(session().get("username"));
        if (User.authenticate(user.username,requestData.get("oldPassword")) != null){
            if (requestData.get("newPassword").equals(requestData.get("confirmPassword"))){
                user.password = requestData.get("newPassword");
                user.update();
                flash("success","Change password complete!!!");
            }
            else
                flash("error","Confirm password is not match.");
        }
        else
            flash("error","Old password is incorrect.");

        return ok(password.render(user));
    }

}
