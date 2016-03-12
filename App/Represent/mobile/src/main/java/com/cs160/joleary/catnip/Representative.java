package com.cs160.joleary.catnip;

/**
 * Created by aaron on 3/1/16.
 */
public class Representative {
    String name;
    String email;
    String website;
    int photoID;
    String twitterID;
    String party;
    String bioguide;

    public Representative(String name, String email, String website, int photoID) {
        this.name = name;
        this.email = email;
        this.website = website;
        this.photoID = photoID;
    }

    public Representative(String name, String email, String website, String twitterID, String bioguide, String party ) {
        this.name = name;
        this.email = email;
        this.website = website;
        this.twitterID = twitterID;
        this.photoID = R.drawable.fred_160;
        this.party = party;
        this.bioguide = bioguide;
    }
}
