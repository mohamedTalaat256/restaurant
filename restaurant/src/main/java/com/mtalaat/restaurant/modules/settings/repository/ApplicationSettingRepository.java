package com.mtalaat.restaurant.modules.settings.repository;

import com.mtalaat.restaurant.modules.settings.entity.ApplicationSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationSettingRepository extends JpaRepository<ApplicationSetting, Long> {

    ApplicationSetting findFirstByOrderByIdAsc();
}
