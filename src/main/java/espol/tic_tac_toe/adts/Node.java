package espol.tic_tac_toe.adts;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Node<E> implements Serializable{

    private E content;
    private List<Tree<E>> children;

    public Node(E content) {
        this.content = content;
        this.children = new LinkedList<>();
    }

    public E getContent() {
        return this.content;
    }

    public void setContent(E content) {
        this.content = content;
    }

    public List<Tree<E>> getChildren() {
        return this.children;
    }

    public void addChild(Tree<E> child) {
        this.children.add(child);
    }

    public void addChildren(List<Tree<E>> children) {
        this.children.addAll(children);
    }
}
