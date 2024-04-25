package com.mjrafg.springsecurestarterkit.base.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

import static com.mjrafg.springsecurestarterkit.data.SecurityData.getLoggedUserName;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @Size(max = 50)
    @GeneratedValue(strategy = GenerationType.UUID)
    @Schema(description = "Automatically generated unique identifier of the entity. Do not provide or modify this value in API requests.", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false, updatable = false)
    @Schema(description = "Timestamp marking when the entity was initially created, automatically set by the system at the moment of creation. Not modifiable by API consumers.", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Timestamp of the last update made to the entity, automatically set by the system at the moment of the latest update.", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;

    @Size(max = 50)
    @Schema(description = "Identifier of the user who last updated the entity. This is captured automatically and cannot be modified manually.", accessMode = Schema.AccessMode.READ_ONLY)
    private String changedBy;

    @Size(max = 50)
    @Schema(description = "Identifier of the user who created the entity. Automatically set by the system to the current user at the time of entity creation.", accessMode = Schema.AccessMode.READ_ONLY)
    private String createdBy;

    @Schema(description = "Flag to indicate if the entity is soft-deleted. It defaults to 'false' and should be changed via dedicated API endpoints handling delete operations.", example = "false")
    private Boolean softDelete = false;

    @Schema(description = "Additional notes about the entity; can be set and modified by API consumers.", example = "This is a sample note.")
    private String note;

    @Schema(description = "Indicates whether the entity is active. Defaults to 'true' and can be modified by API consumers to deactivate the entity.", example = "true")
    private boolean active = true;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.createdBy= getLoggedUserName();
    }
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.changedBy= getLoggedUserName();
    }
}
