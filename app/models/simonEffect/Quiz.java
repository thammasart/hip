package models.simonEffect;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
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
@Table (name = "simon_effect_quiz")
public class Quiz extends Model{

    public static final String[] positions = {"up", "down", "right", "left"};

	@Id
	public long id;
	public String position;

    @ManyToOne
    @JsonBackReference
    public Trial trial;
    @ManyToOne
    public Question question;
    @OneToMany(cascade=CascadeType.REMOVE, mappedBy = "quiz")
    private List<Answer> answers = new ArrayList<Answer>();

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;

            int col = 0;
            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Quiz");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Simon Effect Quiz");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell someCell = headerRow.createCell(col++);
            someCell.setCellValue("ID");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Position");

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
                someCell.setCellValue(temp.position);

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

    public Quiz(Trial trial, Question question,String position ){
    	this.trial = trial;
    	this.question = question;
    	this.position = position;
    }

	@SuppressWarnings("unchecked")
	public static Finder<Long, Quiz> find = new Finder(Long.class, Quiz.class);

    public Quiz() {

    }

    public static Quiz create(Trial trial) {
        Quiz quiz = new Quiz();
        quiz.trial = trial;
        quiz.position = randomPosition();
        quiz.question = Question.findQuestionByType(trial.questionType);
        return  quiz;
    }

    public static String randomPosition() {
        Random random = new Random();
        return positions[random.nextInt(positions.length)];

    }
}
