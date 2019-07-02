package com.eos.sms.service;

import com.eos.sms.Session;
import com.eos.sms.entity.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentInfoService {

    private static final Logger logger = LoggerFactory.getLogger(StudentInfoService.class);
    private JdbcUtils jdbcUtils;

    public StudentInfoService() {
        jdbcUtils = new JdbcUtils();
        jdbcUtils.getConnection();
    }

    public boolean saveStudentInfo(Student student) {
        logger.debug("修改前的参数", student);
        String sql = "update student set age = ? ,sex = ? ,birthday = ?, address = ? , phone = ?," +
                " email = ?,studentClass = ? where studentCode = ? ";
        List<Object> params = new ArrayList<>();
        params.add(student.getAge());
        params.add(student.getSex());
        params.add(student.getBirthday());
        params.add(student.getAddress());
        params.add(student.getPhone());
        params.add(student.getEmail());
        params.add(student.getStudentClass());
        params.add(student.getStudentCode());

        boolean flag = false;
        try {
            flag = jdbcUtils.updateByPreparedStatement(sql, params);
            if (flag == true) {
                Student newStudent =
                        findStudentInfoByUsername(((Student) Session.userInfo).getUsername());
                Session.userInfo = newStudent;
                logger.debug("修改后的值", newStudent);
            }
        } catch (SQLException e) {
            logger.error("saveStudentInfo", e);
        }
        return flag;

    }

    public Student findStudentInfoByUsername(String username) {
        String sql = "select * from student where username = ?";
        List<Object> params = new ArrayList<>();
        params.add(username);
        Student student = null;
        try {
            student = jdbcUtils.findSimpleRefResult(sql, params, Student.class);
            if (student != null) {
                // 更新session中的对象
                Session.userInfo = student;
            }
        } catch (Exception e) {
            logger.error("findStudentInfoByUsername", e);
        }
        return student;
    }

    /**
     * 获得所有学生信息
     *
     * @return
     */
    public List<Map<String, Object>> getAllStudentsInfo() {
        String sql = "select * from student";
        List<Map<String, Object>> list = new ArrayList<>();

        try {
            list = jdbcUtils.findModeResult(sql, null);
        } catch (SQLException e) {
            logger.error("getAllStudentsInfo", e);
        }
        return list;
    }


    public boolean deleteStudentById(int id) {
        String sql = "delete from student where id = ?";
        List<Object> params = new ArrayList<>();
        params.add(id);
        boolean flag = false;
        try {
            flag = jdbcUtils.updateByPreparedStatement(sql, params);
        } catch (SQLException e) {
            logger.error("deleteStudentById", e);
        }
        return flag;
    }

    public boolean saveStudentInfoByMap(Map<String, Object> map) {
        String sql = "";
        if (map.containsKey("id")) {
            sql = "update student set username = ?,password = ?,studentCode = ? ,name = ?, " +
                    "studentClass = ? , age = ?,sex = ?,birthday = ?,address = ?,phone = ? ," +
                    "email = ? where id = ?";
        } else {
            sql = "insert into student(username,password,studentCode,name,studentClass,age,sex," +
                    "birthday,address,phone,email) values (?,?,?,?,?,?,?,?,?,?,?)";
        }
        List<Object> params = new ArrayList<>();
        params.add(map.get("username"));
        params.add(map.get("password"));
        params.add(map.get("studentCode"));
        params.add(map.get("name"));
        params.add(map.get("studentClass"));
        params.add(Integer.parseInt(map.get("age").toString().trim()));
        params.add(map.get("sex"));
        params.add(map.get("birthday"));
        params.add(map.get("address"));
        params.add(map.get("phone"));
        params.add(map.get("email"));
        if (map.containsKey("id")) {
            params.add(map.get("id"));
        }
        boolean flag = false;
        try {
            flag = jdbcUtils.updateByPreparedStatement(sql, params);
        } catch (SQLException e) {
            logger.error("saveStudentInfoByMap", e);
        }
        return flag;

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (jdbcUtils != null) {
            jdbcUtils.releaseConn();
            jdbcUtils = null;
        }
        logger.debug("destroy", this.getClass().toString());
    }
}
