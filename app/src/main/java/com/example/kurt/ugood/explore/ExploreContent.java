package com.example.kurt.ugood.explore;

import com.google.firebase.firestore.Exclude;

import java.util.Comparator;
import java.util.Date;

public class ExploreContent {

    private boolean favorite, selected;

    public String content;
    //not to sure on what else this feature should include but i was thinking maybe number of favs and number of crowns
    //u could use theses variables to filter data for user(example user applies filter to see quotes with the most favorites
    public int numberOfFavorites;
    public int numberOfCrowns;

    @Exclude
    public Date dateCreated;

    public String iD;

    public ExploreContent() {
    }

    public ExploreContent(String content, int numberOfFavorites, int numberOfCrowns, Date dateCreated) {
        this.content = content;
        this.numberOfFavorites = numberOfFavorites;
        this.numberOfCrowns = numberOfCrowns;
        this.dateCreated = dateCreated;
    }

    public int getNumberOfFavorites() {
        return numberOfFavorites;
    }

    public void setNumberOfFavorites(int numberOfFavorites) {
        this.numberOfFavorites = numberOfFavorites;
    }

    public int getNumberOfCrowns() {
        return numberOfCrowns;
    }

    public void setNumberOfCrowns(int numberOfCrowns) {
        this.numberOfCrowns = numberOfCrowns;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getiD() {
        return iD;
    }

    public void setiD(String iD) {
        this.iD = iD;
    }


    public ExploreContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

//class ExploreComparator implements Comparator<ExploreContent> {
//    @Override
//    public int compare(ExploreContent notifications, ExploreContent notification2) {
//        return notification2.getiD().compareTo(notifications.getiD());
//    }
//}
