package com.imagepot.xyztk.model;

import lombok.Data;

import javax.persistence.*;
import java.net.URL;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity(name = "PotFile")
@Table(
        name = "pot_file",
        uniqueConstraints = {
                @UniqueConstraint(name = "file_key_unique", columnNames = "key")
        }
)
public class PotFile {
    @Id
    @Column(
            name = "file_id",
            updatable = false,
            nullable = false,
            columnDefinition = "UUID"
    )
    private UUID file_id;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            updatable = false,
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_id")
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
            name = "url",
            nullable = false,
            updatable = false,
            columnDefinition = "VARCHAR"
    )
    private URL url;

    @Column(
            name = "name",
            nullable = false,
            updatable = false,
            columnDefinition = "VARCHAR"
    )
    private String name;

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
