package ge.edu.freeuni.android.entertrainment.music.data;

public  class Song  {
    public  String id;
    public  String name;
    public  int rating;
    public  String image;

    private String voted = "null";

    public Song(){}

    public Song(String voted, String id, String name, int rating, String image) {
        this.id = id;
        this.voted = voted;
        this.name = name;
        this.rating = rating;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRating() {
        return rating;
    }

    public String getImage() {
        return image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVoted() {
        return voted;
    }


    public void setVoted(String voted) {
        this.voted = voted;
    }

}