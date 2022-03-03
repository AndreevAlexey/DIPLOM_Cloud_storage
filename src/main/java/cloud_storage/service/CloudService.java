package cloud_storage.service;

import cloud_storage.constant.Constant;
import cloud_storage.exceptions.ErrorInputData;
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
        if (filename == null || filename.isEmpty()) throw new ErrorInputData(Constant.EMPTY_FILE_NAME);
        cloudRepository.uploadFile(filename, file);
    }

    /** изменить имя файла **/
    public void editFileName(String filename, String name) {
        if (filename == null || filename.isEmpty() || name == null || name.isEmpty()) throw new ErrorInputData(Constant.EMPTY_FILE_NAME);
        if (filename.equals(name)) throw new ErrorInputData(Constant.EQL_FILE_NAMES);
        cloudRepository.editFileName(filename, name);
    }

    /** удалить файл **/
    public void deleteFile(String filename) {
        if (filename == null || filename.isEmpty()) throw new ErrorInputData(Constant.EMPTY_FILE_NAME);
        cloudRepository.deleteFile(filename);
    }

    /** скачать файл **/
    public Resource downloadFile(String filename) {
        if (filename == null || filename.isEmpty()) throw new ErrorInputData(Constant.EMPTY_FILE_NAME);
        return cloudRepository.downloadFile(filename);
    }

    /** получить список файлов **/
    public List<FileInfo> getUserFileList(int limit) {
        if (limit <= 0) throw new ErrorInputData(Constant.WRN_LIST_LIM);
        return cloudRepository.getUserFileList(limit);
    }

    public String makeErrorJson(String msg) { return cloudRepository.makeErrorJson(msg);}

}
