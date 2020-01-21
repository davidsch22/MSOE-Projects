package schulzd;

public class SimpleLinkedList<E> {

    private static class Node<E> {
        private E item;
        private Node<E> next;

        private Node(E item) {
            this.item = item;
            this.next = null;
        }

        private Node(E item, Node<E> next) {
            this.item = item;
            this.next = next;
        }
    }

    private Node<E> head = null;

    public boolean isEmpty() {
        return head == null;
    }

    public void addFirst(E item) {
        head = new Node<>(item, head);
    }

    public void clear() {
        head = null;
    }

    public void addLast(E newItem) {
        if ( head == null )
            addFirst(newItem);
        else {
            Node<E> tmp = head;
            while ( tmp.next != null )
                tmp = tmp.next;
            tmp.next = new Node<>(newItem, null);
        }
    }

    // precondition: list not empty
    public E first() {
        E tmp = head.item;
        head = head.next;
        return tmp;
    }

    // precondition: list not empty
    public E last() {
        if ( head.next == null )
            return first();
        else {
            Node<E> tmp = head;
            while ( tmp.next.next != null )
                tmp = tmp.next;
            E item = tmp.next.item;
            tmp.next = null;
            return item;
        }
    }

    public String toString() {
        if ( isEmpty() )
            return "[]";

        String result = "[" + head.item;
        Node<E> node = head.next;
        while ( node != null ) {
            result += ", " + node.item;
            node = node.next;
        }
        return result + "]";
    }

    public static void main(String[] args) {
        // cheap testing
        SimpleLinkedList<String> words = new SimpleLinkedList<>();
        words.addFirst("A");
        words.addFirst("B");
        words.addFirst("C");
        String wordlist = words.toString();
        if ( !wordlist.equals("[C, B, A]") )
            System.out.println("Unexpected word list: " + wordlist);
        String last = words.last();
        if ( !last.equals("A") )
            System.out.println("Wrong last: " + last);
        words.addLast("D");
        words.addLast("E");
        wordlist = words.toString();
        if ( !wordlist.equals("[C, B, D, E]") )
            System.out.println("Unexpected word list after adding D, E: "
                    + wordlist);
        System.out.println("All tests completed.");
    }
}
