package models.signalDetection;

import play.db.ebean.Model;
import javax.persistence.*;
import play.db.ebean.Model.Finder;
import java.util.List;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table (name="signal_detection_quiz")
public class Quiz extends Model{
	@Id
    public long id;

    public double displayTime;
    public int noOfTarget = 1;
    public int length = 10;
    @OneToOne(cascade=CascadeType.REMOVE)
	public Question question;
    @ManyToOne
    @JsonBackReference
    public Trial trial;
    @OneToMany(cascade=CascadeType.REMOVE)
    private List<Answer> answers = new ArrayList<Answer>();

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;

            int col = 0;
            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Quiz");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Signal Detection Quiz");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell someCell = headerRow.createCell(col++);
            someCell.setCellValue("ID");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Display Time");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Number of Target");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Length");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Trial Id");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Question Id");

            List<Quiz> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){

                col = 0;

                Quiz temp = tempList.get(row-headerRowIndex);
                Row dataRow = userSheet.createRow(row);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.id);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.displayTime);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.noOfTarget);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.length);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.trial.id);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.question.id);

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

    public List<Answer> findAnswers(){
        return answers;
    }

    public Quiz(){
    	this.displayTime = 0;
    	this.noOfTarget = 0;
    }

    public Quiz(double displayTime,int noOfTarget,int length){
    	this.displayTime = displayTime;
    	this.noOfTarget = noOfTarget;
        this.length = length;
    }

    public static Quiz create(double displayTime,int noOfTarget,int length,Trial trial,Question question){
    	if(displayTime > 1.0){
    		displayTime = 1.0;
    	}
    	else if(displayTime < 0.5){
    		displayTime = 0.5;
    	}
    	Quiz quiz = new Quiz(displayTime,noOfTarget,length);
    	quiz.question = question;
    	quiz.trial = trial;
    	return quiz;
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Quiz> find = new Finder(Long.class,Quiz.class);
}
