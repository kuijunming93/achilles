package com.security.achilles.rest;

import com.security.achilles.config.AppConfiguration;
import com.security.achilles.model.AdminRequest;
import com.security.achilles.model.UserRequest;
import com.security.achilles.model.UserResponse;
import com.security.achilles.services.AchillesServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

//@org.springframework.web.bind.annotation.RestController
@Controller
@RequestMapping("/achilles")
public class RestController {

    @Autowired
    AppConfiguration appConfiguration;

    @Autowired
    AchillesServices achillesServices;


    @PostMapping("/authenticate")
    public ResponseEntity<UserResponse> authenticate(@RequestBody UserRequest request, @RequestHeader Map<String,Object> headers) {
        String userSecret = headers.get("secret").toString();
        UserResponse response = new UserResponse();
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

    @PostMapping("/update")
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

}
