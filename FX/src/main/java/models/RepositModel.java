package models;

public class RepositModel {
    private int number;
    private String name;
    private String description;
    private int watched;
    private int stars;
    private int forks;
    private String url;

    public RepositModel(int number, String name, String description, int watched, int stars, int forks, String url) {
        this.number = number;
        this.name = name;
        this.description = description;
        this.watched = watched;
        this.stars = stars;
        this.forks = forks;
        this.url = url;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWatched() {
        return watched;
    }

    public void setWatched(int watched) {
        this.watched = watched;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
