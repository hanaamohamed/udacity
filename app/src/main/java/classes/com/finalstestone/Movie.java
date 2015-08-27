package classes.com.finalstestone;


public class Movie {
    String name;
    String description;
    String category;
    int img;

    public Movie(String name, int img) {
        this.name = name;
        this.img = img;
    }

    public Movie(String category, String description, int img, String name) {
        this.category = category;
        this.description = description;
        this.img = img;
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
