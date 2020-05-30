package test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.junit4.PowerMockRunnerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.spy;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * @program: admin_server
 * @description: powermock使用
 * @author: td
 * @create: 2019-11-29 20:04
 **/
@PowerMockRunnerDelegate(SpringJUnit4ClassRunner.class)
@PowerMockIgnore(value = { "javax.net.ssl.*", "javax.management.*",
        "javax.net.SocketFactory", "javax.crypto.*"})
@RunWith(PowerMockRunner.class)
@PrepareForTest(FinalClass.class)
public class PowerMockTester {



    @Test
    public void basicTest() {
        FinalClass mockedObj = mock(FinalClass.class);
        PowerMockito.when(mockedObj.finalMethod()).thenReturn("Mock final method Success");
        assertEquals(mockedObj.finalMethod(), "Mock final method Success");
    }

    @Test
    public void partialMock() {
        FinalClass finalObj = new FinalClass();
        FinalClass spyObj = spy(finalObj);
        assertEquals("I am final Method!", spyObj.finalMethod());
        when(spyObj.finalMethod()).thenReturn("Mock final method Success");
        assertEquals(spyObj.finalMethod(), "Mock final method Success");

    }



}

final class  FinalClass {
   public final   String finalMethod() {
       return "I am fianlMethod!";
   }
}