package bio.parsimony;
import java.util.ArrayList;
import java.util.List;

public class Network implements Cloneable {
    int label = 0;
    int mark = 1;//标记
    int Kmax;
    List<String> tree_genest = new ArrayList<>();
    public List<String> trees_String = new ArrayList<String>();
    public List<Integer> list_forKmax = new ArrayList<Integer>();
    public List<Node> nodes = new ArrayList<Node>();
    public List<OriginalEdges> originalEdges = new ArrayList<>();
    public List<Edge> edges = new ArrayList<Edge>();
    public List<Tree> trees = new ArrayList<Tree>();
    List<Integer> UseTreeCount = new ArrayList<>();//在每棵树上进化的基因有几个，如[1,0,1,0]
    //public List<Integer> postOrderTraverse=new ArrayList<Integer>();
    Tree orginalTree = new Tree();
    public List<Integer> orginalTreePostOrder = new ArrayList<Integer>();
    public List<Integer> orginalTreePreOrder = new ArrayList<Integer>();
    int UselessTreeCount = 0;//有几棵树上没有基因在上面进化
    int UselessTreeCount_forKmax = 0;//有几棵树上没有基因在上面进化(为缩减阈值)
    public int root;
    public int k = 0;//网状节点个数,k=0时是特殊的网络树→树
    public int ParsimonyScore;
    @Override
    public Object clone() {
        Network network_a = null;
        try {
            network_a = (Network) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        network_a.trees.clear();
        List<Node> temp = new ArrayList<Node>();
        for (int i = 0; i < nodes.size(); i++) {
            Node node = (Node) nodes.get(i).clone();
            node.parents = new ArrayList<>(nodes.get(i).parents);
            node.x = new ArrayList<>(nodes.get(i).x);
            node.children = new ArrayList<>(nodes.get(i).children);
            temp.add(node);
        }
        assert network_a != null;
        network_a.nodes = new ArrayList<Node>(temp);
        List<Edge> temp2 = new ArrayList<Edge>();
        for (int i = 0; i < edges.size(); i++) {
            Edge edge = (Edge) edges.get(i).clone();
            temp2.add(edge);
        }
        network_a.edges = new ArrayList<Edge>(temp2);
        network_a.trees_String = new ArrayList<String>(trees_String);
        network_a.UseTreeCount.clear();//清空
        // network_a.postOrderTraverse.clear();//清空后序遍历
        //network_a.preOrderTraverse.clear();//清空前序遍历
        network_a.originalEdges.clear();//清空新加网络边所在的原始边的集合
        //network_a.trees.clear();
        return network_a;
    }
    public Node getNodeThroughLabel(Integer x) throws Exception {//传入某节点的label
        int j = -1;
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).label.equals(x))
                j = i;
        }
        if (j != -1) {
            return nodes.get(j);
        } else {
            throw new Exception("can't find a node which label equals x");
        }
    }
    public void setRnodesName() {
        int num = 0;
        for (Node node : nodes) {
            if (node.isRNode) {
                StringBuffer buf = new StringBuffer();
                buf.append('H');
                buf.append(num);
                num++;
                String string = buf.toString();
                node.name = string;
            }
        }
    }
    public void getOriginalEdges(List<Integer> reticulateEdges) throws Exception {
        int node1, node2, sum = 0;
////System.out.println("reticulateEdges:"+reticulateEdges);
        for (Integer i : reticulateEdges) {
            node1 = edges.get(i).node1.label;
            node2 = edges.get(i).node2.label;
                /*//System.out.println("findOriginalParent(node1):" + findOriginalParent(node1));
                //System.out.println("findOriginalParent(node2):" + findOriginalParent(node2));
                //System.out.println("findOriginalChild(node1):" + findOriginalChild(node1));
                //System.out.println("findOriginalChild(node2):" + findOriginalChild(node2));*/
            OriginalEdges oe = new OriginalEdges(findOriginalParent(node1), findOriginalChild(node1), findOriginalParent(node2), findOriginalChild(node2));
            for (OriginalEdges oes : originalEdges) {
                if (oe.equals(oes))
                    sum += 1;
            }
            if (sum == 0)
                originalEdges.add(oe);
        }
    }
    public int findOriginalParent(int nodeLabel) throws Exception {
        int node1, node2, Root;
        Root = getRoot();
        if (getNodeThroughLabel(nodeLabel).parents.size() == 1) {
            node1 = getNodeThroughLabel(nodeLabel).parents.get(0);
            if (node1 <= Root)
                return node1;
            else
                return findOriginalParent(node1);
        } else if (getNodeThroughLabel(nodeLabel).parents.size() == 2) {
            node1 = getNodeThroughLabel(nodeLabel).parents.get(0);
            node2 = getNodeThroughLabel(nodeLabel).parents.get(1);
/*            if(node1>Root){
                if(node2<=Root)
                    return node2;
                else
                    return findOriginalParent(node2);
            }
            if(node2>Root){
                if(node1<=getRoot())
                    return node1;
                else
                    return findOriginalParent(node1);
            }*/
            if (node2 < node1 && node2 <= Root) {
                return node2;
            }
            if (node1 < node2 && node1 <= Root)
                return node1;
            //node1和node2都>root
            if (getOldREdgeThroughRNode(nodeLabel).node1.label == node1)
                return findOriginalParent(node1);
            if (getOldREdgeThroughRNode(nodeLabel).node1.label == node2)
                return findOriginalParent(node2);
        }
        throw new Exception("新增网络边顶点的原始父节点仅能有1个或2个");
    }
    public int findOriginalChild(int nodeLabel) throws Exception {
        int node1, node2, Root;
        Root = getRoot();
/*            //System.out.println("getNodeThroughLabel(nodeLabel).children:"+getNodeThroughLabel(nodeLabel).children);
        //System.out.println("getNodeThroughLabel(nodeLabel).children.size():"+getNodeThroughLabel(nodeLabel).children.size());*/
        if (getNodeThroughLabel(nodeLabel).children.size() == 1) {
            node1 = getNodeThroughLabel(nodeLabel).children.get(0);
            if (node1 <= Root)
                return node1;
            else
                return findOriginalChild(node1);
        }
        if (getNodeThroughLabel(nodeLabel).children.size() == 2) {
            node1 = getNodeThroughLabel(nodeLabel).children.get(0);
            node2 = getNodeThroughLabel(nodeLabel).children.get(1);
/*            if(node1>Root){
                if(node2<=Root)
                    return node2;
                else
                    return findOriginalChild(node2);
            }
            if(node2>Root){
                if(node1<=Root)
                    return node1;
                else
                    return findOriginalChild(node1);
            }*/
            if (node2 < node1 && node2 <= Root) {
                return node2;
            }
            if (node1 < node2 && node1 <= Root)
                return node1;
            if (getRNodeThroughNRNode(nodeLabel) == node1)
                return findOriginalChild(node2);
            if (getRNodeThroughNRNode(nodeLabel) == node2)
                return findOriginalChild(node1);
        }
        throw new Exception("新增网络边顶点的原始子节点仅能有1个或2个");
    }
    public Edge getOldREdgeThroughRNode(Integer Rnode) throws Exception {
        int j = -1, node2;
        for (int i = 0; i < edges.size(); i++) {
            node2 = edges.get(i).node2.label;
            if (edges.get(i).isOldREdge && getNodeThroughLabel(node2).label.equals(Rnode))
                j = i;
        }
        if (j != -1) {
            return edges.get(j);
        } else {
            throw new Exception("can't find a OldREdge which node2.label equals Rnode");
        }
    }
    public Edge getAddREdgeThroughRNode(Integer Rnode) throws Exception {
        int j = -1, node2;
        for (int i = 0; i < edges.size(); i++) {
            node2 = edges.get(i).node2.label;
            if (edges.get(i).isAddREdge && getNodeThroughLabel(node2).label.equals(Rnode))
                j = i;
        }
        if (j != -1) {
            return edges.get(j);
        } else {
            throw new Exception("can't find a AddREdge which node2.label equals Rnode");
        }
    }
    public int getRNodeThroughNRNode(Integer NRnode) throws Exception {
        int j = -1, node1;
        for (int i = 0; i < edges.size(); i++) {
            node1 = edges.get(i).node1.label;
            if (edges.get(i).isAddREdge && getNodeThroughLabel(node1).label.equals(NRnode))
                j = i;
        }
        if (j != -1) {
            return edges.get(j).node2.label;
        } else {
            throw new Exception("can't find a Node which node1.label equals NRnode");
        }
    }
    public List<Integer> getRNode() {
        List<Integer> RNode = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).isRNode)
                RNode.add(nodes.get(i).label);
        }
        return RNode;
    }
    public Integer getRoot() {
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i).parents.size() == 0) {
                root = nodes.get(i).label;
            }
        }
        return root;
    }
    public void setRoot(int root) {
        this.root = root;
    }
    public Network creatNetwork(List<Node> nodes, List<Edge> edges, int k) {
        Network network = new Network();
        this.edges = edges;
        this.nodes = nodes;
        //execute(list,edges);
        this.k = k;
        return network;
    }
    //获得当前网络中标号最大的节点
    public int getMaxLable() {
        int x = 0;
        for (int i = 0; i < nodes.size(); i++) {
            if (x < nodes.get(i).label)
                x = nodes.get(i).label;
        }
        return x;
    }
    public void setNumber() throws Exception {
        //int m = 1, n = 2;
        Integer n = 1;
        for (int i = 0; i < edges.size(); i++) {
/*            if (edges.get(i).isOldREdge) {
                edges.get(i).setNumber(m);
                m += 2;
            }*/
            if (edges.get(i).isAddREdge) {
                edges.get(i).setNumber(n);
                getOldREdgeThroughRNode(edges.get(i).node2.label).setNumber(n + 1);
                n += 2;
            }
        }
    }
    public Edge getEdgeThroughNumber(int num) throws Exception {
        int j = -1;
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).number == num && (edges.get(i).isAddREdge || edges.get(i).isOldREdge))
                j = i;
        }
