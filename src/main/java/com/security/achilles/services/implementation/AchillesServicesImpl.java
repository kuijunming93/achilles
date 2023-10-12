package com.security.achilles.services.implementation;

import com.security.achilles.model.AdminRequest;
import com.security.achilles.model.UserResponse;
import com.security.achilles.model.VersionKey;
import com.security.achilles.services.AchillesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class AchillesServicesImpl implements AchillesServices {

    @Autowired
    VersionKey versionKey;

    public boolean authenticateUser(String versionId, UserResponse response){
        try{
            String decoded = new String(Base64.getDecoder().decode(versionId));
            String received = versionKey.getMemory().get(decoded);
            if (received != null){
                String encoded = Base64.getEncoder().encodeToString(received.getBytes());
                response.setKey(encoded);
                response.setState(true);
                return true;
            } else throw new NullPointerException("Invalid version ID");
        } catch (Exception e){
            response.setState(false);
            response.setKey("Invalid version ID");
            return false;
        }
    }


    public boolean updateSystemVersionId(AdminRequest request, UserResponse response){
        try {
            String versionId = request.getVersionId();
            String decoder = request.getDecoder();
            if (!versionKey.getMemory().containsKey(versionId)) {
                versionKey.getMemory().put(versionId, decoder);
                String encoded = Base64.getEncoder().encodeToString(versionId.getBytes());
                response.setKey("Completed - Encoded Base64: " + encoded);
            } else {
                response.setKey("Already exists");
            }
            response.setState(true);
            return true;
        } catch (Exception e){
            response.setState(false);
            return false;
        }
    }

    public boolean logAssetsService(UserResponse response){
        response.setState(true);
        response.setKey("Logged");
        System.out.println("Assets logging requested: " + LocalDateTime.now());
        System.out.println("VersionId : Decoder");
        versionKey.getMemory().forEach((key, value) -> System.out.println(key + " : " + value));
        return true;
    }

}
