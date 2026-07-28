package com.mtalaat.restaurant.modules.settings.repository;

import com.mtalaat.restaurant.modules.settings.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, String> {
    Optional<Language> findByLanguageCode(String code);
}