////System.out.println("第几条边是number="+num+"的边:"+j);
        if (j != -1) {
            return edges.get(j);
        } else {
            throw new Exception("can't find a edge which number equals num");
        }
    }
    public void addNode(Node node) {
        nodes.add(node);
    }
    public void removeNode(Node node) throws Exception {
        nodes.remove(getNodeThroughLabel(node.label));
    }
    public void addEdge(Edge edge) {
        edges.add(edge);
    }
    public void removeEdge(Edge edge) throws Exception {
        getNodeThroughLabel(edge.node1.label).children.remove(edge.node2.label);
        getNodeThroughLabel(edge.node2.label).parents.remove(edge.node1.label);
        edges.remove(getEdge(getNodeThroughLabel(edge.node1.label), getNodeThroughLabel(edge.node2.label)));
/*            if(edge.node1.child1==edge.node2.label)
            edge.node1.child1=-1;
            else
            edge.node1.child2=-1;
        if(edge.node2.parent1==edge.node1.label)
            edge.node2.parent1=-1;
        else
            edge.node2.parent2=-1;*/
    }
    public Edge getEdge(Node node1, Node node2) {
        int j = 0;
        for (int i = 0; i < edges.size(); i++)
            if (node1.label == edges.get(i).node1.label && node2.label == edges.get(i).node2.label)
                j = i;
        return edges.get(j);
    }
    public Network addTree(Tree tree) {
        Network network1 = new Network();
        trees.add(tree);
        return network1;
    }
    public void setParentsAndChildren(Edge edge) throws Exception {
        Node node1 = getNodeThroughLabel(edge.node1.label);
        Node node2 = getNodeThroughLabel(edge.node2.label);
        int m, n;
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
/*        getNodeThroughLabel(edge.node1.label).children.add(edge.node2.label);
        getNodeThroughLabel(edge.node2.label).parents.add(edge.node1.label);*/
        node2.parents.add(node1.label);
    }
    /*    public void preOrderTraverse(Integer root) throws Exception {
            if (root != null&&!postOrderTraverse.contains(root)) {
                postOrderTraverse.add(root);
                if(getNodeThroughLabel(root).children.size()==2) {
                    preOrderTraverse(getNodeThroughLabel(root).getLChild());
                    preOrderTraverse(getNodeThroughLabel(root).getRChild());
                }
                if(getNodeThroughLabel(root).children.size()==1){
                    preOrderTraverse(getNodeThroughLabel(root).getLChild());
                }
            }
        }
        public void postOrderTraverse(Integer root) throws Exception {
            preOrderTraverse(root);
            Collections.reverse(postOrderTraverse);
        }*/
