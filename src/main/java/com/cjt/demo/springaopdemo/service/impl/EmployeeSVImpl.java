package com.cjt.demo.springaopdemo.service.impl;

import com.cjt.demo.springaopdemo.entity.Employee;
import com.cjt.demo.springaopdemo.mapper.EmployeeMapper;
import com.cjt.demo.springaopdemo.params.EmployeePageReq;
import com.cjt.demo.springaopdemo.service.IEmployeeSV;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

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
        // 使用pageHelper 开启分页查询
        PageHelper.startPage(pageReq.getPageNum(), pageReq.getPageSize());
        // 做模糊查询封装改造，全部右模糊
        pageReq.setName(StringUtils.hasText(pageReq.getName()) ? pageReq.getName() + "%" : pageReq.getName());
        pageReq.setPhone(StringUtils.hasText(pageReq.getPhone()) ? pageReq.getPhone() + "%" : pageReq.getPhone());
        pageReq.setAddress(StringUtils.hasText(pageReq.getAddress()) ? pageReq.getAddress() + "%" : pageReq.getAddress());
        pageReq.setEmail(StringUtils.hasText(pageReq.getEmail()) ? pageReq.getEmail() + "%" : pageReq.getEmail());

        List<Employee> employeeList = employeeMapper.selectByPage(pageReq);
        return new PageInfo<>(employeeList);
    }
}
