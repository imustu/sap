package bio.parsimony;


import java.io.*;
import java.util.*;

public class GetTreesFromNetwork {
    public static File output;
    public static PrintWriter outt;

    public static int[] number = new int[1];

    public int getScore(Network network_, int geneNum) throws Exception {//k is the number of reticulation node
        output = new File("tree.tree");
        outt = new PrintWriter(new FileOutputStream(output));
        PrintN print = new PrintN();
        List<Integer> edgeNumbers = new ArrayList<Integer>();
        List<Integer> list = new ArrayList<>();
        List<Integer> list_forKmax = new ArrayList<>();
        List<Integer> listTemp = new ArrayList<>();
        network_.trees.clear();
        int nodeNum = network_.getRoot();
        int y = -1;

        int k;
        k = network_.k;
        if (k == 0) {
            int AllScore = 0;
            Tree tree = new Tree();
            tree.edges = network_.edges;
            tree.nodes = network_.nodes;
            tree.postOrderTraverse(tree.getRoot());
            print.print(network_, outt, number);
            System.out.println("只有1棵树:" + tree);
            for (int geneTH = 0; geneTH < geneNum; geneTH++) {
                int score1 = getScore(tree, geneTH, nodeNum);
                AllScore += score1;
                System.out.println("第" + (geneTH + 1) + "个基因在物种树上的简约值：" + score1);
            }
            network_.UseTreeCount.add(1);
            return AllScore;
            /*network_.addTree(tree);*/
        } else {
            int AllScore = 0;
            Set<String> choose = new TreeSet<>();
            int data[] = new int[k + 1];
            int n;
            String s = "";
            //2^k=8
            n = (int) Math.pow(2, k);
            int[][] arr = new int[geneNum][n];
            int num[] = new int[n];
            int num_forKmax[] = new int[n];
            int sum_forKmax[] = new int[n];
            while (choose.size() != n) {
                for (int i = 1, j = 1; i <= k; i++, j += 2) {
                    double d = Math.random();
                    //System.out.println("d:" + d);
                    if (d <= 0.5) {
                        data[i] = j;
                    } else {
                        data[i] = j + 1;
                    }
                    if (i != k)
                        s += data[i] + "-";
                    else s += data[i];
                }
                //if(!choose.contains(s))
                choose.add(s);
                s = "";
            }
            System.out.println(choose);
            List choose_1 = new ArrayList(choose);
            String temp;
            print.print(network_, outt, number);
            /*i是第i棵树*/
            for (int i = 0; i < n; i++) {
                temp = (String) choose_1.get(i);
                String[] result = temp.split("-");

                Network new_Network = null;
                new_Network = (Network) network_.clone();

                for (String r : result) {
                    int x = Integer.valueOf(r);
                    edgeNumbers.add(x);
                    //System.out.println("分割结果是: " + x);
                }
                System.out.println("deleteEdgeNumbers:" + edgeNumbers);


                for (int j = 0; j < k; j++) {
                    /*判断edgeNumber的删除顺序，最先删除两顶点都不是RNode的边-start*/
                    y = setDelSequence(edgeNumbers, new_Network);
                    /*判断edgeNumber的删除顺序，最先删除两顶点都不是RNode的边-end*/
                    if (y != -1) {
                        GetTreesOperation getTreesOperation = new GetTreesOperation();
                        Edge edge = new_Network.getEdgeThroughNumber(y);
//System.out.println("能否得到number=" + y + "的边:" + edge);
                        getTreesOperation.setParameters(new_Network, edge, null, null);
                        new_Network = (Network) getTreesOperation.performOperation().clone();
                    } else {
                        AllScore = Integer.MAX_VALUE;
                        return AllScore;
                    }//not find a edgeSequence!
                }
/*原写法
                for (int j = 0; j < edgeNumbers.size(); j++) {
                    GetTreesOperation getTreesOperation = new GetTreesOperation();
                    Edge edge = new_Network.getEdgeThroughNumber(edgeNumbers.get(j));
                    System.out.println("能否得到number=" + edgeNumbers.get(j) + "的边:" + edge);
                    getTreesOperation.setParameters(new_Network, edge, null, null);
                    new_Network = (Network) getTreesOperation.performOperation().clone();
                }*/
                edgeNumbers.clear();
                System.out.println("--------------");

                Tree tree = new Tree();
                tree.nodes = new_Network.nodes;
                tree.edges = new_Network.edges;
                print.print(new_Network, outt, number);
                outt.flush();


                //后序遍历及打印
                tree.postOrderTraverse(tree.getRoot());
/*                        Iterator print1 = tree.postOrderTraverse.iterator();
                        while (print1.hasNext()) {
                            System.out.println(print1.next());
                        }*/
//System.out.println("第" + (i + 1) + "棵树:" + tree);

                for (int geneTH = 0; geneTH < geneNum; geneTH++) {
                    int score1 = getScore(tree, geneTH, nodeNum);
                    if (score1 == Integer.MAX_VALUE) {
                        network_.mark = -1;
                    }
                    arr[geneTH][i] = score1;
                    System.out.println("第" + (geneTH + 1) + "个基因在第" + (i + 1) + "棵树的简约值：" + score1);
                    //oneTreeScoreforEveryGene.add(score1);
                }


            }
            ArrayList<String> array = toArrayByFile("tree.tree");
            network_.trees_String = new ArrayList<String>(array);
            int score = 0;
            for (int p = 0; p < geneNum; p++) {
                for (int q = 0; q < n; q++)
                    list.add(arr[p][q]);
                listTemp.addAll(list);
                score = Collections.min(list);
                if (Collections.frequency(list, score) == 1) {
                    num[list.indexOf(score)]++;
                }
                //阈值缩减代码-start
                else {
                    Random random = new Random();
                    int frequency = Collections.frequency(listTemp, score);
                    int random_forKmax = random.nextInt(frequency);

                    for (int x = 0; x < n; x++) {
                        if (listTemp.get(x) == score)
                            list_forKmax.add(x);
                    }

                    num_forKmax[list_forKmax.get(random_forKmax)]++;
                }
                //阈值缩减代码-end
                list.clear();
                listTemp.clear();

                AllScore += score;
            }
            int count = 0;
            int count_forKmax = 0;
            for (int p = 0; p < n; p++) {
                network_.UseTreeCount.add(num[p]);
//System.out.println("第" + (p + 1) + "棵树有多少个基因在其上进化计数：" + num[p]);
                if (num[p] == 0) {
                    count++;

                }
                sum_forKmax[p] = num[p] + num_forKmax[p];
//System.out.println("第" + (p + 1) + "棵树有多少个基因在其上进化计数_forKmax：" + sum_forKmax[p]);
                if (sum_forKmax[p] == 0) {
                    count_forKmax++;
                }


            }
            network_.UselessTreeCount = count;
            network_.UselessTreeCount_forKmax = count_forKmax;
            return AllScore;
        }


    }

