package models.garnerInterference;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import models.TimeLog;

import models.ExperimentSchedule;
import play.db.ebean.Model;
import javax.persistence.*;
import java.util.List;
import java.util.ArrayList;

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
    @OneToMany(cascade=CascadeType.REMOVE)
    @JsonManagedReference
    public List<Quiz> quizzes = new ArrayList<Quiz>();

    public Trial(ExperimentSchedule schedule){
    	this.schedule = schedule;
    }

    public static List<Trial> findInvolving(ExperimentSchedule ex){
        return find.where().eq("schedule", ex).findList();
    }

    public void updateResult(){
        this.totalScore = 0;
        this.totalUsedTime = 0;
        for(Quiz q:quizzes){
            this.totalScore += Answer.calculateTotalScore(q.answers);
            this.totalUsedTime += Answer.calculateTotalUsedTime(q.answers);
        }
        this.totalUser = TimeLog.calaulateTotalUserTakeExp(schedule,id);
    }

    @SuppressWarnings("unchecked")
    public static Finder<Long, Trial> find = new Finder(Long.class, Trial.class);

    public static Trial create(ExperimentSchedule experimentSchedule) {
        Trial trial = new Trial(experimentSchedule);
        List<Color> colors = Color.find.where().eq("color", trial.color).findList();
        if(colors.size() > 0) {
            Color colorDark = colors.get(0);
            Color colorLight = colors.size() > 1 ? colors.get(1) : colors.get(0);
            if(colorDark.saturation < colorLight.saturation){
                Color temp = colorDark;
                colorDark = colorLight;
                colorLight = temp;
            }
            trial.colorDark = colorDark;
            trial.colorLight = colorLight;
        }
        return trial;
    }

    public void generateQuiz() {
        Quiz.create(this, QuestionType.COLOR, true).save();
        Quiz.create(this, QuestionType.COLOR, false).save();
        Quiz.create(this, QuestionType.SIZE, true).save();
        Quiz.create(this, QuestionType.SIZE, false).save();
        Quiz.create(this, QuestionType.BOTH, true).save();
        Quiz.create(this, QuestionType.BOTH, false).save();
    }
}
