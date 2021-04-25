package com.imagepot.xyztk.repository;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.imagepot.xyztk.model.AppUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public class LoginUserRepository {

    @Autowired
    private JdbcTemplate jdbc;

    private static final String SELECT_USER_SQL = "SELECT *" + " FROM mywork.pot_user" + " WHERE user_email = ?";

    private static final String SELECT_USER_ROLE_SQL = "SELECT" + " user_role" + " FROM mywork.pot_user"
            + " WHERE user_email = ?";

    public UserDetails selectOne(String email) {
        Map<String, Object> userMap = jdbc.queryForMap(SELECT_USER_SQL, email);
        List<GrantedAuthority> grantedAuthorityList = getRoleList(email);
        AppUserDetails user = buildUserDetails(userMap, grantedAuthorityList);
        return user;
    }

    private List<GrantedAuthority> getRoleList(String email) {
        List<Map<String, Object>> authorityList = jdbc.queryForList(SELECT_USER_ROLE_SQL, email);

        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();

        for (Map<String, Object> map : authorityList) {
            String roleName = (String) map.get("user_role");
            GrantedAuthority authority = new SimpleGrantedAuthority(roleName);
            grantedAuthorityList.add(authority);
        }
        return grantedAuthorityList;

    }

    private AppUserDetails buildUserDetails(Map<String, Object> userMap, List<GrantedAuthority> grantedAuthorityList) {

        String appUserName = (String) userMap.get("user_name");
        String email = (String) userMap.get("user_email");
        String password = (String) userMap.get("user_password");
        Date passUpdateDate = (Date) userMap.get("user_password_updated_at");
        Integer signinMissTimes = (Integer) userMap.get("user_signin_miss_times");
        boolean unlock = (boolean) userMap.get("user_unlock");
        boolean enabled = (boolean) userMap.get("user_enabled");

        StringBuffer icon = new StringBuffer();
        String base64 = new String(Base64.getEncoder().encodeToString((byte[]) userMap.get("user_icon")));
        icon.append("data:image/png;base64,");
        icon.append(base64);

        new AppUserDetails();
        AppUserDetails user = AppUserDetails
                .builder()
                .appUserName(appUserName)
                .email(email)
                .password(password)
                .passUpdateDate(passUpdateDate)
                .signinMissTimes(signinMissTimes)
                .unlock(unlock)
                .enabled(enabled)
                .icon(icon.toString())
                .build();

        return user;

    }

}
