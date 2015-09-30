package animelabs.socialise.Utils;

import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Asheesh on 9/29/2015.
 */
public class TouchEffect implements View.OnTouchListener {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN)
        {
            Drawable d=v.getBackground();
            d.mutate();
            d.setAlpha(150);
            v.setBackground(d);
        }
        else if(event.getAction()==MotionEvent.ACTION_CANCEL||event.getAction()==MotionEvent.ACTION_UP)
        {
            Drawable d=v.getBackground();
            d.setAlpha(255);
            v.setBackground(d);
        }
        return false;
    }
}
