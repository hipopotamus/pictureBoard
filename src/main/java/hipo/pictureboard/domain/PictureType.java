package hipo.pictureboard.domain;

public enum PictureType {
    PEOPLE("인물"), SCENERY("풍경"), TRAVEL("여행");

    private final String description;

    PictureType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
