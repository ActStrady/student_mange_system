package com.eos.sms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ChooseCourseService {
    private static final Logger logger = LoggerFactory.getLogger(ChooseCourseService.class);
    private JdbcUtils jdbcUtils;

    public ChooseCourseService() {
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
        logger.debug("销毁", this.getClass().toString());
    }

    public List<Map<String, Object>> findAllChooseCourse(int studentId) {
        String sql = "select c.id as id,courseName,academicYear,term,teacherId,name from course " +
                "as c ,teacher as t where c.teacherId = t.id";
        List<Map<String, Object>> list = null;
        try {
            list = jdbcUtils.findModeResult(sql, null);
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> map = list.get(i);
                map.put("hasChoosen", "");
            }
        } catch (SQLException e) {
            logger.error("findAllChooseCourse", e);
        }
        String sql2 = "select * from chooseCourse where studentId = ?";
        List<Object> params = new ArrayList<>();
        params.add(studentId);
        List<Map<String, Object>> chooseList = null;
        try {
            chooseList = jdbcUtils.findModeResult(sql2, params);
        } catch (SQLException e) {

        }
        for (int i = 0; i < chooseList.size(); i++) {
            Map<String, Object> map = chooseList.get(i);
            for (int j = 0; j < list.size(); j++) {
                Map<String, Object> beforeMap = list.get(j);
                if (beforeMap.get("id").equals(map.get("courseId"))) {
                    beforeMap.put("hasChoosen", "已选");
                }
            }
        }
        return list;
    }


    /**
     * 保存选课
     *
     * @param studentId
     * @param courseId
     * @return
     */
    public boolean saveChoosen(int studentId, int courseId) {
        String sql = "insert into chooseCourse(studentId,courseId) values (?,?)";
        List<Object> params = new ArrayList<>();
        params.add(studentId);
        params.add(courseId);

        boolean flag = false;
        try {
            flag = jdbcUtils.updateByPreparedStatement(sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("saveChoosen", e);
        }
        return flag;
    }
}
