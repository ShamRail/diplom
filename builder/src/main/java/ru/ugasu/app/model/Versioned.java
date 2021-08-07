package ru.ugasu.app.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

/**
 * Абстракция для сущностей, имеющих имя и версию
 * Например,
 * @see ru.ugasu.app.model.build.config.Builder
 */
@MappedSuperclass
public abstract class Versioned extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "version")
    private String version;

    public Versioned(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public Versioned(int id, String name, String version) {
        super(id);
        this.name = name;
        this.version = version;
    }

    protected Versioned() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Versioned versioned = (Versioned) o;
        return Objects.equals(name, versioned.name) && Objects.equals(version, versioned.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, version);
    }

    @Override
    public String toString() {
        return "Versioned{"
                + "name='" + name + '\''
                + ", version='" + version + '\''
                + ", id=" + id
                + '}';
    }
}
