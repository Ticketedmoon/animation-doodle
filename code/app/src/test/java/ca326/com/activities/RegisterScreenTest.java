package ca326.com.activities;

import android.database.Cursor;
import android.net.Uri;

import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;


public class RegisterScreenTest {


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
}