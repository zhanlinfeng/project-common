package test;

import com.deepthink.common.locale.I18nMessage;
import org.junit.Test;

/**
 * @program: admin_server
 * @description: 国际化测试
 * @author: td
 * @create: 2019-11-28 10:23
 **/
public class LocaleTest {

    @Test
    public void testInit() {
        I18nMessage i18nMessage = new I18nMessage();
        i18nMessage.init();
    }

    @Test
    public void testGetMessage() {
        I18nMessage i18nMessage = new I18nMessage();
        i18nMessage.init();
        String msg = i18nMessage.getMsg("ch");
    }
}
