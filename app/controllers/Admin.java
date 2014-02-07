package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import views.html.*;
import models.*;
import views.html.admin.*;
import java.util.List;
import java.util.ArrayList;
import models.brownPeterson.*;

public class Admin extends Controller{

    private static final Form<ExperimentSchedule> expForm = Form.form(ExperimentSchedule.class);
    private static final Form<Trial> trialForm = Form.form(Trial.class);

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
        for(int i = 0; i < exp.noOfTrial; i++){
            Trial.create(exp).save();
        }
        return redirect(routes.Admin.addExperiment());
    }

    public static Result updateExperiment(long id){
        Form<ExperimentSchedule> boundForm = expForm.bindFromRequest();
        ExperimentSchedule exp = ExperimentSchedule.find.byId(id);
        List<Trial> trials = Trial.findInvolving(exp);
        if(boundForm.hasErrors()){
            flash("error", "please correct the form above.");
            return badRequest(views.html.admin.experiment.edit.render(exp, trials));
        }

        boundForm.get().update(id);
        exp = ExperimentSchedule.find.byId(id);
        flash("success", "update success.");
        return ok(views.html.admin.experiment.edit.render(exp, trials));
    }

    public static Result parameter(long id){
        final ExperimentSchedule exp = ExperimentSchedule.find.byId(id);
        if(exp == null){
            return notFound("This Experiment does not exist.");
        }
        List<Trial> trials = Trial.findInvolving(exp);
        return ok(views.html.admin.experiment.edit.render(exp, trials));
    }
}