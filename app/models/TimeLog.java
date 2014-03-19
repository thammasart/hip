package models;

import play.db.ebean.Model;
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
	public long trialId;

	@ManyToOne
	public ExperimentSchedule exp;

	public TimeLog(Date startTime){
		this.startTime = startTime;
	}

	public TimeLog(Date startTime, Date endTime){
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public static TimeLog create(Date startTime, User user
		,long trialId,ExperimentSchedule exp){
		TimeLog timeLog = new TimeLog(startTime);
		timeLog.user = user;
		timeLog.trialId = trialId;
		timeLog.endTime = null;
		timeLog.exp = exp;
		return timeLog;
	}

	public static boolean isRepeatlong(User user, long trialId){
		TimeLog timeLog = TimeLog.find.where().eq("user", user).eq("trialId", trialId).findUnique();
		return timeLog != null;
	}

	@SuppressWarnings("unchecked")
	public static Finder<Long, TimeLog> find = new Finder(Long.class, TimeLog.class);
}