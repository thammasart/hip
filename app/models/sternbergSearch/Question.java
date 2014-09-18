package models.sternbergSearch;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import play.db.ebean.Model.Finder;

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
@Table (name = "sternberg_search_question")
public class Question extends Model{

    private static final String ENGLISH_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBER_CASE = "0123456789";
    private static final String THAI_CASE = "กขคฆงจฉชฌญฎฐฒณดถทธนบผพภมยรลวศษสหฬอ";
    @Id
    public long id;
    public String memorySet;
    public QuestionType questionType;
    @OneToMany(mappedBy = "question")
    private List<Quiz> quizzes;

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;
            int col = 0;
            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Question");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Sternberg Search Question");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell someCell = headerRow.createCell(col++);
            someCell.setCellValue("ID");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Memory Set");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Question Type");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Quiz Ids");

            List<Question> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){
                Question temp = tempList.get(row-headerRowIndex);

                col = 0;

                Row dataRow = userSheet.createRow(row);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.id);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.memorySet);

                String type = "";
                if (temp.questionType == QuestionType.ENGLISH)
                    type = "English";
                else if (temp.questionType == QuestionType.NUMBER)
                    type = "Number";
                else if (temp.questionType == QuestionType.THAI)
                    type = "Thai";
                someCell = dataRow.createCell(col++);
                someCell.setCellValue(type);

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

    public Question(){}
    public Question(String memorySet, QuestionType questionType) {
    	this.memorySet = memorySet;
    	this.questionType = questionType;
    }

	@SuppressWarnings("unchecked")
	public static Finder<Long, Question> find = new Finder(Long.class, Question.class);

    public static Question generateQuestion(QuestionType type, int length) {
        Question question = new Question();
        question.questionType = type;
        question.generateMemorySet(length);
        return question;
    }

    private void generateMemorySet(int length) {
        StringBuffer memorySetBuffer = new StringBuffer();

        for(int i = 0; i < length; i++){
            memorySetBuffer.append(generateRandomChar());
        }

        this.memorySet = memorySetBuffer.toString();
    }

    private char generateRandomChar() {
        Random random = new Random();
        char c = 'a';
        switch (questionType){
            case ENGLISH: c = ENGLISH_CASE.charAt(random.nextInt(ENGLISH_CASE.length()));break;
            case THAI: c = THAI_CASE.charAt(random.nextInt(THAI_CASE.length()));break;
            case NUMBER: c = NUMBER_CASE.charAt(random.nextInt(NUMBER_CASE.length()));break;
        }

        return c;
    }
}
