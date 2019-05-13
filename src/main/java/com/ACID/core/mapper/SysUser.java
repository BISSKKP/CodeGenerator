package com.ACID.core.mapper;

import com.ACID.core.mapper.BasePojo;
import java.io.Serializable;

public class SysUser extends BasePojo implements Serializable {
    /**
     * 姓
     */
    private String name;

    /**
     * 名
     */
    private String firstName;

    /**
     * 性别
     */
    private String sex;

    /**
     * 账户/邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 头像
     */
    private String headImage;

    /**
     * 头像id
     */
    private String headImageId;

    /**
     * 账户状态
     */
    private String status;

	 private String token;	//秘钥

    private static final long serialVersionUID = 1L;

    /**
     * 姓
     * @return name 姓
     */
    public String getName() {
        return name;
    }

    /**
     * 姓
     * @param name 姓
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 名
     * @return first_name 名
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * 名
     * @param firstName 名
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName == null ? null : firstName.trim();
    }

    /**
     * 性别
     * @return sex 性别
     */
    public String getSex() {
        return sex;
    }

    /**
     * 性别
     * @param sex 性别
     */
    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    /**
     * 账户/邮箱
     * @return email 账户/邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 账户/邮箱
     * @param email 账户/邮箱
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 密码
     * @return password 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 密码
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 头像
     * @return head_image 头像
     */
    public String getHeadImage() {
        return headImage;
    }

    /**
     * 头像
     * @param headImage 头像
     */
    public void setHeadImage(String headImage) {
        this.headImage = headImage == null ? null : headImage.trim();
    }

    /**
     * 头像id
     * @return head_image_id 头像id
     */
    public String getHeadImageId() {
        return headImageId;
    }

    /**
     * 头像id
     * @param headImageId 头像id
     */
    public void setHeadImageId(String headImageId) {
        this.headImageId = headImageId == null ? null : headImageId.trim();
    }

    /**
     * 账户状态
     * @return status 账户状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 账户状态
     * @param status 账户状态
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
    
    
    
}