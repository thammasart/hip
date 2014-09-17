package models.magicNumber7;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import play.db.ebean.Model.Finder;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
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
@Table (name = "magic_number_7_quiz")
public class Quiz extends Model{
	@Id
	public long id;
	public double displayTime;
	public int chunkSize;
    public int length;
	@ManyToOne
    @JsonBackReference
	public Trial trial;
	@OneToOne(cascade=CascadeType.REMOVE)
	public Question question;
    @OneToMany(cascade=CascadeType.REMOVE)
    private List<Answer> answers = new ArrayList<Answer>();

    public List<Answer> findAnswers(){
        return answers;
    }

	public Quiz(Trial trial, Question question){
		this.trial = trial;
		this.question = question;
	}

    public Quiz(){}

	public static Quiz create(Trial trial, Question question, double displayTime, int chunkSize, int length){
		Quiz newQuiz = new Quiz(trial, question);
		newQuiz.displayTime = displayTime;
		newQuiz.chunkSize = chunkSize;
        newQuiz.length = length;
		return newQuiz;
	}

    public static Quiz create(Trial trial){
        Quiz quiz = new Quiz();
        quiz.length = 9;
        quiz.displayTime = 3.0;
        quiz.chunkSize = 3;
        Question question = Question.generateQuestion(trial.questionType, quiz.length);
        question.save();
        quiz.question = question;
        quiz.trial = trial;
        return quiz;
    }

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;

            int col = 0;

            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Quiz");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Magic Number 7 Trial");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell idHeaderCell = headerRow.createCell(col++);
            idHeaderCell.setCellValue("ID");

            Cell questionTypeCell = headerRow.createCell(col++);
            questionTypeCell.setCellValue("Display Time");

            Cell chunkCell = headerRow.createCell(col++);
            chunkCell.setCellValue("Chunk Size");

            Cell lengthCell = headerRow.createCell(col++);
            lengthCell.setCellValue("Length");

            Cell trialCell = headerRow.createCell(col++);
            trialCell.setCellValue("Trial Id");

            Cell questionCell = headerRow.createCell(col++);
            questionCell.setCellValue("Question Id");

            Cell ansCell = headerRow.createCell(col++);
            ansCell.setCellValue("Answers Ids");

            List<Quiz> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){

                col = 0;

                Quiz temp = tempList.get(row-headerRowIndex);
                Row dataRow = userSheet.createRow(row);

                Cell dataId = dataRow.createCell(col++);
                dataId.setCellValue(temp.id);

                Cell data1 = dataRow.createCell(col++);
                data1.setCellValue(temp.displayTime);

                Cell data2 = dataRow.createCell(col++);
                data2.setCellValue(temp.chunkSize);

                Cell data3 = dataRow.createCell(col++);
                data3.setCellValue(temp.length);

                Cell data4 = dataRow.createCell(col++);
                data4.setCellValue(temp.trial.id);

                Cell data5 = dataRow.createCell(col++);
                data5.setCellValue(temp.question.id);

                String answers_id = "";

                int subListSize = temp.answers.size();

                for (int i=0;i<subListSize;i++){
                    if (i < subListSize-1)
                        answers_id = answers_id + String.valueOf( temp.answers.get(i).id) + ",";
                    else
                        answers_id = answers_id + String.valueOf( temp.answers.get(i).id);
                }
                Cell data6 = dataRow.createCell(col++);
                data6.setCellValue(answers_id);

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
