package com.mtalaat.restaurant.modules.settings.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "languages")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Language {

    @Id
    @Column(name = "language_code", nullable = false, unique = true, length = 10)
    private String languageCode;

    @Column(name = "name", nullable = false)
    private String name;
}
