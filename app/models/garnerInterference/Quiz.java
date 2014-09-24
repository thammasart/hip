package models.garnerInterference;

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
@Table (name="garner_interference_quiz")
public class Quiz extends Model{
    @Id
    public long id;
    public QuestionType questionType = QuestionType.COLOR; // color or size or both


    @ManyToOne
    @JsonBackReference
    public Trial trial;
    @OneToOne
    public Question question;
    @OneToMany(cascade=CascadeType.REMOVE)
    private List<Answer> answers = new ArrayList<Answer>();

    public Quiz(){}

    public Quiz(Trial trial, Question question){
    	this.trial = trial;
    	this.question = question;
    }


    @SuppressWarnings("unchecked")
    public static Finder<Long, Quiz> find = new Finder(Long.class, Quiz.class);

    public static Quiz create(Trial trial, QuestionType questionType, boolean isNotFake) {
        Quiz quiz = new Quiz();
        quiz.trial = trial;
        quiz.questionType = questionType;
        Question question = Question.create(questionType, isNotFake);
        question.save();
        quiz.question = question;
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
            tableName.setCellValue("Garner Interference Quiz");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell idHeaderCell = headerRow.createCell(col++);
            idHeaderCell.setCellValue("ID");

            Cell questionCell = headerRow.createCell(col++);
            questionCell.setCellValue("Question Type");

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

                String questionType = "";
                if (temp.questionType == QuestionType.BOTH){
                    questionType = "Both";
                }else if (temp.questionType == QuestionType.SIZE){
                    questionType = "Size";
                }
                else if (temp.questionType == QuestionType.COLOR){
                    questionType = "Color";
                }
                Cell data1 = dataRow.createCell(col++);
                data1.setCellValue(questionType);

                Cell data2 = dataRow.createCell(col++);
                data2.setCellValue(temp.trial.id);

                Cell data3 = dataRow.createCell(col++);
                data3.setCellValue(temp.question.id);

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
