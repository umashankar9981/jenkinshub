package com.lcwd.user.service.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.lcwd.user.service.entities.Hotel;
import com.lcwd.user.service.entities.Rating;
import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.exception.ResourceNotFoundException;
import com.lcwd.user.service.repositories.UserRepository;
import com.lcwd.user.service.services.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository userrepository;
	
	@Autowired
	RestTemplate retTemplet;
	
	private Logger  logger=LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Override
	public User saveUser(User user) {
		// TODO Auto-generated method stub
		String randomUUId=UUID.randomUUID().toString();
		user.setUserid(randomUUId);
		return userrepository.save(user);
	}

	@Override
	public List<User> getAllUser() {
		// TODO Auto-generated method stub
		
		
		return userrepository.findAll();
	}

	@Override
	public User getUserId(String userId) {
		// TODO Auto-generated method stub
		User user=userrepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User with given ID is not found on server !! "+userId));
		//http://localhost:8083/ratings/users/a44665a8-ed9b-478a-a6ef-5cf6ba0bf504
		
		Rating[] forobject=retTemplet.getForObject("http://RATING-SERVICE/ratings/users/"+user.getUserid(), Rating[].class);
		logger.info("{} ",forobject);
		
		List<Rating> rating=Arrays.asList(forobject);
		
		List<Rating> ratingList=rating.stream().map(ratings->{
			System.out.println(ratings.getHotelId());
			ResponseEntity<Hotel> forEntity=retTemplet.getForEntity("http://HOTEL-SERVICE/hotels/"+ratings.getHotelId(), Hotel.class);
		Hotel hotel=forEntity.getBody();
		logger.info("response status code {}",forEntity.getStatusCode());
		ratings.setHotel(hotel);
		return ratings;
		}).collect(Collectors.toList());
		
		
		user.setRating(ratingList);
		return user;
	}

}
