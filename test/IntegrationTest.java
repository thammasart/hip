import org.junit.*;
import play.mvc.*;
import play.test.*;
import play.libs.F.*;

import static play.test.Helpers.*;
import static org.fest.assertions.Assertions.*;
import static org.fluentlenium.core.filter.FilterConstructor.*;

public class IntegrationTest {
    @Test
    public void login_page_test(){
        running(testServer(3333, fakeApplication()),
                HTMLUNIT, new Callback<TestBrowser>(){
                    public void invoke(TestBrowser browser){
                        browser.goTo("http://localhost:3333");
                        browser.fill("input[name='username']").with("s550");
                        browser.fill("input[name='password']").with("password");
                        browser.submit("button[type='submit']");
                        assertThat(browser.title()).contains("Home Page");
                    }
                }
            );
    }
}