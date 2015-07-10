package models.visualSearch;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import models.ExperimentSchedule;

import models.TimeLog;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
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
@Table (name="visual_search_trial")
public class Trial extends Model{

    public static final int SMALLER_WIDTH = 300;
    public static final int SMALLER_HEIGHT = 200;
    public static final int SMALL_WIDTH = 500;
    public static final int SMALL_HEIGHT = 300;
    public static final int MEDIUM_WIDTH = 600;
    public static final int MEDIUM_HEIGHT = 400;
    public static final int BIG_WIDTH = 800;
    public static final int BIG_HEIGHT = 500;
    public static final int EXTRA_WIDTH = 1000;
    public static final int EXTRA_HEIGHT = 500;

    @Id
    public long id;
    @ManyToOne
    public ExperimentSchedule schedule;
    @OneToOne(mappedBy = "trial",cascade = CascadeType.ALL)
    @JsonManagedReference(value="trial")
    public Quiz quiz;
    public double totalScore = 0;
    public double totalUsedTime = 0;
    public int totalUser = 0;

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;

            int col = 0;

            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Trial");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Visual Search Trial");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell someCell = headerRow.createCell(col++);
            someCell.setCellValue("ID");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Experiment Schedule Id");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Quiz Id");

            List<Trial> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){

                col = 0;

                Trial temp = tempList.get(row-headerRowIndex);
                Row dataRow = userSheet.createRow(row);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.id);


                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.schedule.id);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.quiz.id);

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

    public Trial(ExperimentSchedule schedule){
    	this.schedule = schedule;
    }

    public void updateResult(){
        this.totalUser = TimeLog.calaulateTotalUserTakeExp(schedule,id);
        if(totalUser > 0){
            this.totalScore = Answer.calculateTotalScore(quiz.findAnswers());
            this.totalUsedTime = Answer.calculateTotalUsedTime(quiz.findAnswers());
            this.totalScore /=totalUser;
            this.totalUsedTime /=totalUser;
            this.update();
        }
    }
    
    public static Trial findById(long id){
        return find.byId(new Long(id));
    }

    public static List<Trial> findInvolving(ExperimentSchedule ex){
        return find.where().eq("schedule", ex).findList();
    }

    public static void displayJson(){
        System.out.println(Json.stringify(Json.toJson(Trial.find.all().get(0))));
    }
    public String toJson(){
        return Json.stringify(Json.toJson(this));
    }
    public static String toJsonArray(List<Trial> trials){
        String result = "[";
        for(int i=0;i< trials.size();i++){
            result += trials.get(i).toJson();
            if(i < trials.size() - 1){
                result += ",";
            }
        }
        result += "]";
        return result;

    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Trial> find = new Finder(Long.class, Trial.class);

    public static Trial create(ExperimentSchedule schedule) {
        Trial trial = new Trial(schedule);
        return trial;
    }

    public void generateQuiz() {
        Quiz quiz = Quiz.create(this);
        quiz.save();
    }
}
