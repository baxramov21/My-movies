package com.sheyx.mymovies.pojos;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "trailers")
public class Trailer {


    public int getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(int unique_id) {
        this.unique_id = unique_id;
    }

    @PrimaryKey(autoGenerate = true)
    private int unique_id;

    @SerializedName("iso_639_1")
    @Expose
    private String iso6391;
    @SerializedName("iso_3166_1")
    @Expose
    private String iso31661;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("key")
    @Expose
    private String key;
    @SerializedName("site")
    @Expose
    private String site;
    @SerializedName("size")
    @Expose
    private Integer size;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("official")
    @Expose
    private Boolean official;
    @SerializedName("published_at")
    @Expose
    private String publishedAt;
    @SerializedName("id")
    @Expose
    private String id;

    /**
     * No args constructor for use in serialization
     *
     */

    public Trailer(int unique_id, String iso6391, String iso31661, String name, String key, String site, Integer size, String type, Boolean official, String publishedAt, String id) {
        this.unique_id = unique_id;
        this.iso6391 = iso6391;
        this.iso31661 = iso31661;
        this.name = name;
        this.key = key;
        this.site = site;
        this.size = size;
        this.type = type;
        this.official = official;
        this.publishedAt = publishedAt;
        this.id = id;
    }

    /**
     *
     * @param site
     * @param size
     * @param publishedAt
     * @param name
     * @param iso31661
     * @param official
     * @param iso6391
     * @param id
     * @param type
     * @param key
     */



    @Ignore
    public Trailer(String iso6391, String iso31661, String name, String key, String site, Integer size, String type, Boolean official, String publishedAt, String id) {
        super();
        this.iso6391 = iso6391;
        this.iso31661 = iso31661;
        this.name = name;
        this.key = key;
        this.site = site;
        this.size = size;
        this.type = type;
        this.official = official;
        this.publishedAt = publishedAt;
        this.id = id;
    }

    public String getIso6391() {
        return iso6391;
    }

    public void setIso6391(String iso6391) {
        this.iso6391 = iso6391;
    }

    public String getIso31661() {
        return iso31661;
    }

    public void setIso31661(String iso31661) {
        this.iso31661 = iso31661;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getOfficial() {
        return official;
    }

    public void setOfficial(Boolean official) {
        this.official = official;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
