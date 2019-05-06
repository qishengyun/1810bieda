package com.jk.dao;

import com.jk.bean.TreeBean;
import com.jk.bean.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.HashMap;
import java.util.List;

public interface UserMapper {

    List<User> findUserById();
   @Select("select count(*)  from h_user ")
    int findUserCount(User user);


    List<User> findUserPage(HashMap<String, Object> hashMap2);

    @Select("select id,text,url as href,pid from e_tree where pid=#{value} ")

    List<TreeBean> findTreeList(Integer id);
    @Select(" select * from  h_user where  id=#{value} ")
    User findProDialogById(Integer id);

    @Insert("  insert into h_user (name,sex,password,createTime,imgId)values (#{name},#{sex},#{password},#{createTime},#{imgId}) ")
    void saveUser(User user);



    @Update("  update  h_user set  name=#{name},createTime=#{createTime},sex=#{sex},password=#{password},imgId=#{imgId} where id=#{id} ")
    void updateUser(User user);

    void delall(Integer[] ids);

    @Select("select *  from e_tree where pid=#{value} ")
    List<TreeBean> findTreeListBypid(Integer id);

    @Select("select * from h_user where username=#{username} and password=#{password}")
    User getUserByUsernamePwd(User user);
}
