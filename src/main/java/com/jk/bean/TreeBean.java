/** 
 * <pre>项目名称:text 
 * 文件名称:TreeBean.java 
 * 包名:com.jk.swq.pojo 
 * 创建日期:2019年3月18日下午7:25:40 
 * Copyright (c) 2019, yuxy123@gmail.com All Rights Reserved.</pre> 
 */  
package com.jk.bean;

import lombok.Data;

import java.util.List;

/** 
 * <pre>项目名称：text    
 * 类名称：TreeBean    
 * 类描述：    
 * 创建人：戚生运
 * 创建时间：2019年3月18日 下午7:25:40    
 * 修改人：戚生运
 * 修改时间：2019年3月18日 下午7:25:40    
 * 修改备注：       
 * @version </pre>    
 */
@Data
public class TreeBean {
	private Integer id;
	private String text;
	
	private String href;
	
	private Integer pid;
	
	private List<TreeBean>nodes;
	
	private Boolean selectable;


    
}
