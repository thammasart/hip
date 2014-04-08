package controllers;

import play.*;
import play.mvc.*;
import play.data.*;

import models.User;
import models.sternbergSearch.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import views.html.sternbergSearch.*;
import views.html.iframe.*;
import java.util.Date;

public class SternbergSearch extends Controller{

    //แสดงหน้าข้อมูลการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result info(){
        User user = User.find.byId(request().username());
        return ok(info.render(user));
    }

    //แสดงกรอบในหน้าข้อมูลการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result infoIframe(){
        return ok(sternberg_search_iframe.render());
    }

    //แสดงหน้าขั้นตอนการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result proc(){
        User user = User.find.byId(request().username());
        return ok(proc.render(user));
    }

    //แสดงกรอบในหน้าขั้นตอนการทดลอง
    @Security.Authenticated(Secured.class)
    public static Result procIframe(){
        return ok(sternberg_search_proc_iframe.render());
    }

}
