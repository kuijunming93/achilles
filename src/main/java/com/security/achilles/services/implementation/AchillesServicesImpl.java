package com.security.achilles.services.implementation;

import com.security.achilles.model.AdminRequest;
import com.security.achilles.model.UserResponse;
import com.security.achilles.model.VersionKey;
import com.security.achilles.services.AchillesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AchillesServicesImpl implements AchillesServices {

    @Autowired
    VersionKey versionKey;

    public boolean authenticateUser(String versionId, UserResponse response){
        try{
            String received = versionKey.getMemory().get(versionId);
            if (received != null){
                response.setKey(versionKey.getMemory().get(versionId));
                response.setState(true);
                return true;
            } else throw new NullPointerException("Invalid version ID");
        } catch (Exception e){
            response.setState(false);
            response.setKey("Invalid version ID");
            e.printStackTrace();
            return false;
        }
    }


    public boolean updateSystemVersionId(AdminRequest request, UserResponse response){
        try {
            String versionId = request.getVersionId();
            String decoder = request.getDecoder();
            if (!versionKey.getMemory().containsKey(versionId)) {
                versionKey.getMemory().put(versionId, decoder);
                response.setKey("Completed");
            } else {
                response.setKey("Already exists");
            }
            response.setState(true);
            return true;
        } catch (Exception e){
            response.setState(false);
            e.printStackTrace();
            return false;
        }
    }

}
