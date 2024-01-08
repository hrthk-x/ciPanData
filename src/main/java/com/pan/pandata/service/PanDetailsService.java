package com.pan.pandata.service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pan.pandata.entity.PanDetails;
import com.pan.pandata.repository.PanDetailsRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PanDetailsService {

	private PanDetailsRepository pr;
	
	public PanDetails savePanDetails(PanDetails pd){
		if(pr.findByPanNumber(pd.getPanNumber()).isPresent()) {
			return null;
		}else {
				pd.setPassword(getEncryptedPassword(pd.getPassword()));
			
			return pr.save(pd);
		}
	}
	public List<PanDetails> getALlPanDetails(){
		return pr.findAll();
	}
	public PanDetails updatePanDetails(PanDetails pd) {
		return pr.save(pd);
	}
	public PanDetails findPanDetailsById(Integer customerId) {
		Optional<PanDetails> pan=pr.findById(customerId);
		return pan.isPresent()?pan.get():null;
	}
	public PanDetails findPanDetailsByPanNumber(String panNumber) {
		Optional<PanDetails> pan=pr.findByPanNumber(panNumber);
		return pan.isPresent()?pan.get():null;
	}
	public String deletePanDetails(Integer customerId) {
		Optional<PanDetails> pan=pr.findById(customerId);
		if(pan.isPresent()) {
			pr.delete(pan.get());
			return "Deleted Successfully";
		}
		return "Record Not Found";
	}
	public static String toHexString(byte[] hash)  
    {  
        BigInteger number = new BigInteger(1, hash);  
        StringBuilder hexString = new StringBuilder(number.toString(16));  
        while (hexString.length() < 32)  
        {  
            hexString.insert(0, '0');  
        }  
        return hexString.toString();  
    }  

	public static byte[] getSHA(String input) throws NoSuchAlgorithmException  
	    {  
	        /* MessageDigest instance for hashing using SHA256 */  
	        MessageDigest md = MessageDigest.getInstance("SHA-256");  
	  
	        /* digest() method called to calculate message digest of an input and return array of byte */  
	        return md.digest(input.getBytes(StandardCharsets.UTF_8));  
	    }
	public String getEncryptedPassword(String password) {
		
		try {
			return toHexString(getSHA(password));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}  
	
}
