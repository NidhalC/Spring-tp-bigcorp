package com.training.springcore.model;

import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
public class Site {
    /**
     * Site id
     */
    @Id
    private String id;

    @Version
    private int version;

    /**
     * Site name
     */
    @NotNull
    @Size( min = 3, max = 100)
    private String name;

    /**
     * Site captors
     */
    @OneToMany(mappedBy = "site" )
    private Set<Captor> captors;

    /**
     * Constructor to use with required property
     *
     */

    public Site() {
        // Use for serializer or deserializer
    }
    /** getteur setteur**/

    public Site(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Captor> getCaptors() {
        return captors;
    }

    public void setCaptors(Set<Captor> captors) {
        this.captors = captors;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    /** Method equals to String generate id**/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Site site = (Site) o;
        return Objects.equals(name, site.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Site{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", captors=" + captors +
                '}';
    }
    @PrePersist
    public  void generateId(){
        this.id = UUID.randomUUID().toString();
    }
}
