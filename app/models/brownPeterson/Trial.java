package models.brownPeterson;

import play.db.ebean.*;
import javax.persistence.*;
import models.*;
import java.util.List;
import java.util.ArrayList;

@Entity
public class Trial extends Model{
	@Id
	public long id;
	@Column(nullable=false, length=20)
	public String trigramType = "word";
	@Column(nullable=false, length=20)
	public String trigramLanguage = "english";

	@ManyToOne
	public ExperimentSchedule schedule;
	@OneToMany
	public List<TimeLog> timeLogs = new ArrayList<TimeLog>();

	public static Trial create(ExperimentSchedule experimentSchedule){
		Trial trial = new Trial();
		trial.schedule = experimentSchedule;
		return trial;
	}

	public static Trial findById(int id) {
		return find.byId(new Long(id));
	}

	public static List<Trial> findInvolving(ExperimentSchedule ex){
		return find.where().eq("schedule", ex).findList();
	}
	@SuppressWarnings("unchecked")
	public static Finder<Long, Trial> find = new Finder(Long.class, Trial.class);

}