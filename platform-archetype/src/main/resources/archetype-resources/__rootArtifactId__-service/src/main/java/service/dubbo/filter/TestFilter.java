#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service.dubbo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TestFilter
 *
 * @author suzhihua
 * @date 2015/9/11.
 */
@Activate(group = Constants.PROVIDER)
public class TestFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(TestFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        long start = System.currentTimeMillis();
        Result result = invoker.invoke(invocation);
        long elapsed = System.currentTimeMillis() - start;
        logger.info("url:{},time:{}", invocation.getInvoker().getUrl().toString(), elapsed);
        return result;
    }
}
