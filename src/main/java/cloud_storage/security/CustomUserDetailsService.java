package cloud_storage.security;

import cloud_storage.constant.Constant;
import cloud_storage.exceptions.ErrorInputData;
import cloud_storage.model.user.User;
import cloud_storage.repository.UserCrudRepository;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final UserCrudRepository userCrudRepository;

    public CustomUserDetailsService(UserCrudRepository userCrudRepository) {
        this.userCrudRepository = userCrudRepository;
    }

    @Override
    public User loadUserByUsername(String username) {
        return userCrudRepository.findByLogin(username)
                .orElseThrow(()-> new ErrorInputData(Constant.USR_NOT_FOUND));
    }
}
