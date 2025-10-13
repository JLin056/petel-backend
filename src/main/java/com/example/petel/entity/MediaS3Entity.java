package com.example.petel.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * S3 ’Ô”HæÔ - PETEL_MEDIA_S3 h
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PETEL_MEDIA_S3")
public class MediaS3Entity {

    /**
     * ’ÔID (;uÜo0 PETEL_MEDIA „ ID)
     */
    @Id
    @Column(name = "MEDIA_ID")
    private Long mediaId;

    /**
     * S3 Bucket 1
     */
    @Column(name = "BUCKET", length = 255)
    private String bucket;

    /**
     * S3 Object Key (”Hï‘)
     */
    @Column(name = "OBJECT_KEY", length = 1500)
    private String objectKey;
}
