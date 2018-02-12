package org.briarproject.briar.android.contact;

import org.briarproject.briar.android.TestBriarApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 21, application = TestBriarApplication.class,
        packageName = "org.briarproject.briar")
public class ConservationItemViewHolderTest {

    @Test
    public void testBindMethodWithImageTagInBody(){
        //Create test ConservationItem with test body that starts with "ImageTag:"
        //Check if returned value of decodedImage is same as real image
    }
}
