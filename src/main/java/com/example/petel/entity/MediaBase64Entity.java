package com.example.petel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * PETEL_MEDIA_BASE64 表
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PETEL_MEDIA_BASE64")
public class MediaBase64Entity {

    /**
     * 媒體ID (主鍵, varchar2(10))
     */
    @Id
    @Column(name = "ID", length = 10)
    private String id;

    /**
     * Base64 編碼數據 (CLOB)
     */
    @Lob
    @Column(name = "BASE64_DATA", columnDefinition = "CLOB")
    private String base64Data;

    /**
     * Bucket
     */
    @Column(name = "BUCKET", length = 50)
    private String bucket;

    /**
     * 檔案名稱
     */
    @Column(name = "FILE_NAME", length = 50)
    private String fileName;

    /**
     * MIME 類型
     */
    @Column(name = "MIME_TYPE", length = 100)
    private String mimeType;

    /**
     * 檔案大小 (位元組)
     */
    @Column(name = "SIZE_BYTES", precision = 38, scale = 0)
    private Long sizeBytes;

    /**
     * 創建時間
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
