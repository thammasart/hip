package models;

import play.db.ebean.Model;
import javax.persistence.*;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.List;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import play.db.DB;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;
            int col = 0;
            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("TimeLog");

            Row headerRow = userSheet.createRow(headerRowIndex++);

            Cell someCell = headerRow.createCell(0);
            someCell.setCellValue("TimeLog");

            headerRow = userSheet.createRow(headerRowIndex++);

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("ID");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Start Time(dd/MM/yyyy HH:mm:ss)");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("End Time(dd/MM/yyyy HH:mm:ss)");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("UserName");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("ExperimentSchedule Id");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Trial Id");

            List<TimeLog> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){
                TimeLog temp = tempList.get(row-headerRowIndex);
                col = 0;
                Row dataRow = userSheet.createRow(row);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.id);

                DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                someCell = dataRow.createCell(col++);
                someCell.setCellValue(df.format(temp.startTime));

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(df.format(temp.endTime));

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.user.username);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.exp.id);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.trialId);

            }

            //File file = new File("brown_peterson_user.xls");
            //FileOutputStream out = new FileOutputStream(file);
            //wb.write(out);
            //out.close();
            //return file;

        } catch (Exception e) {
            e.printStackTrace();
            //return null;
        }
    }

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

	public static int calaulateTotalUserTakeExp(ExperimentSchedule exps ,long trialId ){
		return TimeLog.find.where().eq("exp",exps).eq("trialId",trialId).findList().size();
	}

	public static boolean isRepeatTrial(User user, long trialId, ExperimentSchedule exp){
		TimeLog timeLog = TimeLog.find.where().eq("user", user).eq("trialId", trialId).eq("exp", exp).findUnique();
		return timeLog != null;
	}

	public static TimeLog findByUserAndTrialId(User user, Long trialId){
		return TimeLog.find.where().eq("user", user).eq("trialId", trialId).findUnique();
	}
	public static TimeLog findByUserAndTrialId(User user, Long trialId,ExperimentSchedule exp){
		return TimeLog.find.where().eq("user", user).eq("trialId", trialId).eq("exp",exp).findUnique();
	}

	@SuppressWarnings("unchecked")
	public static Finder<Long, TimeLog> find = new Finder(Long.class, TimeLog.class);
}
