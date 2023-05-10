package com.devsuperior.movieflix.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.dto.UserDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.repositories.UserRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;


@Service
public class ReviewService {

	@Autowired
	private ReviewRepository repository;
	
	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
    private AuthService authService;

	@Transactional
	public ReviewDTO insert(ReviewDTO dto) {	
		User user = authService.authenticated();
		dto.setUser(new UserDTO(user));
		Review entity = new Review();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ReviewDTO(entity);
	}
	
	
	
	private void copyDtoToEntity(ReviewDTO dto, Review entity) {
		Optional<Movie> movie = movieRepository.findById(dto.getMovieId());
		entity.setMovie(movie.orElseThrow(() -> new ResourceNotFoundException("Entity not found")));
		entity.setText(dto.getText());
		Optional<User> user = userRepository.findById(dto.getUser().getId());
		entity.setUser(user.orElseThrow(() -> new ResourceNotFoundException("Entity not found")));
	}	
}

