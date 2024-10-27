package com.hacken.test_task.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "properties")
@Data
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "key")
    private String key;

    @Column(name = "value")
    private String value;
}
