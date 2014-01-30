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
        return ok(home.render(User.find.byId(request().username())));
    }
	
	public static Result authenticate() {
    	Form<UserForm> userForm = Form.form(UserForm.class).bindFromRequest();
    	if(userForm.hasErrors()) {
    		return badRequest(index.render(userForm));
    	}
    	else {
    		session().clear();
    		session("id", userForm.get().id);
    		return redirect(routes.Application.home());
    	}
    }

}
