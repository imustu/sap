package bio.parsimony;
import java.util.List;

public class GetTreesOperation extends NetworkOperation {
    @Override
    public Network performOperation() throws Exception {
        //List<Integer> reticulateEdges= new ArrayList<>();
        Integer m = _targetEdge.node1.label;
        Integer n = _targetEdge.node2.label;
        Node node1 = _network.getNodeThroughLabel(m);
        Node node2 = _network.getNodeThroughLabel(n);
        if (m.equals(_network.getRoot())) {//if a node doesn't have parent nodes ,it is a root node
            Node newDestinationEdgeItem1 = null, newDestinationEdgeItem2 = null, newSourceEdgeItem1 = null;
            newSourceEdgeItem1 = _network.getNodeThroughLabel(node1.getAnotherChild(n));
            newDestinationEdgeItem1 = _network.getNodeThroughLabel(node2.getAnotherParent(m));
            newDestinationEdgeItem2 = _network.getNodeThroughLabel(node2.children.get(0));
            Edge _destinationEdge = new Edge(newDestinationEdgeItem1, newDestinationEdgeItem2);
            _network.addEdge(_destinationEdge);
            _network.removeEdge(_network.getEdge(node1, newSourceEdgeItem1));//(u,u.child)
            _network.removeEdge(_network.getEdge(newDestinationEdgeItem1, node2));//(v.p2,v)
            _network.removeEdge(_network.getEdge(node2, newDestinationEdgeItem2));//(v,v.child)
            _network.removeEdge(_targetEdge);//(u,v)
            _network.removeNode(node1);//u
            _network.removeNode(node2);//v
            _network.k -= 1;
            for (int i = 0; i < _network.nodes.size(); i++) {
                _network.nodes.get(i).parents.clear();
                _network.nodes.get(i).children.clear();
            }
            for (int i = 0; i < _network.edges.size(); i++) {
                _network.setParentsAndChildren(_network.edges.get(i));
                _network.edges.get(i).isOldREdge = false;
            }
            for (int i = 0; i < _network.edges.size(); i++) {
                int xa = _network.edges.get(i).node2.label;
                if ((!_network.edges.get(i).isAddREdge) && _network.getNodeThroughLabel(xa).isRNode) {
                    _network.edges.get(i).isOldREdge = true;
                }
            }
        } else {
            Node newSourceEdgeItem1 = null, newSourceEdgeItem2 = null, newDestinationEdgeItem1 = null, newDestinationEdgeItem2 = null;
            ////System.out.println("node2.parents:" + node2.parents);
            newSourceEdgeItem1 = _network.getNodeThroughLabel(node1.parents.get(0));
            newDestinationEdgeItem1 = _network.getNodeThroughLabel(node2.getAnotherParent(m));
            newSourceEdgeItem2 = _network.getNodeThroughLabel(node1.getAnotherChild(n));
            newDestinationEdgeItem2 = _network.getNodeThroughLabel(node2.children.get(0));
            Edge _sourceEdge = new Edge(newSourceEdgeItem1, newSourceEdgeItem2);
            _network.addEdge(_sourceEdge);
            Edge _destinationEdge = new Edge(newDestinationEdgeItem1, newDestinationEdgeItem2);
            _network.addEdge(_destinationEdge);
            _network.removeEdge(_network.getEdge(newSourceEdgeItem1, node1));
            _network.removeEdge(_network.getEdge(node1, newSourceEdgeItem2));
            _network.removeEdge(_network.getEdge(newDestinationEdgeItem1, node2));
            _network.removeEdge(_network.getEdge(node2, newDestinationEdgeItem2));
            _network.removeEdge(_targetEdge);
            _network.removeNode(node1);
            _network.removeNode(node2);
            _network.k -= 1;
            for (int i = 0; i < _network.nodes.size(); i++) {
                _network.nodes.get(i).parents.clear();
                _network.nodes.get(i).children.clear();
            }
            for (int i = 0; i < _network.edges.size(); i++) {
                _network.setParentsAndChildren(_network.edges.get(i));
                //_network.edges.get(i).isOldREdge=false;
            }
        }
        List<Integer> temp_Rnodes = _network.getRNode();
        Integer x = -1, y = -1;
//assign parameters to the edges of the old network again because the edges in the network changed after deleting an edge
        for (int i = 0; i < temp_Rnodes.size(); i++) {
            Edge Addedge = _network.getAddREdgeThroughRNode(temp_Rnodes.get(i));
            x = Addedge.node1.label;//head node
            if (x != -1)
                y = _network.getNodeThroughLabel(temp_Rnodes.get(i)).getAnotherParent(x);
            if (y != -1)
                _network.getEdge(_network.getNodeThroughLabel(y), _network.getNodeThroughLabel(temp_Rnodes.get(i))).setNumber(Addedge.number + 1);
            _network.getEdge(_network.getNodeThroughLabel(y), _network.getNodeThroughLabel(temp_Rnodes.get(i))).isOldREdge = true;
        }
        return _network;
    }
}