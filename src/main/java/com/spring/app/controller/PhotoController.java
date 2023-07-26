package com.spring.app.controller;


import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.spring.app.model.Photo;
import com.spring.app.service.PhotoService;
import com.spring.app.util.PagePagination;



@Controller
public class PhotoController {
	
	@Autowired
	private PhotoService photoService;
		
//	@Autowired
//	private PhotoRepository photoRepository;
	
	
	
//	@GetMapping("/photoGallery/{id}")
	@RequestMapping(value = "/photoGallery/{id}", method = RequestMethod.GET)
	public String loadPhotoGallery(@PathVariable("id") Long id, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size, Model model) {
		
        int currentPage = page.orElse(0);
        int pageSize = size.orElse(6); 
        
        String pageId = String.valueOf(id);
             
        Page<Photo> photos = photoService.paginated(PageRequest.of(currentPage, pageSize), id);

        PagePagination<Photo> pagePagination = new PagePagination<Photo>(photos, "/photoGallery/" + pageId);     
        
        model.addAttribute("page", pagePagination);
        
        model.addAttribute("photosGallery", photos);
        
//        int totalPages = photos.getTotalPages();
//        if (totalPages > 0) {
//            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
//                .boxed()
//                .collect(Collectors.toList());
//            model.addAttribute("pageNumbers", pageNumbers);
//        }
        
        
//		model.addAttribute("photosGalleryMap", photoService.loadPhotoGalleryByUserId(id));
		
		
		return "photo-gallery";
		
		
	}
	
	
	
	@GetMapping("/photos")
	public String loadPhotosPage(Model model) {
		
		
		model.addAttribute("images", photoService.loadUserImages());
		
		return "photos";
		
	}
	
	@GetMapping("/deletePhoto/{id}")
	public String deletePhoto(@PathVariable("id") Long id, Photo photo, Model model) {
			
			photoService.deletePhoto(id);
			
			model.addAttribute("images", photoService.loadUserImages());
		
		return "redirect:/photos";
		
	}
	
	@PostMapping("/changePhotoTitle/{id}")
	public String changePhotoTitle(@PathVariable("id") Long id, String title, Model model) {
			System.out.println(title);
			photoService.changePhotoTitle(id, title);
		
			model.addAttribute("images", photoService.loadUserImages());
			
		return "redirect:/photos";
	}

	@GetMapping("/selectAvatar/{id}")
	public String selectAvatar(@PathVariable("id") Long id, Model model) {
		
		photoService.selectAvatar(id);

		model.addAttribute("images", photoService.loadUserImages());
		
		return "redirect:/photos";
		
	}
	

}
