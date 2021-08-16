package hipo.pictureboard.domain;

public enum PictureType {
    PEOPLE("인물"), SCENERY("풍경"), TRAVLE("여행");

    private String descreption;

    PictureType(String descreption) {
        this.descreption = descreption;
    }
}
