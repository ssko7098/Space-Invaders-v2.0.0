package invaders.observer;

import java.util.ArrayList;

public abstract class Subject {

    private ArrayList<Observer> observers = new ArrayList<>();

    public void attach(Observer obs) {
        observers.add(obs);
    }

    public void detach(Observer obs) {
        observers.remove(obs);
    }

    public void Notify() {
        for(Observer obs : observers) {
            obs.update();
        }
    }

}
