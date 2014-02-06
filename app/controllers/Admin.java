package controllers;

import play.*;
import play.mvc.*;
import play.data.*;
import play.data.DynamicForm;
import views.html.*;
import models.*;
import views.html.admin.*;

import java.util.List;
import java.util.StringTokenizer;
import java.util.ArrayList;


public class Admin extends Controller{

    public static Result index(){
        User user = User.find.where().eq("username", session().get("username")).findUnique();
        List<User> userList = User.find.all();

        if(user == null){
            return redirect(routes.Application.index());
        }

        if(user.status != UserRole.ADMIN){
            return redirect(routes.Application.home());
        }
        return ok(index_admin.render(User.getAllUser()));
        
    }
    public static Result renderUserInfo(){
        List<User> userList = User.find.all();
        return ok(user_info.render(User.getAllUser()));
    }

    public static Result saveUser(){
        DynamicForm  stringForm = Form.form().bindFromRequest();
        String userString = stringForm.get("users");
        StringTokenizer stz = new StringTokenizer(userString,"\n");
        List<User> userList = new ArrayList<User>();

        while(stz.hasMoreTokens()){ 
            String username = stz.nextToken();
            User usr = new User(username,username);
            usr.save();
            userList.add(usr);
        }
        return ok(user_info.render(userList));
    }
}


