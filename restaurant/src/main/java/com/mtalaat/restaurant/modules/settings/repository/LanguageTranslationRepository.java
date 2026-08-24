package com.mtalaat.restaurant.modules.settings.repository;

import com.mtalaat.restaurant.modules.settings.entity.LanguageTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LanguageTranslationRepository extends JpaRepository<LanguageTranslation, Long> {

    @Query("SELECT lt.value FROM LanguageTranslation lt WHERE lt.key = :key AND lt.language.languageCode = :languageCode")
    Optional<String> findValueByKeyAndLanguageCode(
            @Param("key") String key,
            @Param("languageCode") String languageCode
    );


    //find All By Language Code
    @Query("SELECT lt FROM LanguageTranslation lt WHERE lt.language.languageCode = :languageCode")
    List<LanguageTranslation> findAllByLanguageCode(
            @Param("languageCode") String languageCode
    );
}
