package cloud_storage.controller;

import cloud_storage.constant.Constant;
import cloud_storage.exceptions.ErrorInputData;
import cloud_storage.service.CloudService;
import cloud_storage.model.userfile.FileInfo;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins = {"${settings.cors_origin}"}, allowedHeaders = "*", allowCredentials = "true")
@RestController
public class CloudController {
    private final CloudService cloudService;
    public CloudController(CloudService cloudService) {
        this.cloudService = cloudService;
    }

    @PostMapping("/login")
    public Map<String, String> loginUser(@RequestBody Map<String, String> auth) {
        String token = cloudService.login(auth.get("login"), auth.get("password"));
        return Collections.singletonMap("auth-token", token);
    }

    @PostMapping("/logout")
    public String logoutUser() {
        return Constant.SUCCESS_LOGOUT;
    }

    // фронт отправляет http://localhost:9999/login?logout вместо post?как указано в спецификации
    @GetMapping("/login")
    public String logout() {
        return Constant.SUCCESS_LOGOUT;
    }

    /** POST сохранить файл **/
    @PostMapping(value = "/file", consumes = "multipart/form-data")
    public String uploadFile(@RequestParam("filename") String filename, @RequestBody MultipartFile file) {
        cloudService.uploadFile(filename, file);
        return Constant.SUCCESS_UPLOAD;
    }

    /** PUT изменить имя файла **/
    @PutMapping(value = "/file", consumes = "application/json")
    public String editFileName(@RequestParam("filename") String filename, @RequestBody Map<String, String> bodyParams) {
        cloudService.editFileName(filename, bodyParams.get("filename"));
        return Constant.SUCCESS_UPLOAD;
    }

    /** DELETE удалить файл **/
    @DeleteMapping("/file")
    public String deleteFile(@RequestParam("filename") String filename) {
        cloudService.deleteFile(filename);
        return Constant.SUCCESS_DEL;
    }

    /** GET скачать файл **/
    @GetMapping("/file")
    public ResponseEntity<Resource> downloadFile(@RequestParam("filename") String filename) {
        Resource resource = cloudService.downloadFile(filename);
        return ResponseEntity.ok().body(resource);
    }

    /** получить список файлов **/
    @GetMapping("/list")
    public List<FileInfo> getUserFileList(@RequestParam("limit") int limit) {
        return cloudService.getUserFileList(limit);
    }

    @ExceptionHandler(ErrorInputData.class)
    ResponseEntity<String> handlerErrorInputData(ErrorInputData exp) {
        return new ResponseEntity<>(makeErrorJson(exp.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    ResponseEntity<String> handlerRuntimeException(RuntimeException exp) {
        exp.printStackTrace();
        return new ResponseEntity<>(makeErrorJson(Constant.SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private String makeErrorJson(String msg) { return "{\"message\":\"" + msg + "\",\"id\":\"0\"}";}
}
