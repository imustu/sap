package bio.parsimony;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ReadString {
    public Vector<Integer> postTraversal = new Vector<Integer>();
    public List<Edge> edges = new ArrayList<Edge>();
    public List<Node> leavesNames = new ArrayList<Node>();
    public List<Node> nodes = new ArrayList<Node>();
    public List<String> line_Seq = new ArrayList<String>();

    Vector leavesnames = new Vector();
    Network network = new Network();

    public ReadString() {
    }

    public int networkSum = 0;
    public int num = 0;

    //Networks networkss=new Networks();
    public void read1(String filename) throws IOException {
        network.label = 0;
        String string = null;
        BufferedReader br1 = new BufferedReader(new FileReader(filename));
        Stack stack = new Stack();
        string = br1.readLine();
        BufferedReader br = new BufferedReader(new FileReader(filename));
        int record = br.read();

        while (record != -1) {
            char readone = (char) record;
            if (readone == '(') {
                stack.add((char) record);
                string += (char) readone;
                record = br.read();
                continue;
            } else if (readone != ':' && readone != ';' && readone != ')' && readone != ','
                    && readone != '\n' && readone != '\r' && readone != '\t') {
                StringBuffer buf = new StringBuffer();
                do {
                    buf.append((char) record);

                    record = br.read();

                } while ((char) record != ':' && (char) record != ',' && (char) record != ')');
                String taxon = buf.toString();
                if (!leavesnames.contains(taxon)) {
                    leavesnames.add(taxon);
                    num++;

                }
                stack.add(taxon);
                readone = (char) record;
            }
            if (readone == ',') {
                record = br.read();
                readone = (char) record;
                continue;
            }
            if (readone == ')') {
                record = br.read();
                continue;
            }
            if (readone == ';') {
                stack.clear();
                record = br.read();
                continue;
            }
            if (readone == '\n' || readone == '\r' || readone == '\t') {
                record = br.read();
                continue;
            }
        }


        System.out.println("taxaNames-----------" + leavesnames);

    }

    public void read2(String string, int num) throws Exception {

        int x = num;
        int i = 0;
        int sum = 0;

        Stack stack = new Stack();
        //String new11String=newString;
        BufferedReader br = new BufferedReader(new FileReader(string));
        int record = br.read();
        while (record != -1) {
            //转成字符
            char readone = (char) record;

            if (readone == '(') {
                stack.add((char) record);

                record = br.read();
                continue;
            } else if (readone != ':' && readone != ';' && readone != ')' && readone != ','
                    && readone != '\n' && readone != '\r' && readone != '\t') {
                StringBuffer buf = new StringBuffer();
                do {
                    buf.append((char) record);

                    record = br.read();
                } while ((char) record != ':' && (char) record != ',' && (char) record != ')');
                String taxon = buf.toString();
                stack.add(taxon);


                i++;
                sum++;
                Node node = new Node();
                node.setName(taxon);
                node.setLabel(sum);
                node.isLeafNode = true;
                leavesNames.add(node);
                postTraversal.add(node.label);
                nodes.add(node);
                readone = (char) record;
            }
            if (readone == ',') {
                record = br.read();
                readone = (char) record;
                continue;
            }
            if (readone == ')') {
                Node node = new Node();
                num++;
                node.setLabel(num);
                node.setName(Integer.toString(num));
                nodes.add(node);
                leavesNames.add(node);
                postTraversal.add(node.label);
                i++;

                Edge edge1 = new Edge(node, leavesNames.get(i - 2));
                Edge edge2 = new Edge(node, leavesNames.get(i - 3));
                //System.out.println("测试--------"+edge1.equals(edge2));
                edges.add(edge1);
                edges.add(edge2);
                    /*node.setChild1(leavesNames.get(i-2));
                    node.setChild2(leavesNames.get(i-3));*/
                leavesNames.remove(i - 2);
                leavesNames.remove(i - 3);
                i -= 2;
                // String s="("+leavesnames.elementAt(0).toString()+","+leavesnames.elementAt(1).toString()+")";
                // new11String = new11String.replace(s, Integer.toString(num));
                //  System.out.println(s);
                //  System.out.println("new11String"+new11String);
                record = br.read();
                //leavesnames.clear();
                continue;
            }
            if (readone == ';') {

                network.creatNetwork(nodes, edges, 0);
                stack.clear();
                record = br.read();
                networkSum++;

                num = x;
                sum = 0;
                i = 0;
                  /*  leavesNames.clear();
                    nodes.clear();
                    edges.clear();
                    postTraversal.clear();*/
                continue;
            }
            if (readone == '\n' || readone == '\r' || readone == '\t') {
                record = br.read();
                continue;
            }
/*               if(readone==';'){
				stack.clear();
				record=br.read();
				tempclus.clusters.remove(tempclus.taxa);
				clusters.addAll(tempclus);
				tempclus.clusters.clear();
				tempclus.taxa.clear();
				tree++;
				continue;
			}
			if(readone=='\n'||readone=='\r'||readone=='\t'){
				record=br.read();
				continue;
			}*/
        }

        //networkss.addNetwork(networks);
        // System.out.println("total number of networks------"+networkss.label);

        //System.out.println("postTraversal:"+postTraversal);

        network.orginalTreePostOrder = Collections.list(postTraversal.elements());
        network.preOrderTraverse(network.getRoot());
        System.out.println("network.orginalTreePreOrder:" + network.orginalTreePreOrder);
        System.out.println("network.orginalTreePostOrder:" + network.orginalTreePostOrder);
        for (Node node : network.nodes)
            network.orginalTree.nodes.add((Node) node.clone());
        /*for( Node node:network.nodes)
            if(node.parents.size()!=0)
                node.allParents.add(node.parents.get(0));*/

        for (Node node : network.nodes) {
            if (node.parents.size() != 0)
                fun(node, node.parents.get(0));
        }


    }

    void fun(Node a, int b) throws Exception {
        int root = network.getRoot();
        if (b == root) {
            a.allParents.add(b);
            return;
        } else {
            a.allParents.add(b);
            fun(a, network.getNodeThroughLabel(b).parents.get(0));
        }
    }

    public static String listToString(List<String> list) {

        if (list == null) {
            return null;
        }

        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (String string : list) {

            result.append(string);
        }
        return result.toString();

    }

    /*calculate the number of gene sequence alignments*/
    public int read3(String filename_geneSeq, int num) throws Exception {
        //Stack stack = new Stack();
        String temp, string = null;
        String new1;
        char s_temp;
        int geneNum = 0;
        List<String> name = new ArrayList<String>();
        BufferedReader br_0 = new BufferedReader(new FileReader(filename_geneSeq));

        int line = 0;

        while ((temp = br_0.readLine()) != null) {
            if ((line % (leavesnames.size() + 1)) != 0) {
                line_Seq.add(temp);
                for (int i = 0; i < temp.length(); i++) {
                    s_temp = temp.charAt(i);
                    if (s_temp != ' ') {
                        name.add(String.valueOf(s_temp));
                    } else
                        break;
                }
                string = listToString(name);
                name.clear();
                new1 = temp.replaceFirst(string, "");
                new1 = new1.replace(" ", "");

                for (int j = 0; j < network.nodes.size(); j++) {
                    if (network.nodes.get(j).getName().equals(string)) {
                        network.nodes.get(j).geneSeq.add(new1);
                    }
                }

            }
            line++;
        }
        geneNum = network.nodes.get(0).geneSeq.size();
        int x;
        String s = null;
        for (int n = 0; n < geneNum; n++) {
            for (int m = 1; m < num + 1; m++) {
                while (network.getNodeThroughLabel(m).geneSeq.get(n).indexOf('-') != -1) {
                    x = network.getNodeThroughLabel(m).geneSeq.get(n).indexOf('-');
                    for (int k = 1; k < num + 1; k++) {
                        s = network.getNodeThroughLabel(k).geneSeq.get(n);
                        network.getNodeThroughLabel(k).geneSeq.set(n, s.substring(0, x) + s.substring(x + 1));
                    }
                }
            }
        }

        return geneNum;

    }
}
