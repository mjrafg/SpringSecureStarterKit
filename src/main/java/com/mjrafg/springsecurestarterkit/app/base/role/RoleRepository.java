package com.mjrafg.springsecurestarterkit.app.base.role;

import java.util.Optional;

import com.mjrafg.springsecurestarterkit.base.repository.BaseRepository;

public interface RoleRepository  extends BaseRepository<RoleEntity, String> {
  Optional<RoleEntity> findByName(String name);

}
