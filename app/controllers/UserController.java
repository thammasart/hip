
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

        if(user.status == UserRole.ADMIN) {
            return redirect(routes.Application.home());
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
        oldUser.section = newUser.section;
        oldUser.semester = newUser.semester;
        oldUser.year = newUser.year;
        oldUser.eMail = newUser.eMail;
        
        oldUser.update();
        return ok(user_profile.render(oldUser));
    }
}
