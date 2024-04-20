package com.mjrafg.springsecurestarterkit.app.base.user;

import com.mjrafg.springsecurestarterkit.app.base.file.FileEntity;
import com.mjrafg.springsecurestarterkit.app.base.file.FileService;
import com.mjrafg.springsecurestarterkit.base.service.BaseServiceImpl;
import com.mjrafg.springsecurestarterkit.utils.CommonUtils;
import com.mjrafg.springsecurestarterkit.utils.FileUtils;
import com.mjrafg.springsecurestarterkit.utils.ModelMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService  extends BaseServiceImpl<UserEntity,String> {
    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }
    @Autowired
    private ModelMapperUtil modelMapperUtil;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    FileService fileService;


    @Override
    public Optional<UserEntity> update(String id, UserEntity userDetails) {
        return userRepository.findById(id).map(existingUser -> {
            String originalId = existingUser.getId();
            String currentPassword = existingUser.getPassword();
            if (CommonUtils.isNotEmpty(userDetails.getChangedPassword())) {
                currentPassword = encoder.encode(userDetails.getChangedPassword());
            }
            modelMapperUtil.map(userDetails, existingUser);
            existingUser.setId(originalId);
            existingUser.setPassword(currentPassword);
            return userRepository.save(existingUser);
        });
    }

    public Optional<UserEntity> updateProfileImage(String userId, String fileId) {
        return userRepository.findById(userId).map(existingUser -> {
            Optional<FileEntity> file = fileService.findById(fileId);
            if (file.isPresent()) {
                existingUser.setProfileImage(file.get());
                return userRepository.save(existingUser);
            }
            throw new RuntimeException("Profile image not found.");
        });
    }
    public void deleteProfileImage(String userId, String fileId) {
        Optional<UserEntity> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Optional<FileEntity> optionalFile = fileService.findById(fileId);
            if (optionalFile.isPresent()) {
                UserEntity userEntity = optionalUser.get();
                userEntity.setProfileImage(null);
                userRepository.save(userEntity);
                fileService.deleteById(fileId);
                FileEntity file = optionalFile.get();
                FileUtils.deleteFile(file.getSavedName(), file.getType());
            } else {
                throw new RuntimeException("Profile image not found.");
            }
        } else {
            throw new RuntimeException("User not found.");
        }
    }


    public boolean checkUserName(String searchUserName, String currentUserName) {
        return userRepository.existsByUserNameAndUserNameNot(searchUserName, currentUserName);
    }
  public   Optional<UserEntity> findByUserName(String username){
        return userRepository.findByUserName(username);
  }

   public boolean existsByUserName(String userName){
        return userRepository.existsByUserName(userName);
   }
    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

}
