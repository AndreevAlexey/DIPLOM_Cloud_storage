package cloud_storage.service;

import cloud_storage.constant.Constant;
import cloud_storage.exceptions.ErrorInputData;
import cloud_storage.repository.CloudRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    private final CloudRepository cloudRepository;

    public AuthorizationService(CloudRepository cloudRepository) {
        this.cloudRepository = cloudRepository;
    }

    /** login **/
    public String login(String login, String password) {
        if (login == null || login.isEmpty() || password == null || password.isEmpty()) throw new ErrorInputData(Constant.EMPTY_LOG_PASS);
        return cloudRepository.login(login, password);
    }

    public String makeErrorJson(String msg) { return cloudRepository.makeErrorJson(msg);}
}
