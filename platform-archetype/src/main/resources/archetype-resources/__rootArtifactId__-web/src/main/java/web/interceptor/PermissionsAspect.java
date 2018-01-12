#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.web.interceptor;

import com.yy.ent.platform.core.exception.BaseException;
import com.yy.ent.platform.core.service.ent.UdbEnv;
import com.yy.ent.platform.core.web.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * PermissionsAspect,用配置文件配置
 *
 * @author suzhihua
 * @date 2015/7/23.
 */
public class PermissionsAspect {
    protected final Logger logger = LoggerFactory.getLogger(PermissionsAspect.class);
    @Autowired
    private UdbEnv udbEnv;

    public Object aroundInterceptor(ProceedingJoinPoint call) throws Throwable {
        Permissions interceptor = getAnnotation(call, Permissions.class);
        if (interceptor != null) {
            PermissionsEnum[] interceptors = interceptor.value();
            logger.info("interceptors:{}", Arrays.toString(interceptors));
            for (PermissionsEnum e : interceptors) {
                switch (e) {
                    case LOGIN:
                        login();
                        break;
                    case ADMIN_LOGIN:
                        adminLogin();
                        break;
                    default:
                        break;
                }
            }
        }
        Object result = call.proceed();
        return result;
    }

    private String login() {
        String loginUidStr = getLoginUidStr();
        if (StringUtils.isBlank(loginUidStr)) {
            throw new BaseException(BaseException.CODE_NO_LOGIN, "请登录后再操作");
        }
        logger.info("loginUid:{}", loginUidStr);
        return loginUidStr;
    }

    private void adminLogin() {
        String login = login();
        //TODO:进一步处理
    }

    /**
     * 获得Annotation对象
     *
     * @param jp
     * @param clazz
     * @param <T>
     * @return
     */
    private <T extends Annotation> T getAnnotation(ProceedingJoinPoint jp, Class<T> clazz) {
        MethodSignature joinPointObject = (MethodSignature) jp.getSignature();
        Method method = joinPointObject.getMethod();
        return method.getAnnotation(clazz);
    }

    private String getLoginUidStr() {
        String uid = "";
        try {
            uid = udbEnv.getLoginUid(HttpUtil.getRequest(), HttpUtil.getResponse());
        } catch (Exception e) {
            logger.warn("取uid出错", e);
        }
        return uid;
    }
}
