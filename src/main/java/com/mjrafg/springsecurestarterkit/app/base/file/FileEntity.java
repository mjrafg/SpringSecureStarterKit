package com.mjrafg.springsecurestarterkit.app.base.file;

import com.mjrafg.springsecurestarterkit.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileEntity extends BaseEntity {
    private String type;

    private String originalName;

    private String savedName;

    private String downloadPath;

    private String thumbnailPath;

    private Boolean isDraw;

    private Long size;
}
