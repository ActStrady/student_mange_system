package com.eos.sms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GradeService {
    private static final Logger logger = LoggerFactory.getLogger(GradeService.class);
    private JdbcUtils jdbcUtils;

    public GradeService() {
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
        logger.debug("销毁了", this.getClass().toString());
    }

    public boolean saveGradeToDraft(Map<String, Object> gradeMap) {
        if (gradeMap.get("score") == null) {
            return false;
        }
        String sql = "insert into gradeDraft(courseId,studentId,score) values(?,?,?)";
        List<Object> params = new ArrayList<>();
        params.add(gradeMap.get("courseId"));
        params.add(gradeMap.get("id"));
        params.add(gradeMap.get("score"));
        boolean flag = false;
        try {
            flag = jdbcUtils.updateByPreparedStatement(sql, params);
        } catch (SQLException e) {
            logger.error("saveGradeToDraft", e);
        }
        return flag;

    }

    public boolean saveGrade(Map<String, Object> gradeMap) {
        String sql = "insert into grade(courseId,studentId,score) values(?,?,?)";
        List<Object> params = new ArrayList<>();
        params.add(gradeMap.get("courseId"));
        params.add(gradeMap.get("id"));
        params.add(gradeMap.get("score"));
        boolean flag = false;
        try {
            flag = jdbcUtils.updateByPreparedStatement(sql, params);
        } catch (SQLException e) {
            logger.error("saveGrade", e);
            e.printStackTrace();
        }
        return flag;
    }

    public List<Map<String, Object>> getGradesByStudentId(int studentId) {


        String sql = "select c.id as id, courseName,academicYear,term,name,score from course as c, " +
                "grade as g,teacher as t where g.studentId = ? and g.courseId = c.id and c.teacherId = t.id ";
        List<Object> params = new ArrayList<>();
        params.add(studentId);
        List<Map<String, Object>> list = new ArrayList<>();

        try {
            list = jdbcUtils.findModeResult(sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("getGradesByStudentId", e);
        }
        return list;
    }



}
