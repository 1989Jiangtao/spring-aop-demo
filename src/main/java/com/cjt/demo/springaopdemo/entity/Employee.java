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
    @ApiModelProperty(value = "雇员年龄")
    private Integer age;
    @ApiModelProperty(value = "雇员地址")
    private String address;
    @ApiModelProperty(value = "雇员薪资")
    private Double salary;


}
