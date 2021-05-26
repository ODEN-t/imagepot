package com.imagepot.xyztk.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity(name = "File")
@Table(
        name = "pot_file",
        uniqueConstraints = {
                @UniqueConstraint(name = "file_key_unique", columnNames = "key")
        }
)
public class File {
    @Id
    @Column(
            name = "file_id",
            updatable = false,
            nullable = false,
            columnDefinition = "CHAR"
    )
    private char file_id;

    @ManyToOne
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name="fk_user_id"))
    @Column(
            name = "user_id",
            updatable = false,
            nullable = false
    )
    private User user_id;

    @Column(
            name = "key",
            nullable = false,
            updatable = false,
            columnDefinition = "VARCHAR"
    )
    private String key;

    @Column(
            name = "size",
            nullable = false,
            updatable = false,
            columnDefinition = "NUMERIC"
    )
    private double size;

    @Column(
            name = "type",
            nullable = false,
            updatable = false,
            columnDefinition = "VARCHAR"
    )
    private String type;

    @Column(
            name = "last_modified_at",
            nullable = false,
            columnDefinition = "TIMESTAMP"
    )
    private Timestamp lastModifiedAt;
}
