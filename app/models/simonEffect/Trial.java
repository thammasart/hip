package models.simonEffect;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import models.ExperimentSchedule;
import models.TimeLog;

import play.db.ebean.*;
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
@Table (name = "simon_effect_trial")
public class Trial extends Model{
	@Id
	public long id;
	public QuestionType questionType;
	public double blinkTime = 0.5;
    public int noOfQuiz = 3;
    public double totalScore = 0;
    public int totalUser = 0;
    public double totalUsedTime = 0;

    @ManyToOne
    public ExperimentSchedule schedule;

    @OneToMany(cascade=CascadeType.REMOVE)
    @JsonManagedReference
    public List<Quiz> quizzes = new ArrayList<Quiz>();

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;

            int col = 0;

            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Trial");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Simon Effect Trial");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell someCell = headerRow.createCell(col++);
            someCell.setCellValue("ID");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Question Type");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Blink Time");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Experiment Schedule Id");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Quiz Ids");

            List<Trial> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){

                col = 0;

                Trial temp = tempList.get(row-headerRowIndex);
                Row dataRow = userSheet.createRow(row);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.id);

                String type = "";
                if (temp.questionType == QuestionType.ONEFEATURE)
                    type = "One Feature";
                else if (temp.questionType == QuestionType.TWOFEATURE)
                    type = "Two Feature";
                someCell = dataRow.createCell(col++);
                someCell.setCellValue(type);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.blinkTime);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.schedule.id);

                String quizzes_id = "";

                int subListSize = temp.quizzes.size();

                for (int i=0;i<subListSize;i++){
                    if (i < subListSize-1)
                        quizzes_id = quizzes_id + String.valueOf( temp.quizzes.get(i).id) + ",";
                    else
                        quizzes_id = quizzes_id + String.valueOf( temp.quizzes.get(i).id);
                }
                someCell = dataRow.createCell(col++);
                someCell.setCellValue(quizzes_id);

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

    public Trial(ExperimentSchedule schedule, QuestionType questionType, double blinkTime){
    	this.schedule = schedule;
    	this.questionType = questionType;
    	this.blinkTime = blinkTime;
    }

    public static List<Trial> findInvolving(ExperimentSchedule ex){
        return find.where().eq("schedule", ex).findList();
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
    
    public static List<Trial> findAllTrial(int feature){

        if(feature == 1){ 
            return find.where().eq("question_type",QuestionType.ONEFEATURE).findList();
        }else if(feature == 2 ){ 
            return find.where().eq("question_type",QuestionType.TWOFEATURE).findList();
        }else{ 
            return find.all();
        }
    }
    
    public static List<String> getFeature(){
        List<String> expFeature = new ArrayList<String>();
        expFeature.add("1-Feature");
        expFeature.add("2-Feature");
        
        return expFeature;
    }


    @SuppressWarnings("unchecked")
	public static Finder<Long, Trial> find = new Finder(Long.class, Trial.class);

    public static Trial create(ExperimentSchedule experimentSchedule) {
        return new Trial(experimentSchedule, QuestionType.ONEFEATURE, 0.8);
    }

    public void generateQuiz() {
        for(int i = 0; i < noOfQuiz; i++){
            Quiz quiz = Quiz.create(this);
            quiz.save();
        }
    }
}
