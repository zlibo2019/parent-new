package com.weds.bean.license;

import com.alibaba.fastjson.JSONObject;
import com.weds.core.license.LicenseEntity;
import com.weds.core.license.LicenseUtil;
import com.weds.core.license.ServerIDGenerator;
import com.weds.core.utils.coder.AESCoder;
import org.apache.commons.codec.digest.DigestUtils;
import sun.security.krb5.internal.crypto.Aes128;

import javax.annotation.Resource;
import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;

public class LicenseService {
    private static final String PRODUCT_NAME = "WEDS";
    @Resource
    private LicenseParams licenseParams;


    public LicenseEntity parseLicenseEntity() {
        LicenseEntity licenseEntity = null;
        try {
            String[] ids = ServerIDGenerator.make(licenseParams.getProduct());
            if (ids.length > 0) {
                String license = LicenseUtil.checkLicense(licenseParams.getPollCode(), licenseParams.getPubKey());
                licenseEntity = JSONObject.parseObject(license, LicenseEntity.class);
                licenseEntity.setTemp(ids);
            }
        } catch (Exception e) {
            return licenseEntity;
        }
        return licenseEntity;
    }

    public boolean checkRight(String pollCode) {
        try{
            String[] ids = ServerIDGenerator.make(PRODUCT_NAME);
            String machineCode = ids[0];
            String decryptMachineCode = AESCoder.decrypt(pollCode,"1234567890123zlb","0123456789012345");
            return decryptMachineCode.equals(machineCode);
        }
        catch (Exception e){
            return  false;
        }


//        LicenseEntity licenseEntity = parseLicenseEntity();
//        if (licenseEntity != null) {
//            String id = licenseEntity.getId()[0];
//            long current = System.currentTimeMillis();
//            boolean flag1 = licenseParams.getCustomer().equals(licenseEntity.getCustomer());
//            boolean flag2 = Arrays.asList(licenseEntity.getTemp()).contains(id);
//            boolean flag3 = current >= licenseEntity.getStartDate() && current <= licenseEntity.getEndDate();
//            boolean flag4 = licenseParams.getProduct().equals(licenseEntity.getProduct());
//            boolean flag5 = licenseParams.getVersion().equals(licenseEntity.getInfo().getVersion());
//            return flag1 && flag2 && flag3 && flag4 && flag5;
//        } else {
//            return false;
//        }


    }
}
