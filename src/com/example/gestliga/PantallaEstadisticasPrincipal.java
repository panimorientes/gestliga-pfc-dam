package com.example.gestliga;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import fragments.MyFragmentPagerAdapterEst;

public class PantallaEstadisticasPrincipal extends SherlockFragmentActivity {

	private ActionBar mActionBar;
    private ViewPager mPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pantalla_estadisticas_principal);
		
		/* Se asigna una referencia al ActionBar de esta actividad */
        mActionBar = getSupportActionBar();
 
        /* Se establece el método de navegación entre pestañas */
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
 
        /* Se referencia el ViewPager desde el layout */
        mPager = (ViewPager) findViewById(R.id.pager);
 
        /* Se asigna una referencia al FragmentManager */
        FragmentManager fm = getSupportFragmentManager();
 
        /* Se define un listener para cuando se cambia de deporte */
        ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mActionBar.setSelectedNavigationItem(position);
            }
        };
 
        /* Se define el listener del ViewPager */
        mPager.setOnPageChangeListener(pageChangeListener);
 
        /* Se crea una instancia de MyFragmentPagerAdapterEst */
        MyFragmentPagerAdapterEst fragmentPagerAdapter = new MyFragmentPagerAdapterEst(fm);
 
        /* Se establece el adaptador al ViewPager */
        mPager.setAdapter(fragmentPagerAdapter);
 
        mActionBar.setDisplayShowTitleEnabled(true);
 
        /* Se define el listener */
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
 
            @Override
            public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            }
 
            @Override
            public void onTabSelected(Tab tab, FragmentTransaction ft) {
                mPager.setCurrentItem(tab.getPosition());
            }
 
            @Override
            public void onTabReselected(Tab tab, FragmentTransaction ft) {
            }
        };
 
        Tab tab = mActionBar.newTab()
                .setText("Fútbol")
                .setIcon(R.drawable.futbol)
                .setTabListener(tabListener);
 
        mActionBar.addTab(tab);
 
        tab = mActionBar.newTab()
                .setText("Tenis")
                .setIcon(R.drawable.tenis)
                .setTabListener(tabListener);
 
        mActionBar.addTab(tab);
        
        tab = mActionBar.newTab()
                .setText("Baloncesto")
                .setIcon(R.drawable.baloncesto)
                .setTabListener(tabListener);
 
        mActionBar.addTab(tab);
        
        tab = mActionBar.newTab()
                .setText("Pádel")
                .setIcon(R.drawable.padel)
                .setTabListener(tabListener);
 
        mActionBar.addTab(tab);
        
        tab = mActionBar.newTab()
                .setText("Balonmano")
                .setIcon(R.drawable.balonmano)
                .setTabListener(tabListener);
 
        mActionBar.addTab(tab);
	}
}
