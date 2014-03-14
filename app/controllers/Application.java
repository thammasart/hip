package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import views.html.*;
import views.html.iframe.*;
import models.*;

public class Application extends Controller {

	public static Result index() {
        return ok(index.render(Form.form(UserForm.class)));
    }

    @Security.Authenticated(Secured.class)
	public static Result home(){
        User user = User.find.byId(request().username());
        return ok(home.render(user));
    }
        
    @Security.Authenticated(Secured.class)
	public static Result selectExperiment(){
        User user = User.find.byId(request().username());
        return ok(selectExperiment.render(user));
    }
    @Security.Authenticated(Secured.class)
    public static Result about() {
        User user = User.find.byId(request().username());
        return ok(about.render(user));
    }

    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(routes.Application.index());
    }

	public static Result authenticate() {
        Form<UserForm> userForm = Form.form(UserForm.class).bindFromRequest();
        if(userForm.hasErrors()) {
            flash("unauthenticate", "Incorrect Username/Password.");
            return badRequest(index.render(userForm));
        }
        else {
            session().clear();
            session("username", userForm.get().username);
            return redirect(routes.Application.home());
        }
    }

}
