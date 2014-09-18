package models.sternbergSearch;

import play.db.ebean.*;

import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

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
@Table(name = "sternberg_search_quiz")
public class Quiz extends Model {
    @Id
    public long id;
    public String questionChar;
    public boolean isTrue;
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
            tableName.setCellValue("Sternberg Search Quiz");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell someCell = headerRow.createCell(col++);
            someCell.setCellValue("ID");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("QuestionChar");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("IsTrue");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Trial Id");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Question Id");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Answer Ids");

            List<Quiz> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){

                col = 0;

                Quiz temp = tempList.get(row-headerRowIndex);
                Row dataRow = userSheet.createRow(row);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.id);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.questionChar);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.isTrue);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.trial.id);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.question.id);

                String answers_id = "";

                int subListSize = temp.answers.size();

                for (int i=0;i<subListSize;i++){
                    if (i < subListSize-1)
                        answers_id = answers_id + String.valueOf( temp.answers.get(i).id) + ",";
                    else
                        answers_id = answers_id + String.valueOf( temp.answers.get(i).id);
                }
                someCell = dataRow.createCell(col++);
                someCell.setCellValue(answers_id);

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
    public Quiz(Trial trial, Question question) {
        this.trial = trial;
        this.question = question;
    }

    public static Quiz create(Trial trial, Question question, String questionWord, boolean isTrue) {
        Quiz newQuiz = new Quiz(trial, question);
        newQuiz.questionChar = questionWord;
        newQuiz.isTrue = isTrue;
        return newQuiz;
    }

    public static Quiz create(Trial trial){
        Quiz newQuiz = new Quiz();
        Question question = Question.generateQuestion(trial.questionType, trial.length);
        question.save();
        newQuiz.question = question;
        newQuiz.trial = trial;
        newQuiz.isTrue = true;
        newQuiz.questionChar = Character.toString(question.memorySet.charAt(new Random().nextInt(question.memorySet.length())));
        return newQuiz;
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Quiz> find = new Finder(Long.class, Quiz.class);
}
