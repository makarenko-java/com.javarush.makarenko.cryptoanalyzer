package com.javarush.makarenko.cryptoanalyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class InputValidator {

    private static final String ERROR_MESSAGE_SEPARATOR = "\n\t\t\t\t\t\t\t\t\t\t***\n";

    private boolean isEncryptedOperation;
    private boolean isBruteForce;
    private String shift;
    private String textAreaDecrypted;
    private String textAreaEncrypted;
    private String inputPath;
    private String outputPath;
    private boolean isInputFileSelected;
    private boolean isOutputFileSelected;

    public boolean checkPassed;
    public String checkPassedInfo = "";

    public InputValidator(boolean isEncryptedOperation, boolean isBruteForce, String shift, String textAreaDecrypted, String textAreaEncrypted,
                          String inputPath, String outputPath, boolean isInputFileSelected, boolean isOutputFileSelected) {
        this.isEncryptedOperation = isEncryptedOperation;
        this.isBruteForce = isBruteForce;
        this.shift = shift;
        this.textAreaDecrypted = textAreaDecrypted;
        this.textAreaEncrypted = textAreaEncrypted;
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.isInputFileSelected = isInputFileSelected;
        this.isOutputFileSelected = isOutputFileSelected;

        this.check();
    }

    private void check() {

        if (isEncryptedOperation) {
            if (isInputFileSelected & isOutputFileSelected) {                      // шифрование: чтение из файла + запись в файл
                checkPassed = checkShift() & checkPath(inputPath) & checkPath(outputPath);
            }
            if (isInputFileSelected & !isOutputFileSelected) {                     // шифрование: чтение из файла + запись в окно
                checkPassed = checkShift() & checkPath(inputPath);
            }
            if (!isInputFileSelected & isOutputFileSelected) {                     // шифрование: чтение из окна + запись в файл
                checkPassed = checkShift() & checkTextAreaDecrypted() & checkPath(outputPath);
            }
            if (!isInputFileSelected & !isOutputFileSelected) {                    // шифрование: чтение из окна + запись в окно
                checkPassed = checkShift() & checkTextAreaDecrypted();
            }
        } else {
            if (isBruteForce) {
                if (isInputFileSelected & isOutputFileSelected) {                  // BruteForce: чтение из файла + запись в файл
                    checkPassed = checkPath(inputPath) & checkPath(outputPath);
                }
                if (isInputFileSelected & !isOutputFileSelected) {                 // BruteForce: чтение из файла + запись в окно
                    checkPassed = checkPath(inputPath);
                }
                if (!isInputFileSelected & isOutputFileSelected) {                 // BruteForce: чтение из окна + запись в файл
                    checkPassed = checkTextAreaEncrypted() & checkPath(outputPath);
                }
                if (!isInputFileSelected & !isOutputFileSelected) {                // BruteForce: чтение из окна + запись в окно
                    checkPassed = checkTextAreaEncrypted();
                }
            } else {
                if (isInputFileSelected & isOutputFileSelected) {                  // расшифрование: чтение из файла + запись в файл
                    checkPassed = checkShift() & checkPath(inputPath) & checkPath(outputPath);
                }
                if (isInputFileSelected & !isOutputFileSelected) {                 // расшифрование: чтение из файла + запись в окно
                    checkPassed = checkShift() & checkPath(inputPath);
                }
                if (!isInputFileSelected & isOutputFileSelected) {                 // расшифрование: чтение из окна + запись в файл
                    checkPassed = checkShift() & checkTextAreaEncrypted() & checkPath(outputPath);
                }
                if (!isInputFileSelected & !isOutputFileSelected) {                // расшифрование: чтение из окна + запись в окно
                    checkPassed = checkShift() & checkTextAreaEncrypted();
                }
            }
        }
    }

    private boolean checkShift() {
        if (shift.isBlank()) {
            this.checkPassedInfo += "Неверный ключ шифрования.\nКлюч шифрования отсутствует." + ERROR_MESSAGE_SEPARATOR;
            return false;
        }
        try {
            int shiftParse = Integer.parseInt(shift);
            if (shiftParse > 0 && shiftParse <= CipherLogic.shiftRange) {
                return true;
            } else {
                this.checkPassedInfo += "Неверный ключ шифрования.\nЗначение ключа должно быть >0 и <= 39." + ERROR_MESSAGE_SEPARATOR;
                return false;
            }
        } catch (NumberFormatException e) {
            this.checkPassedInfo = this.checkPassedInfo + "Неверный ключ шифрования.\nЗначение ключа должно быть натуральным числом." + ERROR_MESSAGE_SEPARATOR;
            return false;
        }
    }

    private boolean checkTextAreaDecrypted() {
        if (textAreaDecrypted.isBlank()) {
            this.checkPassedInfo += "Ошибка с окном \"Дешифрованный текст\".\nДанные отсутствуют." + ERROR_MESSAGE_SEPARATOR;
            return false;
        } else {
            return true;
        }
    }

    private boolean checkTextAreaEncrypted() {
        if (textAreaEncrypted.isBlank()) {
            this.checkPassedInfo += "Ошибка с окном \"Зашифрованный текст\".\nДанные отсутствуют." + ERROR_MESSAGE_SEPARATOR;
            return false;
        } else {
            return true;
        }
    }

    private boolean checkPath(String filePath) {
        // получаем Path из строки
        Path path = Paths.get(filePath);

        if (filePath.equals(inputPath)) {
            return (checkIsExist(path) && checkIsRegularFile(path) && checkIsTxtExtension(path) && checkIsReadable(path) && checkIsFileNotEmpty(path));
        }
        else {
            return (checkIsExist(path) && checkIsRegularFile(path) && checkIsTxtExtension(path) && checkIsWritable(path));
        }
    }

    // проверяем, что файл существует
    private boolean checkIsExist(Path path) {
        if (Files.exists(path)) {
            return true;
        } else {
            this.checkPassedInfo += ((path.toString().equals(inputPath)) ? "Ошибка с input-файлом.\n" : "Ошибка с output-файлом.\n") + "Файл или каталог не существует." + ERROR_MESSAGE_SEPARATOR;
            return false;
        }
    }

    // проверяем, что это файл, а не директория
    private boolean checkIsRegularFile(Path path) {
        if (Files.isRegularFile(path)) {
            return true;
        } else {
            this.checkPassedInfo += ((path.toString().equals(inputPath)) ? "Ошибка с input-файлом.\n" : "Ошибка с output-файлом.\n") + "Путь является каталогом." + ERROR_MESSAGE_SEPARATOR;
            return false;
        }
    }

    // проверяем имеет ли файл расширение .txt
    private boolean checkIsTxtExtension(Path path) {
        String fileName = path.getFileName().toString();
        if (fileName.toLowerCase().endsWith(".txt")) {
            return true;
        }
        else {
            this.checkPassedInfo += ((path.toString().equals(inputPath)) ? "Ошибка с input-файлом.\n" : "Ошибка с output-файлом.\n") + "Выбранный файл не имеет расширение .txt." + ERROR_MESSAGE_SEPARATOR;
            return false;
        }
    }

    // проверяем, что файл доступен для чтения
    private boolean checkIsReadable(Path path) {
        if (path.toString().equals(inputPath)) {    // если path = inputPath, то выполняется проверка файла на чтение
            if (Files.isReadable(path)) {
                return true;
            } else {
                this.checkPassedInfo += "Ошибка с input-файлом.\nИсходный файл не доступен для чтения." + ERROR_MESSAGE_SEPARATOR;
                return false;
            }
        }
        else {
            return true;                            // если path = outputPath, то проверка файла на чтение пропускается
        }
    }

    // проверяем, что файл доступен для записи
    private boolean checkIsWritable(Path path) {
        if (path.toString().equals(outputPath)) {    // если path = outputPath, то выполняется проверка файла на запись
            if (Files.isWritable(path)) {
                return true;
            } else {
                this.checkPassedInfo += "Ошибка с output-файлом.\nФайл для записи результатов не доступен для записи." + ERROR_MESSAGE_SEPARATOR;
                return false;
            }
        }
        else {
            return true;                            // если path = inputPath, то проверка файла на запись пропускается
        }
    }

    // проверяем, что файл не пустой
    private boolean checkIsFileNotEmpty(Path path) {
        try {
            long fileSize = Files.size(path);
            if (fileSize > 0) {
                return true;
            } else {
                this.checkPassedInfo += ((path.toString().equals(inputPath)) ? "Ошибка с input-файлом.\n" : "Ошибка с output-файлом.\n") + "Файл пуст." + ERROR_MESSAGE_SEPARATOR;
                return false;
            }
        } catch (IOException e) {
            this.checkPassedInfo += ((path.toString().equals(inputPath)) ? "Ошибка с input-файлом.\n" : "Ошибка с output-файлом.\n") + "Произошла ошибка при проверке размера файла." + ERROR_MESSAGE_SEPARATOR;
            return false;
        }
    }
}
