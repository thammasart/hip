package controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.ExperimentSchedule;
import models.visualSearch.Question;
import models.visualSearch.Quiz;
import models.visualSearch.Trial;
import models.visualSearch.FrameSize;
import models.visualSearch.ShapeType;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Created by ohmcstu on 7/6/14.
 */
public class RequestController extends Controller {

    @BodyParser.Of(BodyParser.Json.class)
    public static Result init(long id) {
        ObjectNode result = Json.newObject();
        JsonNode json;
        try {
            ExperimentSchedule exp = ExperimentSchedule.find.byId(id);
            List<Trial> trials = Trial.findInvolving(exp);
            ObjectMapper mapper = new ObjectMapper();
            String jsonArray = mapper.writeValueAsString(trials);
            json = Json.parse(jsonArray);
            result.put("message", "success");
            result.put("status", "ok");
            result.put("trials", json);
        }catch (JsonProcessingException e) {
            result.put("message", e.getMessage());
            result.put("status", "error");
        }catch(RuntimeException e){
            result.put("message", e.getMessage());
            result.put("status", "error");
        }catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error");
        }

        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result saveVisualSearch(long trialId , 
            int circleGreen, int circleRed,int circleBlue, int squareBlue, int squareRed, int squareGreen,
            double positionXofTarget, double positionYofTarget, String frameSize, String target) {
        ObjectNode result = Json.newObject();
        try {
            JsonNode json = request().body().asJson();
            String sharps = Json.stringify(json);
            Trial trial = Trial.find.byId(trialId);
            Question question = Question.find.byId(trial.quiz.question.id);
            question.sharps = sharps;
            question.update();
            Quiz quiz = Quiz.find.byId(trial.quiz.id);
            quiz.circleGreen = circleGreen;
            quiz.circleRed = circleRed;
            quiz.circleBlue = circleBlue;
            quiz.squareBlue = squareBlue;
            quiz.squareGreen = squareGreen;
            quiz.squareRed = squareRed;
            quiz.positionXofTarget = positionXofTarget;
            quiz.positionYofTarget = positionYofTarget;
            if(frameSize.equals("SMALLER"))
                quiz.frameSize = FrameSize.SMALLER;
            else if(frameSize.equals("SMALL"))
                quiz.frameSize = FrameSize.SMALL;
            else if(frameSize.equals("MEDIUM"))
                quiz.frameSize = FrameSize.MEDIUM;
            else if(frameSize.equals("BIG"))
                quiz.frameSize = FrameSize.BIG;
            else if(frameSize.equals("EXTRA"))
                quiz.frameSize = FrameSize.EXTRA;

            switch(target){
                case "SQAURE_GREEN": quiz.target = ShapeType.SQAURE_GREEN;break;
                case "SQAURE_BLUE": quiz.target = ShapeType.SQAURE_BLUE;break;
                case "SQAURE_RED": quiz.target = ShapeType.SQAURE_RED;break;
                case "CIRCLE_GREEN": quiz.target = ShapeType.CIRCLE_GREEN;break;
                case "CIRCLE_BLUE": quiz.target = ShapeType.CIRCLE_BLUE;break;
                case "CIRCLE_RED": quiz.target = ShapeType.CIRCLE_RED;break;
            }

            quiz.update();
            result.put("message", "success");
            result.put("status", "ok");
        // }
        // catch (JsonProcessingException e) {
        //     result.put("message", e.getMessage());
        //     result.put("status", "json Process error");
        }catch(RuntimeException e){
            result.put("message", e.getMessage());
            result.put("status", "Runtime error");
        }catch(Exception e){
            result.put("message", e.getMessage());
            result.put("status", "error");
        }
        return ok(result);
    }
}
