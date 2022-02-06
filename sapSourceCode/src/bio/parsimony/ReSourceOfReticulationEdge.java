package bio.parsimony;

public class ReSourceOfReticulationEdge extends NetworkOperation {

    @Override
    public Network performOperation() throws Exception {
        Node targetEdgeSibling = null, targetEdgeParent = null;
        Integer m = _targetEdge.node1.label;
        Integer n = _targetEdge.node2.label;
        Node node1 = _network.getNodeThroughLabel(m);
        Node node2 = _network.getNodeThroughLabel(n);
        Integer x = _sourceEdge.node1.label;
        Integer y = _sourceEdge.node2.label;
        Node node3 = _network.getNodeThroughLabel(x);//_sourceEdge.node1
        Node node4 = _network.getNodeThroughLabel(y);//_sourceEdge.node2
        targetEdgeSibling = _network.getNodeThroughLabel(node1.getAnotherChild(n));
        targetEdgeParent = _network.getNodeThroughLabel(node1.parents.get(0));
        //delete (w,u1) and（u1,z）
        _network.removeEdge(_network.getEdge(targetEdgeParent, node1));
        _network.removeEdge(_network.getEdge(node1, targetEdgeSibling));
        //add（w,z）
        _destinationEdge = new Edge(targetEdgeParent, targetEdgeSibling);
        _network.addEdge(_destinationEdge);
        //(v2,u1)
        Edge newEdge1OnSource = new Edge(node3, node1);
        //(u1,v2)
        Edge newEdge2OnSource = new Edge(node1, node4);
        _network.addEdge(newEdge1OnSource);
        _network.addEdge(newEdge2OnSource);
        //(u2,v2)
        _network.removeEdge(_sourceEdge);
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
