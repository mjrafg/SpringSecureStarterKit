package com.mjrafg.springsecurestarterkit.app.base.role;

import com.mjrafg.springsecurestarterkit.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "roles")
public class RoleEntity extends BaseEntity {


  @Column(length = 20)
  private String name;

  @Column(length = 20)
  private String nativeName;

  public String getNativeName() {
    return nativeName;
  }



  public RoleEntity() {

  }
  public RoleEntity(String name, String nativeName) {
    this.name = name;
    this.nativeName=nativeName;
  }

}