package br.com.altamira.security.oauth2.model;

import br.com.altamira.security.oauth2.serialize.NullCollectionSerializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author alessandro.holanda
 */
@Entity
@Table(name = "SS_RESOURCE")
public class Resource extends br.com.altamira.security.oauth2.model.Entity {

    /**
     *
     */
    private static final long serialVersionUID = -73112110881650016L;

    @NotNull
    @Size(max = 50)
    @Column(name = "NAME")
    private String name = "";

    @JsonIgnore
    @JsonSerialize(using = NullCollectionSerializer.class)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "resource", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Permission> permission = new ArrayList<>();

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
