package ua.tremtyachiy.newsukraine.utils;


public class Item {

    public Item(String title, String text, String imageUrl, String imageUrlFull, String urlNews, String company, String timeAgo) {
        this.title = title;
        this.text = text;
        this.imageUrl = imageUrl;
        this.imageUrlFull = imageUrlFull;
        this.urlNews = urlNews;
        this.company = company;
        this.timeAgo = timeAgo;
    }

    public String title;
    public String text;
    public String imageUrl;
    public String imageUrlFull;
    public String urlNews;
    public String company;
    public String timeAgo;
}
