package com.pan.pandata.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pan.pandata.entity.PanDetails;
@Repository
public interface PanDetailsRepository extends JpaRepository<PanDetails, Integer> {
	
	Optional<PanDetails> findByPanNumber(String panNumber);

}
