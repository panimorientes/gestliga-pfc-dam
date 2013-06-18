package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPagerAdapterEst extends FragmentPagerAdapter{
	 
    final int PAGE_COUNT = 5;
 
    //Constructor
    public MyFragmentPagerAdapterEst(FragmentManager fm) {
        super(fm);
    }
 
    // Este método se llamará cuando se vaya a crear una nueva pestaña
    @Override
    public Fragment getItem(int arg0) {
        Bundle data = new Bundle();
        switch(arg0){
            case 0:
                FutbolFragmentEst futbolFragment = new FutbolFragmentEst();
                data.putInt("current_page", arg0+1); //+1
                futbolFragment.setArguments(data);
                return futbolFragment;
            case 1:
                TenisFragmentEst tenisFragment = new TenisFragmentEst();
                data.putInt("current_page", arg0+1);
                tenisFragment.setArguments(data);
                return tenisFragment;
            case 2:
                BaloncestoFragmentEst baloncestoFragment = new BaloncestoFragmentEst();
                data.putInt("current_page", arg0+1);
                baloncestoFragment.setArguments(data);
                return baloncestoFragment;
            case 3:
                PadelFragmentEst padelFragment = new PadelFragmentEst();
                data.putInt("current_page", arg0+1);
                padelFragment.setArguments(data);
                return padelFragment;
            case 4:
                BalonmanoFragmentEst balonmanoFragment = new BalonmanoFragmentEst();
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