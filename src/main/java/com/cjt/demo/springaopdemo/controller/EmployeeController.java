package com.cjt.demo.springaopdemo.controller;

import com.cjt.demo.springaopdemo.entity.Employee;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation(value = "通过ID查询雇员信息",notes ="查询雇员信息" )
    @GetMapping(value = "/query-by-id/{id}")
    public Employee queryById(@PathVariable Integer id){
        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("张三");
        employee.setSalary(36897.0d);
        employee.setAddress("北京海淀-建行稻香湖");
        employee.setAge(35);
        return employee;
    }


}
