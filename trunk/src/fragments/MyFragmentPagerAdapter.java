package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter{
	 
    final int PAGE_COUNT = 5;
 
    //Constructor
    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    // Este método se llamará cuando se vaya a crear una nueva pestaña
    @Override
    public Fragment getItem(int arg0) {
        Bundle data = new Bundle();
        switch(arg0){
            case 0:
                FutbolFragment futbolFragment = new FutbolFragment();
                data.putInt("current_page", arg0+1);
                futbolFragment.setArguments(data);
                return futbolFragment;
            case 1:
                TenisFragment tenisFragment = new TenisFragment();
                data.putInt("current_page", arg0+1);
                tenisFragment.setArguments(data);
                return tenisFragment;
            case 2:
                BaloncestoFragment baloncestoFragment = new BaloncestoFragment();
                data.putInt("current_page", arg0+1);
                baloncestoFragment.setArguments(data);
                return baloncestoFragment;
            case 3:
                PadelFragment padelFragment = new PadelFragment();
                data.putInt("current_page", arg0+1);
                padelFragment.setArguments(data);
                return padelFragment;
            case 4:
                BalonmanoFragment balonmanoFragment = new BalonmanoFragment();
                data.putInt("current_page", arg0+1);
                balonmanoFragment.setArguments(data);
                return balonmanoFragment;
        }
        return null;
    }
 
    //Devuelve el número de pestañas
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}