package com.jk.service;

import com.jk.bean.TreeBean;
import com.jk.bean.User;
import com.jk.dao.UserMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;


    @Autowired
    MongoTemplate mongoTemplate;



    @Override
    public List<User> findUserById() {
        return userMapper.findUserById();
    }

    /*查询*/
    @Override
    public HashMap<String, Object> findUserPage(Integer page, Integer rows, User user) {
        HashMap<String, Object> hashMap = new HashMap<>();

        HashMap<String, Object> hashMap2 = new HashMap<>();
        //查询总条数
        hashMap2.put("user", user);
        int total = userMapper.findUserCount(user);
        //分页查询
        int start = (page-1)*rows;//开始条数

        hashMap2.put("start",start);
        hashMap2.put("rows",rows);

        List<User> list = userMapper.findUserPage(hashMap2);
        hashMap.put("total", total);
        hashMap.put("rows", list);
        return hashMap;
    }




    @Override
    public User findProDialogById(Integer id) {
        return userMapper.findProDialogById(id);
    }

    @Override
    public void saveUser(User user) {
        userMapper.saveUser(user);
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateUser(user);
    }

    @Override
    public void delall(Integer[] ids) {
        userMapper.delall(ids);
    }

    @Override
    public List<TreeBean> findTreeList2() {
        Integer  id=0;
        List<TreeBean> node = getNode(id);

        return node;
    }



    @Override
    public HashMap<String, Object> uploadImg(MultipartFile file) {
        GridFS gridFS=new GridFS(mongoTemplate.getDb());
        String id= UUID.randomUUID().toString();
        String filename = file.getOriginalFilename();
        HashMap<String, Object> map=new HashMap<>();
        try {
            GridFSInputFile createFile = gridFS.createFile(file.getInputStream());
            createFile.setId(id);
            createFile.setFilename(filename);
            createFile.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.put("imgId", id);
        map.put("fileName", filename);
        return map;
    }

    @Override
    public User getUserByUsernamePwd(User user) {
        return userMapper.getUserByUsernamePwd(user);
    }

    @Override
    public GridFSDBFile findImgById(String imgId) {
        DBObject query  = new BasicDBObject("_id", imgId);
        GridFS gridFS=new GridFS(mongoTemplate.getDb());
        GridFSDBFile findOne = gridFS.findOne(query);
        return findOne;
    }


    private List<TreeBean> getNode(Integer id) {
        List<TreeBean> findTreeListBypid = userMapper.findTreeListBypid(id);
        for (TreeBean treeBean : findTreeListBypid) {
            Integer id2 = treeBean.getId();
            List<TreeBean>nodes=getNode(id2);
            treeBean.setNodes(nodes);
        }
        return findTreeListBypid;
    }


    @Override
    public List<TreeBean> findTreeList() {
        Integer id=0;
        return   queryTree(id);
    }

    private List<TreeBean> queryTree(Integer id) {
        List<TreeBean>trees=userMapper.findTreeList(id);
        for (TreeBean treeBean : trees) {
            List<TreeBean>queryTree= queryTree(treeBean.getId());
            if(queryTree!=null && queryTree.size()>0) {
                treeBean.setNodes(queryTree);
                treeBean.setSelectable(false);
            }else {
                treeBean.setSelectable(true);
            }
        }
        return trees;
    }
}
