package com.example.petel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PETEL_LICENSES")
@Getter
@Setter
public class LicenseEntity {

    @Id
    @Column(name = "ID", nullable = false)
    private String id;

    @Column(name = "NAME", nullable = false)
    private String name; // 業者名稱

    @Column(name = "BUSINESS_CODE", nullable = false)
    private String businessCode; // 特寵業編號（原本的 LICENSE_NO）
}
