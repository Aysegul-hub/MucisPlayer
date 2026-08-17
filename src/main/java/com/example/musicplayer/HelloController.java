package com.example.musicplayer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;




public class HelloController {

    @FXML
    private Label songTitleLabel;

    @FXML
    private Label artistLabel;

    @FXML
    private Label currentTimeLabel;

    @FXML
    private Label totalTimeLabel;

    @FXML
    private Slider progressSlider;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Button playPauseButton;

    @FXML
    private Button previousButton;

    @FXML
    private Button nextButton;

    private MediaPlayer mediaPlayer;


    @FXML
    public void initialize() {

        // ŞARKIYI BUL
        var songUrl = getClass().getResource("/music/song1.mp3");

        if (songUrl == null) {
            System.out.println("Şarkı bulunamadı!");
            return;
        }

        // MEDIA OLUŞTUR
        Media media = new Media(songUrl.toExternalForm());

        // MEDIAPLAYER OLUŞTUR
        mediaPlayer = new MediaPlayer(media);

        // HAZIR OLDUĞUNDA
        mediaPlayer.setOnReady(() -> {

            double totalSeconds =
                    mediaPlayer.getTotalDuration().toSeconds();

            // ŞARKI SÜRESİ
            progressSlider.setMin(0);
            progressSlider.setMax(totalSeconds);

            totalTimeLabel.setText(
                    formatTime(totalSeconds)
            );

            // ŞARKI OYNADIKÇA SLIDER İLERLESİN
            mediaPlayer.currentTimeProperty().addListener(
                    (obs, oldTime, newTime) -> {

                        if (!progressSlider.isValueChanging()) {

                            progressSlider.setValue(
                                    newTime.toSeconds()
                            );
                        }

                        currentTimeLabel.setText(
                                formatTime(newTime.toSeconds())
                        );
                    }
            );
        });


        // MEDIAPLAYER HATASI
        mediaPlayer.setOnError(() ->
                System.out.println(
                        "MediaPlayer HATASI: "
                                + mediaPlayer.getError()
                )
        );

        // MEDIA HATASI
        media.setOnError(() ->
                System.out.println(
                        "Media HATASI: "
                                + media.getError()
                )
        );

        // PLAY BAŞLANGIÇTA KAPALI
        mediaPlayer.setAutoPlay(false);


        // =========================
        // İLERİ / GERİ SARMA
        // =========================

        progressSlider.valueProperty().addListener(
                (obs, oldValue, newValue) -> {

                    if (progressSlider.isValueChanging()) {

                        mediaPlayer.seek(
                                javafx.util.Duration.seconds(
                                        newValue.doubleValue()
                                )
                        );
                    }
                }
        );


        // =========================
        // SES
        // =========================

        volumeSlider.setMin(0);
        volumeSlider.setMax(1);
        volumeSlider.setValue(0.5);

        mediaPlayer.setVolume(0.5);

        volumeSlider.valueProperty().addListener(
                (obs, oldValue, newValue) -> {

                    mediaPlayer.setVolume(
                            newValue.doubleValue()
                    );
                }
        );
    }

    private String formatTime(double seconds) {
        int minutes = (int) seconds / 60;
        int secs = (int) seconds % 60;

        return String.format("%02d:%02d", minutes, secs);
    }


    // PLAY / PAUSE
    @FXML
    private void playPause() {

        if (mediaPlayer == null) {
            return;
        }

        if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {

            mediaPlayer.pause();
            playPauseButton.setText("Play");

        } else {

            mediaPlayer.play();
            playPauseButton.setText("Pause");
        }
    }


    // ÖNCEKİ
    @FXML
    private void previousSong() {

        if (mediaPlayer == null) {
            return;
        }

        mediaPlayer.seek(javafx.util.Duration.ZERO);
    }


    // SONRAKİ
    @FXML
    private void nextSong() {

        if (mediaPlayer == null) {
            return;
        }

        mediaPlayer.seek(javafx.util.Duration.ZERO);

        System.out.println("Sonraki şarkıya geçilecek.");
    }



}