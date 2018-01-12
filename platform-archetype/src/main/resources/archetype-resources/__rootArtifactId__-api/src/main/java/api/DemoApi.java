#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.api;

/**
 * DemoApi
 *
 * @author suzhihua
 * @date 2015/8/21.
 */
public interface DemoApi {
    String echo(String msg);

    String exception();
}
