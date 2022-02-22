package cloud_storage.repository;

import cloud_storage.constant.Constant;
import cloud_storage.exceptions.ErrorInputData;
import cloud_storage.exceptions.ErrorServer;
import cloud_storage.logger.*;
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
    private final ILogger logger  = FileLogger.get();

    @Autowired
    private UserFileCrudRepository userFileCrudRepository;

    @Autowired UserCrudRepository userCrudRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // добавить в лог
    public void log(String msg) {
        logger.log(msg);
    }

    public String getUserName() {
        return ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }

    // TODO encode password
    /** login **/
    public String login(String login, String password) {
        log("login user=" + login);
        User user = userCrudRepository.findByLogin(login)
                .orElseThrow(()-> new ErrorInputData(Constant.USR_NOT_FOUND));

        if(!(passwordEncoder.encode(password)).equals(user.getPassword()))
            throw new ErrorInputData(Constant.WRN_USR_PASS);
        return jwtProvider.generateToken(login);
    }

    /** сохранить файл **/
    public void uploadFile(String filename, MultipartFile file) {
        User user = userCrudRepository.findByLogin(getUserName())
                .orElseThrow(()-> new ErrorInputData(Constant.USR_NOT_FOUND));
        log("user=" + user.getLogin() + ", action='upload', file=" + filename);
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
        } catch (IOException e) {
            throw new ErrorInputData(Constant.ERR_UPLOAD);
        }
    }

    /** изменить имя файла **/
    public void editFileName(String filename, String name) {
        String username = getUserName();
        log("user=" + username + ", action='edit file name', file=" + filename + ", new name=" + name);
        long fileId = userFileCrudRepository.getIdUserFileByName(filename, username)
                .orElseThrow(()->new ErrorInputData(Constant.FILE_NOT_FOUND));
        if(userFileCrudRepository.updateFileNameById(name, fileId) == 0)
            throw new ErrorServer(Constant.ERR_EDIT_NAME);
    }

    /** удалить файл **/
    public void deleteFile(String filename) {
        String username = getUserName();
        log("user=" + username + ", action='delete', file=" + filename);
        long fileId = userFileCrudRepository.getIdUserFileByName(filename, username)
                .orElseThrow(()->new ErrorInputData(Constant.FILE_NOT_FOUND));
        userFileCrudRepository.deleteById(fileId);
        if(userFileCrudRepository.countByIdAndUserLogin(fileId, username) > 0)
            throw new ErrorServer(Constant.ERR_DELETE);
    }

    /** скачать файл **/
    public Resource downloadFile(String filename) {
        String username = getUserName();
        log("user=" + username + ", action='download', file=" + filename);
        byte[] content = userFileCrudRepository.findByFileInfoFilenameAndUserLogin(filename, username)
                .orElseThrow(()-> new ErrorInputData(Constant.FILE_NOT_FOUND))
                .getContent();
        return new ByteArrayResource(content);
    }

    /** получить список файлов **/
    public List<FileInfo> getUserFileList(int limit) {
        String username = getUserName();
        log("user=" + username + ", action='list of files'");
        PageRequest paging = PageRequest.of(0, limit, Sort.by("fileInfo.filename"));
        return userFileCrudRepository.findAllUserFiles(username, paging);
    }

}
