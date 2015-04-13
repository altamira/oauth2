package br.com.altamira.security.oauth2.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.altamira.security.oauth2.serialize.JSonViews;
import br.com.altamira.security.oauth2.serialize.NullCollectionSerializer;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 *
 * @author
 */
@Entity
@Table(name = "SS_USER")
public class User extends br.com.altamira.security.oauth2.model.Entity {

    /**
     * Serial number ID
     */
    private static final long serialVersionUID = -3725014293364656727L;

    @NotNull
    @Size(min = 3)
    @Column(name = "USERNAME", unique = true)
    private String user = "";

    @NotNull
    @Size(min = 3)
    @Column(name = "PASSWORD")
    private String password = "";

    @NotNull
    @Size(min = 1)
    @Column(name = "FIRST_NAME")
    private String firstName = "";

    @NotNull
    @Size(min = 1)
    @Column(name = "LAST_NAME")
    private String lastName = "";

    @NotNull
    @Size(min = 5)
    @Column(name = "EMAIL")
    private String email = "";

    @Column(name = "THEME")
    private String theme = "dark";

    @JsonView(JSonViews.EntityView.class)
    @JsonSerialize(using = NullCollectionSerializer.class)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<AccessToken> accessTokens = new ArrayList<>();

    @JsonView(JSonViews.EntityView.class)
    @JsonSerialize(using = NullCollectionSerializer.class)
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "SS_USER_PROFILE", joinColumns = {
        @JoinColumn(name = "USER_ID", nullable = true, updatable = false)},
            inverseJoinColumns = {
                @JoinColumn(name = "PROFILE_ID",
                        nullable = true, updatable = false)})
    private List<Profile> profiles = new ArrayList<>();

    /**
     *
     */
    public User() {

    }

    /**
     *
     * @param id
     * @param user
     */
    public User(Long id, String user) {
        this.id = id;
        this.user = user;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<AccessToken> getAccessTokens() {
        return accessTokens;
    }

    public void setAccessTokens(List<AccessToken> accessTokens) {
        this.accessTokens = accessTokens;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Profile> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<Profile> profiles) {
        this.profiles = profiles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the theme
     */
    public String getTheme() {
        return theme;
    }

    /**
     * @param theme the theme to set
     */
    public void setTheme(String theme) {
        this.theme = theme;
    }

}
