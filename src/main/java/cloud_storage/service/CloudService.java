package cloud_storage.service;

import cloud_storage.repository.CloudRepository;
import cloud_storage.model.userfile.FileInfo;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class CloudService {
    private final CloudRepository cloudRepository;

    public CloudService(CloudRepository cloudRepository) {
        this.cloudRepository = cloudRepository;
    }

    /** сохранить файл **/
    public void uploadFile(String filename, MultipartFile file) {
        cloudRepository.uploadFile(filename, file);
    }

    /** изменить имя файла **/
    public void editFileName(String filename, String name) {
        cloudRepository.editFileName(filename, name);
    }

    /** удалить файл **/
    public void deleteFile(String filename) {
        cloudRepository.deleteFile(filename);
    }

    /** скачать файл **/
    public Resource downloadFile(String filename) {
        return cloudRepository.downloadFile(filename);
    }

    /** получить список файлов **/
    public List<FileInfo> getUserFileList(int limit) {
        return cloudRepository.getUserFileList(limit);
    }

    /** login **/
    public String login(String login, String password) {
        return cloudRepository.login(login, password);
    }

    // добавить в лог
    public void log(String msg) {
        cloudRepository.log(msg);
    }


}
