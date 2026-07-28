package com.mtalaat.restaurant.modules.settings.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "language_translations")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LanguageTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "language_code", nullable = false)
    private Language language;

    @Column(name = "translation_key", nullable = false)
    private String key;

    @Column(name = "translation_value", nullable = false)
    private String value;
}
