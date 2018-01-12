#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.yy.ent.platform.core.exception.BaseException;
import ${package}.api.DemoApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DemoApiImpl
 *
 * @author suzhihua
 * @date 2015/8/21.
 */
@Service
public class DemoApiImpl /*extends BaseApiImpl*/ implements DemoApi {
    protected Logger logger = LoggerFactory.getLogger(DemoApiImpl.class);

    @Override
    public String echo(String msg) {
        return msg;
    }

    @Override
    public String exception() {
        throw new BaseException(9999, "异常君来了");
    }
}
