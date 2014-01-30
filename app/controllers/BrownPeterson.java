package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import views.html.*;
import views.html.iframe.*;

public class BrownPeterson extends Controller {
	
	@Security.Authenticated(Secured.class)
	public static Result renderShortTermMemoryBrownPetersonTask(){
		return ok(brown_peterson_info.render());
	}
	
	@Security.Authenticated(Secured.class)
	public static Result renderShortTermMemoryBrownPetersonTaskIframe(){
		return ok(brown_peterson_iframe.render());
	}
	
	@Security.Authenticated(Secured.class)
	public static Result renderShortTermMemoryBrownPetersonTaskProc(){
		return ok(brown_peterson_proc.render());
	}
	
	@Security.Authenticated(Secured.class)
	public static Result renderShortTermMemoryBrownPetersonTaskProcIframe(){
		return ok(brown_peterson_proc_iframe.render());
	}
	
	@Security.Authenticated(Secured.class)
	public static Result renderShortTermMemoryBrownPetersonTaskExp(){
		return ok(brown_peterson_exp.render());
	}
}
