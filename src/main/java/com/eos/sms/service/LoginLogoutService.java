package com.eos.sms.service;

import com.eos.sms.Session;
import com.eos.sms.entity.Admin;
import com.eos.sms.entity.Student;
import com.eos.sms.entity.Teacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LoginLogoutService {
    private static final Logger logger = LoggerFactory.getLogger(LoginLogoutService.class);
    private JdbcUtils jdbcUtils;

    public LoginLogoutService() {
        jdbcUtils = new JdbcUtils();
        jdbcUtils.getConnection();
    }

    /**
     * 登陆
     *
     * @param
     * @return
     */
    public String login(Object object) {

        String resultStr = "登陆失败";
        String username = null;
        String password = null;
        String sql = null;

        if (Admin.class == object.getClass()) {
            Admin admin = (Admin) object;
            username = admin.getUsername();
            password = admin.getPassword();
            sql = "select * from admin where username = ? and password = ?";
            List<Object> params = new ArrayList<>();
            params.add(username);
            params.add(password);
            try {
                Admin databaseAdmin = jdbcUtils.findSimpleRefResult(sql, params, Admin.class);
                if (databaseAdmin != null) {
                    Session.userInfo = databaseAdmin;
                    resultStr = "登陆成功";
                }
            } catch (Exception e) {
                logger.error("findSimpleRefResult", e);
            }
        } else if (Student.class == object.getClass()) {
            Student student = (Student) object;
            username = student.getUsername();
            password = student.getPassword();
            sql = "select * from student where username = ? and password = ?";
            List<Object> params = new ArrayList<>();
            params.add(username);
            params.add(password);
            try {
                Student databaseStudent = jdbcUtils.findSimpleRefResult(sql, params, Student.class);
                if (databaseStudent != null) {
                    Session.userInfo = databaseStudent;
                    resultStr = "登陆成功";
                }
            } catch (Exception e) {
                logger.error("databaseStudentError", e);
            }
        } else if (Teacher.class == object.getClass()) {
            Teacher teacher = (Teacher) object;
            username = teacher.getUsername();
            password = teacher.getPassword();
            sql = "select * from teacher where username = ? and password = ?";
            List<Object> params = new ArrayList<>();
            params.add(username);
            params.add(password);
            try {
                Teacher databaseTeacher = jdbcUtils.findSimpleRefResult(sql, params, Teacher.class);
                if (databaseTeacher != null) {
                    Session.userInfo = databaseTeacher;
                    resultStr = "登陆成功";
                }
            } catch (Exception e) {
                logger.error("databaseTeacher", e);
            }
        } else {

        }
        return resultStr;
    }

    /**
     * 登出
     */
    public void logout() {
        Session.userInfo = null;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (jdbcUtils != null) {
            jdbcUtils.releaseConn();
            jdbcUtils = null;

        }
        logger.error("销毁", this.getClass().toString());
    }
}
