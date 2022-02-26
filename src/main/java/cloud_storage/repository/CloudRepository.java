package cloud_storage.repository;

import cloud_storage.constant.Constant;
import cloud_storage.exceptions.ErrorInputData;
import cloud_storage.exceptions.ErrorServer;
import cloud_storage.model.history.History;
import cloud_storage.model.user.User;
import cloud_storage.model.userfile.FileInfo;
import cloud_storage.model.userfile.UserFile;
import cloud_storage.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Transactional
@Repository
public class CloudRepository {

    @Autowired
    private UserFileCrudRepository userFileCrudRepository;

    @Autowired
    private UserCrudRepository userCrudRepository;

    @Autowired
    private HistoryCrudRepository historyCrudRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // добавить запись в историю запросов
    public void addHistory(User user, String desc) {
        History history = History.builder()
                .user(user)
                .description(desc)
                .uploadDate(LocalDate.now())
                .build();
        historyCrudRepository.save(history);

    }

    // имя пользователя из контекста
    public String getUserName() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    /** login **/
    public String login(String login, String password) {
        User user = userCrudRepository.findByLogin(login)
                .orElseThrow(()-> new ErrorInputData(Constant.USR_NOT_FOUND));

        if(!passwordEncoder.matches(password, user.getPassword()))
            throw new ErrorInputData(Constant.WRN_USR_PASS);
        addHistory(user, "login");
        return jwtProvider.generateToken(login);
    }

    /** сохранить файл **/
    public void uploadFile(String filename, MultipartFile file) {
        User user = userCrudRepository.findByLogin(getUserName())
                .orElseThrow(()-> new ErrorInputData(Constant.USR_NOT_FOUND));
        if(userFileCrudRepository.countByFileInfoFilenameAndUserId(filename, user.getId()) > 0)
            throw new ErrorInputData(Constant.ERR_FILE_EXISTS);

        try {
            UserFile userFile = UserFile.builder()
                    .fileInfo(new FileInfo(filename, file.getSize()))
                    .content(file.getBytes())
                    .uploadDate(LocalDate.now())
                    .user(user)
                    .build();
            userFileCrudRepository.save(userFile);
            addHistory(user, "upload file=" + filename);
        } catch (IOException e) {
            throw new ErrorInputData(Constant.ERR_UPLOAD);
        }
    }

    /** изменить имя файла **/
    public void editFileName(String filename, String name) {
        User user = userCrudRepository.findByLogin(getUserName())
                .orElseThrow(()-> new ErrorInputData(Constant.USR_NOT_FOUND));

        long fileId = userFileCrudRepository.getIdUserFileByName(filename, user.getUsername())
                .orElseThrow(()->new ErrorInputData(Constant.FILE_NOT_FOUND));

        if(userFileCrudRepository.countByFileInfoFilenameAndUserId(name, user.getId()) > 0)
            throw new ErrorInputData(Constant.ERR_FILE_EXISTS);

        if(userFileCrudRepository.updateFileNameById(name, fileId) == 0)
            throw new ErrorServer(Constant.ERR_EDIT_NAME);
        addHistory(user, "edit file name=" + filename + ", new name=" + name);
    }

    /** удалить файл **/
    public void deleteFile(String filename) {
        User user = userCrudRepository.findByLogin(getUserName())
                .orElseThrow(()-> new ErrorInputData(Constant.USR_NOT_FOUND));

        long fileId = userFileCrudRepository.getIdUserFileByName(filename, user.getUsername())
                .orElseThrow(()->new ErrorInputData(Constant.FILE_NOT_FOUND));

        userFileCrudRepository.deleteById(fileId);
        if(userFileCrudRepository.countByIdAndUserLogin(fileId, user.getUsername()) > 0)
            throw new ErrorServer(Constant.ERR_DELETE);
        addHistory(user, "delete file =" + filename);
    }

    /** скачать файл **/
    public Resource downloadFile(String filename) {
        User user = userCrudRepository.findByLogin(getUserName())
                .orElseThrow(()-> new ErrorInputData(Constant.USR_NOT_FOUND));

        byte[] content = userFileCrudRepository.findByFileInfoFilenameAndUserLogin(filename, user.getUsername())
                .orElseThrow(()-> new ErrorInputData(Constant.FILE_NOT_FOUND))
                .getContent();
        addHistory(user, "download file =" + filename);
        return new ByteArrayResource(content);
    }

    /** получить список файлов **/
    public List<FileInfo> getUserFileList(int limit) {
        User user = userCrudRepository.findByLogin(getUserName())
                .orElseThrow(()-> new ErrorInputData(Constant.USR_NOT_FOUND));

        PageRequest paging = PageRequest.of(0, limit, Sort.by("fileInfo.filename"));
        addHistory(user, "list of files");
        return userFileCrudRepository.findAllUserFiles(user.getUsername(), paging);
    }

}
