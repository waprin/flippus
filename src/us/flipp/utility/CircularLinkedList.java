package us.flipp.utility;

import us.flipp.simulation.Player;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class CircularLinkedList<T> {

    private List<T> list;
    private Iterator<T> current;

    public CircularLinkedList () {
        this.list = new LinkedList<T>();
        current = null;
    }

    public List<T> getList() {
        return this.list;
    }

    public void add(T t) {
        this.list.add(t);
        current = null;
    }

    public T getNext()  {
        if (list.isEmpty()) {
            return null;
        }
        if (current == null) {
            current = list.iterator();
            return current.next();
        }
        if (!current.hasNext())
            current = list.iterator();
        return current.next();
    }
}
