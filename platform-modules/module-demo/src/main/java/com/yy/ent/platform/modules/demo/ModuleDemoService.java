package com.yy.ent.platform.modules.demo;

import org.springframework.stereotype.Service;

/**
 * DemoService
 *
 * @author suzhihua
 * @date 2015/7/28.
 */
@Service
public class ModuleDemoService {
    public String sayHello(String name) {
        return "hello " + name+",welcome to ModuleDemoService";
    }
}
