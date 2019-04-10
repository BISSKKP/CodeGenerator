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

public class AmStaff extends BasePojo implements Serializable {
    /**
     * 名
     */
    private String firstName;

    /**
     * 姓
     */
    private String lastName;

    /**
     * 英文名
     */
    private String englishName;

    /**
     * 邮件
     */
    private String email;

    /**
     * 工号
     */
    private String businessNo;

    /**
     * 职能
     */
    private Integer jobTitle;

    /**
     * 入职时间
     */
    private Date employmentDate;

    /**
     * 离职时间
     */
    private Date resignationDate;

    /**
     * skyp 账号
     */
    private String skype;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 联系电话
     */
    private String tel;

    private static final long serialVersionUID = 1L;

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
     * 姓
     * @return last_name 姓
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * 姓
     * @param lastName 姓
     */
    public void setLastName(String lastName) {
        this.lastName = lastName == null ? null : lastName.trim();
    }

    /**
     * 英文名
     * @return english_name 英文名
     */
    public String getEnglishName() {
        return englishName;
    }

    /**
     * 英文名
     * @param englishName 英文名
     */
    public void setEnglishName(String englishName) {
        this.englishName = englishName == null ? null : englishName.trim();
    }

    /**
     * 邮件
     * @return email 邮件
     */
    public String getEmail() {
        return email;
    }

    /**
     * 邮件
     * @param email 邮件
     */
    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    /**
     * 工号
     * @return business_no 工号
     */
    public String getBusinessNo() {
        return businessNo;
    }

    /**
     * 工号
     * @param businessNo 工号
     */
    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo == null ? null : businessNo.trim();
    }

    /**
     * 职能
     * @return job_title 职能
     */
    public Integer getJobTitle() {
        return jobTitle;
    }

    /**
     * 职能
     * @param jobTitle 职能
     */
    public void setJobTitle(Integer jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     * 入职时间
     * @return employment_date 入职时间
     */
    public Date getEmploymentDate() {
        return employmentDate;
    }

    /**
     * 入职时间
     * @param employmentDate 入职时间
     */
    public void setEmploymentDate(Date employmentDate) {
        this.employmentDate = employmentDate;
    }

    /**
     * 离职时间
     * @return resignation_date 离职时间
     */
    public Date getResignationDate() {
        return resignationDate;
    }

    /**
     * 离职时间
     * @param resignationDate 离职时间
     */
    public void setResignationDate(Date resignationDate) {
        this.resignationDate = resignationDate;
    }

    /**
     * skyp 账号
     * @return skype skyp 账号
     */
    public String getSkype() {
        return skype;
    }

    /**
     * skyp 账号
     * @param skype skyp 账号
     */
    public void setSkype(String skype) {
        this.skype = skype == null ? null : skype.trim();
    }

    /**
     * 性别
     * @return gender 性别
     */
    public Integer getGender() {
        return gender;
    }

    /**
     * 性别
     * @param gender 性别
     */
    public void setGender(Integer gender) {
        this.gender = gender;
    }

    /**
     * 联系电话
     * @return tel 联系电话
     */
    public String getTel() {
        return tel;
    }

    /**
     * 联系电话
     * @param tel 联系电话
     */
    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }
}