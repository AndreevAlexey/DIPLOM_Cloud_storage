package cloud_storage;

import cloud_storage.repository.UserCrudRepository;
import cloud_storage.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CommandLineApp implements CommandLineRunner {

    @Autowired
    private UserCrudRepository userCrudRepository;

    @Override
    @Transactional
    public void run(String... args) {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        List<User> users = Stream.of("user1", "user2", "user3")
                .map(n -> User.builder()
                        .login(n)
                        .password(encoder.encode(n))
                        //.password(n)
                        .role("USER")
                        .build())
                .collect(Collectors.toList());
        userCrudRepository.saveAll(users);
    }
}
