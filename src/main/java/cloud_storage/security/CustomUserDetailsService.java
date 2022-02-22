package cloud_storage.security;

import cloud_storage.constant.Constant;
import cloud_storage.exceptions.ErrorInputData;
import cloud_storage.model.user.User;
import cloud_storage.repository.UserCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserCrudRepository userCrudRepository;

    @Override
    public User loadUserByUsername(String username) {
        return userCrudRepository.findByLogin(username)
                .orElseThrow(()-> new ErrorInputData(Constant.USR_NOT_FOUND));
    }
}
