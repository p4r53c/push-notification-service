package com.msr.rnip.smev3.push.dao;

import com.msr.rnip.smev3.push.model.PushNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushNotificationDao extends JpaRepository<PushNotification, String> {
}
