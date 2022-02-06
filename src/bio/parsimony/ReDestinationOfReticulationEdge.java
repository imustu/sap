package bio.parsimony;

public class ReDestinationOfReticulationEdge extends NetworkOperation {

    @Override
    public Network performOperation() throws Exception {
        Integer m = _targetEdge.node1.label;
        Integer n = _targetEdge.node2.label;
        Node node1 = _network.getNodeThroughLabel(m);
        Node node2 = _network.getNodeThroughLabel(n);
        Integer x = _destinationEdge.node1.label;
        Integer y = _destinationEdge.node2.label;
        Node node3 = _network.getNodeThroughLabel(x);//_destinationEdge.node1
        Node node4 = _network.getNodeThroughLabel(y);//_destinationEdge.node2
        Node targetEdgeChild = null, targetEdgeSibling = null;
        targetEdgeChild = _network.getNodeThroughLabel(node2.children.get(0));//z
        targetEdgeSibling = _network.getNodeThroughLabel(node2.getAnotherParent(m));//w
        _sourceEdge = new Edge(targetEdgeSibling, targetEdgeChild);
        _network.addEdge(_sourceEdge);//(w,z)
        _network.removeEdge(_network.getEdge(targetEdgeSibling, node2));//(w,v1)
        _network.removeEdge(_network.getEdge(node2, targetEdgeChild));//(v1,z)
        Edge newEdge1OnDestination = new Edge(node3, node2);
        newEdge1OnDestination.isOldREdge = true;
        _network.addEdge(newEdge1OnDestination);//(u2,x)
        Edge newEdge2OnDestination = new Edge(node2, node4);
        _network.addEdge(newEdge2OnDestination);//(x,v2)
        _network.removeEdge(_destinationEdge);//(u2,v2)
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
        return _network;
    }
}
