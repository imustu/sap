package bio.parsimony;

public class ReverseEdge extends NetworkOperation {
    @Override
    public Network performOperation() throws Exception {
        Integer m = _targetEdge.node1.label;
        Integer n = _targetEdge.node2.label;
        Node node1 = _network.getNodeThroughLabel(m);
        Node node2 = _network.getNodeThroughLabel(n);

        System.out.println("node2:" + node2 + ",_targetEdge.node2:" + _targetEdge.node2);
        _network.getEdge(_network.getNodeThroughLabel(node2.getAnotherParent(m)), node2).isOldREdge = false;
        Edge newEdge = new Edge(node2, node1);
        newEdge.isAddREdge = true;
        _network.getEdge(_network.getNodeThroughLabel(newEdge.node2.getAnotherParent(newEdge.node1.label)), newEdge.node2).isOldREdge = true;
        newEdge.node2.isRNode = true;
        newEdge.node1.isRNode = false;
        _network.removeEdge(_targetEdge);
        _network.addEdge(newEdge);
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
