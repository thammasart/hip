package models.attentionBlink;

import play.db.ebean.*;
import javax.persistence.*;
import java.util.Random;
import java.util.List;
import play.db.ebean.Model.Finder;
import java.util.ArrayList;
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
@Table (name = "attention_blink_question")
public class Question extends Model{

    private static final String ENGLISH_CASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBER_CASE = "0123456789";
    private static final String THAI_CASE = "กขคฆงจฉชฌญฎฐฒณดถทธนบผพภมยรลวศษสหฬอ";

	@Id
	public long id;
    @Column(nullable=false)
	public String letter;
    @Column(name="question_set",nullable=false, length=20)
    public String set;
    public boolean correctAnswer;
    public QuestionType questionType;

    @OneToOne(mappedBy="question",cascade=CascadeType.REMOVE)
    private Quiz quiz;

	public Question(String letter, String set, boolean correctAnswer, QuestionType questionType) {
        this.letter = letter;
        this.set = set;
        this.correctAnswer = correctAnswer;
        this.questionType = questionType; 
	}

    public static Question create(){
        Question question = new Question("", "", false, QuestionType.ENGLISH);
        return question;
    }

    public static Question generateQuestion(QuestionType questionType, int length, int numberOfTarget, boolean isCorrect){
        StringBuffer setbuffer = new StringBuffer();
        String typeString = getQuestionTypeCase(questionType);

        Random random = new Random();
        StringBuffer letterBuffer = generateLetterBuffer(numberOfTarget, typeString);

        for(int i =0; i < length; i++){
            setbuffer.append(typeString.charAt(random.nextInt(typeString.length())));
        }

        if(isCorrect){
            int index = random.nextInt(setbuffer.length() - numberOfTarget - 1);
            for(int i = 0; i < numberOfTarget; i++){
                setbuffer.setCharAt(index + i, letterBuffer.charAt(i));
            }
        }else{
            while(setbuffer.toString().contains(letterBuffer)){
                StringBuffer newLetter = new StringBuffer();
                for(int i = 0; i < numberOfTarget; i++){
                    newLetter.append(typeString.charAt(random.nextInt(typeString.length())));
                }
                String temp = setbuffer.toString();
                temp = temp.replace(letterBuffer, newLetter);
                setbuffer = new StringBuffer(temp);
            }

            if(numberOfTarget == 2){
                int index = random.nextInt(setbuffer.length() - 2);
                setbuffer.setCharAt(index, letterBuffer.charAt(0));
            }else if(numberOfTarget == 3){
                int index = random.nextInt(setbuffer.length() - 3);
                setbuffer.setCharAt(index, letterBuffer.charAt(0));
                setbuffer.setCharAt(index + 2, letterBuffer.charAt(2));
            }

        }

        Question question = new Question(letterBuffer.toString(), setbuffer.toString(), isCorrect, questionType);
        question.save();
        return question;
    }

    private static StringBuffer generateLetterBuffer(int numberOfTarget, String typeString){
        StringBuffer letterBuffer = new StringBuffer();
        Random random = new Random();

        for(int i = 0; i < numberOfTarget; i++){
            letterBuffer.append(typeString.charAt(random.nextInt(typeString.length())));
        }
        if(numberOfTarget == 2){
            if(letterBuffer.charAt(0) == letterBuffer.charAt(1)){
                letterBuffer = generateLetterBuffer(numberOfTarget, typeString);
            }
        }else if(numberOfTarget == 3){
            if(letterBuffer.charAt(0) == letterBuffer.charAt(1) ||
                letterBuffer.charAt(0) == letterBuffer.charAt(2) ||
                letterBuffer.charAt(1) == letterBuffer.charAt(2)
                ){
                letterBuffer = generateLetterBuffer(numberOfTarget, typeString);
            }
        }
        return letterBuffer;
    }

    public void changeQuestionSetByCorrectAnswer(){
        String typeString = getQuestionTypeCase(questionType);
        Random random = new Random();
        if(this.correctAnswer){
            StringBuffer setbuffer = new StringBuffer(this.set);
            int index = random.nextInt(setbuffer.length() - this.letter.length() - 1);
            for(int i = 0; i < this.letter.length(); i++){
                setbuffer.setCharAt(index + i, this.letter.charAt(i));
            }
            this.set = setbuffer.toString();
        }else{
            while(this.set.contains(this.letter)){
                StringBuffer letterBuffer = new StringBuffer();
                for(int i = 0; i < this.letter.length(); i++){
                    letterBuffer.append(typeString.charAt(random.nextInt(typeString.length())));
                }
                this.set = this.set.replace(this.letter, letterBuffer);
            }
        }

        this.update();
    }

    private static String getQuestionTypeCase(QuestionType questionType){
        String typeString = ENGLISH_CASE;
        switch(questionType){
            case ENGLISH : typeString = ENGLISH_CASE;break;
            case THAI : typeString = THAI_CASE;break;
            case NUMBER : typeString = NUMBER_CASE;break;
        }
        return typeString;
    }

    public static void deleteAllUnusedQuestion(){
        List<Question> questions = find.all();
        for(Question question : questions){
            if(question.quiz == null)
                question.delete();
        }
    }

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;
            int col = 0;
            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Question");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Attention Blink Question");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell idHeaderCell = headerRow.createCell(col++);
            idHeaderCell.setCellValue("ID");

            Cell firstCell = headerRow.createCell(col++);
            firstCell.setCellValue("Letter");

            Cell secondCell = headerRow.createCell(col++);
            secondCell.setCellValue("Set");

            Cell thirdCell = headerRow.createCell(col++);
            thirdCell.setCellValue("Correct Answer");

            Cell userCell = headerRow.createCell(col++);
            userCell.setCellValue("Question Type");

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
                data1.setCellValue(temp.letter);

                Cell data2 = dataRow.createCell(col++);
                data2.setCellValue(temp.set);

                Cell data3 = dataRow.createCell(col++);
                data3.setCellValue(temp.correctAnswer);

                String type = "";
                if (temp.questionType == QuestionType.THAI)
                    type = "Thai";
                else if (temp.questionType == QuestionType.ENGLISH)
                    type = "English";
                else if (temp.questionType == QuestionType.NUMBER)
                    type = "Number";

                Cell data4 = dataRow.createCell(col++);
                data4.setCellValue(type);

                Cell data5 = dataRow.createCell(col++);
                data5.setCellValue(temp.quiz.id);

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
