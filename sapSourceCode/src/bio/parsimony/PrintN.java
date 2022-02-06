package bio.parsimony;

import java.io.PrintWriter;

public class PrintN {

    public void print(Network network, PrintWriter out, int[] num) throws Exception {

        network.setRnodesName();
        Node digraph = network.getNodeThroughLabel(network.getRoot());
        //System.out.println("digraph:"+digraph);
        PrintTree(network, digraph, out, num);
        System.out.println(';');
        out.write(';');
        out.println();
        out.flush();
    }

    public static void PrintTree(Network network, Node digraph, PrintWriter out, int[] num) throws Exception {
        if (digraph.parents.size() < 2) {//except reticulate node
            if (digraph.children.size() == 0) {//leaf nodes
                String str = digraph.name;
                System.out.print(str);
                out.print(str);

                return;
            }
            System.out.print('(');
            out.write('(');


            if (network.getNodeThroughLabel(digraph.children.get(0)).isRNode) {
                if (IsFromSameAddEdge(network, digraph.label, digraph.children.get(0)).equals(true))
                    network.getNodeThroughLabel(digraph.children.get(0)).visited = "true";
                else
                    network.getNodeThroughLabel(digraph.children.get(0)).visited = "false";
            }
            PrintTree(network, network.getNodeThroughLabel(digraph.children.get(0)), out, num);
            if (digraph.children.size() == 2) {
                System.out.print(',');
                out.write(',');

                if (network.getNodeThroughLabel(digraph.children.get(1)).isRNode) {
                    if (IsFromSameAddEdge(network, digraph.label, digraph.children.get(1)).equals(true))
                        network.getNodeThroughLabel(digraph.children.get(1)).visited = "true";
                    else
                        network.getNodeThroughLabel(digraph.children.get(1)).visited = "false";
                }
                PrintTree(network, network.getNodeThroughLabel(digraph.children.get(1)), out, num);


            }
            System.out.print(')');
            out.write(')');

        }
        //reticulate node

        if (digraph.isRNode && digraph.visited.equals("false")) {//parents and children
            System.out.print('(');
            out.write('(');
            //digraph.visited=false;
            if (network.getNodeThroughLabel(digraph.children.get(0)).isRNode) {
                if (IsFromSameAddEdge(network, digraph.label, digraph.children.get(0)).equals(true))
                    network.getNodeThroughLabel(digraph.children.get(0)).visited = "true";
                else
                    network.getNodeThroughLabel(digraph.children.get(0)).visited = "false";
            }
            PrintTree(network, network.getNodeThroughLabel(digraph.children.get(0)), out, num);
            System.out.print(')');
            out.write(')');
            System.out.print('#');
            out.write('#');
            System.out.print(digraph.name);
            out.write(digraph.name);
        }
        if (digraph.isRNode && digraph.visited.equals("true")) {//siblings
/*			if(!digraph.visited&&digraph.children.size()==1&&digraph.children.get(0)<network.getRoot()){
				PrintTree(network, network.getNodeThroughLabel(digraph.children.get(0)), out, num);
			}*/
            System.out.print('#');
            out.write('#');
            System.out.print(digraph.name);
            out.write(digraph.name);

        }
    }

    public static Boolean IsFromSameAddEdge(Network network, Integer node1, Integer node2) throws Exception {
        Integer x = network.getAddREdgeThroughRNode(node2).node1.label;
        if (x.equals(node1))
            return true;
        else
            return false;
    }

}