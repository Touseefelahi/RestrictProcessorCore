package Test;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import org.junit.Assert;
import org.junit.Test;
import sample.Controller;

/**
 * Created by touseef.elahi on 09-Jul-17.
 */
public class ContollerTest{
    @Test
    public void addTest(){
        Controller controller=new Controller();
        long actualResult=controller.add(2,5);
        Assert.assertEquals(7,actualResult);
    }
}
