package models.positionError;

import play.db.ebean.*;
import javax.persistence.*;
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
@Table (name="position_error_quiz")
public class Quiz extends Model{
	@Id
	public long id;

	public int length = 7;

	@OneToOne(cascade=CascadeType.REMOVE)
	public Question question;
    @ManyToOne
    @JsonBackReference
    public Trial trial;
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
            tableName.setCellValue("Position Error Quiz");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell idHeaderCell = headerRow.createCell(col++);
            idHeaderCell.setCellValue("ID");

            Cell initCoundownCell = headerRow.createCell(col++);
            initCoundownCell.setCellValue("Length");

            Cell trialId = headerRow.createCell(col++);
            trialId.setCellValue("Trial Id");

            Cell questionId = headerRow.createCell(col++);
            questionId.setCellValue("Question Id");

            Cell answerId = headerRow.createCell(col++);
            answerId.setCellValue("Answer Ids");

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

                Cell data5 = dataRow.createCell(col++);
                data5.setCellValue(temp.trial.id);

                Cell data6 = dataRow.createCell(col++);
                data6.setCellValue(temp.question.id);

                String answers_id = "";

                int subListSize = temp.answers.size();

                for (int i=0;i<subListSize;i++){
                    if (i < subListSize-1)
                        answers_id = answers_id + String.valueOf( temp.answers.get(i).id) + ",";
                    else
                        answers_id = answers_id + String.valueOf( temp.answers.get(i).id);
                }
                Cell data7 = dataRow.createCell(col++);
                data7.setCellValue(answers_id);

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

    public Quiz(){}

    public Quiz(int length,Question question, Trial trial){
    	this.length = length;
    	this.question = question;
    	this.trial = trial;
    }

    public static Quiz create(int length, Question question, Trial trial){
    	Quiz newQuiz = new Quiz(length, question, trial);
    	return newQuiz;
    }
    public static Quiz create(Trial trial){
    	Quiz newQuiz = new Quiz();
        Question question = Question.generateQuestion(trial.questionType, newQuiz.length);
        question.save();
        newQuiz.question = question;
        newQuiz.trial = trial;
    	return newQuiz;
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Quiz> find = new Finder(Long.class,Quiz.class);

}
