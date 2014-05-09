import play.*;
import play.libs.*;
import com.avaje.ebean.Ebean;
import models.*;
import java.util.*;

public class Global extends GlobalSettings {
	@Override
	public void onStart(Application app) {
		if(User.find.findRowCount() == 0) {
			Ebean.save((List) Yaml.load("initial-data.yml"));
            Ebean.save((List) Yaml.load("sternbergSearch.yml"));
            Ebean.save((List) Yaml.load("magicNumber7.yml"));
			Ebean.save((List) Yaml.load("simonEffect.yml"));
			Ebean.save((List) Yaml.load("mullerLayer.yml"));
			Ebean.save((List) Yaml.load("user.yml"));
		}
	}
}
