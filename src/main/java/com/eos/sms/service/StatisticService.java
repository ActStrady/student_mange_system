package com.eos.sms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StatisticService {

    private static final Logger logger = LoggerFactory.getLogger(StatisticService.class);
    private JdbcUtils jdbcUtils;

    public StatisticService() {
        jdbcUtils = new JdbcUtils();
        jdbcUtils.getConnection();
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

    public List<Map<String, Object>> getAllCourses() {
        String sql = "select distinct courseName from course";

        List<Map<String, Object>> list = null;
        try {
            list = jdbcUtils.findModeResult(sql, null);
        } catch (SQLException e) {
            logger.error("getAllCourses error", e);
        }
        return list;
    }

    public List<Map<String, Object>> getAllClasses() {
        String sql = "select distinct studentClass from student";
        List<Map<String, Object>> list = null;
        try {
            list = jdbcUtils.findModeResult(sql, null);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("getAllClasses", e);
        }
        return list;
    }

    public List<Map<String, Object>> getAllStudents() {
        String sql = "select studentCode,name from student";
        List<Map<String, Object>> list = null;
        try {
            list = jdbcUtils.findModeResult(sql, null);
        } catch (SQLException e) {
            logger.error("getAllStudents", e);
        }

        return list;
    }


    public List<Map<String, Object>> getAllTimes() {
        String sql = "select distinct academicYear,term from course";
        List<Map<String, Object>> list = null;
        try {
            list = jdbcUtils.findModeResult(sql, null);
        } catch (SQLException e) {
            logger.error("getAllTimes", e);
        }
        return list;
    }

    public List<Map<String, Object>> getCourseStatisticalByCourseName(String courseName) {

        String sql = "SELECT " +
                "  sum(CASE when score<60 then 1 else 0 end)   AS '不及格', " +
                "  sum(CASE when score>=60 and score<70 then 1 else 0 end)   AS '差', " +
                "  sum(CASE when score>=70 and score<80 then 1 else 0 end)   AS '中', " +
                "  sum(CASE when score>=80 and score<90 then 1 else 0 end)   AS '良', " +
                "  sum(CASE when score>=90 and score<=100 then 1 else 0 end)   AS '优' " +
                "from grade as g,course as c where courseId = c.id and courseName = ? ";
        List<Object> params = new ArrayList<>();
        params.add(courseName);
        List<Map<String, Object>> list = null;
        try {
            list = jdbcUtils.findModeResult(sql, params);
        } catch (SQLException e) {
            logger.error("getCourseStatisticalByCourseName", e);
        }
        return list;
    }

    public List<Map<String, Object>> getClassStatisticalByClass(int studentClass) {

        String sql = "SELECT " +
                "  sum(CASE when score<60 then 1 else 0 end)   AS '不及格', " +
                "  sum(CASE when score>=60 and score<70 then 1 else 0 end)   AS '差', " +
                "  sum(CASE when score>=70 and score<80 then 1 else 0 end)   AS '中', " +
                "  sum(CASE when score>=80 and score<90 then 1 else 0 end)   AS '良', " +
                "  sum(CASE when score>=90 and score<=100 then 1 else 0 end)   AS '优' " +
                "from grade as g,student as s where g.studentId = s.id and studentClass = ?";
        List<Object> params = new ArrayList<>();
        params.add(studentClass);
        List<Map<String, Object>> list = null;
        try {
            list = jdbcUtils.findModeResult(sql, params);
        } catch (SQLException e) {
            logger.error("getClassStatisticalByClass", e);
        }
        return list;
    }

    public List<Map<String, Object>> getStudentStatisticalByStudentCode(String studentCode) {

        String sql = "SELECT " +
                "  sum(CASE when score<60 then 1 else 0 end)   AS '不及格', " +
                "  sum(CASE when score>=60 and score<70 then 1 else 0 end)   AS '差', " +
                "  sum(CASE when score>=70 and score<80 then 1 else 0 end)   AS '中', " +
                "  sum(CASE when score>=80 and score<90 then 1 else 0 end)   AS '良', " +
                "  sum(CASE when score>=90 and score<=100 then 1 else 0 end)   AS '优' " +
                "from grade as g,student as s where g.studentId = s.id and studentCode = ?";
        List<Object> params = new ArrayList<>();
        params.add(studentCode);
        List<Map<String, Object>> list = null;
        try {
            list = jdbcUtils.findModeResult(sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("getStudentStatisticalByStudentCode", e);
        }
        return list;
    }

    public List<Map<String, Object>> getTimeStatisticalTime(String academicYear, String term) {

        String sql = "SELECT " +
                "  sum(CASE when score<60 then 1 else 0 end)   AS '不及格', " +
                "  sum(CASE when score>=60 and score<70 then 1 else 0 end)   AS '差', " +
                "  sum(CASE when score>=70 and score<80 then 1 else 0 end)   AS '中', " +
                "  sum(CASE when score>=80 and score<90 then 1 else 0 end)   AS '良', " +
                "  sum(CASE when score>=90 and score<=100 then 1 else 0 end)   AS '优' " +
                "from grade as g,course as c where g.courseId = c.id and academicYear = ? and " +
                "term = ?";
        List<Object> params = new ArrayList<>();
        params.add(academicYear);
        params.add(term);
        List<Map<String, Object>> list = null;
        try {
            list = jdbcUtils.findModeResult(sql, params);
        } catch (SQLException e) {
            logger.error("getTimeStatisticalTime", e);
        }
        return list;
    }


}
