package cloud_storage;

import cloud_storage.exceptions.ErrorInputData;
import cloud_storage.repository.CloudRepository;
import cloud_storage.service.AuthorizationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class AuthorizationServiceTests {
    private static AuthorizationService authorizationService;
    private final static String text = "test content";

    @BeforeAll
    static void init() {
        CloudRepository cloudRepository = mock(CloudRepository.class);
        authorizationService = new AuthorizationService(cloudRepository);
    }

    @Test
    public void test_login_empty_password_thrown_EMPTY_FILE_NAME() {
        // given
        String login = "user";
        String password = "";
        // then
        Assertions.assertThrows(ErrorInputData.class, () -> authorizationService.login(login, password));
    }
}
