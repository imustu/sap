package bio.parsimony;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Node implements Serializable, Cloneable {
    public String visited = "";
    String name;
    Integer label;
    public Boolean isLeafNode = false;
    Boolean isRNode = false;//是否网状顶点
    public List<String> geneSeq = new ArrayList<String>();
    /*                    public List<Node> parents=new ArrayList<Node>();
                    public List<Node> x=new ArrayList<Node>();
                    public List<Node> children=new ArrayList<Node>();*/
    public List<Integer> parents = new ArrayList<Integer>();
    public List<Integer> allParents = new ArrayList<Integer>();
    public List<Integer> x = new ArrayList<Integer>();
    public List<Integer> children = new ArrayList<Integer>();
    public List<String> temp = new ArrayList<>();
    /*//初始化为-1，即没有
    int parent1=-1;
    int parent2=-1;
    int child1=-1;
    int child2=-1;
    public int getParent1() {
        return parent1;
    }
    public void setParent1(int parent1) {
        this.parent1 = parent1;
    }
    public int getParent2() {
        return parent2;
    }
    public void setParent2(int parent2) {
        this.parent2 = parent2;
    }
    public int getChild1() {
        return child1;
    }
    public void setChild1(int child1) {
        this.child1 = child1;
    }
    public int getChild2() {
        return child2;
    }
    public void setChild2(int child2) {
        this.child2 = child2;
    }*/
    @Override
    public Object clone() {
        Node node_a = null;
        try {
            node_a = (Node) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        node_a.parents = new ArrayList<Integer>(parents);
        node_a.x = new ArrayList<Integer>(x);
        node_a.children = new ArrayList<Integer>(children);
        return node_a;
    }
    public Integer getAnotherParent(Integer l) throws Exception {
        x = new ArrayList<>(parents);
////System.out.println("parents:"+parents+"x:"+x);
        if (parents.size() == 2) {
            x.remove(l);
            return x.get(0);
        } else throw new Exception("节点" + label + "父节点个数不为2");
    }
    //对于原始树中的节点
    public Integer getLorRChild(int child, List<Integer> orginalTreePostOrder) throws Exception {
        int child1, child2;
        if (children.size() == 2) {
            child1 = children.get(0);
            child2 = children.get(1);
            if (child == 0) {
                if (orginalTreePostOrder.indexOf(child1) < orginalTreePostOrder.indexOf(child2))
                    return child1;
                else return child2;
            } else {
                if (orginalTreePostOrder.indexOf(child1) > orginalTreePostOrder.indexOf(child2))
                    return child1;
                else return child2;
            }
        } else if (children.size() == 0) {
            return null;
        } else throw new Exception("this node just have one child");
    }
    public Integer getLChild() throws Exception {
        if (children.size() == 2) {
            if (children.get(0) < children.get(1))
                return children.get(0);
            else return children.get(1);
        } else if (children.size() == 0) {
            return null;
        }
/*
        else throw new Exception("this node just have one child");
*/
        else return children.get(0);//网络中有节点（比如网状节点）可以只有一个child
    }
    public Integer getRChild() throws Exception {
        if (children.size() == 2) {
            if (children.get(0) < children.get(1))
                return children.get(1);
            else return children.get(0);
        } else if (children.size() == 0) {
            return null;
        } else throw new Exception("this node just have one child");
    }
    public Integer getAnotherChild(Integer label) {
        x = new ArrayList<>(children);
        x.remove(label);
        return x.get(0);
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setLabel(int label) {
        this.label = label;
    }
    public int getLabel() {
        return label;
    }
    /*    public Iterable<Node> getChildern(){
            return children;
        }*/
/*    public String getParents(){
        Iterator print= parents.iterator();
        String y="";
        while (print.hasNext()) {
            String x=print.next().toString();
            y+=x;
        }
        return y;
    }*/
    @Override
    public String toString() {
        if (isLeafNode) {
            return "taxa_name:" + name + "，its label：" + label + ",isRNode:" + isRNode + ",parents:" + parents + ",allParents:" + allParents;
        } else {
            return "taxa_name:" + name + "，its label：" + label + ",isRNode:" + isRNode + ",parents:" + parents + ",allParents:" + allParents + ",children:" + children;
        }
    }
}