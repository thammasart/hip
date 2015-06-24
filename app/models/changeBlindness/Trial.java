package models.changeBlindness;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import models.TimeLog;

import models.ExperimentSchedule;
import play.db.ebean.Model;
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
@Table (name="change_blindness_trial")
public class Trial extends Model{
    @Id
    public long id;
    public int displayTime = 60;

    @ManyToOne
    public ExperimentSchedule schedule;
    @OneToMany(cascade=CascadeType.REMOVE, mappedBy = "trial")
    @JsonManagedReference
    public List<Quiz> quizzes = new ArrayList<Quiz>();
    public double totalScore = 0;
    public double totalUsedTime = 0;
    public int totalUser = 0;
    public static final int noOfQuiz = 1;

    public Trial(ExperimentSchedule schedule){
    	this.schedule = schedule;
    }

    public void updateResult(){
        this.totalUser = TimeLog.calaulateTotalUserTakeExp(schedule,id);
        if(totalUser > 0){
            this.totalScore = 0;
            this.totalUsedTime = 0;
            for(Quiz q:quizzes){
                this.totalScore += Answer.calculateTotalScore(q.findAnswers());
                this.totalUsedTime += Answer.calculateTotalUsedTime(q.findAnswers());
            }
            this.totalScore /=totalUser;
            this.totalUsedTime /=totalUser;
            this.update();
        }
    }
    
    public static List<Trial> findInvolving(ExperimentSchedule ex){
        return find.where().eq("schedule", ex).findList();
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Trial> find = new Finder(Long.class, Trial.class);

    public static Trial create(ExperimentSchedule experimentSchedule) {
        return new Trial(experimentSchedule);
    }

    public void generateQuiz() {
        List<Question> questions = Question.find.all();
        for(int i=0; i< noOfQuiz; i++){
            Quiz.create(this, questions).save();
        }
    }

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;

            int col = 0;

            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Trial");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Change Blindness Trial");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell idHeaderCell = headerRow.createCell(col++);
            idHeaderCell.setCellValue("ID");

            Cell timeCell = headerRow.createCell(col++);
            timeCell.setCellValue("Display Time");

            Cell expCell = headerRow.createCell(col++);
            expCell.setCellValue("Experiment Schedule Id");

            Cell quizCell = headerRow.createCell(col++);
            quizCell.setCellValue("Quiz Ids");

            List<Trial> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){

                col = 0;

                Trial temp = tempList.get(row-headerRowIndex);
                Row dataRow = userSheet.createRow(row);

                Cell dataId = dataRow.createCell(col++);
                dataId.setCellValue(temp.id);

                Cell data1 = dataRow.createCell(col++);
                data1.setCellValue(temp.displayTime);

                Cell data2 = dataRow.createCell(col++);
                data2.setCellValue(temp.schedule.id);

                String quizzes_id = "";

                int subListSize = temp.quizzes.size();

                for (int i=0;i<subListSize;i++){
                    if (i < subListSize-1)
                        quizzes_id = quizzes_id + String.valueOf( temp.quizzes.get(i).id) + ",";
                    else
                        quizzes_id = quizzes_id + String.valueOf( temp.quizzes.get(i).id);
                }
                Cell data3 = dataRow.createCell(col++);
                data3.setCellValue(quizzes_id);

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

}
