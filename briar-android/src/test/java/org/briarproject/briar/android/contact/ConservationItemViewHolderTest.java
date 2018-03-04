package org.briarproject.briar.android.contact;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

//@RunWith(RobolectricTestRunner.class)
//@RunWith(MockitoJUnitRunner.class)
//@Config(sdk = 21, application = TestBriarApplication.class,
//        packageName = "org.briarproject.briar")
public class ConservationItemViewHolderTest {

    @Test
    public void testBindMethodWithImageTagInBody(){

        ConversationItem ci = Mockito.mock(ConversationItem.class);
        ci.body = "Image:";

        ConversationItemViewHolder civh = Mockito.mock(ConversationItemViewHolder.class);
        civh.bind(ci);

        Assert.assertEquals(ci.getBody(), civh.getText());
    }
}
