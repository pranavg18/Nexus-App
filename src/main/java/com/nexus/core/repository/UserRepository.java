package com.nexus.core.repository;

import com.nexus.core.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<User> findByUsername(String username) {
        String sql = "SELECT username, password FROM users WHERE username = ?";
        try {
            // queryForObject runs the SQL and maps the result to a User object
            User user = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                User u = new User();
                u.setUsername(rs.getString("username"));
                u.setPassword(rs.getString("password"));
                return u;
            }, username); // username replaces the '?' in the SQL string

            return Optional.ofNullable(user);
        }
        catch (Exception e) {
            return Optional.empty(); // if no user is found, queryForObject throws an exception
        }
    }

    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return (count != null && count > 0);
    }

    public void save(User user) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        jdbcTemplate.update(sql, user.getUsername(), user.getPassword());
    }

    public void deleteByUsername(String username) {
        String sql = "DELETE FROM users WHERE username = ?";
        jdbcTemplate.update(sql, username);
    }
}
