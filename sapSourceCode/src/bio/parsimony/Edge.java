package bio.parsimony;
import java.util.ArrayList;

public class Edge implements Cloneable {
    public Node node1;
    public Node node2;
    public Boolean isAddREdge = false;//true :the edge is a newly added reticulate edge
    public Boolean isOldREdge = false;//true :the edge is not a newly added reticulate edge
    public int number = 0;
    @Override
    public Object clone() {
        Edge edge_a = null;
        try {
            edge_a = (Edge) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
/*            for(int i=0;i<nodes.size();i++){
                edge_a.nodes.add((Node)nodes.get(i));
            }*/
        }
        if (node1 != null) {
            assert edge_a != null;
            edge_a.node1 = (Node) node1.clone();
        }
        if (node2 != null) {
            assert edge_a != null;
            edge_a.node2 = (Node) node2.clone();
        }
        edge_a.node1.parents = new ArrayList<Integer>(node1.parents);
        edge_a.node1.x = new ArrayList<Integer>(node1.x);
        edge_a.node1.children = new ArrayList<Integer>(node1.children);
        edge_a.node2.parents = new ArrayList<Integer>(node2.parents);
        edge_a.node2.x = new ArrayList<Integer>(node2.x);
        edge_a.node2.children = new ArrayList<Integer>(node2.children);
        return edge_a;
    }
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public Edge(Node node1, Node node2) throws Exception {
        int m, n;
        this.node1 = node1;
        this.node2 = node2;
        if (node1.children.size() == 0) {
            node1.children.add(node2.label);
        } else if (node1.children.size() == 1) {
            m = node1.children.get(0);
            n = node2.label;
            if (m < n) {
                node1.children.add(n);
            } else {
                node1.children.remove(0);
                node1.children.add(n);
                node1.children.add(m);
            }
        }
        node2.parents.add(node1.label);
    }
    /*public Node getHead(){
        return node1;
    }
    public Node getTail(){
        return node2;
    }*/
    public String toString() {
        if (isAddREdge) {
            return "(" + node1.label + ", " + node2.label + ")" + ",isAddREdge:" + isAddREdge + ",number:" + number;
        } else if (isOldREdge) {
            return "(" + node1.label + ", " + node2.label + ")" + ",isOldREdge:" + isOldREdge + ",number:" + number;
        } else {
            return "(" + node1.label + ", " + node2.label + ")";
        }
    }
    public boolean equals(Edge edge2) {
        if (edge2.node1.getLabel() == this.node1.getLabel() && edge2.node2.getLabel() == this.node2.getLabel()) {
            return true;
        } else return false;
    }
}
