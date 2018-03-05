package ca326.com.activities;

import android.content.Intent;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import ca326.com.activities.ItemTwoFragment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoginTest {

    @Mock private ItemTwoFragment mock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_loginButtonClick_1() {
        doCallRealMethod().when(mock).attemptLogin("james@james.com","Wellowwellow%5");
    }



}