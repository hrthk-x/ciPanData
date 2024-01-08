package com.pan.pandata.repository;

import com.pan.pandata.entity.BlackListedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackListedTokenRepository extends JpaRepository<BlackListedToken, String> {

}
