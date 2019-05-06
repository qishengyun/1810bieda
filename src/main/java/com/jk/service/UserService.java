package com.jk.service;

import com.jk.bean.TreeBean;
import com.jk.bean.User;
import com.mongodb.gridfs.GridFSDBFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

public interface UserService {
    List<User> findUserById();

    HashMap<String, Object> findUserPage(Integer page, Integer rows, User user);

    List<TreeBean> findTreeList();

    User findProDialogById(Integer id);

    void saveUser(User user);

    void updateUser(User user);

    void delall(Integer[] ids);

    List<TreeBean> findTreeList2();

    GridFSDBFile findImgById(String imgId);

    HashMap<String, Object> uploadImg(MultipartFile file);

    User getUserByUsernamePwd(User user);
}
