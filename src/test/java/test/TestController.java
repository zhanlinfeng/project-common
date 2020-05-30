package test;

import com.deepthink.common.web.ApiResult;
import com.deepthink.common.web.ApiResultEnum;
import com.deepthink.common.web.RequestThreadLocal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: admin_server
 * @description: 测试用的Contoller
 * @author: td
 * @create: 2019-11-28 20:28
 **/
@RestController
@RequestMapping("/test")
public class TestController {
    @RequestMapping("/ThreadLocalTestLocale")
    public ApiResult<?> threadLocalTestLocale() {
        String locale = RequestThreadLocal.getLocale();
        return new ApiResult<>(ApiResultEnum.result_200, locale);
    }

    @RequestMapping("/ThreadLocalTestUserId")
    public ApiResult<?> threadLocalTestUserId() {
        Long userId = RequestThreadLocal.getUserId();
        return new ApiResult<>(ApiResultEnum.result_200, userId);
    }
}
