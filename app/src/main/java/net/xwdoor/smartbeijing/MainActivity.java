package net.xwdoor.smartbeijing;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import net.xwdoor.smartbeijing.fragment.ContentFragment;
import net.xwdoor.smartbeijing.fragment.LeftMenuFragment;

public class MainActivity extends SlidingFragmentActivity {

    private static final String TAG_FRAGMENT_CONTENT = "Tag_Fragment_Content";
    private static final String TAG_FRAGMENT_LEFT_MENU = "Tag_Fragment_Left_Menu";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        
        setBehindContentView(R.layout.left_menu);
        SlidingMenu slidingMenu = getSlidingMenu();
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        slidingMenu.setBehindOffset(500);

        initFragment();
    }

    private void initFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_content, new ContentFragment(), TAG_FRAGMENT_CONTENT);
        transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(), TAG_FRAGMENT_LEFT_MENU);
        transaction.commit();
    }

    public LeftMenuFragment getLeftMenuFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(TAG_FRAGMENT_LEFT_MENU);
        return (LeftMenuFragment) fragment;
    }

    public ContentFragment getContentFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment) fragmentManager.findFragmentByTag(TAG_FRAGMENT_CONTENT);
        return fragment;
    }
}
