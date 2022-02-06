package bio.parsimony;

public class DelReticulationEdge extends NetworkOperation {

    @Override
    public Network performOperation() throws Exception {
        Integer m = _targetEdge.node1.label;
        Integer n = _targetEdge.node2.label;
        Node node1 = _network.getNodeThroughLabel(m);
        Node node2 = _network.getNodeThroughLabel(n);
        Node newSourceEdgeItem1 = null, newSourceEdgeItem2 = null, newDestinationEdgeItem1 = null, newDestinationEdgeItem2 = null;
        newSourceEdgeItem1 = _network.getNodeThroughLabel(node1.parents.get(0));
        newDestinationEdgeItem1 = _network.getNodeThroughLabel(node2.getAnotherParent(m));
        newSourceEdgeItem2 = _network.getNodeThroughLabel(node1.getAnotherChild(n));
        newDestinationEdgeItem2 = _network.getNodeThroughLabel(node2.children.get(0));
        //System.out.println(newSourceEdgeItem1.label + " " + newDestinationEdgeItem1.label + " " + newSourceEdgeItem2.label + " " + newDestinationEdgeItem2.label);
        Edge _sourceEdge = new Edge(newSourceEdgeItem1, newSourceEdgeItem2);
        _network.addEdge(_sourceEdge);
        Edge _destinationEdge = new Edge(newDestinationEdgeItem1, newDestinationEdgeItem2);
        _network.addEdge(_destinationEdge);
/*            System.out.println(_network.getEdge(newSourceEdgeItem1, node1));
            System.out.println(_network.getEdge(node1, newSourceEdgeItem2));
            System.out.println(_network.getEdge(newDestinationEdgeItem1, node2));
            System.out.println(_network.getEdge(node2, newDestinationEdgeItem2));*/
        _network.removeEdge(_network.getEdge(newSourceEdgeItem1, node1));
        _network.removeEdge(_network.getEdge(node1, newSourceEdgeItem2));
        _network.removeEdge(_network.getEdge(newDestinationEdgeItem1, node2));
        _network.removeEdge(_network.getEdge(node2, newDestinationEdgeItem2));
        _network.removeEdge(_targetEdge);
        //System.out.println(m + " " + n);

        _network.removeNode(node1);
        _network.removeNode(node2);
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
        return _network;

    }
}