/*public void postOrderTraverse(Integer root) throws Exception {
    if (root != null&&!postOrderTraverse.contains(root)) {
        if(getNodeThroughLabel(root).children.size()==2) {
            postOrderTraverse(getNodeThroughLabel(root).getLChild());
            postOrderTraverse(getNodeThroughLabel(root).getRChild());
        }
        if(getNodeThroughLabel(root).children.size()==1){
            postOrderTraverse(getNodeThroughLabel(root).getLChild());
        }
        postOrderTraverse.add(root);
    }
}*/
//原始树的前序遍历
    public void preOrderTraverse(Integer root) throws Exception {
        if (root != null && !orginalTreePreOrder.contains(root)) {
            orginalTreePreOrder.add(root);
            if (getNodeThroughLabel(root).children.size() == 2) {
                preOrderTraverse(getNodeThroughLabel(root).getLorRChild(0, orginalTreePostOrder));
                preOrderTraverse(getNodeThroughLabel(root).getLorRChild(1, orginalTreePostOrder));
            }
            if (getNodeThroughLabel(root).children.size() == 1) {
                preOrderTraverse(getNodeThroughLabel(root).getLorRChild(0, orginalTreePostOrder));
            }
        }
    }
    public int getParsimonyScore() {
        return ParsimonyScore;
    }
    public void setParsimonyScore(int ParsimonyScore) {
        this.ParsimonyScore = ParsimonyScore;
    }
    public void reAddAndOldRdges() {
    }
    @Override
    public String toString() {
        return "root label：" + getRoot() + "the number of nodes" + Integer.toString(nodes.size()) + ",the number of edges" + Integer.toString(edges.size());
    }
/*    public Integer execute(List<Node> list,List<Edge> edges) {
        int inDegree = 0;
        for (Node node : list) {
            for (Edge edge : edges) {
                if (edge.node2.equals(node)){
                    inDegree++;
                }
            }
            return inDegree;
        }
    }*/
}