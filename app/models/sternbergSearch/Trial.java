package models.sternbergSearch;

import models.ExperimentSchedule;
import models.TimeLog;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonManagedReference;

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
@Table (name = "sternberg_search_trial")
public class Trial extends Model{
	@Id
	public long id;
	public int length;
    public double totalScore = 0;
    public double totalUsedTime = 0;
    public int totalUser = 0;
	public double blinkTime;
	public int oneCharIsCorrect = 1;
	public int oneCharIsInCorrect = 0;
	public int twoCharIsCorrect = 0;
	public int twoCharIsInCorrect = 0;
    public QuestionType questionType = QuestionType.ENGLISH;
    
    @ManyToOne
    public ExperimentSchedule schedule;
    @OneToMany(cascade=CascadeType.REMOVE, mappedBy = "trial")
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
            tableName.setCellValue("Sternberg Search Trial");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell someCell = headerRow.createCell(col++);
            someCell.setCellValue("ID");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Blink Time");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("One Char isCorrect");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("One Char isInCorrect");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Two Char isCorrect");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Two Char isInCorrect");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Question Type");

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

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.blinkTime);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.oneCharIsCorrect);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.oneCharIsInCorrect);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.twoCharIsCorrect);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.twoCharIsInCorrect);


                String type = "";
                if (temp.questionType == QuestionType.ENGLISH)
                    type = "English";
                else if (temp.questionType == QuestionType.NUMBER)
                    type = "Number";
                else if (temp.questionType == QuestionType.THAI)
                    type = "Thai";
                someCell = dataRow.createCell(col++);
                someCell.setCellValue(type);

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

    public Trial(ExperimentSchedule schedule){
    	this.schedule = schedule;
    }

    public static Trial create(ExperimentSchedule schedule){
        Trial trial = new Trial(schedule);
        trial.length = 3;
        trial.blinkTime = 0.3;
        return trial;

    }

    public void updateResult(){
        this.totalScore = 0;
        this.totalUsedTime = 0;
        for(Quiz q:quizzes){
            this.totalScore += Answer.calculateTotalScore(q.findAnswers());
            this.totalUsedTime += Answer.calculateTotalUsedTime(q.findAnswers());
        }
        this.totalUser = TimeLog.calaulateTotalUserTakeExp(schedule,id);
        this.totalScore /=totalUser;
        this.totalUsedTime /=totalUser;
        this.update();
    }

    public static Trial create(ExperimentSchedule schedule, int length, double blinkTime, int oneCharIsCorrect, int oneCharIsInCorrect, int twoCharIsCorrect, int twoCharIsInCorrect, QuestionType questionType){
    	Trial newTrial = new Trial(schedule);
    	newTrial.length = length;
    	newTrial.blinkTime = blinkTime;
    	newTrial.oneCharIsCorrect = oneCharIsCorrect;
    	newTrial.oneCharIsInCorrect = oneCharIsInCorrect;
    	newTrial.twoCharIsCorrect = twoCharIsCorrect;
    	newTrial.twoCharIsInCorrect = twoCharIsInCorrect;
    	newTrial.questionType = questionType;
    	return newTrial;
    }

    public static List<Trial> findInvolving(ExperimentSchedule ex){
        return find.where().eq("schedule", ex).findList();
    }

	@SuppressWarnings("unchecked")
	public static Finder<Long, Trial> find = new Finder(Long.class, Trial.class);

    public void generateQuiz() {
        Quiz.create(this).save();
    }
}
