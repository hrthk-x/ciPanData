package com.pan.pandata.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PanDetails {

    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Integer customerId;

	String panNumber;
	List<Integer> accounts;
	String password,firstName,lastName;
}
