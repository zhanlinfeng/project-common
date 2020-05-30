package test;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @program: admin_server
 * @description: web层测试基类
 * @author: td
 * @create: 2019-11-28 20:15
 **/
public class BaseWebTest extends AutoRollbackBaseDatabaseTest {
    @Autowired
    private WebApplicationContext wac;
    protected MockMvc mockMvc;
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();   //构造MockMvc
    }
}
