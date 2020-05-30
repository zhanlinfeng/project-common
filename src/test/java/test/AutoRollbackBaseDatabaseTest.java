package test;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @program: admin_server
 * @description: 数据库测试基类，为避免脏数据的产生，所有单元测试的都要会滚数据
 * @author: td
 * @create: 2019-11-27 20:51
 **/
@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = Application.class)
// 自动回滚数据库操作
@Transactional
public class AutoRollbackBaseDatabaseTest {

}
