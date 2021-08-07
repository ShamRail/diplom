package ru.ugasu.app.model;

import javax.persistence.*;
import java.util.Objects;

/**
 * Базовая сущность для выделения общей части работы с ID в сущностях.
 */
@MappedSuperclass
public abstract class BaseEntity {

    /**
     * Идентификатор сущности для всех сущностей
     * id генериуется базой данных последовательно (serial)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected int id;

    public BaseEntity() { }

    public BaseEntity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseEntity that = (BaseEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
