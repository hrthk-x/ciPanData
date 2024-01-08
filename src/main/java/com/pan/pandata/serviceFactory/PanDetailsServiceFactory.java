package com.pan.pandata.serviceFactory;

import com.pan.pandata.controller.PanDetailsController;
import com.pan.pandata.service.PanDetailsService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PanDetailsServiceFactory {
    private static PanDetailsService pDS;
    public static PanDetailsService getPanDetailsService(){
        return pDS;
    }
}
