package fruitNinja.views.pages;

import fruitNinja.models.gameModes.*;
import fruitNinja.models.gameModes.Stratgies.GameStrategy;
import fruitNinja.models.gameModes.StrategyType;
import fruitNinja.models.users.Player;
import fruitNinja.models.users.PlayerSingleton;
import fruitNinja.utils.events.Timer;
import fruitNinja.views.guiUtils.Navigation;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameController implements Initializable {

    @FXML
    public Button pauseButton;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label timerLabel;
    @FXML
    private Label livesLabel;



    @FXML
    private Canvas canvas;

    private Navigation navigation;

    Timer timer;
    private StrategyFactory strategyFactory = new StrategyFactory();
    private Player player;
    private StrategyType strategyType;


    public GameController(StrategyType strategyType)
    {
        this.strategyType = strategyType;
        this.player= PlayerSingleton.getInstance();
    }



    public void countdownStart() {
        timer = new Timer(strategyType, timerLabel);
        //timerLabel.setText(String.valueOf(timer.getSTARTTIME()));
        timer.startTimer();
        timer.updateTimer();
        //to call sth after game is over
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        //todo transfer score and open game done
                        System.out.println("gameover");
                        System.out.println(timer.getSTARTTIME());
                    }
                },
                timer.getSTARTTIME()*1000
        );
    }

    public void pauseButtonClicked(ActionEvent actionEvent) throws IOException {
        PauseDialogController pauseDialog = new PauseDialogController();
        pauseDialog.show(scoreLabel.getScene().getWindow());

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        startGame(strategyType);
        if(!strategyType.equals(StrategyType.CLASSIC)) {
            livesLabel.setVisible(false);
            countdownStart();
        }
        else timerLabel.setVisible(false);
//        startTimer();
//        updateTimer();
//        timerLabel.textProperty().bind(timeSeconds.asString());
    }

    private void startGame(StrategyType strategyType)
    {
        GameStrategy strategy = strategyFactory.createStrategy(strategyType);
        ModeContext modeContext = new ModeContext();
        modeContext.setGameStrategy(strategy);

        modeContext.startGame(canvas);
    }
}
