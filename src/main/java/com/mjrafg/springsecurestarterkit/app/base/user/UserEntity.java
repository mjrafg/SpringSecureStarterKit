package com.mjrafg.springsecurestarterkit.app.base.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mjrafg.springsecurestarterkit.app.base.file.FileEntity;
import com.mjrafg.springsecurestarterkit.app.base.role.RoleEntity;
import com.mjrafg.springsecurestarterkit.base.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "userName"),
                @UniqueConstraint(columnNames = "email")
        })
public class UserEntity extends BaseEntity {


    @NotBlank
    @Size(max = 20, min = 3)
    private String userName;

    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    @Size(max = 30)
    private String mobile;


    @JsonIgnore
    @Size(max = 120)
    private String password;

    @Transient
    @JsonProperty("changedPassword")
    private String changedPassword;


    @NotNull
    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;

    @ManyToOne
    @JoinColumn(name = "profile_image_id")
    private FileEntity profileImage;

    public UserEntity(String userName, String name, String email, String password) {
        this.userName = userName;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
