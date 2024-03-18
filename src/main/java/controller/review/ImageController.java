package controller.review;

import com.dbfp.footprint.dto.review.response.ImageResDto;
import com.dbfp.footprint.service.review.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService){
        this.imageService = imageService;
    }

    @GetMapping("/test")
    public String test(){
        return "test";
    }


    @PostMapping(value = "/images", consumes = "multipart/form-data")
    public ResponseEntity<ImageResDto> imageUpload(@RequestPart("image")MultipartFile image){
        ImageResDto upload = imageService.upload(image);

        return new ResponseEntity<>(upload, HttpStatus.OK);
    }
}
