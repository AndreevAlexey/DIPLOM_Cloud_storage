package cloud_storage.controller;

import cloud_storage.constant.Constant;
import cloud_storage.exceptions.ErrorInputData;
import cloud_storage.service.AuthorizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@CrossOrigin(origins = {"${settings.cors_origin}"}, allowedHeaders = "*", allowCredentials = "true")
@RestController
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    public AuthorizationController(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping("/login")
    public Map<String, String> loginUser(@RequestBody Map<String, String> auth) {
        String token = authorizationService.login(auth.get("login"), auth.get("password"));
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

    @ExceptionHandler(ErrorInputData.class)
    ResponseEntity<String> handlerErrorInputData(ErrorInputData exp) {
        return new ResponseEntity<>(authorizationService.makeErrorJson(exp.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    ResponseEntity<String> handlerRuntimeException(RuntimeException exp) {
        exp.printStackTrace();
        return new ResponseEntity<>(authorizationService.makeErrorJson(Constant.SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
