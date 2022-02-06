package bio.parsimony;
import java.util.ArrayList;

public class LocalSearch {
    public NetworkOperation[] _networkOperators;
    public LocalSearch() {
        _networkOperators = new NetworkOperation[5];
        _networkOperators[0] = new AddReticulationEdge();
        _networkOperators[1] = new DelReticulationEdge();
        _networkOperators[2] = new ReSourceOfReticulationEdge();
        _networkOperators[3] = new ReDestinationOfReticulationEdge();
        _networkOperators[4] = new ReverseEdge();
    }
    public Network performRearrangement(Network network, Integer operation, Edge edge1, Edge edge2, int geneNum) throws Exception {
        Network tempNetwork = (Network) network.clone();
        /*test-start*/
        edge1 = tempNetwork.edges.get(0);
        edge2 = tempNetwork.edges.get(6);
        /*test-end*/
        //targetEdge:目标边（网络边）,sorceEdge（网络边起点所在的边）,destinationEdge（网络边终点所在的边）
        if (operation == 0) {
            _networkOperators[operation].setParameters(tempNetwork, null, edge1, edge2);//add a reticulate edge
        } else if (operation == 1 || operation == 4) {
            _networkOperators[operation].setParameters(tempNetwork, edge1, null, null);//delete or reverse a reticulate edge
        } else if (operation == 2) {
            _networkOperators[operation].setParameters(tempNetwork, edge1, edge2, null);//modify the start point of a reticulate edge
        } else if (operation == 3) {
            _networkOperators[operation].setParameters(tempNetwork, edge1, null, edge2);//modify the end point of a reticulate edge
        }
        Network newTempNetwork = _networkOperators[operation].performOperation();
        /*calculate the parsimony score of a network →start*/
        newTempNetwork.setNumber();
        GetTreesFromNetwork getTreesFromNetwork1 = new GetTreesFromNetwork();
        Network temp14Network = (Network) newTempNetwork.clone();
        newTempNetwork.ParsimonyScore = getTreesFromNetwork1.getScore(temp14Network, geneNum);
        newTempNetwork.trees_String = new ArrayList<String>(temp14Network.trees_String);
        newTempNetwork.tree_genest = new ArrayList<String>(temp14Network.tree_genest);
        newTempNetwork.UselessTreeCount = temp14Network.UselessTreeCount;
        //System.out.println("temp14Network.UseTreeCount:" + temp14Network.UseTreeCount);
        //System.out.println("newTempNetwork.UseTreeCount:" + newTempNetwork.UseTreeCount);
        /*calculate the parsimony score of a network →end*/
        return newTempNetwork;
    }
}