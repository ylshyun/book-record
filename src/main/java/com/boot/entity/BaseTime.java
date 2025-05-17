package com.boot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Access(AccessType.FIELD)
public abstract class BaseTime {
    @CreatedDate //현재 시간 생성
    @Column(updatable = false, nullable = false)
    private LocalDateTime createDate;

    @LastModifiedDate //수정 시간 생성
    @Column(nullable = false)
    private LocalDateTime modifiedDate;
}
