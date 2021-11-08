import com.answer.RunApplication;
import com.answer.service.ValueAnnotateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * created by liufeng
 * 2020/7/14
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RunApplication.class)
public class ValueAnnotateTest {

    @Autowired
    private ValueAnnotateService valueAnnotateService;

    @Test
    public void testAnnotate(){
        valueAnnotateService.systemValue();
    }
}
