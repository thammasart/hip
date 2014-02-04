package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import views.html.*;
import views.html.admin.*;

public class AdminController extends Controller {
	
	public static Result createExperiment() {
		return ok(create_exp.render());
	}

	public static Result createExpBrownPeterson() {
		return ok(create_exp_brownpeterson.render());
	}

}
