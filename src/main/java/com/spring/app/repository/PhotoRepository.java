package com.spring.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.app.model.Photo;

import jakarta.transaction.Transactional;


@Repository
@Transactional
public interface PhotoRepository extends JpaRepository<Photo, Long> {
	
	public Photo getPhotoById(Long id);
	
	public Photo getPhotoByName(String name);
	
	public void deletePhotoById(Long id);
	
	public void deletePhotoByName(String name);
	
	public void delete(Photo photo);
	
	public List<Photo> findPhotoByUserId(Long id);
	
    public List<Photo> getPhotoByUserId(Long id);
    
	public Photo findPhotoByName(String name);
	
	public Page<Photo> findAll(Pageable pageable);

}
