package models.stroopEffect;

import play.db.ebean.Model;

import javax.persistence.*;

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
@Table (name="stroop_engword_question")
public class Question extends Model{

    public static final String[] colors = { "black", "blue", "red", "green", "purple", "yellow"};
    public static final String[] colorsTH = { "ดำ", "น้ำเงิน", "แดง", "เขียว", "ม่วง", "เหลือง"};


    @Id
    public long id;
    @Column(nullable=false, length=20)
    public String colorWord;
    @Column(nullable=false, length=20)
    public String inkColor;

    public QuestionType questionType = QuestionType.ENGLISH;

    @OneToMany(cascade=CascadeType.REMOVE, mappedBy = "question")
    private List<Quiz> quizzes;

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;
            int col = 0;
            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Question");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Stroop Effect Question");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell someCell = headerRow.createCell(col++);
            someCell.setCellValue("ID");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Color Word");

            someCell = headerRow.createCell(col++);
            someCell.setCellValue("Ink Color");

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
                someCell.setCellValue(temp.colorWord);

                someCell = dataRow.createCell(col++);
                someCell.setCellValue(temp.inkColor);

                String type = "";
                if (temp.questionType == QuestionType.ENGLISH)
                    type = "English";
                else if (temp.questionType == QuestionType.SHAPE)
                    type = "Shape";
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

    public Question(){
        colorWord = "Black";
        inkColor = "black";
    }

    public Question(String colorWord, String inkColor){
        this.colorWord = colorWord;
        this.inkColor = inkColor;
    }

    public static List<Question> findInvoving(List<Quiz> quizzes){
        List<Question> questions = new ArrayList<Question>();
        for(Quiz quiz : quizzes){
            questions.add(quiz.question);
        }
        return questions;
    }

    public boolean isMatch(){

        String colorWord = "";

        if(this.questionType == QuestionType.ENGLISH){
            colorWord = this.colorWord;
        }else if(this.questionType == QuestionType.THAI){
            colorWord = findEnglishColorWord(this.colorWord);
        }
        return colorWord.equalsIgnoreCase(inkColor);
    }

    public String findEnglishColorWord(String colorWordTH){
        final String BLACK_TH = "ดำ";
        final String BLUE_TH = "น้ำเงิน";
        final String RED_TH = "แดง";
        final String GREEN_TH = "เขียว";
        final String PURPLE_TH = "ม่วง";
        final String YELLOW_TH = "เหลือง";
        final String BLACK = "black";
        final String BLUE = "blue";
        final String RED = "red";
        final String GREEN = "green";
        final String PURPLE = "purple";
        final String YELLOW = "yellow";
        String colorWord = "";
        if (colorWordTH.equals(BLACK_TH)){
            colorWord = BLACK;
        }
        else if(colorWordTH.equals(BLUE_TH)){
            colorWord = BLUE;
        }
        else if(colorWordTH.equals(RED_TH)){
            colorWord = RED;
        }
        else if(colorWordTH.equals(GREEN_TH)){
            colorWord = GREEN;
        }
        else if(colorWordTH.equals(PURPLE_TH)){
            colorWord = PURPLE;
        }
        else if(colorWordTH.equals(YELLOW_TH)){
            colorWord = YELLOW;
        }
        return colorWord;
    }

    public static List<Question> findAllMatchQuestion(QuestionType questionType){
        List<Question> questions = find.where().eq("questionType", questionType).findList();
        List<Question> matchQuestions = new ArrayList<Question>();
        for(Question question : questions){
            if(question.isMatch()){
                matchQuestions.add(question);
            }
        }
        return matchQuestions;
    }
    public static List<Question> findAllNotMatchQuestion(QuestionType questionType){
        List<Question> questions = find.where().eq("questionType", questionType).findList();
        List<Question> notMatchQuestions = new ArrayList<Question>();
        for(Question question : questions){
            if(!question.isMatch()){
                notMatchQuestions.add(question);
            }
        }
        return notMatchQuestions;
    }

    public static Question randomNewQuestion(List<Question> questions){
        Random random = new Random();
        int index = random.nextInt(questions.size());
        return questions.get(index);
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Question> find = new Finder(Long.class,Question.class);

}
