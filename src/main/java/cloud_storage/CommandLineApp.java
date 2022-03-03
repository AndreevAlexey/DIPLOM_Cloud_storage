package cloud_storage;

import cloud_storage.repository.UserCrudRepository;
import cloud_storage.model.user.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CommandLineApp implements CommandLineRunner {

    private final UserCrudRepository userCrudRepository;
    private final PasswordEncoder passwordEncoder;

    public CommandLineApp(UserCrudRepository userCrudRepository, PasswordEncoder passwordEncoder) {
        this.userCrudRepository = userCrudRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        List<User> users = Stream.of("user1", "user2", "user3", "test")
                .map(n -> User.builder()
                        .login(n)
                        .password(passwordEncoder.encode(n))
                        //.password(n)
                        .role("USER")
                        .build())
                .collect(Collectors.toList());
        userCrudRepository.saveAll(users);
    }
}
