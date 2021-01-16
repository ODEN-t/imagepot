package com.imagepot.oden.login.repository.jdbc;

import java.sql.Timestamp;
import java.util.Map;

import com.imagepot.oden.login.model.Users;
import com.imagepot.oden.login.repository.UsersDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository("UsersDaoJdbcImpl")
public class UsersDaoJdbcImpl implements UsersDao {
    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Users selectOne(String userId) throws DataAccessException {
        // queryForMap(sql, パラメータ); 1レコードを取得
        // 戻り値は String → カラム名 Object → 値(どんな型にも対応できるためObject)
        // loacl Map<String, Object> map = jdbc.queryForMap("SELECT * FROM mywork.users"
        // + " WHERE user_id = ?", userId);
        Map<String, Object> map = jdbc.queryForMap("SELECT * FROM public.users" + " WHERE user_id = ?", userId);
        // 返却用変数
        Users users = new Users();

        users.setUserId((String) map.get("user_id"));
        users.setUserPass((String) map.get("user_pass"));
        users.setFirstname((String) map.get("firstname"));
        users.setLastname((String) map.get("lastname"));
        users.setCreateDate((Timestamp) map.get("create_date"));
        users.setRecStatus((int) map.get("rec_status"));
        users.setLastAccessDate((Timestamp) map.get("last_access_date"));
        users.setPassed((boolean) map.get("passed"));

        return users;
    }
}
