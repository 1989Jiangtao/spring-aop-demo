package com.cjt.demo.springaopdemo.service.impl;

import com.cjt.demo.springaopdemo.entity.Employee;
import com.cjt.demo.springaopdemo.mapper.EmployeeMapper;
import com.cjt.demo.springaopdemo.params.EmployeePageReq;
import com.cjt.demo.springaopdemo.service.IEmployeeSV;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/*****************************************************
 * @package com.cjt.demo.springaopdemo.service.impl
 * @class EmployeeSVImpl
 * @author caojiantao
 * @datetime 2024/6/21 11:53
 * @describe 雇员信息业务处理实现类
 ****************************************************/
@Slf4j
@Service
public class EmployeeSVImpl implements IEmployeeSV {

    @Resource
    private EmployeeMapper employeeMapper;


    @Override
    public Employee queryById(Integer id) {
        return employeeMapper.selectById(id);
    }

    @Override
    public PageInfo<Employee> queryByPage(EmployeePageReq pageReq) {
        return employeeMapper.selectByPage(pageReq);
    }
}
