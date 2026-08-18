package com.example.musicplayer;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HelloController {

    // FXML elemanları
    @FXML
    private MediaView mediaView;

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


    // MediaPlayer'lar
    private MediaPlayer mediaPlayer;
    private MediaPlayer videoPlayer;


    // Şarkı listesi
    private List<Song> songs = new ArrayList<>();

    private int currentSongIndex = 0;


    // BAŞLANGIÇ
    @FXML
    public void initialize() {

        volumeSlider.setValue(100);

        volumeSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newValue.doubleValue() / 100);
            }
        });

        songs.add(new Song(
                "Şarkı 1",
                "Sanatçı 1",
                "/music/song1.mp3",
                "/video/video1.mp4"
        ));

        songs.add(new Song(
                "Şarkı 2",
                "Sanatçı 2",
                "/music/song2.mp3",
                "/video/video2.mp4"
        ));

        loadSong(0);
    }


    // ŞARKI YÜKLEME
    private void loadSong(int index) {

        Song song = songs.get(index);

        // Eski müzik player'ı kapat
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }

        // Eski video player'ı kapat
        if (videoPlayer != null) {
            videoPlayer.stop();
            videoPlayer.dispose();
        }


        // Dosyaları bul
        URL audioUrl =
                getClass().getResource(song.getAudioPath());

        URL videoUrl =
                getClass().getResource(song.getVideoPath());


        if (audioUrl == null || videoUrl == null) {
            System.out.println("Şarkı veya video bulunamadı!");
            return;
        }


        // Media oluştur
        Media audioMedia =
                new Media(audioUrl.toExternalForm());

        Media videoMedia =
                new Media(videoUrl.toExternalForm());


        // Player oluştur
        mediaPlayer =
                new MediaPlayer(audioMedia);

        videoPlayer =
                new MediaPlayer(videoMedia);


        // Videoyu ekrana bağla
        mediaView.setMediaPlayer(videoPlayer);


        // Şarkı adı ve sanatçı
        songTitleLabel.setText(song.getTitle());

        artistLabel.setText(song.getArtist());


        // Müzik hazır olduğunda
        mediaPlayer.setOnReady(() -> {

            double totalSeconds =
                    mediaPlayer
                            .getTotalDuration()
                            .toSeconds();

            totalTimeLabel.setText(
                    formatTime(totalSeconds)
            );

            progressSlider.setMin(0);

            progressSlider.setMax(totalSeconds);

            progressSlider.setValue(0);

            currentTimeLabel.setText("00:00");
        });


        // Şarkı ilerledikçe slider ilerlesin
        mediaPlayer.currentTimeProperty().addListener(
                (observable, oldValue, newValue) -> {

                    if (!progressSlider.isValueChanging()) {

                        double seconds =
                                newValue.toSeconds();

                        progressSlider.setValue(seconds);

                        currentTimeLabel.setText(
                                formatTime(seconds)
                        );
                    }
                }
        );


        // Slider ile ileri / geri sarma
        progressSlider.setOnMouseReleased(event -> {

            double seconds =
                    progressSlider.getValue();

            Duration newTime =
                    Duration.seconds(seconds);

            // Müziği sar
            mediaPlayer.seek(newTime);

            // Videoyu da aynı yere sar
            videoPlayer.seek(newTime);
        });


        // Başlangıçta Play sembolü
        playPauseButton.setText("▶");
    }


    // PLAY / PAUSE
    @FXML
    private void playPause() {

        if (mediaPlayer == null ||
                videoPlayer == null) {
            return;
        }


        if (mediaPlayer.getStatus()
                == MediaPlayer.Status.PLAYING) {

            // Müzik dur
            mediaPlayer.pause();

            // Video dur
            videoPlayer.pause();

            // Buton Play olsun
            playPauseButton.setText("▶");

        } else {

            // Müzik başla
            mediaPlayer.play();

            // Video başla
            videoPlayer.play();

            // Buton Pause olsun
            playPauseButton.setText("⏸");
        }
    }


    // SONRAKİ ŞARKI
    @FXML
    private void nextSong() {

        currentSongIndex++;

        if (currentSongIndex >= songs.size()) {
            currentSongIndex = 0;
        }

        loadSong(currentSongIndex);
    }


    // ÖNCEKİ ŞARKI
    @FXML
    private void previousSong() {

        currentSongIndex--;

        if (currentSongIndex < 0) {
            currentSongIndex =
                    songs.size() - 1;
        }

        loadSong(currentSongIndex);
    }


    // SÜREYİ 00:00 FORMATINA ÇEVİR
    private String formatTime(double seconds) {

        int totalSeconds =
                (int) seconds;

        int minutes =
                totalSeconds / 60;

        int secs =
                totalSeconds % 60;

        return String.format(
                "%02d:%02d",
                minutes,
                secs
        );
    }
}