//package com.sky.service.impl;
//import com.sky.Detail.LoginEmployee;
//import com.sky.constant.MessageConstant;
//import com.sky.entity.Employee;
//import com.sky.exception.AccountNotFoundException;
//import com.sky.mapper.EmployeeMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class UserDetailServiceImpl implements UserDetailsService {
//    @Autowired
//    private EmployeeMapper employeeMapper;
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        //查询用户信息
//        Employee employee = employeeMapper.getByUsername(username);
//
//        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
//        if (employee == null) {
//            //账号不存在
//            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
//        }
//        //查询对应的权限信息
//        return new LoginEmployee(employee);
//    }
//}
