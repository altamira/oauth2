package br.com.altamira.security.oauth2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author
 */
@Entity
@Table(name = "SS_PERMISSION")
public class Permission extends br.com.altamira.security.oauth2.model.Entity {

    /**
     *
     */
    private static final long serialVersionUID = -7539995003808419586L;

    @JsonIgnore
    @JoinColumn(name = "PROFILE", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Profile profile;
 
    @JsonIgnore
    @JoinColumn(name = "SS_RESOURCE", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Resource resource;
    
    @NotNull
    @Size(max = 255)
    @Column(name = "PERMISSION")
    private String permission = "";

    public Permission() {

    }

    public Permission(Long id, Resource resource, Profile profile, String permission) {

        this.id = id;
        this.resource = resource;
        this.profile = profile;
        this.permission = permission;

    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    /**
     * @return the profile
     */
    public Profile getProfile() {
        return profile;
    }

    /**
     * @param profile the profile to set
     */
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    /**
     * @return the resource
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * @param resource the resource to set
     */
    public void setResource(Resource resource) {
        this.resource = resource;
    }

}
