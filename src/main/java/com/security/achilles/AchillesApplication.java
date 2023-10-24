package com.security.achilles;

import com.security.achilles.model.AdminRequest;
import com.security.achilles.model.DefaultLoad;
import com.security.achilles.model.UserResponse;
import com.security.achilles.services.implementation.AchillesServicesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class AchillesApplication {
	@Autowired
	DefaultLoad defaultLoad;

	@Autowired
	AchillesServicesImpl achillesServices;

	public static void main(String[] args) {
		SpringApplication.run(AchillesApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void insertDefaultData(){
		if (!defaultLoad.getVersionId().isEmpty() && !defaultLoad.getDecoder().isEmpty()){
			AdminRequest adminRequest = new AdminRequest();
			adminRequest.setVersionId(defaultLoad.getVersionId());
			adminRequest.setDecoder(defaultLoad.getDecoder());
			if (achillesServices.updateSystemVersionId(adminRequest, new UserResponse())) {
				System.out.println("Preload of default value successful..");
				return;
			}
		}
		System.out.println("Preload of default value unsuccessful..");
	}

}
