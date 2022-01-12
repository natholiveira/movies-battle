package com.letscode.moviesbattle.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.letscode.moviesbattle.web.request.UserRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    private String id;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", updatable = false, nullable = false)
    private OffsetDateTime updateAt;

    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "name")
    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password")
    private String password;

    @Column(name = "score")
    private Long score = 0L;

    @Column(name = "ranking_position")
    private Long rankingPosition = 0L;

    public User(UserRequest userRequest, String password) {
        this.name = userRequest.getName();
        this.username = userRequest.getUsername();
        this.password = password;
        this.id = UUID.randomUUID().toString();
        this.createdAt = OffsetDateTime.now(ZoneOffset.UTC);
        this.updateAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
}
