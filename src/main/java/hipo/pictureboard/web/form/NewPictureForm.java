package hipo.pictureboard.web.form;

import hipo.pictureboard.domain.Img;
import hipo.pictureboard.domain.PictureType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class NewPictureForm {

    @NotEmpty
    private String title;

    private String content;

    @NotNull
    private PictureType pictureType;

    private MultipartFile pictureFile;
}
