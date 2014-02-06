package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import views.html.*;
import views.html.iframe.*;
import views.html.signalDetection.*;
import views.html.changeBlindness.*;
import models.*;

public class Application extends Controller {

	public static Result index() {
        return ok(index.render(Form.form(UserForm.class)));
    }

    @Security.Authenticated(Secured.class)
	public static Result perceptionSignalDetection(){
        return ok(signal_detection_info.render());
    }
    
	@Security.Authenticated(Secured.class)
	public static Result perceptionChangeBlindnessIframe(){
        return ok(change_blindness_iframe.render());
    }

    @Security.Authenticated(Secured.class)
	public static Result perceptionChangeBlindnessProcIframe(){
        return ok(change_blindness_proc_iframe.render());
    }
    
	@Security.Authenticated(Secured.class)
	public static Result perceptionChangeBlindness(){
        return ok(change_blindness_info.render());
    }

    @Security.Authenticated(Secured.class)
	public static Result perceptionChangeBlindnessProc(){
        return ok(change_blindness_proc.render());
    }

    @Security.Authenticated(Secured.class)
	public static Result home(){
        User user = User.find.byId(request().username());
        return ok(home.render(user));
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
            return badRequest(index.render(userForm));
        }
        else {
            session().clear();
            session("username", userForm.get().username);
            return redirect(routes.Application.home());
        }
    }

}
