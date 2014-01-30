package models;

import models.brownPeterson.*;
import java.util.List;
import play.db.ebean.*;
import javax.persistence.*;
import java.util.Date;
@Entity
public class TimeLog extends Model{
	public Date startTime;
	public Date endTime;

	@ManyToOne
	public User user;
	@ManyToOne
	public Trial trial;

	public TimeLog(Date startTime, Date endTime){
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public static TimeLog create(Date startTime, Date endTime, User user, Trial trial){
		TimeLog timeLog = new TimeLog(startTime, endTime);
		timeLog.user = user;
		timeLog.trial = trial;
		return timeLog;
	}

}