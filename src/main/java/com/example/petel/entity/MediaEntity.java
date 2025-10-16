package com.example.petel.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 媒體檔案實體 - PETEL_MEDIA 表
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PETEL_MEDIA")
public class MediaEntity{

    /**
     * 媒體ID (主鍵，varchar2(10))
     */
    @Id
    @Column(name = "ID", length = 10)
    private String id;

    /**
     * 儲存類型 (S3, LOCAL, etc.)
     */
    @Column(name = "STORAGE_TYPE", length = 20)
    private String storageType;

    /**
     * MIME 類型 (image/jpeg, image/png, etc.)
     */
    @Column(name = "MIME_TYPE", length = 100)
    private String mimeType;

    /**
     * 檔案大小 (bytes)
     */
    @Column(name = "SIZE_BYTES")
    private Long sizeBytes;

    /**
     * 可見性 (PUBLIC, PRIVATE)
     */
    @Column(name = "VISIBILITY", length = 20)
    private String visibility;

    /**
     * 建立時間
     */
    @CreationTimestamp
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新時間
     */
    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}
