package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import views.html.*;
import models.*;
import views.html.admin.*;

public class Admin extends Controller{
    public static Result index(){
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        if(user == null){
            return redirect(routes.Application.index());
        }
        if(user.status != UserRole.ADMIN){
            return redirect(routes.Application.home());
        }
        return ok(index_admin.render());
    }
}