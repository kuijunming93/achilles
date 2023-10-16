package com.security.achilles.services.implementation;

import com.security.achilles.entity.IPStoreEntity;
import com.security.achilles.entity.VersionStoreEntity;
import com.security.achilles.model.AdminRequest;
import com.security.achilles.model.UserResponse;
import com.security.achilles.repository.IPStoreRepository;
import com.security.achilles.repository.VersionStoreRepository;
import com.security.achilles.services.AchillesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
public class AchillesServicesImpl implements AchillesServices {

    @Autowired
    VersionStoreRepository versionStoreRepository;

    public boolean authenticateUser(String versionId, UserResponse response){
        try{
            String decoded = new String(Base64.getDecoder().decode(versionId));
            byte[] received = versionStoreRepository.findById(decoded).get().getDecoder();
            if (received != null){
                String encoded = Base64.getEncoder().encodeToString(received);
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
            String versionId = new String(Base64.getDecoder().decode(request.getVersionId()));
            byte[] decoder = Base64.getDecoder().decode(request.getDecoder());
            if (versionStoreRepository.findById(versionId).isEmpty()) {
                VersionStoreEntity versionStoreEntity = new VersionStoreEntity(versionId, decoder);
                versionStoreRepository.save(versionStoreEntity);
                String encoded = Base64.getEncoder().encodeToString(versionId.getBytes());
                response.setKey("Completed - Version ID added: " + encoded);
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
        List<VersionStoreEntity> data = (List<VersionStoreEntity>) versionStoreRepository.findAll();
        response.setState(true);
        response.setKey("Logged: " + data.size());
        System.out.println("Assets logging requested: " + LocalDateTime.now());
        System.out.println("VersionId : Decoder");
        data.forEach(e -> System.out.println(e.getVersionId().trim() + " : " + new String(e.getDecoder()).trim()));
        return true;
    }

    public boolean deleteVersionIdService(String versionId, UserResponse response){
        try {
            String decoded = new String(Base64.getDecoder().decode(versionId));
            if (!versionStoreRepository.findById(decoded).isEmpty()) {
                versionStoreRepository.deleteById(decoded);
                response.setKey("Deleted - Version ID (Decoded) removed: " + decoded.trim());
            } else {
                response.setKey("Version Id does not exists");
            }
            response.setState(true);
            return true;
        } catch (Exception e){
            response.setState(false);
            return false;
        }
    }

    public boolean purgeService(UserResponse response){
        response.setState(true);
        int count = ((List<VersionStoreEntity>) versionStoreRepository.findAll()).size();
        if (count > 0){
            response.setKey("Success. Total removal count: " + count);
        } else {
            response.setKey("No data persisted in application at the moment");
        }
        versionStoreRepository.deleteAll();
        return true;
    }

}
