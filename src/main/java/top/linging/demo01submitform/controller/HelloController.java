package top.linging.demo01submitform.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.linging.demo01submitform.anno.CheckSubmitForm;
import top.linging.demo01submitform.pojo.User;

import java.util.HashMap;

/**
 * @author Linging
 * @version 1.0.0
 * @since 1.0
 */
@Controller
public class HelloController {

    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        return "hello springboot!!!";
    }

    @RequestMapping("/goLogin")
    public String hello2(){
        return "hello";
    }

    @PostMapping("/login")
    @ResponseBody
    @CheckSubmitForm(retrySecond = 5000)
    public HashMap<String,Object> login(User user){
        System.out.println(user);
        HashMap<String, Object> map = new HashMap<>();
        map.put("data","ok");
        map.put("code",200);
        return map;
    }

}
