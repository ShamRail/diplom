package ru.ugasu.app.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/**
 * Абстрактная сущность для выделения общих методов для работы с датами в сущностях
 */
@MappedSuperclass
public abstract class TimeRanged extends BaseEntity {

    /**
     * Начало выполнения
     */
    @Column(name = "start_at")
    protected LocalDateTime startAt;

    /**
     * Завершение выполнения
     */
    @Column(name = "end_at")
    protected LocalDateTime endAt;

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }

}
