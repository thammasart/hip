package models.garnerInterference;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import models.TimeLog;

import models.ExperimentSchedule;
import play.db.ebean.Model;
import javax.persistence.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
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
@Table (name="garner_interference_trial")
public class Trial extends Model{
    @Id
    public long id;
    public double totalScore = 0;
    public double totalUsedTime = 0;
    public int totalUser = 0;
    public double lengthBigSquare = 5.5;
    public double lengthSmallSquare = 5.0;

    public int noOfColorQuestion = 1;
    public int noOfFakeColorQuestion = 1;
    public int noOfSizeQuestion = 1;
    public int noOfFakeSizeQuestion = 1;
    public int noOfBiDimensionQuestion = 1;
    public int noOfFakeBiDimentsionQuestion = 1;

    public String color = "grey";
    public Feature feature = Feature.TWOFEATURE; // one feature or two feature
    @ManyToOne
    public Color colorDark;
    @ManyToOne
    public Color colorLight;
    @ManyToOne
    public ExperimentSchedule schedule;
    @OneToMany(cascade={CascadeType.MERGE, CascadeType.REMOVE}, mappedBy = "trial")
    @JsonManagedReference
    public List<Quiz> quizzes = new ArrayList<Quiz>();

    public static final String[] COLORS = {"red", "blue", "yellow", "green", "grey"};

    public Trial(ExperimentSchedule schedule){
    	this.schedule = schedule;
    }

    public static List<Trial> findInvolving(ExperimentSchedule ex){
        return find.where().eq("schedule", ex).findList();
    }

    public static List<Trial> findAllTrial(int feature){

        if(feature == 1){ 
            return find.where().eq("feature",Feature.ONEFEATURE).findList();
        }else if(feature == 2 ){ 
            return find.where().eq("feature",Feature.ONEFEATURE).findList();
        }else if(feature == 3 ){ 
            return find.where().eq("feature",Feature.TWOFEATURE).findList();
        }else{ 
            return find.all();
        }
    }

