
    package com.example.musicplayer;

    public class Song {

        private String title;
        private String artist;
        private String audioPath;
        private String videoPath;

        public Song(String title, String artist,
                    String audioPath, String videoPath) {

            this.title = title;
            this.artist = artist;
            this.audioPath = audioPath;
            this.videoPath = videoPath;
        }

        public String getTitle() {
            return title;
        }

        public String getArtist() {
            return artist;
        }

        public String getAudioPath() {
            return audioPath;
        }

        public String getVideoPath() {
            return videoPath;
        }
    }