    public static ArrayList<String> toArrayByFile(String filePath) {
        // 使用ArraryList用来存取每行读取到的数据
        ArrayList<String> arrayList = new ArrayList<>();

        // 获取文件
        try {
            File file = new File(filePath);
            InputStreamReader streamReader;
            streamReader = new InputStreamReader(new FileInputStream(file));
            BufferedReader bf = new BufferedReader(streamReader);
            String str;
            while ((str = bf.readLine()) != null) {
                //System.out.println(str);
                // 如果不需要去掉后去则arrayList.add(str);
                arrayList.add(str);
                //arrayList.add(str.substring(0, str.lastIndexOf(".")));

            }
            bf.close();
            streamReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public int setDelSequence(List<Integer> edgeNumbers, Network network) throws Exception {
        int y = -1, x;
        int i = 0;
        while (i < edgeNumbers.size()) {
            y = edgeNumbers.get(i);

            Edge edge = network.getEdgeThroughNumber(y);
            //如果删的时候边集里只有一条边或者边是新加网状边，都可以直接删
            if (edge.isAddREdge || edgeNumbers.size() == 1) {
                edgeNumbers.remove(i);
                return y;
            }
            //如果要删的边是旧网络边，并且要删的边集有至少2条边，需要考虑两种情况，这两种情况不能删，其他都可以直接删
            if (edge.isOldREdge) {
                x = edge.node1.label;
                //排除2种情况:1.起点终点都是网络点，2.起点是网络边起点，并且边是oldedge
                if (!network.getNodeThroughLabel(x).isRNode && x < network.getRoot()) {
                    edgeNumbers.remove(i);
                    return y;
                } else i++;
            }
        }
        return -1;
    }

    public int getScore(Tree tree, int geneTH, int nodeNum) throws Exception {
        int score;
        score = 0;
        if (nodeNum == tree.postOrderTraverse.size()) {
            //geneTH是第几个基因

            //x是序列第几个位点
            for (int x = 0; x < tree.getNodeThroughLabel(1).geneSeq.get(geneTH).length(); x++) {
                for (int y = 0; y < tree.postOrderTraverse.size(); y++) {//遍历节点，y是第几个节点
                    int m = tree.postOrderTraverse.get(y);//m是第y个节点的label号
                    //一个基因序列
                    if (tree.getNodeThroughLabel(m).isLeafNode) {
                        //叶节点直接取
                        tree.getNodeThroughLabel(m).temp.add(tree.getNodeThroughLabel(m).geneSeq.get(geneTH).substring(x, x + 1));
                    } else {
                        //非叶节点
                        int child1 = tree.getNodeThroughLabel(m).children.get(0);
                        int child2 = tree.getNodeThroughLabel(m).children.get(1);
                        List<String> temp_list = new ArrayList<>(tree.getNodeThroughLabel(child1).temp);
                        List<String> temp_list2 = new ArrayList<>(tree.getNodeThroughLabel(child1).temp);
                        temp_list.retainAll(tree.getNodeThroughLabel(child2).temp);
                        if (temp_list.size() != 0) {//有交集取交集
                            tree.getNodeThroughLabel(m).temp.addAll(temp_list);
                        } else {//没交集取并集
                            score += 1;
                            temp_list2.addAll(tree.getNodeThroughLabel(tree.getNodeThroughLabel(m).children.get(1)).temp);
                            tree.getNodeThroughLabel(m).temp = new ArrayList<>(temp_list2);
                        }

                    }

                }
                for (int p = 0; p < tree.nodes.size(); p++)
                    tree.nodes.get(p).temp.clear();

            }
        } else
            score = Integer.MAX_VALUE;//设置一个最大值
        return score;
    }

}
