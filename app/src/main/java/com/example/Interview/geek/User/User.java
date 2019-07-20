package com.example.Interview.geek.User;

import java.io.Serializable;

public class User implements Serializable {
    private String name, username, email, phone_number, address, dob, password, bio;
    private String cf_handle, topcoder_handle, hackerrank_handle, codechef_handle, uhunt_handle;
    private String github, linkedin, imgurl, institution;

    public User(){
        name = "N/A";
        username = "N/A";

        email = "N/A";
        phone_number = "N/A";
        address = "N/A";
        dob = "N/A";
        password = "N/A";
        bio = "N/A";

        cf_handle = "N/A";
        topcoder_handle = "N/A";
        hackerrank_handle = "N/A";
        codechef_handle = "N/A";
        uhunt_handle = "N/A";

        github = "N/A";
        linkedin = "N/A";
        imgurl = "N/A";
        institution = "N/A";
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCf_handle() {
        return cf_handle;
    }

    public void setCf_handle(String cf_handle) {
        this.cf_handle = cf_handle;
    }

    public String getTop_coder_handle() {
        return topcoder_handle;
    }

    public void setTop_coder_handle(String top_coder_handle) {
        this.topcoder_handle = top_coder_handle;
    }

    public String getHackerrank_handle() {
        return hackerrank_handle;
    }

    public void setHackerrank_handle(String hackerrank_handle) {
        this.hackerrank_handle = hackerrank_handle;
    }

    public String getCodechef_handle() {
        return codechef_handle;
    }

    public void setCodechef_handle(String codechef_handle) {
        this.codechef_handle = codechef_handle;
    }

    public String getUhunt_handle() {
        return uhunt_handle;
    }

    public void setUhunt_handle(String uhunt_handle) {
        this.uhunt_handle = uhunt_handle;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
