import org.junit.*;
import java.util.*;
import play.mvc.*;
import play.test.*;
import play.libs.F.*;
import play.libs.Yaml;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import com.google.common.collect.ImmutableMap;
import com.avaje.ebean.Ebean;

public class FunctionalTest {

    @Before
    public void setUp(){
        start(fakeApplication(inMemoryDatabase(), fakeGlobal()));
        Ebean.save((List) Yaml.load("test-data.yml"));
    }


    @Test
    public void authenticateFailure(){
            /*
                Create result from play.mvc and assign its with play.test.Helpers.callAction()
                with HandleRef and fakeRequest() --> build a new GET
             */
        Result result = callAction(
                            controllers.routes.ref.Application.home(),
                            fakeRequest()
                        );
                /*
                assert play.test.Helpers.status(Result) assume its equal to play.mvc.Http.Status.SEE_OTHER
                (303 redirect to other page)
                 */
        assertThat(status(result)).isEqualTo(SEE_OTHER);
                /*
                    assert play.test.Helpers.redirectLocation(Result) equal to "/"
                 */
        assertThat(redirectLocation(result)).isEqualTo("/");
    }

    @Test
    public void authenticateSuccess(){

        Result result = callAction(
                            controllers.routes.ref.Application.authenticate(),
                            fakeRequest().withFormUrlEncodedBody(ImmutableMap.of(
                                "username","s550",
                                "password","password"
                            ))
                        );
        assertThat(status(result)).isEqualTo(SEE_OTHER);
        assertThat(redirectLocation(result)).isEqualTo("/home");
        assertThat(session(result).get("username")).isEqualTo("s550");  
    }

    @Test
    public void authenticate_Failure_With_Wrong_password(){

        Result result = callAction(
                            controllers.routes.ref.Application.authenticate(),
                            fakeRequest().withFormUrlEncodedBody(ImmutableMap.of(
                                "username","s550",
                                "password","wrong"
                            ))
                        );
        assertThat(status(result)).isEqualTo(BAD_REQUEST);
        assertThat(redirectLocation(result)).isNull();
        assertThat(flash(result).containsKey("unauthenticate")).isTrue();
    }

    @Test
    public void logoutSuccess(){
        Result result = callAction(
                controllers.routes.ref.Application.logout(),
                fakeRequest().withSession("username", "s550")
            );
        assertThat(status(result)).isEqualTo(SEE_OTHER);
        assertThat(redirectLocation(result)).isEqualTo("/");
        assertThat(session(result).get("username")).isNull();
    }


    @Test
    public void viewAboutSuccess() {
        Result result = callAction(
                controllers.routes.ref.Application.about(),
                fakeRequest().withSession("username", "s550")
            );
        assertThat(status(result)).isEqualTo(OK);
        assertThat(redirectLocation(result)).isNull();
    }

    @Test
    public void viewAboutFailure() {
        Result result = callAction(
                controllers.routes.ref.Application.about(),
                fakeRequest()
            );
        assertThat(status(result)).isEqualTo(SEE_OTHER);
        assertThat(redirectLocation(result)).isEqualTo("/");
    }

    @Test
    public void viewBrownPetersonInfoSuccess() {
        Result result = callAction(
                controllers.routes.ref.BrownPeterson.info(),
                fakeRequest().withSession("username", "s550")
            );
        assertThat(status(result)).isEqualTo(OK);
        assertThat(redirectLocation(result)).isNull();
    }

    @Test
    public void viewBrownPetersonInfoFailure() {
        Result result = callAction(
                controllers.routes.ref.BrownPeterson.info(),
                fakeRequest()
            );
        assertThat(status(result)).isEqualTo(SEE_OTHER);
        assertThat(redirectLocation(result)).isEqualTo("/");
    }

    @Test
    public void viewBrownPetersonProcSuccess(){
        Result result = callAction(
                controllers.routes.ref.BrownPeterson.proc(),
                fakeRequest().withSession("username", "s550")
        );
        assertThat(status(result)).isEqualTo(OK);
        assertThat(redirectLocation(result)).isNull();
    }

    @Test
    public void viewBrownPetersonProcFailure(){
        Result result = callAction(
                controllers.routes.ref.BrownPeterson.proc(),
                fakeRequest()
        );
        assertThat(status(result)).isEqualTo(SEE_OTHER);
        assertThat(redirectLocation(result)).isEqualTo("/");
    }

    @Test
    public void viewStroopEffectInfoSuccess() {
        Result result = callAction(
                controllers.routes.ref.StroopEffect.info(),
                fakeRequest().withSession("username", "s550")
            );
        assertThat(status(result)).isEqualTo(OK);
        assertThat(redirectLocation(result)).isNull();
    }

    @Test
    public void viewStroopEffectInfoFailure() {
        Result result = callAction(
                controllers.routes.ref.StroopEffect.info(),
                fakeRequest()
            );
        assertThat(status(result)).isEqualTo(SEE_OTHER);
        assertThat(redirectLocation(result)).isEqualTo("/");
    }
    @Test
    public void viewStroopEffectProcSuccess(){
        Result result = callAction(
                controllers.routes.ref.StroopEffect.proc(),
                fakeRequest().withSession("username", "s550")
        );
        assertThat(status(result)).isEqualTo(OK);
        assertThat(redirectLocation(result)).isNull();
    }
    @Test
    public void viewStroopEffectProcFailure(){
        Result result = callAction(
                controllers.routes.ref.StroopEffect.proc(),
                fakeRequest()
        );
        assertThat(status(result)).isEqualTo(SEE_OTHER);
        assertThat(redirectLocation(result)).isEqualTo("/");
    }

    @Test
    public void viewStroopEffectExperimentCheckUserTakeRepeatExperimentSuccess(){
        Result result = callAction(
                controllers.routes.ref.StroopEffect.checkUserTakeRepeatExperiment(),
                fakeRequest().withSession("username", "s550")
        );
        assertThat(status(result)).isEqualTo(SEE_OTHER);
        assertThat(redirectLocation(result)).isEqualTo("/stroop_effect/experiment?trialId=2&questionNo=0");
    }

    @Test
    public void viewBrownPetersonExperimentCheckUserTakeRepeatExperimentFailure(){
        Result result = callAction(
                controllers.routes.ref.BrownPeterson.checkUserTakeRepeatExperiment(),
                fakeRequest().withSession("username", "s550")
        );
        assertThat(status(result)).isEqualTo(OK);
        assertThat(redirectLocation(result)).isNull();
        assertThat(flash(result).containsKey("repeat")).isTrue();

    }

    @Test
    public void viewStroopEffectExperimentSuccess(){

        long id = 2;
        int i = 0;
        Result result = callAction(
                controllers.routes.ref.StroopEffect.saveAnswer(id,i),
                fakeRequest().withSession("username", "s550")
        );
        assertThat(status(result)).isEqualTo(OK);

    }
    @Test
    public void viewStroopEffectExperimentFailure(){
        Result result = callAction(
                controllers.routes.ref.StroopEffect.experiment(new Long(2), 0),
                fakeRequest()
        );
        assertThat(status(result)).isEqualTo(SEE_OTHER);
        assertThat(redirectLocation(result)).isEqualTo("/");
    }


}