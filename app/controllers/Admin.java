package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.data.DynamicForm;
import views.html.*;
import models.*;
import views.html.admin.*;
import java.util.List;

import java.util.List;
import java.util.StringTokenizer;
import java.util.ArrayList;


public class Admin extends Controller {
    private static final Form<ExperimentSchedule> expForm = Form.form(ExperimentSchedule.class);

    public static Result index() {
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        List<User> userList = User.find.all();

        if(user == null){
            return redirect(routes.Application.index());
        }

        if(user.status != UserRole.ADMIN) {
            return redirect(routes.Application.home());
        }
        return ok(index_admin.render(User.getAllUser()));
        
    }

    public static Result renderUserInfo() {
        List<User> userList = User.find.all();
        return ok(user_info.render(User.getAllUser()));
    }

    public static Result saveUser() {
        DynamicForm  stringForm = Form.form().bindFromRequest();
        String userString = stringForm.get("users");
        StringTokenizer stz = new StringTokenizer(userString,"\n");
        List<User> userList = new ArrayList<User>();

        while(stz.hasMoreTokens()) { 
            String username = stz.nextToken();
            User usr = new User(username,username);
            usr.save();
            userList.add(usr);
        }
        return ok(user_info.render(userList));
    }

    public static Result experiment() {
        List<ExperimentSchedule> expList = ExperimentSchedule.find.all();
        return ok(views.html.admin.experiment.main.render(expList));
    }

    public static Result addExperiment() {
        return ok(views.html.admin.experiment.add.render(expForm));
    }

    public static Result saveExperiment() {
        Form<ExperimentSchedule> boundForm = expForm.bindFromRequest();
        if(boundForm.hasErrors()){
            flash("error", "please correct the form above.");
            return badRequest(views.html.admin.experiment.add.render(expForm));
        }
        ExperimentSchedule exp = boundForm.get();
        exp.save();
        flash("success","Successfully");
        return redirect(routes.Admin.addExperiment());
    }

}
