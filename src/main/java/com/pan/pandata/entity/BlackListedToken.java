package com.pan.pandata.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;

@Entity

@RequiredArgsConstructor
public class BlackListedToken {
	@Id
String token;
}
