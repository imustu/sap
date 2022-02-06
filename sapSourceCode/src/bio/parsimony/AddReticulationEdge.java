package bio.parsimony;

public class AddReticulationEdge extends NetworkOperation {

    @Override
    public Network performOperation() throws Exception {
        // Network tempNetwork=(Network)_network.clone();
        Node node1 = _network.getNodeThroughLabel(_sourceEdge.node1.label);
        Node node2 = _network.getNodeThroughLabel(_sourceEdge.node2.label);
        Node node3 = _network.getNodeThroughLabel(_destinationEdge.node1.label);
        Node node4 = _network.getNodeThroughLabel(_destinationEdge.node2.label);
        Node sourceEdgeNewNode;
        sourceEdgeNewNode = new Node();
        int x = _network.getMaxLable();
        sourceEdgeNewNode.setLabel(x + 1);
        sourceEdgeNewNode.setName(Integer.toString(x + 1));
        _network.addNode(sourceEdgeNewNode);

        _network.removeEdge(_sourceEdge);

               /* System.out.println("_sourceEdge.node1.children"+node1.children);
                System.out.println("_sourceEdge.node2.parents"+node2.parents);*/
        Edge newEdge1OnSource = new Edge(node1, sourceEdgeNewNode);
        //System.out.println("newEdge1OnSource:"+newEdge1OnSource);

        _network.addEdge(newEdge1OnSource);
        Edge newEdge2OnSource = new Edge(sourceEdgeNewNode, node2);

        _network.addEdge(newEdge2OnSource);
        Node destinationEdgeNewNode;
        destinationEdgeNewNode = new Node();
        int y = _network.getMaxLable();
        destinationEdgeNewNode.setLabel(y + 1);
        destinationEdgeNewNode.setName(Integer.toString(y + 1));
        //destinationEdgeNewNode.isRNode=true;
        _network.addNode(destinationEdgeNewNode);
        _network.getNodeThroughLabel(y + 1).isRNode = true;
        _network.removeEdge(_destinationEdge);

        Edge newEdge1OnDestination = new Edge(node3, destinationEdgeNewNode);

        newEdge1OnDestination.isOldREdge = true;
        _network.addEdge(newEdge1OnDestination);
        Edge newEdge2OnDestination = new Edge(destinationEdgeNewNode, node4);

        _network.addEdge(newEdge2OnDestination);
        _targetEdge = new Edge(sourceEdgeNewNode, destinationEdgeNewNode);

        _targetEdge.isAddREdge = true;
        _network.addEdge(_targetEdge);
        _network.k += 1;

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

       /* System.out.println("tempNetwork's edges:::::::::::::::");
        Iterator print3 = tempNetwork.edges.iterator();
        while (print3.hasNext()) {
            System.out.println(print3.next());
        }
        System.out.println("tempNetwork's nodes:::::::::::::::");
        Iterator print4 = tempNetwork.nodes.iterator();
        while (print4.hasNext()) {
            System.out.println(print4.next());
        }*/


        return _network;
    }
}
