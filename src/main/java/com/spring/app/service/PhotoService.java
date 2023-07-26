package com.spring.app.service;

import java.io.UnsupportedEncodingException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.spring.app.model.Photo;
import com.spring.app.model.User;
import com.spring.app.repository.PhotoRepository;
import com.spring.app.repository.UserRepository;
import com.spring.app.security.WebSecurityConfig;


@Service
public class PhotoService {
	
	@Autowired
	private PhotoRepository photoRepository;
	
	@Autowired
	private WebSecurityConfig webSecurityConfig;
	
	@Autowired
	private UserRepository userRepository;;
	
	
	public void savePhoto(Photo photo) {
		
		photoRepository.save(photo);
		
	}
	
	public boolean checkIfPhotoByNameExists(String name) {
		
		if(photoRepository.findPhotoByName(name) != null) {
			
			return true;
			
		}else
		
			return false;
		
	}
	
	public void savePhoto(byte[] bytes, String name, User user) {
		
		Photo photo = new Photo();
		
		photo.setImageBytes(bytes);
		
		photo.setName(name);
		
		photo.setDateAdded(new Date(System.currentTimeMillis()));
		
		user.getPhotos().add(photo);
		
		photoRepository.save(photo);
		
		userRepository.save(user);
		
		
	}
	
    public Page<String> findPaginated(Pageable pageable , Long id) {
    	
        int pageSize = pageable.getPageSize();
        
        int currentPage = pageable.getPageNumber();
        
        int startItem = currentPage * pageSize;
        
        List<String> photos = new ArrayList<String>();
        
        photos = userGalery(id);
        
        List<String> list;

        if (photos.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, photos.size());
            list = photos.subList(startItem, toIndex);
        }

        Page<String> galleryPage
          = new PageImpl<String>(list, PageRequest.of(currentPage, pageSize), photos.size());

        return galleryPage;
        
    }
    
    public Page<Photo> paginated(Pageable pageable , Long id) {
    	
        int pageSize = pageable.getPageSize();
        
        int currentPage = pageable.getPageNumber();
        
        int startItem = currentPage * pageSize;
        
        List<Photo> photos = new ArrayList<Photo>();
        
        photos = loadUserPhotosByUserId(id);
        
        photos.stream().forEach(photo -> photo.setPhotoData(base64ImageEncoder(photo)));
        
        List<Photo> list;

        if (photos.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, photos.size());
            list = photos.subList(startItem, toIndex);
        }

        Page<Photo> galleryPage
          = new PageImpl<Photo>(list, PageRequest.of(currentPage, pageSize), photos.size());

        return galleryPage;
        
    }
    
    public List<Photo> loadUserPhotosByUserId(Long usedId) {
		
		return userRepository.findById(usedId).getPhotos();
    	   	
    }
    
	
	public Map<String, Photo> loadUserImages() {
		
		Map<String, Photo> images = new HashMap<>();
		
		webSecurityConfig.getCurrentlyLoggedUser().getPhotos().stream().forEach(photo -> images.put(base64ImageEncoder(photo), photo));
		
		return images;
		
	}
	
	public List<String> loadUserImagesAsString() {
		
		
		List<String> photos = new ArrayList<String>();
		
		webSecurityConfig.getCurrentlyLoggedUser().getPhotos().stream().forEach(photo -> photos.add(base64ImageEncoder(photo)));
		
		return photos;
	}
	
	public String base64ImageEncoder(Photo photo) {
		
		String encodedString = "";
		
		if(photo == null) {
			
			return encodedString = "";
			
		}
		
		byte[] toEncode = photo.getImageBytes();
		byte[] bytes = Base64.getEncoder().encode(toEncode);
		
	    encodedString = "";
		try {
			encodedString = new String(bytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return encodedString;
		
	}
	
	public List<String> userGalery(Long id) {
		
		List<String> toReturn = new ArrayList<String>();
		
		userRepository.findById(id).getPhotos().stream().forEach(photo -> toReturn.add(base64ImageEncoder(photo)));
		
		return toReturn;
		
	}
	
	
	public void deletePhoto(Long id) {
		
		webSecurityConfig.getCurrentlyLoggedUser().getPhotos().remove(photoRepository.getPhotoById(id));
		photoRepository.delete(photoRepository.getPhotoById(id));
		
		
	}
	
	public void changePhotoTitle(Long id, String title) {
		
		Photo photo = photoRepository.getPhotoById(id);
		
		photo.setName(title);
		
		photoRepository.save(photo);
	}
	
	public void selectAvatar(Long id) {
		
		webSecurityConfig.getCurrentlyLoggedUser().getPhotos().stream().forEach(photo -> photo.setAvatar(false));			
		
		Photo photo = photoRepository.getPhotoById(id);
		
		photo.setAvatar(true);
		
		photoRepository.save(photo);
		
	}
	
//	public List<String> loadFriendsPhotos() {
//		
//		User user = webSecurityConfig.getCurrentlyLoggedUser();
//		
//		List<String> friendPhotos = new ArrayList<>();		
//		
//		for(User friend : user.getFriends()) {
//			
//			friendPhotos.add(loadAvatarPhoto(friend.getId()));
//			
//		}
//		
//		
//		return null;
//		
//	}
	
	public String loadAvatarPhoto(Long id) {
		
		if(id == null) {
			
			return null;
			
		}
		
		for(Photo photo : userRepository.findUserById(id).getPhotos()) {
			
			if(photo.isAvatar()) {
				
				return base64ImageEncoder(photo);
				
			}
			
			
		}
				
		return null;
	
	}
	
	public Map<User,String> loadFriendsListWithData() {
									
		Map<User,String> map = new HashMap<>(); 
		 
		
		webSecurityConfig.getCurrentlyLoggedUser()
		.getFriends()
		.stream()
		.forEach(friend -> map.put(friend, base64ImageEncoder(friend.getPhotos()
				.stream()
				.filter(photo -> photo.isAvatar())
				.findAny()
				.orElse(null))));
		
		Collator englishCollator = Collator.getInstance(new Locale("UK"));
		
		Comparator<User> comparator = (User u1, User u2) -> {
			return englishCollator.compare(u1.getName() , u2.getName());
	    };

	    Map<User,String> sorted = new TreeMap<>(comparator);

	    sorted.putAll(map);

		return sorted;
		
	}
	
	public Map<String, Photo> loadPhotoGalleryByUserId(Long userId) {
				
		Map<String, Photo> images = new HashMap<>();
		
		userRepository.findUserById(userId).getPhotos().stream().forEach(photo -> images.put(base64ImageEncoder(photo), photo));
		
		return images;
		 
	}
	
	
//	public Image loadImageByPhoto(Photo photo) {
//		
//		Image image = null;
//		
//		try {
//			byte[] bytes = photo.getImageBytes();
//
//			InputStream in = new ByteArrayInputStream(bytes);
//			
//			image = ImageIO.read(in);
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return image;
//	}
	
	

}
