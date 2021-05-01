package com.imagepot.xyztk.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity(name = "User")
@Table(
        name = "pot_user",
        uniqueConstraints = {
                @UniqueConstraint(name = "user_email_unique", columnNames = "email")
        }
)
public class User {
    @Id
    @SequenceGenerator(
            name = "user_seq",
            sequenceName = "user_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_seq"
    )
    @Column(
            name = "user_id",
            updatable = false
    )
    private long id;

    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "VARCHAR"
    )
    private String name;

    @Column(
            name = "email",
            nullable = false,
            columnDefinition = "VARCHAR"
    )
    private String email;

    @Column(
            name = "password",
            nullable = false,
            columnDefinition = "VARCHAR"
    )
    private String password;

    @Column(
            name = "icon",
            columnDefinition = "BYTEA"
    )
    private byte[] icon;

    @Column(
            name = "role",
            nullable = false,
            columnDefinition = "VARCHAR"
    )
    private String role;

    @Column(
            name = "password_updated_at",
            columnDefinition = "TIMESTAMP"
    )
    private Timestamp passwordUpdatedAt;

    @Column(
            name = "signin_miss_times",
            columnDefinition = "INTEGER"
    )
    private Integer signinMissTimes;

    @Column(
            name = "unlock",
            columnDefinition = "BOOLEAN"
    )
    private boolean unlock;

    @Column(
            name = "enabled",
            columnDefinition = "BOOLEAN"
    )
    private boolean enabled;

    @Column(
            name = "created_at",
            columnDefinition = "TIMESTAMP"
    )
    private Timestamp createdAt;

    @Column(
            name = "updated_at",
            columnDefinition = "TIMESTAMP"
    )
    private Timestamp updatedAt;
}