    public void updateResult(){
        this.totalUser = TimeLog.calaulateTotalUserTakeExp(schedule,id);
        if(totalUser > 0){
            this.totalScore = 0;
            this.totalUsedTime = 0;
            for(Quiz q:quizzes){
                this.totalScore += Answer.calculateTotalScore(q.findAnswers());
                this.totalUsedTime += Answer.calculateTotalUsedTime(q.findAnswers());
            }
            this.totalScore /=totalUser;
            this.totalUsedTime /=totalUser;
            this.update();
        }
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Trial> find = new Finder(Long.class, Trial.class);

    public static Trial create(ExperimentSchedule experimentSchedule) {
        Random random = new Random();
        Trial trial = new Trial(experimentSchedule);
        trial.color = COLORS[random.nextInt(COLORS.length)];
        List<Color> colors = Color.find.where().eq("color", trial.color).findList();
        if(colors.size() > 0) {
            Color colorDark = colors.get(random.nextInt(colors.size()));
            Color colorLight = colors.size() > 1 ? calculateColorLight(colors, colorDark) : colorDark;
            if(colorDark.saturation < colorLight.saturation){
                Color temp = colorDark;
                colorDark = colorLight;
                colorLight = temp;
            }
            trial.colorDark = colorDark;
            trial.colorLight = colorLight;
        }

        trial.lengthBigSquare = new BigDecimal((random.nextDouble() * 5) + 5, new MathContext(2, RoundingMode.DOWN)).doubleValue();
        trial.lengthSmallSquare = trial.lengthBigSquare - new BigDecimal(random.nextDouble() + 0.2, new MathContext(2, RoundingMode.DOWN)).doubleValue();
        trial.noOfBiDimensionQuestion = random.nextInt(3);
        trial.noOfColorQuestion = random.nextInt(3);
        trial.noOfSizeQuestion = random.nextInt(3);
        trial.noOfFakeBiDimentsionQuestion = random.nextInt(3);
        trial.noOfFakeColorQuestion = random.nextInt(3);
        trial.noOfFakeSizeQuestion = random.nextInt(3);
        return trial;
    }

    private static Color calculateColorLight(List<Color> colors, Color colorDark) {
        Color colorLight = colors.get(new Random().nextInt(colors.size()));
        return colorLight.id != colorDark.id ? colorLight : calculateColorLight(colors, colorDark);
    }

    public void generateQuiz() {
        for(int i=0; i < this.noOfColorQuestion; i++)
            Quiz.create(this, QuestionType.COLOR, true).save();
        for(int i=0; i < this.noOfFakeColorQuestion; i++)
            Quiz.create(this, QuestionType.COLOR, false).save();
        for(int i=0; i < this.noOfSizeQuestion; i++)
            Quiz.create(this, QuestionType.SIZE, true).save();
        for(int i=0; i < this.noOfFakeSizeQuestion; i++)
            Quiz.create(this, QuestionType.SIZE, false).save();
        for(int i=0; i < this.noOfBiDimensionQuestion; i++)
            Quiz.create(this, QuestionType.BOTH, true).save();
        for(int i=0; i < this.noOfFakeBiDimentsionQuestion; i++)
            Quiz.create(this, QuestionType.BOTH, false).save();
    }

    public static void exportToFile(Workbook wb){

        try{
            int headerRowIndex = 0;

            int col = 0;

            //wb = new HSSFWorkbook();
            Sheet userSheet = wb.createSheet("Trial");

            Row headerRow = userSheet.createRow(headerRowIndex++);
            Cell tableName = headerRow.createCell(0);
            tableName.setCellValue("Garner Interference Trial");

            headerRow = userSheet.createRow(headerRowIndex++);

            Cell idHeaderCell = headerRow.createCell(col++);
            idHeaderCell.setCellValue("ID");

            Cell bigCell = headerRow.createCell(col++);
            bigCell.setCellValue("Length Big Square");

            Cell smallCell = headerRow.createCell(col++);
            smallCell.setCellValue("Length Small Square");

            Cell noCCell = headerRow.createCell(col++);
            noCCell.setCellValue("Number of Color Question");

            Cell fnoCSCell = headerRow.createCell(col++);
            fnoCSCell.setCellValue("Number of Fake Color Question");

            Cell noSCell = headerRow.createCell(col++);
            noSCell.setCellValue("Number of Size Question");

            Cell fnoSCell = headerRow.createCell(col++);
            fnoSCell.setCellValue("Number of Fake Size Question");

            Cell noBDCell = headerRow.createCell(col++);
            noBDCell.setCellValue("Number of BiDimension Question");

            Cell fnoBDCell = headerRow.createCell(col++);
            fnoBDCell.setCellValue("Number of Fake BiDimentsion Question");

            Cell colorCell = headerRow.createCell(col++);
            colorCell.setCellValue("Color");

            Cell featureCell = headerRow.createCell(col++);
            featureCell.setCellValue("Feature");

            Cell colorDarkCell = headerRow.createCell(col++);
            colorDarkCell.setCellValue("Color Dark Id");

            Cell colorLightCell = headerRow.createCell(col++);
            colorLightCell.setCellValue("Color Light Id");

            Cell expCell = headerRow.createCell(col++);
            expCell.setCellValue("Experiment Schedule Id");

            Cell quizCell = headerRow.createCell(col++);
            quizCell.setCellValue("Quiz Ids");

            List<Trial> tempList = find.all();

            int listSize = tempList.size();
            for(int row=headerRowIndex;row-headerRowIndex<listSize;row++){

                col = 0;

                Trial temp = tempList.get(row-headerRowIndex);
                Row dataRow = userSheet.createRow(row);

                Cell dataId = dataRow.createCell(col++);
                dataId.setCellValue(temp.id);

                Cell data1 = dataRow.createCell(col++);
                data1.setCellValue(temp.lengthBigSquare);

                Cell data2 = dataRow.createCell(col++);
                data2.setCellValue(temp.lengthSmallSquare);

                Cell data3 = dataRow.createCell(col++);
                data3.setCellValue(temp.noOfColorQuestion);

                Cell data4 = dataRow.createCell(col++);
                data4.setCellValue(temp.noOfFakeColorQuestion);

                Cell data5 = dataRow.createCell(col++);
                data5.setCellValue(temp.noOfSizeQuestion);

                Cell data6 = dataRow.createCell(col++);
                data6.setCellValue(temp.noOfFakeSizeQuestion);

                Cell data7 = dataRow.createCell(col++);
                data7.setCellValue(temp.noOfBiDimensionQuestion);

                Cell data8 = dataRow.createCell(col++);
                data8.setCellValue(temp.noOfFakeBiDimentsionQuestion);

                Cell data9 = dataRow.createCell(col++);
                data9.setCellValue(temp.color);

                String featureString = "";
                if (temp.feature == Feature.ONEFEATURE)
                    featureString = "One Feature";
                else if (temp.feature == Feature.TWOFEATURE)
                    featureString = "Two Feature";

                Cell data10 = dataRow.createCell(col++);
                data10.setCellValue(featureString);

                Cell data11 = dataRow.createCell(col++);
                data11.setCellValue(temp.colorDark.id);

                Cell data12 = dataRow.createCell(col++);
                data12.setCellValue(temp.colorLight.id);

                Cell data13 = dataRow.createCell(col++);
                data13.setCellValue(temp.schedule.id);

                String quizzes_id = "";

                int subListSize = temp.quizzes.size();

                for (int i=0;i<subListSize;i++){
                    if (i < subListSize-1)
                        quizzes_id = quizzes_id + String.valueOf( temp.quizzes.get(i).id) + ",";
                    else
                        quizzes_id = quizzes_id + String.valueOf( temp.quizzes.get(i).id);
                }
                Cell data14 = dataRow.createCell(col++);
                data14.setCellValue(quizzes_id);

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
