package animelabs.socialise.CustomActivitty;

import android.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import animelabs.socialise.R;
import animelabs.socialise.Utils.TouchEffect;

/**
 * Created by Asheesh on 9/29/2015.
 */
public class CustomActivity extends FragmentActivity implements View.OnClickListener {
    public static final TouchEffect TOUCH = new TouchEffect();
    public void setContentView(int layoutID)
    {
        super.setContentView(layoutID);
        setupActionBar();
    }

    private void setupActionBar() {
        final ActionBar actionBar=getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setLogo(R.drawable.logo);
        actionBar.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.actionbg));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }
    public View setTouchNClick(int id)
    {
        View v=setClick(id);
        if(v!=null)
            v.setOnTouchListener(TOUCH);
        return v;
    }
    public View setClick(int id)
    {
        View v=findViewById(id);
        if(v!=null)
            v.setOnClickListener(this);
        return v;
    }
    @Override
    public void onClick(View v) {

    }
}
