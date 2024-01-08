package com.pan.pandata.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@Data
@AllArgsConstructor
@Builder
public class ResponsePanDetails {

	String panNumber,firstName,lastName;Integer customerId;List<Integer> accounts;
}
