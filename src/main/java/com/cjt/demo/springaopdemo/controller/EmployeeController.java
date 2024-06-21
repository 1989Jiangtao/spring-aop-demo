package com.cjt.demo.springaopdemo.controller;

import com.cjt.demo.springaopdemo.entity.Employee;
import com.cjt.demo.springaopdemo.params.EmployeePageReq;
import com.cjt.demo.springaopdemo.service.IEmployeeSV;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/*****************************************************
 * @package com.cjt.demo.springaopdemo.controller
 * @class CompanyController
 * @author caojiantao
 * @datetime 2024/6/20 16:09
 * @describe 职员表现层
 ****************************************************/
@Api(value = "雇佣信息",tags = "雇员信息接口")
@RestController
@RequestMapping(value = "/employee")
public class EmployeeController {

    @Resource
    private IEmployeeSV employeeSV;

    @ApiOperation(value = "通过ID查询雇员信息",notes ="查询雇员信息" )
    @GetMapping(value = "/query-by-id")
    public Employee queryById(@RequestParam(value = "id") Integer id){
        return employeeSV.queryById(id);
    }

    @ApiOperation(value = "条件搜索分页查询雇员信息",notes ="支持条件搜索，分页返回雇员信息" )
    @PostMapping(value = "/query-by-page")
    public PageInfo<Employee> queryByPage(@RequestBody EmployeePageReq pageReq){
        return employeeSV.queryByPage(pageReq);
    }

}
