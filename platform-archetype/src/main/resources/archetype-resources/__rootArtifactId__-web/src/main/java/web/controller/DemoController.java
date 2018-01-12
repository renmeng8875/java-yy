#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.yy.ent.platform.core.service.ent.Bs2Service;
import com.yy.ent.platform.core.web.controller.BaseController;
import com.yy.ent.platform.core.web.util.HttpUtil;
import ${package}.api.DemoApi;
import ${package}.web.interceptor.Permissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping(value = "/demo")
public class DemoController extends BaseController {
    @Autowired
    private Bs2Service bs2Service;
    @Reference
    private DemoApi demoApi;

    @RequestMapping(value = "/echo")
    @ResponseBody
    public JSONObject echo(@RequestParam(value = "msg", defaultValue = "dubbo") String msg) {
        String result = demoApi.echo(msg);
        return HttpUtil.wrapJsonResultSuccess(result);
    }

    @RequestMapping(value = "/exception")
    @ResponseBody
    public JSONObject exception() {
        String result = demoApi.exception();
        return HttpUtil.wrapJsonResultSuccess(result);
    }

    @RequestMapping(value = "/text", method = RequestMethod.GET)
    @ResponseBody
    public void text() {
        HttpUtil.renderText("spring mvc");
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model) {
        return "demo/index";
    }

    @RequestMapping(value = "/html", method = RequestMethod.GET)
    public String html(Model model) {
        model.addAttribute("bean", "<p style='color:red'>This is html page.</p>");
        return "demo/html";
    }

    @Permissions
    @RequestMapping(value = "/html/login", method = RequestMethod.GET)
    public Object htmlLogin(Model model) {
        Long loginUid = getLoginUid();
        model.addAttribute("loginUid", loginUid);
        return "demo/login";
    }

    @RequestMapping(value = "/html/error", method = RequestMethod.GET)
    public Object htmlError() {
        throw new RuntimeException("htmlError");
    }

    @ResponseBody
    @RequestMapping(value = "/json", method = RequestMethod.GET)
    public Object json() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","platform");
        jsonObject.put("web-name","${parentArtifactId}-web");
        return jsonObject;
    }

    @Permissions
    @RequestMapping(value = "/json/login", method = RequestMethod.GET)
    @ResponseBody
    public Object jsonLogin() {
        Long loginUid = getLoginUid();
        return loginUid;
    }

    @RequestMapping(value = "/json/error", method = RequestMethod.GET)
    @ResponseBody
    public Object jsonError() {
        throw new RuntimeException("jsonError");
        //throw ExceptionEnum.ILLEGAL_OPERATION.getException();
    }


    @RequestMapping(value = "/file/form", method = RequestMethod.GET)
    public String fileForm() {
        return "demo/fileForm";
    }

    @RequestMapping(value = "/file/upload", method = RequestMethod.POST)
    public ModelAndView fileUpload(@RequestParam("msg") String msg,
                                   @RequestParam("file") MultipartFile file) throws Exception {
        String path;
        String contentType = file.getContentType();
        byte[] source = file.getBytes();
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        if (file.getSize() > 1024 * 1024 * 16) {
            path = bs2Service.uploadLargeFile(source, uuid + "." + suffix, contentType);
        } else {
            path = bs2Service.uploadFile(source, uuid + "." + suffix, contentType);
        }
        JSONObject result = new JSONObject();
        result.put("msg", msg);
        result.put("file.size", file.getSize());
        result.put("file.name", file.getName());
        result.put("file.originalFilename", file.getOriginalFilename());
        result.put("file.contentType", contentType);
        result.put("file.upload.url", path);
        HttpUtil.renderHtml(result.toJSONString());
        return null;
    }
}

