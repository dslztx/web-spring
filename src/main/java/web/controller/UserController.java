package web.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import me.dslztx.assist.util.ObjectAssist;
import web.domain.User;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello World! Welcome to visit dslztx.com";
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    @ResponseBody
    public String addUser(@RequestBody User user) {
        if (ObjectAssist.isNotNull(user))
            return user.toString();
        return "NULL";
    }

    @RequestMapping("/hello/way")
    @ResponseBody
    public User helloWay() {
        return new User("dslztx", 33);
    }

    @RequestMapping("/upload/datastream")
    @ResponseBody
    public String dataStream(@RequestBody String body) {

        logger.info("transfered data stream: {}", body);

        return "transfer data stream successfully";
    }

    @RequestMapping("/timeout")
    @ResponseBody
    public String timeout(@RequestBody String body) throws InterruptedException {

        logger.info("timeout: {}", body);

        Thread.sleep(10000L);

        return "timeout";
    }

    @RequestMapping("/uploadChunked")
    public void uploadChunked(HttpServletRequest request, HttpServletResponse response)
        throws InterruptedException, IOException {

        System.out.println(request.getHeader("Transfer-Encoding"));

        InputStream ii = request.getInputStream();

        byte[] dd = IOUtils.toByteArray(ii);

        System.out.println(dd.length);

        response.setStatus(200);
        response.getWriter().println("success");
    }

    @RequestMapping(value = {"/mail/identify"}, method = {RequestMethod.POST})
    @ResponseBody
    public String identify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(request.getHeader("Content-Length"));

        byte[] bb = IOUtils.toByteArray(request.getInputStream());
        System.out.println(bb.length);

        return "success";
    }

}
