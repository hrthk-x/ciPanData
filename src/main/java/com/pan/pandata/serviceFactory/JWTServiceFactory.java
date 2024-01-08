package com.pan.pandata.serviceFactory;

import com.pan.pandata.service.JwtService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class JWTServiceFactory {
    private static JwtService jS;

    public static JwtService getJwtService(){
        return jS;
    }

}
