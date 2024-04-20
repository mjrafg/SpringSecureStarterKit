package com.mjrafg.springsecurestarterkit.app.base.user;

import java.util.Optional;


import com.mjrafg.springsecurestarterkit.base.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<UserEntity, String> {
  Optional<UserEntity> findByUserName(String username);

  boolean existsByUserNameAndUserNameNot(String searchUserName,String currentUserName);
  boolean existsByUserName(String userName);
  boolean existsByEmail(String userName);


}
