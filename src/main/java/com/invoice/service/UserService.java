package com.invoice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.invoice.handler.ExceptionResponse;
import com.invoice.model.User;
import com.invoice.repository.UsernamePasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    private UsernamePasswordRepository usernamePasswordRepository;


	@Autowired
	public UserService(UsernamePasswordRepository usernamePasswordRepository){
		this.usernamePasswordRepository = usernamePasswordRepository;
	}

    public User loadUserByUsername(String s) throws UsernameNotFoundException {
        return usernamePasswordRepository.findByUsername(s);
    }

	public User createUser(User usernamePasswordAuth) {
		return usernamePasswordRepository.save(usernamePasswordAuth);
	}

	public String consumerCreateUser(String userJson) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		String response = null;
		try {
			User user = createUser(mapper.readValue(userJson, User.class));
			response = mapper.writeValueAsString(user);
		} catch (Exception e) {
			ExceptionResponse resp = new ExceptionResponse(new Date(), e.getMessage(), e.getClass().getName());
			response = mapper.writeValueAsString(resp);
		}
		return response;
		
	}
	
}
