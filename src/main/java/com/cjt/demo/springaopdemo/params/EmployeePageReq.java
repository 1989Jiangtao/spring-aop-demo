package com.cjt.demo.springaopdemo.params;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import java.io.Serializable;

/*****************************************************
 * @package com.cjt.demo.springaopdemo.params
 * @class EmployeePageReq
 * @author caojiantao
 * @datetime 2024/6/21 11:13
 * @describe 雇员列表请求入参
 ****************************************************/
@ApiModel(value = "雇员分页请求入参")
@Data
public class EmployeePageReq implements Serializable {

    @ApiModelProperty(value = "页码",required = true,example = "1")
    @NonNull
    private Integer pageNum;
    @ApiModelProperty(value = "页数",required = true,example = "10")
    @NonNull
    private Integer pageSize;

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
    @ApiModelProperty(value = "雇员地址")
    private String address;

}
