package com.cjt.demo.springaopdemo.mapper;

import com.cjt.demo.springaopdemo.entity.Employee;
import com.cjt.demo.springaopdemo.params.EmployeePageReq;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/*****************************************************
 * @package com.cjt.demo.springaopdemo.mapper
 * @class EmployeeMapper
 * @author caojiantao
 * @datetime 2024/6/21 11:11
 * @describe 雇员信息mapper层
 ****************************************************/
@Repository
public interface EmployeeMapper {

    /**
     * 通过ID查询雇员信息
     *
     * @param id 待查询的ID
     * @return 返回单个雇员信息
     */
    Employee selectById(Integer id);

    /**
     * 分页查询雇员信息
     *
     * @param pageReq 查询入参，支持模糊查询
     * @return
     */
    PageInfo<Employee> selectByPage(@Param("req") EmployeePageReq pageReq);

//    /**
//     * 插入一条雇员信息
//     *
//     * @param employee 待插入的雇员信息
//     * @return 返回插入结果
//     */
//    int insertOne(Employee employee);
//
//    /**
//     * 更新一条雇员信息
//     *
//     * @param employee 待更新的实体
//     * @return 返回更新结果
//     */
//    int updateOne(Employee employee);

}
