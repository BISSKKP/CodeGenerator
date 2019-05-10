/*
*
* AmStaff.java
* Copyright(C) 2019-2020 ACID
* @date 2019-04-10
*/
package com.code.pojo;

import com.code.core.pojo.BasePojo;
import java.io.Serializable;
import java.util.Date;

public class AmStaff extends BasePojo {
    /**名*/
    private String firstName;

    /**姓*/
    private String lastName;

    /**英文名*/
    private String englishName;

    /**邮件*/
    private String email;

    /**工号*/
    private String businessNo;

    /**职能*/
    private Integer jobTitle;

    /**入职时间*/
    private Date employmentDate;

    /**离职时间*/
    private Date resignationDate;

    /**skyp 账号*/
    private String skype;

    /**性别*/
    private Integer gender;

    /**联系电话*/
    private String tel;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName=firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName=lastName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName=englishName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email=email;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo=businessNo;
    }

    public Integer getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(Integer jobTitle) {
        this.jobTitle=jobTitle;
    }

    public Date getEmploymentDate() {
        return employmentDate;
    }

    public void setEmploymentDate(Date employmentDate) {
        this.employmentDate=employmentDate;
    }

    public Date getResignationDate() {
        return resignationDate;
    }

    public void setResignationDate(Date resignationDate) {
        this.resignationDate=resignationDate;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype=skype;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender=gender;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel=tel;
    }

    public AmStaff() {
        
    }

    public AmStaff(String id) {
        this.id=id;
    }
}