/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package my.oauth.app;

import org.junit.Test;
import static org.junit.Assert.*;

public class AppTest {
    @Test public void testAppHasAGreeting() {
        ClientOAuth classUnderTest = new ClientOAuth();
        assertNotNull("app should have a greeting", classUnderTest.getGreeting());
    }
}
