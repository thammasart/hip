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
        this.totalScore = 0;
        this.totalUsedTime = 0;
        for(Quiz q:quizzes){
            this.totalScore += Answer.calculateTotalScore(q.findAnswers());
            this.totalUsedTime += Answer.calculateTotalUsedTime(q.findAnswers());
        }
        this.totalUser = TimeLog.calaulateTotalUserTakeExp(schedule,id);
        this.totalScore /=totalUser;
        this.totalUsedTime /=totalUser;
        this.update();
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
}
