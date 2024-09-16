package com.javarush.makarenko.cryptoanalyzer.gui;

import com.javarush.makarenko.cryptoanalyzer.CipherLogic;
import com.javarush.makarenko.cryptoanalyzer.FileHandler;
import com.javarush.makarenko.cryptoanalyzer.InputValidator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class GraphicUserInterfaceController {
    private Stage stage;
    private String[] bruteForceTextResult = new String[CipherLogic.shiftRange];

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    //          alert.getDialogPane().setPrefSize(472,472);
    //          изменить цифры как нибудь на переменные

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextArea textAreaDecrypted;
    @FXML
    private TextArea textAreaEncrypted;
    @FXML
    private TextField textFieldShift;
    @FXML
    private Label labelShiftFound;
    @FXML
    private TextField textFieldShiftFound;
    @FXML
    private RadioButton radioButtonInputFile;
    @FXML
    private RadioButton radioButtonOutputFile;
    @FXML
    private TextField textFieldInputPath;
    @FXML
    private TextField textFieldOutputPath;
    @FXML
    private Button buttonPreviousShift;
    @FXML
    private Button buttonNextShift;


    @FXML
    private void initialize() {
        String title = "CaesarCipher - Шифр Цезаря";
        String text = """
                Добро пожаловать в программу, позволяющую шифровать и расшифровывать текст с использованием шифра Цезаря.
                Ввод исходного текста осуществляется через окна или же считывается из выбранного файла.
                Вывод результата осуществляется через окна или же записывается в выбранный файл.
                Файлы должны быть с разрешением .txt и кодировкой UTF-8.
                Программа использует следующий алфавит
                а б в г д е ж з и к л м н о п р с т у ф х ц ч ш щ ъ ы ь э я . , « » " ' : ! ?
                Также учитываются прописные символы.
                Все символы отличные от алфавита игнорируются и их шифрование пропускается.
                Сдвиг программы должен быть в интервале [1;39].
                В случае ошибки введенных данных - выдается сообщение об ошибке с подробностями возникшей ошибки.
                В программе реализован Brute Force алгоритм, который расшифровывает зашифрованный текст без указания сдвига.
                """;
        showMessage(title, text, Alert.AlertType.INFORMATION);
    }

    @FXML
    private void buttonOpenInputFileCLick() {
        // создание экземпляра FileChooser
        FileChooser fileChooser = new FileChooser();

        // открытие диалога выбора файла
        File selectedFile = fileChooser.showOpenDialog(stage);

        // проверка, был ли файл выбран
        if (selectedFile != null) {
            // вывод пути выбранного файла в textFieldInputPath
            textFieldInputPath.setText(selectedFile.getAbsolutePath());

            // переключение radioButtonInputFile
            radioButtonInputFile.setSelected(true);
        }
    }

    @FXML
    private void buttonOpenOutputFileClick() {
        // создание экземпляра FileChooser
        FileChooser fileChooser = new FileChooser();

        // открытие диалога выбора файла
        File selectedFile = fileChooser.showOpenDialog(stage);

        // проверка, был ли файл выбран
        if (selectedFile != null) {
            // вывод пути выбранного файла в textFieldInputPath
            textFieldOutputPath.setText(selectedFile.getAbsolutePath());

            // переключение radioButtonInputFile
            radioButtonOutputFile.setSelected(true);
        }
    }

    @FXML
    private void buttonEncryptClick() {
        boolean isEncryptedOperation = true;
        boolean isBruteForce = false;

        labelShiftFound.setVisible(false);
        textFieldShiftFound.setVisible(false);
        buttonPreviousShift.setVisible(false);
        buttonNextShift.setVisible(false);

        rootPane.setDisable(true);  // блокирование элементов интерфейса

        InputValidator inputValidator = new InputValidator(
                isEncryptedOperation,
                isBruteForce,
                textFieldShift.getText(),
                textAreaDecrypted.getText(),
                textAreaEncrypted.getText(),
                textFieldInputPath.getText(),
                textFieldOutputPath.getText(),
                radioButtonInputFile.isSelected(),
                radioButtonOutputFile.isSelected()
        );

        try {
            if (inputValidator.checkPassed) {
                String text;
                String result;

                if (radioButtonInputFile.isSelected()) {
                    text = FileHandler.readFile(textFieldInputPath.getText());
                    textAreaDecrypted.setText(text);
                } else {
                    text = textAreaDecrypted.getText();
                }
                CipherLogic cipherLogic = new CipherLogic(text, Integer.parseInt(textFieldShift.getText()));
                result = cipherLogic.encrypt();
                if (radioButtonOutputFile.isSelected()) {
                    FileHandler.writeFile(textFieldOutputPath.getText(), result);
                }
                textAreaEncrypted.setText(result);
            } else {
                this.showMessage("Внимание!", inputValidator.checkPassedInfo, Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            this.showMessage("Внимание!", "Что-то пошло не так, пожалуйста, повторите.", Alert.AlertType.ERROR);
        }
        rootPane.setDisable(false);  // разблокирование элементов интерфейса
    }

    @FXML
    private void buttonDecryptClick() {
        boolean isEncryptedOperation = false;
        boolean isBruteForce = false;

        labelShiftFound.setVisible(false);
        textFieldShiftFound.setVisible(false);
        buttonPreviousShift.setVisible(false);
        buttonNextShift.setVisible(false);

        rootPane.setDisable(true);  // блокирование элементов интерфейса

        InputValidator inputValidator = new InputValidator(
                isEncryptedOperation,
                isBruteForce,
                textFieldShift.getText(),
                textAreaDecrypted.getText(),
                textAreaEncrypted.getText(),
                textFieldInputPath.getText(),
                textFieldOutputPath.getText(),
                radioButtonInputFile.isSelected(),
                radioButtonOutputFile.isSelected()
        );

        try {
            if (inputValidator.checkPassed) {
                String text;
                String result;

                if (radioButtonInputFile.isSelected()) {
                    text = FileHandler.readFile(textFieldInputPath.getText());
                    textAreaEncrypted.setText(text);
                } else {
                    text = textAreaEncrypted.getText();
                }
                CipherLogic cipherLogic = new CipherLogic(text, Integer.parseInt(textFieldShift.getText()));
                result = cipherLogic.decrypt();
                if (radioButtonOutputFile.isSelected()) {
                    FileHandler.writeFile(textFieldOutputPath.getText(), result);
                }
                textAreaDecrypted.setText(result);
            } else {
                this.showMessage("Внимание!", inputValidator.checkPassedInfo, Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            this.showMessage("Внимание!", "Что-то пошло не так, пожалуйста, повторите.", Alert.AlertType.ERROR);
        }
        rootPane.setDisable(false);  // разблокирование элементов интерфейса
    }

    @FXML
    private void buttonBruteForceClick() throws IOException {
        boolean isEncryptedOperation = false;
        boolean isBruteForce = true;

        rootPane.setDisable(true);  // блокирование элементов интерфейса

        InputValidator inputValidator = new InputValidator(
                isEncryptedOperation,
                isBruteForce,
                textFieldShift.getText(),
                textAreaDecrypted.getText(),
                textAreaEncrypted.getText(),
                textFieldInputPath.getText(),
                textFieldOutputPath.getText(),
                radioButtonInputFile.isSelected(),
                radioButtonOutputFile.isSelected()
        );

        try {
            if (inputValidator.checkPassed) {
                String text;
                int scoreMaxIndex;

                if (radioButtonInputFile.isSelected()) {
                    text = FileHandler.readFile(textFieldInputPath.getText());
                    textAreaEncrypted.setText(text);
                } else {
                    text = textAreaEncrypted.getText();
                }
                CipherLogic cipherLogic = new CipherLogic(text, bruteForceTextResult);
                scoreMaxIndex = cipherLogic.bruteForce();
                if (radioButtonOutputFile.isSelected()) {
                    FileHandler.writeFile(textFieldOutputPath.getText(), bruteForceTextResult[scoreMaxIndex]);
                }
                textAreaDecrypted.setText(bruteForceTextResult[scoreMaxIndex]);
                textFieldShiftFound.setText(String.valueOf(scoreMaxIndex + 1));
                labelShiftFound.setVisible(true);
                textFieldShiftFound.setVisible(true);
                buttonPreviousShift.setVisible(true);
                buttonNextShift.setVisible(true);
            } else {
                this.showMessage("Внимание!", inputValidator.checkPassedInfo, Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            this.showMessage("Внимание!", "Что-то пошло не так, пожалуйста, повторите.", Alert.AlertType.ERROR);
        }
        rootPane.setDisable(false);  // разблокирование элементов интерфейса
    }

    @FXML
    private void buttonPreviousShift() {
        int index = Integer.parseInt(textFieldShiftFound.getText()) - 1;
        index = (CipherLogic.shiftRange + index - 1) % CipherLogic.shiftRange;
        textAreaDecrypted.setText(bruteForceTextResult[index]);
        textFieldShiftFound.setText(String.valueOf(index + 1));
    }

    @FXML
    private void buttonNextShift() {
        int index = Integer.parseInt(textFieldShiftFound.getText()) - 1;
        index = (CipherLogic.shiftRange + index + 1) % CipherLogic.shiftRange;
        textAreaDecrypted.setText(bruteForceTextResult[index]);
        textFieldShiftFound.setText(String.valueOf(index + 1));
    }

    private void showMessage(String title, String text, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(text);
        alert.getDialogPane().setPrefSize(472, 472);
        alert.showAndWait();
    }
}
