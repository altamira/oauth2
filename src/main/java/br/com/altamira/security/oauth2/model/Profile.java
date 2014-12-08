package br.com.altamira.security.oauth2.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.com.altamira.security.oauth2.serialize.JSonViews;
import br.com.altamira.security.oauth2.serialize.NullCollectionSerializer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 *
 * @author
 */
@Entity
@Table(name = "SS_PROFILE")
public class Profile extends Resource {

    /**
     * Serial number ID
     */
    private static final long serialVersionUID = -3725014293364656727L;

    @NotNull
    @Size(max = 50)
    @Column(name = "NAME")
    private String name = "";

    @JsonIgnore
    @JsonSerialize(using = NullCollectionSerializer.class)
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "profiles")
    private List<User> users = new ArrayList<>();

    @JsonView(JSonViews.EntityView.class)
    @JoinColumn(name = "PERMISSION_ID", referencedColumnName = "ID")
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Permission permission;

    /**
     *
     */
    public Profile() {

    }

    /**
     *
     * @param number
     * @param customer
     */
    public Profile(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

}
