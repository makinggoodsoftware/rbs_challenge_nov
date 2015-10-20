package com.mgs.rbsnov.logic;

import java.util.List;
import java.util.Set;

public class SetRotator<C> {
    private final List<Set<C>> toRotate;
    private int currentIndex = 0;

    public SetRotator(List<Set<C>> toRotate) {
        this.toRotate = toRotate;
    }


    public void accept(C toAccept) {
        getCurrent().add(toAccept);
        moveNext();
    }

    private void moveNext() {
        if (this.currentIndex + 1 >= toRotate.size()){
            this.currentIndex = 0;
        } else {
            this.currentIndex ++;
        }
    }

    private Set<C> getCurrent() {
        return toRotate.get(currentIndex);
    }

    public Set<C> get(int index) {
        return toRotate.get(index);
    }

    public List<Set<C>> getSets() {
        return toRotate;
    }
}
