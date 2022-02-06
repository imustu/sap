package bio.parsimony;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OneStepOfIteration extends LocalSearch {
    private Random _random;
    public OneStepOfIteration() {
        _random = new Random();
    }
    public Network next(Network network, int operationID, int geneNum, int orderTraverse, int ii) throws Exception {
        Network orginalNetwork = (Network) network.clone();
        int x = setNextMove(network, operationID, orderTraverse, ii);
        if (x == 1) {
            Network newTempNetwork = _networkOperators[operationID].performOperation();
            newTempNetwork.label = network.label + 1;
            //calculate the parsimony score of a network→start
            newTempNetwork.setNumber();
            GetTreesFromNetwork getTreesFromNetwork1 = new GetTreesFromNetwork();
            Network temp14Network = (Network) newTempNetwork.clone();
            newTempNetwork.ParsimonyScore = getTreesFromNetwork1.getScore(temp14Network, geneNum);
            newTempNetwork.UselessTreeCount = temp14Network.UselessTreeCount;
            newTempNetwork.UselessTreeCount_forKmax = temp14Network.UselessTreeCount_forKmax;
            newTempNetwork.trees_String = new ArrayList<String>(temp14Network.trees_String);
            newTempNetwork.tree_genest = new ArrayList<String>(temp14Network.tree_genest);
            //calculate the parsimony score of a network→end
            if (newTempNetwork.ParsimonyScore == Integer.MAX_VALUE)
                return orginalNetwork;
            else
                return newTempNetwork;
        } else {
            orginalNetwork.mark = -1;//it means the current network is the previous network of the current network
            return orginalNetwork;
        }
    }
    public int setNextMove(Network network, int operationID, int orderTraverse, int ii) throws Exception {
        List<Integer> reticulateEdges = new ArrayList<>();
        List<Integer> treeEdges = new ArrayList<>();
        for (Edge edge : network.edges) {
            if (edge.isAddREdge)
                reticulateEdges.add(network.edges.indexOf(edge));
            else treeEdges.add(network.edges.indexOf(edge));
        }
        network.getOriginalEdges(reticulateEdges);
        switch (operationID) {
            case 0:
                int x1 = setParametersForAddReticulationEdge(orderTraverse, network, reticulateEdges, treeEdges, ii);
                if (x1 == -1)
                    return -1;
                else break;
            case 1:
                setParametersForDelReticulationEdge(network, reticulateEdges);
                break;
            case 3:
                int x3 = setParametersForReDestinationOfReticulationEdge(orderTraverse, network, reticulateEdges, treeEdges);
                if (x3 == -1)
                    return -1;
                else break;
            case 2:
                int x2 = setParametersForReSourceOfReticulationEdge(orderTraverse, network, reticulateEdges, treeEdges);
                if (x2 == -1)
                    return -1;
                else break;
            case 4:
                int x4 = setParametersForReverseReticulationEdge(network, reticulateEdges);
                if (x4 == -1)
                    return -1;
                else break;
        }
        return 1;
    }
    public int setParametersForAddReticulationEdge(int orderTraverse, Network network, List<Integer> reticulateEdges, List<Integer> treeEdges, int ii) throws Exception {
        int MAX, leaf_nodes;
        leaf_nodes = (Integer) (network.root + 1) / 2;
        MAX = (leaf_nodes + network.k - 1) * (2 * leaf_nodes + 2 * network.k - 3);
        Network tempNetwork = (Network) network.clone();
        int size = treeEdges.size();
        int source, destination, sourceTemp, destinationTemp, n = 0;
        do {
            n++;
            sourceTemp = _random.nextInt(size);
            destinationTemp = sourceTemp;
            //the two edges are different
            while (sourceTemp == destinationTemp) {
                destinationTemp = _random.nextInt(size);
            }
            source = treeEdges.get(sourceTemp);
            destination = treeEdges.get(destinationTemp);
            if (network.k != 0) {
                if (n > MAX)
                    return -1;
            }
            //judge whether the two edges is appropriate
        } while (notFindEdgesForAddEdge(orderTraverse, network, reticulateEdges, source, destination));
        _networkOperators[0].setParameters(tempNetwork, null, tempNetwork.edges.get(source), tempNetwork.edges.get(destination));
        return 1;
    }
    public void setParametersForDelReticulationEdge(Network network, List<Integer> reticulateEdges) {
        Network tempNetwork = (Network) network.clone();
        int target, targetTemp;
        int size = reticulateEdges.size();
        targetTemp = _random.nextInt(size);
        target = reticulateEdges.get(targetTemp);
        //System.out.println("targetEdge:" + tempNetwork.edges.get(target));
        _networkOperators[1].setParameters(tempNetwork, tempNetwork.edges.get(target), null, null);
    }
    public int setParametersForReDestinationOfReticulationEdge(int orderTraverse, Network network, List<Integer> reticulateEdges, List<Integer> treeEdges) throws Exception {
        int size1, size2, target, destination, targetTemp, destinationTemp, n = 0;
        Network tempNetwork = (Network) network.clone();
        size1 = reticulateEdges.size();
        size2 = treeEdges.size();
        do {
            n++;
            targetTemp = _random.nextInt(size1);
            destinationTemp = _random.nextInt(size2);
            target = reticulateEdges.get(targetTemp);
            destination = treeEdges.get(destinationTemp);
            if (n > 10)
                return -1;
        } while (notFindEdgesForReDestination(orderTraverse, network, reticulateEdges, target, destination));
        _networkOperators[3].setParameters(tempNetwork, tempNetwork.edges.get(target), null, tempNetwork.edges.get(destination));
        return 1;
    }
    public int setParametersForReSourceOfReticulationEdge(int orderTraverse, Network network, List<Integer> reticulateEdges, List<Integer> treeEdges) throws Exception {
        int size1, size2, target, source, targetTemp, sourceTemp, n = 0;
        Network tempNetwork = (Network) network.clone();
        size1 = reticulateEdges.size();
        size2 = treeEdges.size();
        do {
            n++;
            targetTemp = _random.nextInt(size1);
            sourceTemp = _random.nextInt(size2);
            target = reticulateEdges.get(targetTemp);
            source = treeEdges.get(sourceTemp);
            if (n > 10)
                return -1;
        } while (notFindEdgesForReSource(orderTraverse, network, reticulateEdges, target, source));
        _networkOperators[2].setParameters(tempNetwork, tempNetwork.edges.get(target), tempNetwork.edges.get(source), null);
        return 1;
    }
    public int setParametersForReverseReticulationEdge(Network network, List<Integer> reticulateEdges) throws Exception {
        Network tempNetwork = (Network) network.clone();
        int target, targetTemp, n = 0;
        int size = reticulateEdges.size();
        do {
            n++;
            targetTemp = _random.nextInt(size);
            target = reticulateEdges.get(targetTemp);
            if (n > 10)
                return -1;
        } while (notFindEdgesForReverseEdge(network, target));
        _networkOperators[4].setParameters(tempNetwork, tempNetwork.edges.get(target), null, null);
        return 1;
    }
    public boolean notFindEdgesForReverseEdge(Network network, int target) throws Exception {
        int x, y;
        int u1, v1, u2, v2;
        Edge targetEdge = network.edges.get(target);
        y = targetEdge.node1.label;
        x = targetEdge.node2.label;
        u1 = network.findOriginalParent(x);
        v1 = network.findOriginalChild(x);
        u2 = network.findOriginalParent(y);
        v2 = network.findOriginalChild(y);
        /*graphic looping*/
        if (u1 == v2) {
            //System.out.println("true");
            return true;
        }
        if (network.getNodeThroughLabel(u1).allParents.contains(v2)) {
            //System.out.println("true");
            return true;
        }
////System.out.println("u1+v1+u2+v2:" +u1 + ","+ v1 + "," + u2 + ","+v2);
////System.out.println("false");
        return false;
    }
    //随机选边后进行判定
    public boolean notFindEdgesForAddEdge(int orderTraverse, Network network, List<Integer> reticulateEdges, int source, int destination) throws Exception {
        Edge sourceEdge = network.edges.get(source);
        Edge destinationEdge = network.edges.get(destination);
        int m, n, x, y, root, u1, v1, u2, v2;
        root = network.getRoot();
        m = destinationEdge.node1.label;
        n = destinationEdge.node2.label;
        x = sourceEdge.node1.label;
        y = sourceEdge.node2.label;
        if (orderTraverse == 0) {
            if (x <= root)
                u1 = x;
            else u1 = network.findOriginalParent(x);
            if (n <= root)
                v2 = n;
            else v2 = network.findOriginalChild(n);
////System.out.println("u1+v2:" + u1 + "," + v2);
            /*V2 is not a predecessor to U1*/
            if (network.orginalTreePreOrder.indexOf(v2) <= network.orginalTreePreOrder.indexOf(u1)) {
                //System.out.println("true");
                return true;
            }
            if (m <= root)
                u2 = m;
            else u2 = network.findOriginalParent(m);
            if (y <= root)
                v1 = y;
            else v1 = network.findOriginalChild(y);
            //System.out.println("u2+v1:" + u2 + "," + v1);
            /*judge whether there is a reticulate edge between the two edges*/
            network.getOriginalEdges(reticulateEdges);
////System.out.println("network.originalEdges：" + network.originalEdges);
            if (network.k != 0) {
                OriginalEdges oe = new OriginalEdges(u1, v1, u2, v2);
                for (OriginalEdges oes : network.originalEdges) {
////System.out.println("oe:::::::" + oe);
                    if (oe.equals(oes)) {
////System.out.println("true");
                        return true;
                    }
                }
            }
            if (u1 == u2 && v1 == v2) {
                //System.out.println("true");
                return true;
            }
            if (u1 == u2 || v1 == u2 || u1 == v2) {
////System.out.println("true");
                return true;
            }
            if (network.getNodeThroughLabel(u1).allParents.contains(v2) || network.getNodeThroughLabel(u2).allParents.contains(v1)) {
////System.out.println("true");
                return true;
            }
////System.out.println("false");
            return false;
        } else {
            if (x <= root)
                u1 = x;
            else u1 = network.findOriginalParent(x);
            if (n <= root)
                v2 = n;
            else v2 = network.findOriginalChild(n);
////System.out.println("u1+v2:" + u1 + "," + v2);
            if (network.orginalTreePostOrder.indexOf(v2) <= network.orginalTreePostOrder.indexOf(u1)) {
////System.out.println("true");
                return true;
            }
            if (m <= root)
                u2 = m;
            else u2 = network.findOriginalParent(m);
            if (y <= root)
                v1 = y;
            else v1 = network.findOriginalChild(y);
            //System.out.println("u2+v1:" + u2 + "," + v1);
            if (u1 == u2 && v1 == v2) {
                //System.out.println("true");
                return true;
            }
            if (u1 == v2) {
                //System.out.println("true");
                return true;
            }
            if (network.getNodeThroughLabel(u1).allParents.contains(v2)) {
                //System.out.println("true");
                return true;
            }
            network.getOriginalEdges(reticulateEdges);
////System.out.println("network.originalEdges：" + network.originalEdges);
            if (network.k != 0) {
                OriginalEdges oe = new OriginalEdges(u1, v1, u2, v2);
                for (OriginalEdges oes : network.originalEdges) {
////System.out.println("oe:::::::" + oe);
                    if (oe.equals(oes)) {
                        //System.out.println("true");
                        return true;
                    }
                }
            }
            //System.out.println("false");
            return false;
        }
    }
    public boolean notFindEdgesForReDestination(int orderTraverse, Network network, List<Integer> reticulateEdges, int target, int destination) throws Exception {
        Edge targetEdge = network.edges.get(target);
        Edge destinationEdge = network.edges.get(destination);
        int m, n, x, y, root, u1, v1, u2, v2;
        root = network.getRoot();
        m = destinationEdge.node1.label;
        n = destinationEdge.node2.label;
        x = targetEdge.node1.label;
        y = targetEdge.node2.label;
        if (orderTraverse == 0) {
            u1 = network.findOriginalParent(x);
            if (n <= root)
                v2 = n;
            else v2 = network.findOriginalChild(n);
////System.out.println("u1+v2:" + u1 + "," + v2);
            if (network.orginalTreePreOrder.indexOf(v2) <= network.orginalTreePreOrder.indexOf(u1)) {
                //System.out.println("true");
                return true;
            }
            if (m <= root)
                u2 = m;
            else u2 = network.findOriginalParent(m);
            v1 = network.findOriginalChild(x);
////System.out.println("u2+v1:" + u2 + "," + v1);
            network.getOriginalEdges(reticulateEdges);
////System.out.println("network.originalEdges：" + network.originalEdges);
            OriginalEdges oe = new OriginalEdges(u1, v1, u2, v2);
            for (OriginalEdges oes : network.originalEdges) {
////System.out.println("oe:::::::" + oe);
                if (oe.equals(oes)) {
                    //System.out.println("true");
                    return true;
                }
            }
            if (u1 == u2 && v1 == v2) {
                //System.out.println("true");
                return true;
            }
            if (u1 == u2 || v1 == u2 || u1 == v2) {
                //System.out.println("true");
                return true;
            }
            if (network.getNodeThroughLabel(u1).allParents.contains(v2) || network.getNodeThroughLabel(u2).allParents.contains(v1)) {
                //System.out.println("true");
                return true;
            }
            //System.out.println("false");
            return false;
        } else {
            u1 = network.findOriginalParent(x);
            if (n <= root)
                v2 = n;
            else v2 = network.findOriginalChild(n);
////System.out.println("u1+v2:" + u1 + "," + v2);
            if (network.orginalTreePostOrder.indexOf(v2) <= network.orginalTreePostOrder.indexOf(u1)) {
                //System.out.println("true");
                return true;
            }
            if (m <= root)
                u2 = m;
            else u2 = network.findOriginalParent(m);
            v1 = network.findOriginalChild(x);
            //System.out.println("u2+v1:" + u2 + "," + v1);
            network.getOriginalEdges(reticulateEdges);
////System.out.println("network.originalEdges：" + network.originalEdges);
            OriginalEdges oe = new OriginalEdges(u1, v1, u2, v2);
            for (OriginalEdges oes : network.originalEdges) {
////System.out.println("oe:::::::" + oe);
                if (oe.equals(oes)) {
                    //System.out.println("true");
                    return true;
                }
            }
            if (u1 == u2 && v1 == v2) {
                //System.out.println("true");
                return true;
            }
            if (u1 == v2) {
                //System.out.println("true");
                return true;
            }
            if (network.getNodeThroughLabel(u1).allParents.contains(v2)) {
                //System.out.println("true");
                return true;
            }
            //System.out.println("false");
            return false;
        }
    }
    public boolean notFindEdgesForReSource(int orderTraverse, Network network, List<Integer> reticulateEdges, int target, int source) throws Exception {
        Edge targetEdge = network.edges.get(target);
        Edge sourceEdge = network.edges.get(source);
        int m, n, x, y, root, u1, v1, u2, v2;
        root = network.getRoot();
        m = sourceEdge.node1.label;
        n = sourceEdge.node2.label;
        x = targetEdge.node1.label;
        y = targetEdge.node2.label;
        if (orderTraverse == 0) {
            if (m <= root)
                u1 = m;
            else u1 = network.findOriginalParent(m);
            v2 = network.findOriginalChild(y);
////System.out.println("u1+v2:" + u1 + "," + v2);
            if (network.orginalTreePreOrder.indexOf(v2) <= network.orginalTreePreOrder.indexOf(u1)) {
                //System.out.println("true");
                return true;
            }
            u2 = network.findOriginalParent(y);
            if (n <= root)
                v1 = n;
            else v1 = network.findOriginalChild(n);
////System.out.println("u2+v1:" + u2 + "," + v1);
            network.getOriginalEdges(reticulateEdges);
////System.out.println("network.originalEdges：" + network.originalEdges);
            OriginalEdges oe = new OriginalEdges(u1, v1, u2, v2);
            for (OriginalEdges oes : network.originalEdges) {
////System.out.println("oe:::::::" + oe);
                if (oe.equals(oes)) {
                    //System.out.println("true");
                    return true;
                }
            }
            if (u1 == u2 && v1 == v2) {
                //System.out.println("true");
                return true;
            }
            if (u1 == u2 || v1 == u2 || u1 == v2) {
                //System.out.println("true");
                return true;
            }
            if (network.getNodeThroughLabel(u1).allParents.contains(v2) || network.getNodeThroughLabel(u2).allParents.contains(v1)) {
                //System.out.println("true");
                return true;
            }
            //System.out.println("false");
            return false;
        } else {
            if (m <= root)
                u1 = m;
            else u1 = network.findOriginalParent(m);
            v2 = network.findOriginalChild(y);
////System.out.println("u1+v2:" + u1 + "," + v2);
            if (network.orginalTreePostOrder.indexOf(v2) <= network.orginalTreePostOrder.indexOf(u1)) {
                //System.out.println("true");
                return true;
            }
            u2 = network.findOriginalParent(y);
            if (n <= root)
                v1 = n;
            else v1 = network.findOriginalChild(n);
////System.out.println("u2+v1:" + u2 + "," + v1);
            network.getOriginalEdges(reticulateEdges);
////System.out.println("network.originalEdges：" + network.originalEdges);
            OriginalEdges oe = new OriginalEdges(u1, v1, u2, v2);
            for (OriginalEdges oes : network.originalEdges) {
////System.out.println("oe:::::::" + oe);
                if (oe.equals(oes)) {
                    //System.out.println("true");
                    return true;
                }
            }
            if (u1 == u2 && v1 == v2) {
                //System.out.println("true");
                return true;
            }
            if (u1 == v2) {
                //System.out.println("true");
                return true;
            }
            if (network.getNodeThroughLabel(u1).allParents.contains(v2)) {
                //System.out.println("true");
                return true;
            }
            //System.out.println("false");
            return false;
        }
    }
}