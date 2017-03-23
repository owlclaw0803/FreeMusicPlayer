package com.mzdevelopment.freemusicplayer.constants;

import java.util.Random;
import com.mzdevelopment.freemusicplayer.R;

public interface ICloudMusicPlayerConstants
{
    public static final String ADMOB_ID_BANNER = "ca-app-pub-4934154249742370/3703344442";
    public static final String ADMOB_ID_INTERTESTIAL = "ca-app-pub-4934154249742370/5180077648";
    public static final int BUFFER_SIZE = 1024;
    public static final String DATE_PATTERN_CONVERT = "MMM d, yyyy";
    public static final String DATE_PATTERN_ORI = "yyyy/MM/dd hh:mm:ss Z";
    public static final boolean DEBUG = true;
    public static final String FILE_GENRE = "genres/genre_%1$s.dat";
    public static final String FORMAT_URI = "content://media/external/audio/media/%1$s";
    public static final int GENRE_INDEX = 4;
    public static final int HOME_INDEX = 0;
    public static final String KEY_HEADER = "KEY_HEADER";
    public static final String KEY_SONG_ID = "songId";
    public static final String KEY_TYPE = "type";
    public static final int LIBRARY_INDEX = 2;
    public static final int LIST_ICON_MENU[] = {
        R.drawable.ic_home_white_24dp, R.drawable.ic_download
    };
    public static final int LIST_MENU_STRING[] = {
        R.string.title_home, R.string.title_download_songs
    };
    public static final int MAX_PAGE = 14;
    public static final int MAX_RESULT_PER_PAGE = 10;
    public static final int MAX_SONG_TOP = 80;
    public static final String NAME_FOLDER_CACHE = "player";
    public static final String NAME_PLAYLIST_FILE = "list_playlists.dat";
    public static final String NAME_SAVED_TRACK = "list_tracks.dat";
    public static final int NOTIFICATION_ID = 1000;
    public static final int PLAYLIST_INDEX = 3;
    public static final boolean SHOW_ADVERTISEMENT = true;
    public static final String SOUNDCLOUND_CLIENT_ID_[] = {
        "", "9d651646116070d21698223ec10b80f6", "67d575209b7221c90d0285d9665c06fc", "6b5994f8119261e5dfd943399ec23271", "cb59d9390600a23ac5d7ea8b2b4c7f2c", "46d71e69cda553f36d5609f981c04379", "69e3ef51fe186177f16ffbbe26f3b96b", "88f3054ef11a2565c68d8a9eb07e2fd3", "de3d997413841948e43a886cf0adf08f", "5c32a2ac5a02fe65b66d333449982325", 
        "45de06eef99cefb879991c57e6b5e208", "fdf0a7667efd67e55ccabdae62218bd9"
    };
    public static final Random r = new Random();
    public static final int i1 = 1 + r.nextInt(10);
    public static final String SOUNDCLOUND_CLIENT_ID = SOUNDCLOUND_CLIENT_ID_[i1];
    public static final String SOUNDCLOUND_CLIENT_SECRET_[] = {
        "", "c390a63087a735a957ad8cd979ede0a7", "22fd5c9cf9302b2ae48b235416fe9632", "67a5e7560c736f081c3554397aff77e6", "3201bbf416661a731ede0c3811bb27a8", "703cac5590e1dc577f7a6a48b0c1f768", "e985736d43e5f1e2ab53782070293dfe", "2e9d428f18a70348daca2eb7f73aa0e0", "339dce8b5158cb20eac3fbc7c48cd166", "175c52e26eafe28a1437fb9b284a9098", 
        "dbeef179e9e02c5c8acaee5a4a386b0d", "0abc771c284caa656f047b06d2472ad9"
    };
    public static final String SOUNDCLOUND_CLIENT_SECRET = SOUNDCLOUND_CLIENT_SECRET_[i1];
    public static final int TOP_HIT_INDEX = 1;
    public static final String URL_FORMAT_LINK_APP = "https://play.google.com/store/apps/details?id=%1$s";
    public static final String URL_FORMAT_SUGESSTION = "http://suggestqueries.google.com/complete/search?ds=yt&output=toolbar&hl=en-US&q=%1$s";
    public static final String URL_MORE_APPS = "https://play.google.com/store/apps/details?id=kawkaw.callrecorder";
    public static final String URL_YOUR_FACE_BOOK = "https://www.facebook.com/pages/Free-Music-Download-Manager-Player-for-SoundCloud/856065854452495";
    public static final String URL_YOUR_WEBSITE = "http://kawkaw.company";
    public static final String YOUR_EMAIL_CONTACT = "mzdevelopment101@gmail.com";
}
