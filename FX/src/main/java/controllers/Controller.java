package controllers;

import interfaces.UserClient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Data;
import models.Key;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Base64;

public class Controller {

    @FXML private TextField login;
    @FXML private PasswordField password;
    @FXML private Button close;

    private Retrofit.Builder builder  = new Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(GsonConverterFactory.create());

    private Retrofit retrofit = builder.build();

    @FXML void authorization(ActionEvent event) {
        if(login.getText().equals("") || password.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Not all fields are filled.");
            alert.show();
        }else {
            String code = "Basic " + Base64.getEncoder().encodeToString((login.getText() + ":" + password.getText()).getBytes());
            UserClient userClient = retrofit.create(UserClient.class);
            Call<Data> call = userClient.getUser(code);
            try {
                Response<Data> response = call.execute();
                if (response.isSuccessful()) {

                    //set password and login
                    Key key = Key.getInstance(code);

                    //close form
                    Stage c = (Stage) close.getScene().getWindow();
                    c.close();

                    //open new form
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/mainForm.fxml"));

                    //create controller
                    MainController mainController = new MainController(response);
                    fxmlLoader.setController(mainController);
                    Parent root1 = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Git");
                    Scene s = new Scene(root1);
                    stage.setScene(s);
                    stage.show();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Authorization error");
                    alert.show();
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }
}
