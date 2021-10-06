package ru.ugasu.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Сущность языка программирования. От нее зависит выбор конфигурации сборки
 */
@Entity
@Table(name = "language")
public class Language extends Versioned {

    @Column(name = "logo")
    private String logo;

    public Language() { }

    public Language(String name, String version) {
        super(name, version);
    }

    public Language(int id, String name, String version) {
        super(id, name, version);
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
