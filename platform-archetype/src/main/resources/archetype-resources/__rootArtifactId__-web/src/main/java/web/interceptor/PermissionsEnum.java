#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.web.interceptor;

/**
 * PermissionsEnum
 *
 * @author suzhihua
 * @date 2015/7/23.
 */
public enum PermissionsEnum {
    /**
     * 普通用户登录
     */
    LOGIN,
    /**
     * 超级用户登录
     */
    ADMIN_LOGIN,
}

