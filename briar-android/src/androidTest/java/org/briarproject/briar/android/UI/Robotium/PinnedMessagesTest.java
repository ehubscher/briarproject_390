package org.briarproject.briar.android.UI.Robotium;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.robotium.solo.Solo;

import org.briarproject.briar.R;
import org.briarproject.briar.android.login.PasswordActivity;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class PinnedMessagesTest {
    @Rule
    public ActivityTestRule<PasswordActivity> activityTestRule = new ActivityTestRule<>(PasswordActivity.class);

    private Solo solo;

    @Before
    public void setUp() {
        //setUp() is run before a test case is started.
        //This is where the solo object is created.
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), activityTestRule.getActivity());
    }

    @After
    public void tearDown() {
        //tearDown() is run after a test case has finished.
        // finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }

    @Test
    public void pinMessage() {
        //SignInTest login = new SignInTest();
        //login.signIn();

        solo.unlockScreen();
        solo.assertCurrentActivity("Expected Password Activity", PasswordActivity.class);

        // Select password text input field, type password, and then click on the sign in button
        solo.clickOnView(solo.getView(R.id.edit_password));
        solo.enterText(0, "123456");
        solo.clickOnView(solo.getView(R.id.btn_sign_in));

        solo.waitForFragmentById(R.layout.list);
    }
}
