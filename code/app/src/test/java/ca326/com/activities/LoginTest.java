package ca326.com.activities;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.doCallRealMethod;

@RunWith(MockitoJUnitRunner.class)
public class LoginTest {


    @Test
    public void email_validation(){
        ItemTwoFragment signIn = new ItemTwoFragment();
        boolean result = signIn.isEmailValid("James@j.com");
        assertEquals(true,result);
        boolean negativeResult = signIn.isEmailValid("James@j/com");
        assertEquals(false,negativeResult);
    }

    @Test
    public void password_validation() {
        ItemTwoFragment signIn = new ItemTwoFragment();
        boolean result = signIn.isPasswordValid("Knock5");
        assertEquals(true, result);
        boolean negativeResult = signIn.isPasswordValid("Knock");
        assertEquals(false, negativeResult);
    }

    @Mock private ItemTwoFragment mock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_login() {
        doCallRealMethod().when(mock).attemptLogin("james@j.com","Knock5");
        //verify(mock, times(1)).startActivity(any(Intent.class));
    }



}