package com.cjt.demo.springaopdemo.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/*****************************************************
 * @package com.cjt.demo.springaopdemo.entity
 * @class Employee
 * @author caojiantao
 * @datetime 2024/6/20 16:17
 * @describe 职员实体类
 ****************************************************/
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@ApiModel(value = "雇员信息实体类")
public class Employee implements Serializable {

    @ApiModelProperty(value = "主键ID")
    private Integer id;
    @ApiModelProperty(value = "雇员姓名")
    private String name;
    @ApiModelProperty(value = "性别")
    private String gender;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "年龄")
    private String age;
    @ApiModelProperty(value = "身份证号码")
    private String ssn;
    @ApiModelProperty(value = "雇员薪资")
    private Double salary;
    @ApiModelProperty(value = "雇员地址")
    private String address;
    @ApiModelProperty(value = "创建时间")
    private String createTime;
    @ApiModelProperty(value = "更新时间")
    private String updateTime;

}
