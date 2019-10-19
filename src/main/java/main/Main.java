package main;

import views.MainMenu;
import views.View;

/*
 * @author Kevin Saephanh
 * */


public class Main {	
	public static void main(String[] args) {
		View currentView = new MainMenu();
		while (currentView != null) {
			currentView = currentView.process();
		}
	}
}
