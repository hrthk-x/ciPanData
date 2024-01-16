package com.pan.pandata.controller;

import java.util.ArrayList;
import java.util.List;

import com.pan.pandata.service.JwtService;
import com.pan.pandata.serviceFactory.JWTServiceFactory;
import com.pan.pandata.serviceFactory.PanDetailsServiceFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pan.pandata.entity.InputPanDetails;
import com.pan.pandata.entity.LoginData;
import com.pan.pandata.entity.PanDetails;
import com.pan.pandata.entity.ResponsePanDetails;
import com.pan.pandata.service.PanDetailsService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class PanDetailsController {
	
	 private PanDetailsService ps=PanDetailsServiceFactory.getPanDetailsService();
	 private JwtService js= JWTServiceFactory.getJwtService();
	@RequestMapping(value="savePanDetails",method=RequestMethod.POST)
	ResponseEntity<ResponsePanDetails> savePanDetails(@RequestBody InputPanDetails pan){
		PanDetails newPanData=PanDetails.builder().panNumber(pan.getPanNumber()).password(pan.getPassword())
				.firstName(pan.getFirstName()).lastName(pan.getLastName())
				.accounts(new ArrayList<>()).build();
		newPanData=ps.savePanDetails(newPanData);
		ResponsePanDetails rp=ResponsePanDetails.builder()
				.panNumber(newPanData.getPanNumber()).customerId(newPanData.getCustomerId())
				.accounts(newPanData.getAccounts())
				.firstName(newPanData.getFirstName()).lastName(newPanData.getLastName()).build();
		return newPanData!=null?new ResponseEntity<>(rp,HttpStatus.CREATED)
				:new ResponseEntity<>(ResponsePanDetails.builder().build(),HttpStatus.ALREADY_REPORTED);
	}

	@RequestMapping(value="loginToAccountJWT/{customerId}/{password}",method=RequestMethod.GET)
	ResponseEntity<String> loginToAccountJWT(@PathVariable Integer customerId,@PathVariable String password){
		ResponsePanDetails panData=getAccountByLogin(customerId,password);
		if(customerId.equals(panData.getCustomerId())) {
			String token=js.generateToken(panData);
			return new ResponseEntity<>(token,HttpStatus.OK);
		}else {
			return new ResponseEntity<>("",HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value="getPanDataFromJWT/{token}",method=RequestMethod.GET)
	ResponseEntity<ResponsePanDetails> getPanDataFromJWT(@PathVariable("token") String token){
		Integer customerId=js.extractCustomerId(token);
		PanDetails pd=ps.findPanDetailsById(customerId);
		ResponsePanDetails panData=convertPanDetailsToResponsePanDetails(pd);
		if(customerId.equals(panData.getCustomerId())) {
			return new ResponseEntity<>(panData,HttpStatus.OK);

		}else {
			return new ResponseEntity<>(panData,HttpStatus.BAD_REQUEST);
		}

	}
	@RequestMapping(value="getAccountByPanNumber/{panNumber}",method = RequestMethod.GET)
	ResponseEntity<ResponsePanDetails> getPanDetailsByPanNumber(@PathVariable String panNumber){
		PanDetails pd=ps.findPanDetailsByPanNumber(panNumber);
		return pd!=null?new ResponseEntity<>(convertPanDetailsToResponsePanDetails(pd),HttpStatus.OK)
				:new ResponseEntity<>(ResponsePanDetails.builder().build(),HttpStatus.NO_CONTENT);

	}
	@RequestMapping(value="addAccount/{customerId}/{accountNumber}",method=RequestMethod.PUT)
	void addAccount(@PathVariable Integer customerId,@PathVariable Integer accountNumber){
		PanDetails pd=ps.findPanDetailsById(customerId);
		pd.getAccounts().add(accountNumber);
		ps.updatePanDetails(pd);
	}
	ResponsePanDetails getAccountByLogin(Integer customerId,String password){
		PanDetails pd=ps.findPanDetailsById(customerId);
		password=ps.getEncryptedPassword(password);
		if(pd!=null&&pd.getPassword().equals(password)) {
            return convertPanDetailsToResponsePanDetails(pd);
			//ok
		}
		return null;
	}
	ResponsePanDetails convertPanDetailsToResponsePanDetails(PanDetails pd){
		return ResponsePanDetails.builder()
				.panNumber(pd.getPanNumber()).customerId(pd.getCustomerId())
				.accounts(pd.getAccounts())
				.firstName(pd.getFirstName()).lastName(pd.getLastName()).build();
	}
	
	
}
