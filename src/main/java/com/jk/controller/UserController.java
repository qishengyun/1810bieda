package com.jk.controller;

import com.alibaba.druid.util.StringUtils;

import com.jk.bean.TreeBean;
import com.jk.bean.User;
import com.jk.service.UserService;

import com.jk.util.Constant;
import com.mongodb.gridfs.GridFSDBFile;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Controller

public class UserController {

    /**
     *
     */
    @Autowired
       UserService userService;

   /* @Autowired
    ShardedJedisPool shardedJedisPool;*/



    //上传图片
    @RequestMapping("imgUpload")
    @ResponseBody
    public  HashMap<String, Object>  imgUpload(@RequestParam(value = "suppImg", required = false) MultipartFile file,
                                               HttpServletRequest request, HttpServletResponse response){

        return  userService.uploadImg(file);
    }

     //图片展示


    @RequestMapping("findImgById")
    public  void  	findImgById(String imgId, HttpServletResponse response){
        GridFSDBFile file =	userService.findImgById(imgId);
        try {
            if(file!=null){
                response.setContentType("application/octet-stream");
                ServletOutputStream sos= response.getOutputStream();
                file.writeTo(sos);
                sos.flush();
                sos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //删除用户
    @RequestMapping("delall")
    @ResponseBody
    public Boolean delall(Integer[] ids) {
        try {
            userService.delall(ids);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }



    //新增或者修改

    @RequestMapping("saveOrUpdate")
    @ResponseBody
    public  void  saveOrUpdate(User user){
        if(user.getId()!=null){
            userService.updateUser(user);
        }else{
            userService.saveUser(user);
        }

    }

//跳页面
    @RequestMapping("findDialogById")
    public String	findProDialogById(Integer  id,Model model){
        if(id !=null){
           User user=userService.findProDialogById(id);
            model.addAttribute("user", user);
        }

        return  "dialogAddOrUpdate";
    }

//查询
       @RequestMapping("findRolePage")
    @ResponseBody
    public HashMap<String, Object> findRolePage(Integer page, Integer rows, User user){
        return userService.findUserPage(page,rows,user);
    }


// 递归树
@RequestMapping("findTreeList")
@ResponseBody
public List<TreeBean> findTreeList() {
   /* ShardedJedis redis = shardedJedisPool.getResource();
    String string = redis.get("conjk_jk6");
    if (string!=null) {
        List<TreeBean> parseArray = JSON.parseArray(string, TreeBean.class);
        redis.close();
        return parseArray;
    } else {*/
        List<TreeBean> findTreeList = userService.findTreeList();
       /* String jsonString = JSON.toJSONString(findTreeList);
        redis.set("conjk_jk6", jsonString);
        redis.close();

    }*/
    return findTreeList;
}

    //查询树2
    @RequestMapping("findTreeList2")
    @ResponseBody
    public List<TreeBean> findTreeList2() {

        return userService.findTreeList2();

    }

//跳到主页面
  @RequestMapping("toMain")
    public String toMain(){

        return "main";
    }

    //跳到主页面
    @RequestMapping("toMain2")
    public String toMain2(){

        return "main2";
    }
    //跳到展示页面
    @RequestMapping("toShow")
    public String toShow(){

        return "show";
    }

    @ResponseBody
    @RequestMapping("getUser")
    public List<User> getUserById() {


        List<User> user = userService.findUserById();

        return user;
    }

    @RequestMapping("toLogin")
    public String index(HttpServletRequest request, Model model) {

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(Constant.cookieNamePwd)) {
                    String value = cookie.getValue();//保存的是用户名+/分隔符+密码
                    if (value != null) { //str = 123.456  [123,456]
                        String[] split = value.split(Constant.splitChart);

                        model.addAttribute("username", split[0]);
                        model.addAttribute("password", split[1]);
                    }
                }
            }
        }
        return "login";
    }

    @RequestMapping("login")
    public String login(HttpServletResponse response, User user, Model model, HttpSession session) {


        //判断用户名和密码是否正确
        User userFromDb = userService.getUserByUsernamePwd(user);

        if (userFromDb != null) {

            //正确 判断是否记住密码
            if (user.getRemPwd() != null) {
                //是-->  用户名和密码都存到cookie中去

                Cookie cookie = new Cookie(Constant.cookieNamePwd, user.getUsername() + Constant.splitChart + user.getPassword());

                cookie.setMaxAge(604800);

                response.addCookie(cookie);
            } else {
                //否--> 清除cookie
                Cookie cookie = new Cookie(Constant.cookieNamePwd, "");

                cookie.setMaxAge(0);

                response.addCookie(cookie);
            }
            session.removeAttribute("msg");
        } else {
            //密码输入错误 TODO


            session.setAttribute("msg", "密码输入错误");
            Cookie cookie = new Cookie(Constant.cookieNamePwd, "");

            cookie.setMaxAge(0);

            response.addCookie(cookie);

            return "redirect:toLogin";
        }


        return "index";
    }
    /**
     * 业精于勤荒于嬉,行成于思毁于随
     *
     * @Date 2019/4/28 9:31
     * @Created by wuzhuang
     *
     *
    判断用户名和密码是否正确


    正确
    判断是否记住密码

    是-->
    用户名和密码都存到cookie中去

    否-->

    清除cookie

    错误
    提示错误


     *
     *
     *
     */

}
