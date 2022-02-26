package cloud_storage;

import cloud_storage.constant.Constant;
import cloud_storage.repository.CloudRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class DiplomCloudStorageApplicationTests {
    private static String urlLogin;
    private static String urlLogout;
    private static String urlFile;
    private static String urlConfirm;
    private static String token;
    private static HttpHeaders headers, headersFile;
    private static Gson gson;

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    CloudRepository cloudRepository;




    @Container
    public static PostgreSQLContainer<?> DB = new PostgreSQLContainer<>("postgres:12-alpine")
            .withDatabaseName("app_db")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void Properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", DB::getJdbcUrl);
        registry.add("spring.datasource.username", DB::getUsername);
        registry.add("spring.datasource.password", DB::getPassword);
    }

    public static GenericContainer<?> app = new GenericContainer<>("cloud_storage")
            .withExposedPorts(9999);

    @BeforeAll
    public static void init() {
/*
        System.setProperty("proxyHost", "http://localhost");
        System.setProperty("proxyPort", "9999");

        /*
        Map<String, String> params = new HashMap<>();

        params.put("POSTGRES_USER", "postgres");
        params.put("POSTGRES_PASSWORD", "postgres");
        params.put("POSTGRES_DB", "app_db");
        db.withEnv(params).start();

        params.clear();
        params.put("PORT", "9999");
        params.put("SPRING_DATASOURCE_URL", "jdbc:postgresql://postgres/app_db");
        app.start();
*/

        app.withEnv(Map.of("SPRING_DATASOURCE_URL", DB.getJdbcUrl())).start();

        String urlRoot = "http://localhost:" + app.getMappedPort(9999);
        urlLogin = urlRoot + "/login";
        urlLogout = urlRoot + "/logout";
        urlFile = urlRoot + "/file";

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headersFile = new HttpHeaders();
        headersFile.setContentType(MediaType.MULTIPART_FORM_DATA);

        GsonBuilder builder = new GsonBuilder();
        gson = builder.create();
    }

    @Test
    void contextLoads() {
        String token = cloudRepository.login("test", "test");
        System.out.println("!!!!token = " + token);

    }

    @Test
    void test_login_WRN_USR_PASS() {
        JSONObject obj = new JSONObject();
        obj.put("login", "test");
        obj.put("password", "test2");
        String json = obj.toJSONString();
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        String resultJsonStr = restTemplate.postForObject(urlLogin, request, String.class);
        System.out.println("resultJsonStr = " + resultJsonStr);
        String expected = Constant.WRN_USR_PASS;
        Assertions.assertTrue(resultJsonStr.contains(expected));
    }

    @Test
    void test_login_USR_NOT_FOUND() {
        JSONObject obj = new JSONObject();
        obj.put("login", "test5555");
        obj.put("password", "test");
        String json = obj.toJSONString();
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        String resultJsonStr = restTemplate.postForObject(urlLogin, request, String.class);
        System.out.println("resultJsonStr = " + resultJsonStr);
        String expected = Constant.USR_NOT_FOUND;
        Assertions.assertTrue(resultJsonStr.contains(expected));
    }

    @Test
    void test_login_Success() {
        JSONObject obj = new JSONObject();
        obj.put("login", "test");
        obj.put("password", "test");
        String json = obj.toJSONString();
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        JSONObject resultJson = restTemplate.postForObject(urlLogin, request, JSONObject.class);
        System.out.println("resultJson = " + resultJson);
        token = (String) resultJson.get("auth-token");
        Assertions.assertFalse(token.isEmpty());
    }

    @Test
    void test_uploadFile_Success() {
        headersFile.add("auth-token", token);
        MultipartFile file = new MockMultipartFile("test.txt", "test".getBytes(StandardCharsets.UTF_8));
        HttpEntity<MultipartFile> request = new HttpEntity<>(file, headersFile);
        String resultJsonStr = restTemplate.postForObject(urlFile + "?filename=test.txt", request, String.class);
        System.out.println("resultJsonStr = " + resultJsonStr);
        String expected = Constant.SUCCESS_UPLOAD;
        Assertions.assertTrue(resultJsonStr.contains(expected));
    }
/*
    @Test
    void test_logout_Success() {
        String resultJsonStr = restTemplate.getForObject(urlLogout, String.class);
        System.out.println("resultJsonStr = " + resultJsonStr);
        String expected = Constant.SUCCESS_LOGOUT;
        Assertions.assertTrue(resultJsonStr.contains(expected));
    }

*/
}


