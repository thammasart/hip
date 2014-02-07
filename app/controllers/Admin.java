package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import views.html.*;
import models.*;
import views.html.admin.*;
import java.util.List;

public class Admin extends Controller{

    private static final Form<ExperimentSchedule> expForm = Form.form(ExperimentSchedule.class);

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

    public static Result experiment(){
        List<ExperimentSchedule> expList = ExperimentSchedule.find.all();
        return ok(views.html.admin.experiment.main.render(expList));
    }

    public static Result addExperiment(){
        return ok(views.html.admin.experiment.add.render(expForm));
    }

    public static Result saveExperiment(){
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