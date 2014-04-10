package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import views.html.*;
import models.*;

public class InitialController extends Controller {

    @Security.Authenticated(Secured.class)
    public static Result index(){
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        if(!(user.status == UserRole.ADMIN))
            return redirect(routes.Application.index());
        return ok(initial.render());
    }

} 
