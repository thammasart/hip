package models.brownPeterson;

import models.*;
import java.util.List;
import java.util.ArrayList;
import play.db.ebean.*;
import javax.persistence.*;
import java.util.Date;

@Entity
public class TimeLog extends Model{
	@Id
	public long id;
	public Date startTime = new Date();
	public Date endTime;

	@ManyToOne
	public User user;
	@ManyToOne
	public Trial trial;

	public TimeLog(Date startTime){
		this.startTime = startTime;
	}
	public TimeLog(Date startTime, Date endTime){
		this.startTime = startTime;
		this.endTime = endTime;
	}
	public static TimeLog create(Date startTime, User user, Trial trial){
		TimeLog timeLog = new TimeLog(startTime);
		timeLog.user = user;
		timeLog.trial = trial;
		timeLog.endTime = null;
		return timeLog;
	}

	public static TimeLog create(Date startTime, Date endTime, User user, Trial trial){
		TimeLog timeLog = new TimeLog(startTime, endTime);
		timeLog.user = user;
		timeLog.trial = trial;
		return timeLog;
	}

	public static boolean isRepeatTrial(User user, Trial trial){
		TimeLog timeLog = TimeLog.find.where().eq("user", user).eq("trial", trial).findUnique();
		return (timeLog != null && timeLog.endTime == null);
	}
/*
	public static boolean canTakeExperiment(User user, List<ExperimentSchedule> exps) {
		/*
		List<TimeLog> timeLogs = find.where().eq("user", user).findList();
		for(TimeLog timeLog : timeLogs) {
			if(!Trial.inWorkingSchedule(timeLog.trial)) {
				timeLogs.remove(timeLog);
			}
		}
		return timeLogs.size() != 0;
		
	}

/*
	public static List<TimeLog> getUsersTakeTrialFromWorkingExperiments(User user) {
		List<TimeLog> timeLogs = find.where().eq("user", user).findList();
		for(TimeLog timeLog : timeLogs) {
			if(!Trial.inWorkingSchedule(timeLog.trial)) {
				timeLogs.remove(timeLog);
			}
		}
		return timeLogs;
	}
	*/

	@SuppressWarnings("unchecked")
	public static Finder<Long, TimeLog> find = new Finder(Long.class, TimeLog.class);

}