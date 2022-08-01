
package com.example.mymovies.pojos;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.mymovies.pojos.AuthorDetails;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import retrofit2.http.Query;

@Entity(tableName = "reviews")
public class Review {

    public int getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(int unique_id) {
        this.unique_id = unique_id;
    }

    @PrimaryKey(autoGenerate = true)
    private int unique_id;
    @SerializedName("author")
    @Expose
    private String author;
//    @SerializedName("author_details")
//    @Expose
//    private AuthorDetails authorDetails;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("url")
    @Expose
    private String url;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Review() {
    }

    public Review(int unique_id, String author, String content, String createdAt, String id, String updatedAt, String url) {
        this.unique_id = unique_id;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.id = id;
        this.updatedAt = updatedAt;
        this.url = url;
    }

    /**
     * 
     * @param createdAt
     * @param author
     * @param authorDetails
     * @param id
     * @param content
     * @param url
     * @param updatedAt
     */



    @Ignore
    public Review(String author, AuthorDetails authorDetails, String content, String createdAt, String id, String updatedAt, String url) {
        super();
//        this.author = author;
//        this.authorDetails = authorDetails;
        this.content = content;
        this.createdAt = createdAt;
        this.id = id;
        this.updatedAt = updatedAt;
        this.url = url;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
//
//    public AuthorDetails getAuthorDetails() {
//        return authorDetails;
//    }
//
//    public void setAuthorDetails(AuthorDetails authorDetails) {
//        this.authorDetails = authorDetails;
//    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
