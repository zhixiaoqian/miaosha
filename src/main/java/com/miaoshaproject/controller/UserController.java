package com.miaoshaproject.controller;

import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.model.UserModel;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.UserService;
import com.miaoshaproject.service.imp.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

@Controller("user")
@RequestMapping("/user")
@CrossOrigin
public class UserController extends  BaseController{
    @Autowired
    private UserServiceImp userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    //用户获取otp短信接口
    @RequestMapping(value="/getotp",method={RequestMethod.POST},consumes = {"application/x-www-form-urlencoded"})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name="telphone") String telphone){
        //生成otp验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt+=10000;
        String optCode = String.valueOf(randomInt);
        //将otp同用户手机号关联,使用httpsession
        httpServletRequest.getSession().setAttribute(telphone,optCode);


        //将otp通过短信发送给用户，省略
        System.out.println("telphone="+telphone+"&optCode="+optCode);

        return CommonReturnType.create(null);


    }

    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name="id") Integer id){
        //调用service服务获取对应id的用户对象并返回给前端
       UserModel userModel = userService.getUserById(id);
       if(userModel==null){
           try {
               throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
           } catch (BusinessException e) {
               e.printStackTrace();
           }
       }


       //返回通用对象
       return CommonReturnType.create(userModel);
    }


}
