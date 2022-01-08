package com.example.gymbuddy;

public class Workout {

    String category;
    String name;
    String reps;
    String link;
    int rating;
    int numOfRaters;

    public Workout() {
    }

    public Workout(String name, String category, String reps) {
        this.category = category;
        this.name = name;
        this.reps = reps;
        this.link = "hello world";

        this.rating = 0;
        this.numOfRaters = 0;
    }

    public String getReps() {
        return reps;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getNumOfRaters() {
        return numOfRaters;
    }

    public void setNumOfRaters(int numOfRaters) {
        this.numOfRaters = numOfRaters;
    }

    public int getNewRating(int rating){
        rating = ((this.rating * this.numOfRaters) + rating) / (this.numOfRaters + 1);
        return rating;
    }
}
