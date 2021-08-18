package hipo.pictureboard.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
@RequiredArgsConstructor
public class ImgController {

    @Value("${file.img}")
    private String fileImg;

    @Value("${file.profile}")
    private String fileProfile;

    @Value("${file.picture}")
    private String filePicture;

    @GetMapping("/img/{imgName}")
    public Resource getImg(@PathVariable String imgName) throws MalformedURLException {
        return new UrlResource("file:" + fileImg + imgName);
    }

    @GetMapping("/profile/{profileName}")
    public Resource getProfile(@PathVariable String profileName) throws MalformedURLException {
        return new UrlResource("file:" + fileProfile + profileName);
    }

    @GetMapping("/picture/{pictureName}")
    public Resource getPicture(@PathVariable String pictureName) throws MalformedURLException {
        return new UrlResource("file:" + filePicture + pictureName);
    }
}
