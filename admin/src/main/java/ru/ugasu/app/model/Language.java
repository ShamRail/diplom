package ru.ugasu.app.model;

import ru.ugasu.app.model.Versioned;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Сущность языка программирования. От нее зависит выбор конфигурации сборки
 */
@Entity
@Table(name = "language")
public class Language extends Versioned {

    public Language() { }

    public Language(String name, String version) {
        super(name, version);
    }

    public Language(int id, String name, String version) {
        super(id, name, version);
    }

}
