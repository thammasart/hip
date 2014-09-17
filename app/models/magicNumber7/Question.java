package models.magicNumber7;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import play.db.ebean.Model.Finder;
import com.fasterxml.jackson.annotation.JsonBackReference;

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
@Table (name = "magic_number_7_question")
public class Question extends Model{

    private static final String ENGLISH_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBER_CASE = "0123456789";
    private static final String THAI_CASE = "กขคฆงจฉชฌญฎฐฒณดถทธนบผพภมยรลวศษสหฬอ";

    @Id
    public long id;
    public String memorySet;
    public QuestionType questionType;
    @OneToOne(mappedBy = "question")
    private Quiz quiz;

    public Question(){}

    public Question(String memorySet, QuestionType questionType) {
    	this.memorySet = memorySet;
    	this.questionType = questionType;
    }

    public static Question generateQuestion(QuestionType type, int length){
        Question question = new Question();
        question.questionType = type;
        question.memorySet = question.generateMemorySet(length);
        return question;
    }

    private String generateMemorySet(int length){
        StringBuffer memorySetBuffer = new StringBuffer();

        for(int i = 0; i < length; i++){
            memorySetBuffer.append(generateRandomChar());
        }

        return memorySetBuffer.toString();
    }

    private char generateRandomChar(){
        Random random = new Random();
        char c = 'a';
        switch (questionType){
            case ENGLISH: c = ENGLISH_CASE.charAt(random.nextInt(ENGLISH_CASE.length()));break;
            case THAI: c = THAI_CASE.charAt(random.nextInt(THAI_CASE.length()));break;
            case NUMBER: c = NUMBER_CASE.charAt(random.nextInt(NUMBER_CASE.length()));break;
        }

        return c;
    }

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;
            int col = 0;
            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Question");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Magic Number 7 Question");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell idHeaderCell = headerRow.createCell(col++);
            idHeaderCell.setCellValue("ID");

            Cell memCell = headerRow.createCell(col++);
            memCell.setCellValue("Memory Set");

            Cell questionCell = headerRow.createCell(col++);
            questionCell.setCellValue("Question Type");

            Cell quizCell = headerRow.createCell(col++);
            quizCell.setCellValue("Quiz Id");

            List<Question> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){
                Question temp = tempList.get(row-headerRowIndex);

                col = 0;

                Row dataRow = userSheet.createRow(row);

                Cell dataId = dataRow.createCell(col++);
                dataId.setCellValue(temp.id);

                Cell data1 = dataRow.createCell(col++);
                data1.setCellValue(temp.memorySet);

                String type = "";
                if (temp.questionType == QuestionType.ENGLISH)
                    type = "English";
                else if (temp.questionType == QuestionType.THAI)
                    type = "Thai";
                else if (temp.questionType == QuestionType.NUMBER)
                    type = "Number";
                Cell data2 = dataRow.createCell(col++);
                data2.setCellValue(type);

                Cell data3 = dataRow.createCell(col++);
                data3.setCellValue(temp.quiz.id);

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
	public static Finder<Long, Question> find = new Finder(Long.class, Question.class);
}
