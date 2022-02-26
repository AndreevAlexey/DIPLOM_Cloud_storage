package cloud_storage;

import cloud_storage.exceptions.ErrorInputData;
import cloud_storage.repository.CloudRepository;
import cloud_storage.service.CloudService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.mock;

public class CloudServiceTests {
    private static CloudService cloudService;
    private final static String text = "test content";

    @BeforeAll
    static void init() {
        CloudRepository cloudRepository = mock(CloudRepository.class);
        cloudService = new CloudService(cloudRepository);
    }

    @Test
    public void test_uploadFile_null_filename_thrown_EMPTY_FILE_NAME() {
        // given
        String filename = null;
        MultipartFile file = new MockMultipartFile("test.txt", text.getBytes(StandardCharsets.UTF_8));
        // then
        Assertions.assertThrows(ErrorInputData.class, () -> cloudService.uploadFile(filename, file));
    }

    @Test
    public void test_editFileName_null_filename_thrown_EMPTY_FILE_NAME() {
        // given
        String filename1 = "name1.txt";
        String filename2 = null;
        // then
        Assertions.assertThrows(ErrorInputData.class, () -> cloudService.editFileName(filename1, filename2));
    }

    @Test
    public void test_editFileName_null_filename_thrown_EQL_FILE_NAMES() {
        // given
        String filename1 = "name1.txt";
        // then
        Assertions.assertThrows(ErrorInputData.class, () -> cloudService.editFileName(filename1, filename1));
    }

    @Test
    public void test_deleteFile_null_filename_thrown_EMPTY_FILE_NAME() {
        // given
        String filename1 = null;
        // then
        Assertions.assertThrows(ErrorInputData.class, () -> cloudService.deleteFile(filename1));
    }

    @Test
    public void test_downloadFile_null_filename_thrown_EMPTY_FILE_NAME() {
        // given
        String filename1 = null;
        // then
        Assertions.assertThrows(ErrorInputData.class, () -> cloudService.downloadFile(filename1));
    }

    @Test
    public void test_getUserFileList_null_limit_0_thrown_WRN_LIST_LIM() {
        // given
        int limit = 0;
        // then
        Assertions.assertThrows(ErrorInputData.class, () -> cloudService.getUserFileList(limit));
    }

    @Test
    public void test_login_empty_password_thrown_EMPTY_FILE_NAME() {
        // given
        String login = "user";
        String password = "";
        // then
        Assertions.assertThrows(ErrorInputData.class, () -> cloudService.login(login, password));
    }

}
