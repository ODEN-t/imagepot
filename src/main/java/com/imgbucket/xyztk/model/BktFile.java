package com.imgbucket.xyztk.model;

import lombok.Data;

import javax.persistence.*;
import java.net.URL;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity(name = "BktFile")
@Table(
        name = "bkt_file",
        uniqueConstraints = {
                @UniqueConstraint(name = "file_key_unique", columnNames = "key")
        }
)
public class BktFile {
    @Id
    @Column(
            name = "file_id",
            updatable = false,
            nullable = false,
            columnDefinition = "UUID"
    )
    private UUID file_id;

    @OneToOne
    @JoinColumn(
            name = "user_id",
            updatable = false,
            nullable = false
    )
    private User user;

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
            name = "tmb_url",
            nullable = false,
            updatable = false,
            columnDefinition = "VARCHAR"
    )
    private URL tmb_url;

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
