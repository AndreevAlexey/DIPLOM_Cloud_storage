package cloud_storage.constant;

public class Constant {
    // СООБЩЕНИЯ
    /** успешные сообщения **/
    public static final String SUCCESS_UPLOAD = "Файл загружен";
    public static final String SUCCESS_DEL = "Файл удален";
    /** сообщения об ошибках **/
    public static final String SERVER_ERROR = "Ошибка обработки запроса!";
    /** сообщения об ошибках по файлам **/
    public static final String ERR_FILE_EXISTS = "Файл с таким именем уже существует!";
    public static final String ERR_UPLOAD = "Ошибка загрузки файла!";
    public static final String FILE_NOT_FOUND = "Файл с таким именем не найден!";
    public static final String ERR_DELETE = "Ошибка удаления файла!";
    public static final String ERR_EDIT_NAME = "Не удалось изменить имя файла!";
    /** сообщения об ошибках по пользователям **/
    public static final String USR_NOT_FOUND = "Пользователь не найден!";
    public static final String WRN_USR_PASS = "Неверный пароль!";
    // НАСТРОЙКИ
    /** файл настроек для логера **/
    public static final String LOG_SETT_FILE = "settings.json";
}
