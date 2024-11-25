package espol.tic_tac_toe.adts;

import java.io.Serializable;
import java.util.List;

public class Tree<E> implements Serializable{

    private Node<E> root;

    public Tree() {
        this.root = null;
    }

    public Tree(E rootContent) {
        this.root = new Node(rootContent);
    }

    public E getRoot() {
        return this.root.getContent();
    }

    public void setRoot(E content) {
        this.root.setContent(content);
    }

    public Node getRootNode() {
        return this.root;
    }

    public List<Tree<E>> getChildren() {
        return this.root.getChildren();
    }

    public void addChild(Tree<E> child) {
        this.root.addChild(child);
    }

    public void addChild(E childContent) {
        this.root.addChild(new Tree<E>(childContent));
    }

    public void addChildren(List<Tree<E>> children) {
        this.root.addChildren(children);
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public boolean isLeaf() {
        return this.root.getChildren().isEmpty();
    }

    @Override
    public String toString() {
        return "Tree{" + "root=" + root.getContent() + '}';
    }

}
