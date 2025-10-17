package com.example.petel.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * S3 媒體檔案實體 - PETEL_MEDIA_S3 表
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PETEL_MEDIA_S3")
public class MediaS3Entity {

    /**
     * 媒體ID (主鍵，關聯到 PETEL_MEDIA 的 ID, varchar2(10))
     */
    @Id
    @Column(name = "MEDIA_ID", length = 10)
    private String mediaId;

    /**
     * S3 Bucket
     */
    @Column(name = "BUCKET", length = 255)
    private String bucket;

    /**
     * S3 Object Key (檔案路徑)
     */
    @Column(name = "OBJECT_KEY", length = 1500)
    private String objectKey;
}
