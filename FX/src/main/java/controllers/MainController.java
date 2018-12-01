package controllers;

import interfaces.UserClient;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Data;
import models.Key;
import models.RepositModel;
import models.Repository;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML private Label name;
    @FXML private AnchorPane temp1;
    @FXML private ImageView imagege;
    @FXML private TableView<RepositModel> mainTable;
    @FXML private TableColumn<RepositModel, Integer> number;
    @FXML private TableColumn<RepositModel, String> nameT;
    @FXML private TableColumn<RepositModel, String> desciption;
    @FXML private TableColumn<RepositModel, Integer> watched;
    @FXML private TableColumn<RepositModel, Integer> stars;
    @FXML private TableColumn<RepositModel, Integer> forks;
    @FXML private TextField searchName;
    private ObservableList<RepositModel> usersData = FXCollections.observableArrayList();
    private Response<Data> data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        name.setText(data.body().getLogin());

        number.setCellValueFactory(new PropertyValueFactory<>("number"));
        nameT.setCellValueFactory(new PropertyValueFactory<>("name"));
        desciption.setCellValueFactory(new PropertyValueFactory<>("description"));
        watched.setCellValueFactory(new PropertyValueFactory<>("watched"));
        stars.setCellValueFactory(new PropertyValueFactory<>("stars"));
        forks.setCellValueFactory(new PropertyValueFactory<>("forks"));

        mainTable.setItems(usersData);

        getRepository();
        addContext();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Scene s = name.getScene();
                s.setOnKeyPressed(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent ke) {
                        if (ke.getCode() == KeyCode.F5) {
                            searchRepository("");
                        }
                    }
                });
            }
        });
        try {
            loadImage(data.body().getAvatarUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML void search(ActionEvent event) {
        searchRepository(searchName.getText());
    }

    @FXML void signOut(ActionEvent event) {
        Key.resetCode();

        Stage c = (Stage) name.getScene().getWindow();
        c.close();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/xml/sample.fxml"));
        Parent root1 = null;
        try {
            root1 = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setTitle("Authorization");
        stage.setScene(new Scene(root1, 600, 400));
        stage.show();
    }

    public MainController() {
    }

    public MainController(Response<Data> data) {
        this.data = data;
    }

    private void loadImage(String url) throws IOException {
        URL urlInput = new URL(url);
        BufferedImage urlImage = ImageIO.read(urlInput);
        Image image = SwingFXUtils.toFXImage(urlImage, null);
        imagege.setImage(image);
    }

    private void getRepository(){
        Retrofit.Builder retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit builder = retrofit.build();

        UserClient userClient = builder.create(UserClient.class);
        Call<List<Repository>> repositoryCall = userClient.getRepository(Key.getCode(), data.body().getLogin());
        try {
            Response<List<Repository>> response = repositoryCall.execute();
            if(response.isSuccessful()){
                //System.out.println(response.body());
                for (int i = 0; i < response.body().size(); i++) {
                        usersData.add(new RepositModel(
                                i,
                                response.body().get(i).getName(),
                                (response.body().get(i).getDescription() == null) ? "No description." : response.body().get(i).getDescription().toString(),
                                response.body().get(i).getWatchersCount(),
                                response.body().get(i).getStargazersCount(),
                                response.body().get(i).getForks(),
                                response.body().get(i).getCloneUrl()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void searchRepository(String name){
        Retrofit.Builder retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit builder = retrofit.build();

        UserClient userClient = builder.create(UserClient.class);
        Call<List<Repository>> repositoryCall = userClient.getRepository(Key.getCode(), data.body().getLogin());
        try {
            Response<List<Repository>> response = repositoryCall.execute();
            if(response.isSuccessful()){
                mainTable.getItems().clear();
                //System.out.println(response.body());
                for (int i = 0; i < response.body().size(); i++) {
                    if(response.body().get(i).getName().indexOf(name) != -1) {
                        usersData.add(new RepositModel(i,
                                response.body().get(i).getName(),
                                (response.body().get(i).getDescription() == null) ? "No description." : response.body().get(i).getDescription().toString(),
                                response.body().get(i).getWatchersCount(),
                                response.body().get(i).getStargazersCount(),
                                response.body().get(i).getForks(),
                                response.body().get(i).getCloneUrl()));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addContext(){
        mainTable.setRowFactory(new Callback<TableView<RepositModel>, TableRow<RepositModel>>() {
            @Override
            public TableRow<RepositModel> call(TableView<RepositModel> tableView) {
                final TableRow<RepositModel> row = new TableRow<>();
                final ContextMenu contextMenu = new ContextMenu();
                final MenuItem removeMenuItem = new MenuItem("Copy url");
                removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        //System.out.println();
                        StringSelection stringSelection = new StringSelection(mainTable.getItems().get(mainTable.getSelectionModel().getSelectedIndex()).getUrl());
                        java.awt.datatransfer.Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clpbrd.setContents(stringSelection, null);
                    }
                });
                contextMenu.getItems().add(removeMenuItem);
                // Set context menu on row, but use a binding to make it only show for non-empty rows:
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                                .then((ContextMenu)null)
                                .otherwise(contextMenu)
                );
                return row ;
            }
        });
    }


}
