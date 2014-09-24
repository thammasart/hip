package models.mullerLayer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table (name="muller_layer_Quiz")
public class Quiz extends Model{
    @Id
    public long id;
    public int noOfChoice;
    public int differChoice;
    public LengthType lengthType;
    public float differLength;
    public boolean isPositive = false;
    @ManyToOne
    @JsonBackReference("trial")
    public Trial trial;
    @ManyToOne
    public Question question;
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
            tableName.setCellValue("Muller Layer Quiz");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell idHeaderCell = headerRow.createCell(col++);
            idHeaderCell.setCellValue("ID");

            Cell initCoundownCell = headerRow.createCell(col++);
            initCoundownCell.setCellValue("noOfChoice");

            Cell flashTimeCell = headerRow.createCell(col++);
            flashTimeCell.setCellValue("differChoice");

            Cell trialCell = headerRow.createCell(col++);
            trialCell.setCellValue("lengthType");

            Cell questionCell = headerRow.createCell(col++);
            questionCell.setCellValue("differLength");

            Cell questionCell2 = headerRow.createCell(col++);
            questionCell2.setCellValue("isPositive");

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
                data1.setCellValue(temp.noOfChoice);

                Cell data2 = dataRow.createCell(col++);
                data2.setCellValue(temp.differChoice);

                String type = "";
                if (temp.lengthType == LengthType.LONG)
                    type = "Long";
                else if (temp.lengthType == LengthType.MEDIUM)
                    type = "Medium";
                else if (temp.lengthType == LengthType.SHORT)
                    type = "Short";
                Cell data3 = dataRow.createCell(col++);
                data3.setCellValue(type);

                Cell data4 = dataRow.createCell(col++);
                data4.setCellValue(temp.differLength);

                Cell data8 = dataRow.createCell(col++);
                data8.setCellValue(temp.isPositive);

                Cell data5 = dataRow.createCell(col++);
                data5.setCellValue(temp.trial.id);

                Cell data6 = dataRow.createCell(col++);
                data6.setCellValue(temp.question.id);

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
    public Quiz(Trial trial, Question question){
    	this.trial = trial;
    	this.question = question;
    }

    public List<Answer> findAnswers() {
        return answers;
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Quiz> find = new Finder(Long.class,Quiz.class);

    public Quiz() {

    }

    public static Quiz create(Trial trial) {
        Quiz quiz = new Quiz();
        quiz.trial = trial;
        quiz.noOfChoice = 3;
        quiz.differChoice = 2;
        quiz.differLength = 1;
        quiz.lengthType = LengthType.MEDIUM;
        quiz.question = Question.generateQuestion();
        return quiz;
    }
}
