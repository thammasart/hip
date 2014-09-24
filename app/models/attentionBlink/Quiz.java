package models.attentionBlink;

import play.db.ebean.*;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

import play.db.ebean.Model.Finder;
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
@Table(name = "attention_blink_quiz")
public class Quiz extends Model {

    @Id
    public long id;
    public int length = 13;
    public int numberOfTarget = 2;
    public double blinkTime = 0.1;
    public boolean isCorrect = true;

    @ManyToOne
    @JsonBackReference
    public Trial trial;

    @OneToOne(cascade = CascadeType.REMOVE)
    public Question question;

    @OneToMany(cascade = CascadeType.REMOVE)
    private List<Answer> answers = new ArrayList<Answer>();

    public static Quiz create(Trial trial) {
        Quiz quiz = new Quiz();
        Question question = Question.generateQuestion(trial.questionType, quiz.length,
                quiz.numberOfTarget, quiz.isCorrect);
        question.save();
        quiz.question = question;
        quiz.trial = trial;
        return quiz;
    }

    public List<Answer> findAnswers() {
        return answers;
    }

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;

            int col = 0;
            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Quiz");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Attention Blink Quiz");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell idHeaderCell = headerRow.createCell(col++);
            idHeaderCell.setCellValue("ID");

            Cell initCoundownCell = headerRow.createCell(col++);
            initCoundownCell.setCellValue("Length");

            Cell flashTimeCell = headerRow.createCell(col++);
            flashTimeCell.setCellValue("Number of Target");

            Cell trialCell = headerRow.createCell(col++);
            trialCell.setCellValue("Blink Time");

            Cell questionCell = headerRow.createCell(col++);
            questionCell.setCellValue("IsCorrect");

            Cell trialId = headerRow.createCell(col++);
            trialId.setCellValue("Trial Id");

            Cell questionId = headerRow.createCell(col++);
            questionId.setCellValue("Question Id");

            List<Quiz> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){

                col = 0;

                Quiz temp = tempList.get(row-headerRowIndex);
                Row dataRow = userSheet.createRow(row);

                Cell dataId = dataRow.createCell(col++);
                dataId.setCellValue(temp.id);

                Cell data1 = dataRow.createCell(col++);
                data1.setCellValue(temp.length);

                Cell data2 = dataRow.createCell(col++);
                data2.setCellValue(temp.numberOfTarget);

                Cell data3 = dataRow.createCell(col++);
                data3.setCellValue(temp.blinkTime);

                Cell data4 = dataRow.createCell(col++);
                data4.setCellValue(temp.isCorrect);

                Cell data5 = dataRow.createCell(col++);
                data5.setCellValue(temp.trial.id);

                Cell data6 = dataRow.createCell(col++);
                data6.setCellValue(temp.question.id);

                String answers_id = "";

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

    @SuppressWarnings("unchecked")
    public static Finder<Long, Quiz> find = new Finder(Long.class, Quiz.class);
}
