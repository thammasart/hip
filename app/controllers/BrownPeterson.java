package controllers;

import models.brownPeterson.Question;
import models.brownPeterson.Quiz;
import models.brownPeterson.Answer;
import play.*;
import play.mvc.*;
import play.data.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import views.html.brownPeterson.*;
import views.html.iframe.*;

public class BrownPeterson extends Controller {
    public static List<Answer> answerList = new ArrayList<Answer>();
    public static List<Question> questions = Question.getQuestionListBy(2);
    public static int questionNumber = 0;

        public static Result renderShortTermMemoryBrownPetersonTask(){
            return ok(brown_peterson_info.render());
        }

        public static Result renderShortTermMemoryBrownPetersonTaskIframe(){
            return ok(brown_peterson_iframe.render());
        }
        public static Result renderShortTermMemoryBrownPetersonTaskProc(){
            return ok(brown_peterson_proc.render());
        }
        public static Result renderShortTermMemoryBrownPetersonTaskProcIframe(){
            return ok(brown_peterson_proc_iframe.render());
        }
        public static Result renderShortTermMemoryBrownPetersonTaskExp(){
            Form<Answer> filledForm = Form.form(Answer.class);
            Answer answer = filledForm.bindFromRequest().get();
            if (questionNumber > 0){
                answerList.add(answer);
            }
            if (questionNumber+1 <= questions.size())
                return ok(brown_peterson_exp.render(questions.get(questionNumber++),1200));
            else{
                List<Answer> answerListTemp = new ArrayList<Answer>(answerList);
                questionNumber = 0;
                answerList.clear();
                return ok(brown_peterson_result.render(answerListTemp));
            }
        }
}
