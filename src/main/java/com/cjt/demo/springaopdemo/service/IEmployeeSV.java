package com.cjt.demo.springaopdemo.service;

import com.cjt.demo.springaopdemo.entity.Employee;
import com.cjt.demo.springaopdemo.params.EmployeePageReq;
import com.github.pagehelper.PageInfo;

/*****************************************************
 * @package com.cjt.demo.springaopdemo.service
 * @class EmployeeSV
 * @author caojiantao
 * @datetime 2024/6/21 11:53
 * @describe 雇员业务处理接口
 ****************************************************/
public interface IEmployeeSV {

    /**
     * 通过ID查询雇员信息
     *
     * @param id 待查询的ID
     * @return 返回雇员信息
     */
    Employee queryById(Integer id);

    /**
     * 分页查询雇员信息，支持条件搜索分页
     *
     * @param pageReq 搜索条件入参
     * @return 返回分页查询结果
     */
    PageInfo<Employee> queryByPage(EmployeePageReq pageReq);

}
