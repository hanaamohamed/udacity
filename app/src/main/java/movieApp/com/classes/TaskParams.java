package movieApp.com.classes;

/**
 * Created by hanaa mohamed on 06-Sep-15.
 */
public class TaskParams {
    public String urlMovie;
    public String urlVideo;
    public String urlReview;

    public TaskParams(String urlMovie, String urlReview, String urlVideo) {
        this.urlMovie = urlMovie;
        this.urlReview = urlReview;
        this.urlVideo = urlVideo;
    }
}
