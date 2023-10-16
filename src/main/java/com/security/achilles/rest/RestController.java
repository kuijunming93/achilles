package com.security.achilles.rest;

import com.security.achilles.config.AppConfiguration;
import com.security.achilles.entity.IPStoreEntity;
import com.security.achilles.model.AdminRequest;
import com.security.achilles.model.AdminResponse;
import com.security.achilles.model.UserRequest;
import com.security.achilles.model.UserResponse;
import com.security.achilles.repository.IPStoreRepository;
import com.security.achilles.services.AchillesServices;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//@org.springframework.web.bind.annotation.RestController
@Controller
@RequestMapping("/achilles")
public class RestController {

    @Autowired
    AppConfiguration appConfiguration;

    @Autowired
    AchillesServices achillesServices;

    @Autowired
    IPStoreRepository ipStoreRepository;


    @PostMapping("/authenticate")
    public ResponseEntity<UserResponse> authenticate(@RequestBody UserRequest request, @RequestHeader Map<String,Object> headers, HttpServletRequest httpServletRequest) {
        String userSecret = headers.get("secret").toString();
        UserResponse response = new UserResponse();
        String userIP = httpServletRequest.getRemoteAddr();

        // Validating user IP
        Optional<IPStoreEntity> enquired = ipStoreRepository.findById(userIP);
        if (enquired.isEmpty()) {
            IPStoreEntity newEntry = new IPStoreEntity(userIP, 1, false);
            ipStoreRepository.save(newEntry);
        } else {
            IPStoreEntity existingUser = enquired.get();
            if (existingUser.getRestricted()) {
                response.setKey("Unauthorized");
                response.setState(false);
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            } else {
                existingUser.setCount(existingUser.getCount()+1);
                ipStoreRepository.save(existingUser);
            }
        }

        // Validating user secret
        if (userSecret.equals(appConfiguration.getSECRET())){
            if (achillesServices.authenticateUser(request.getVersion(), response)){
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        response.setKey("Unauthorized");
        response.setState(false);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/admin/update")
    public ResponseEntity<UserResponse> updateSupportingVersionId(@RequestBody AdminRequest request, @RequestHeader Map<String,Object> headers) {
        String adminSecret = headers.get("secret").toString();
        UserResponse response = new UserResponse();
        response.setKey(null);
        if (adminSecret.equals(appConfiguration.getADMIN_SECRET())){
            if (achillesServices.updateSystemVersionId(request, response)){
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        response.setKey("Unauthorized");
        response.setState(false);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/admin/assets")
    public ResponseEntity<UserResponse> logAssets(@RequestHeader Map<String, Object> headers){
        String adminSecret = headers.get("secret").toString();
        UserResponse response = new UserResponse();
        if (adminSecret.equals(appConfiguration.getADMIN_SECRET())){
            achillesServices.logAssetsService(response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response.setKey("Unauthorized");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/admin/delete")
    public ResponseEntity<UserResponse> deleteVersionId(@RequestBody AdminRequest request, @RequestHeader Map<String,Object> headers) {
        String adminSecret = headers.get("secret").toString();
        UserResponse response = new UserResponse();
        if (adminSecret.equals(appConfiguration.getADMIN_SECRET())){
            if (achillesServices.deleteVersionIdService(request.getVersionId(), response)){
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        response.setKey("Unauthorized");
        response.setState(false);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/admin/purge")
    public ResponseEntity<UserResponse> purge(@RequestHeader Map<String, Object> headers){
        String adminSecret = headers.get("secret").toString();
        UserResponse response = new UserResponse();
        if (adminSecret.equals(appConfiguration.getADMIN_SECRET())){
            achillesServices.purgeService(response);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        response.setKey("Unauthorized");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/admin/ipstore")
    public ResponseEntity<AdminResponse> returnUserIPs(@RequestHeader Map<String, Object> headers){
        String adminSecret = headers.get("secret").toString();
        AdminResponse response = new AdminResponse();
        if (adminSecret.equals(appConfiguration.getADMIN_SECRET())){
            List<IPStoreEntity> addresses = new ArrayList<>();
            ipStoreRepository.findAll().forEach(addresses::add);
            response.setAddresses(addresses);
            response.setTotal(addresses.size());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/admin/block")
    public ResponseEntity<AdminResponse> blockUserIp(@RequestBody AdminRequest request, @RequestHeader Map<String,Object> headers) {
        String adminSecret = headers.get("secret").toString();
        String userIp = request.getUserIp();
        AdminResponse response = new AdminResponse();
        if (adminSecret.equals(appConfiguration.getADMIN_SECRET())){
            Optional<IPStoreEntity> target = ipStoreRepository.findById(userIp);
            if (target.isPresent()) {
                IPStoreEntity blocked = target.get();
                blocked.setRestricted(true);
                ipStoreRepository.save(blocked);
                response.setAddresses(List.of(blocked));
                response.setTotal(1);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/admin/unblock")
    public ResponseEntity<AdminResponse> unblockUserIp(@RequestBody AdminRequest request, @RequestHeader Map<String,Object> headers) {
        String adminSecret = headers.get("secret").toString();
        String userIp = request.getUserIp();
        AdminResponse response = new AdminResponse();
        if (adminSecret.equals(appConfiguration.getADMIN_SECRET())){
            Optional<IPStoreEntity> target = ipStoreRepository.findById(userIp);
            if (target.isPresent()) {
                IPStoreEntity unblocked = target.get();
                unblocked.setRestricted(false);
                ipStoreRepository.save(unblocked);
                response.setAddresses(List.of(unblocked));
                response.setTotal(1);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
