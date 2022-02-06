package bio.parsimony;

import java.util.ArrayList;
import java.util.List;

public class Tree {
    public List<Node> nodes = new ArrayList<Node>();
    public List<Edge> edges = new ArrayList<Edge>();
    public List<Integer> postOrderTraverse=new ArrayList<Integer>();

    public int root;

public Integer getRoot() {
    for(int i=0;i<nodes.size();i++){
        if(nodes.get(i).parents.size()==0){
            root=nodes.get(i).label;
        }
    }
    return root;
}
    public Node getNodeThroughLabel(Integer x) throws Exception{
        int j=-1;
        for(int i=0;i<nodes.size();i++){
            if(nodes.get(i).label.equals(x))
                j=i;
        }
        if(j!=-1){
            return nodes.get(j);}
        else {
            throw new Exception("can't find a node which label equals x");
        }

    }

public void postOrderTraverse(Integer root) throws Exception {
    if (root != null) {

        postOrderTraverse(getNodeThroughLabel(root).getLChild());
        postOrderTraverse(getNodeThroughLabel(root).getRChild());
        postOrderTraverse.add(root);
    }

}

    @Override
    public String toString() {

        StringBuilder s= new StringBuilder();
        for(Edge edge:edges){
            s.append(edge.toString());
            s.append("\n");
        }

/*        StringBuilder ss= new StringBuilder();
        for(Node node:nodes){
            ss.append(node.toString());
            ss.append("\n");
        }*/
        return "\n"+"edge："+s+"post-order："+postOrderTraverse;
    }
}
