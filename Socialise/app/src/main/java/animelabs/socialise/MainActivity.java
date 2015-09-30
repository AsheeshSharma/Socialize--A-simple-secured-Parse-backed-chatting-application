package animelabs.socialise;


import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

public class MainActivity extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(this, "xtdycntTLKLLqEDTG7ILRo9VX6xLSfvBrZIr565V", "46hN7rELsIsmk3fh0a1cdllWZXqtLlToxM0V1WBy");

    }
}
