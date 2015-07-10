package models.positionError;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import models.TimeLog;

import play.db.ebean.*;
import javax.persistence.*;
import models.TimeLog;
import models.ExperimentSchedule;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonBackReference;

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
@Table (name="position_error_trial")
public class Trial extends Model{
	@Id
	public long id;

	public double flashSpeed;
	public double delayTime;
	public QuestionType questionType;
	public double totalScore = 0;
    public double totalUsedTime = 0;
    public int totalUser = 0;

    public int numberOfQuiz = 3;

	@ManyToOne
    public ExperimentSchedule schedule;
    @OneToMany(cascade=CascadeType.REMOVE, mappedBy = "trial")
    @JsonManagedReference
    public List<Quiz> quizzes;

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;

            int col = 0;

            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Trial");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Position Error Trial");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell idHeaderCell = headerRow.createCell(col++);
            idHeaderCell.setCellValue("ID");

            Cell hdata1 = headerRow.createCell(col++);
            hdata1.setCellValue("Flash Speed");

            Cell hdata2 = headerRow.createCell(col++);
            hdata2.setCellValue("Delay Time");

            Cell hdata3 = headerRow.createCell(col++);
            hdata3.setCellValue("Question Type");

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

                Cell data4 = dataRow.createCell(col++);
                data4.setCellValue(temp.flashSpeed);

                Cell data5 = dataRow.createCell(col++);
                data5.setCellValue(temp.delayTime);

                String type = "";
                if (temp.questionType == QuestionType.ENGLISH)
                    type = "English";
                else if (temp.questionType == QuestionType.THAI)
                    type = "Thai";
                else if (temp.questionType == QuestionType.NUMBER)
                    type = "Number";
                Cell data1 = dataRow.createCell(col++);
                data1.setCellValue(type);

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

	public Trial(double flashSpeed, double delayTime, QuestionType questionType){
		this.flashSpeed = flashSpeed;
		this.delayTime = delayTime;
		this.questionType = questionType;
	}

	public static Trial create(double flashSpeed, double delayTime, QuestionType questionType, 
								ExperimentSchedule schedule){
		Trial newTrial = new Trial(flashSpeed, delayTime, questionType);
		newTrial.schedule = schedule;
		return newTrial;
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
    
    public static Trial create(ExperimentSchedule schedule){
        Trial newTrial = new Trial(0.05,0.1,QuestionType.ENGLISH);
        newTrial.schedule = schedule;
        return newTrial;
    }

    public void generateQuiz(){
        for(int i = 0; i < this.numberOfQuiz; i++){
            Quiz.create(this).save();
        }
    }

    public static List<Trial> findAllTrial(int feature){

        if(feature == 1){ 
            return find.where().eq("question_type",QuestionType.ENGLISH).findList();
        }else if(feature == 2 ){ 
            return find.where().eq("question_type",QuestionType.THAI).findList();
        }else if(feature == 3 ){ 
            return find.where().eq("question_type",QuestionType.NUMBER).findList();
        }else{ 
            return find.all();
        }
    }

    public static List<Trial> findInvolving(ExperimentSchedule ex){
        return find.where().eq("schedule", ex).findList();
    }

	@SuppressWarnings("unchecked")
    public static Model.Finder<Long,Trial> find = new Finder(Long.class, Trial.class);




}
