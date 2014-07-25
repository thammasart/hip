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
    public static Result saveVisualSearch() {
        ObjectNode result = Json.newObject();
        try {
            JsonNode json = request().body().asJson();
            ObjectMapper mapper = new ObjectMapper();
            List<Trial> trials = mapper.readValue(Json.stringify(json), mapper.getTypeFactory().constructCollectionType(List.class, Trial.class));
            for(Trial trial : trials){
                Question question = Question.find.byId(trial.quiz.question.id);
                question.sharps = trial.quiz.question.sharps;
                question.update();
                Quiz quiz = Quiz.find.byId(trial.quiz.id);
                quiz.circleGreen = trial.quiz.circleGreen;
                quiz.circleRed = trial.quiz.circleRed;
                quiz.squareBlue = trial.quiz.squareBlue;
                quiz.squareGreen = trial.quiz.squareGreen;
                quiz.squareRed = trial.quiz.squareRed;
                quiz.positionXofTarget = trial.quiz.positionXofTarget;
                quiz.positionYofTarget = trial.quiz.positionYofTarget;
                quiz.frameSize = trial.quiz.frameSize;
                quiz.update();
            }
            String jsonArray = mapper.writeValueAsString(trials);
            json = Json.parse(jsonArray);
            result.put("message", "success");
            result.put("status", "ok");
            result.put("trials", json);
        }catch (JsonProcessingException e) {
            result.put("message", e.getMessage());
            result.put("status", "json Process error");
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
