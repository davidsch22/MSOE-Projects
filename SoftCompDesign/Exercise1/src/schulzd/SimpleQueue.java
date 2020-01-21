package schulzd;

import java.util.ArrayList;

public class SimpleQueue implements QueueInterface {
    private ArrayList<String> items = new ArrayList<>();

    public boolean isEmpty() {
        return items.size() == 0;
    }

    public void add(String nextItem) {
        items.add(nextItem);
    }

    public void dump() {
        System.out.println("Items on queue:");
        for(int i = 0; i < items.size(); ++i)
            System.out.println("  " + items.get(i));
        System.out.println("---");
    }

    public String get() {
        if ( items.size() == 0 )
            return null;
        else
            return items.remove(0);
    }
}