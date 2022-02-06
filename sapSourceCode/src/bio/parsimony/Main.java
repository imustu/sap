package bio.parsimony;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Iterator;

public class Main {
    public static File output;
    public static PrintWriter outt;
    public static String treeFileName;
    public static String geneSeqFileName;
    public static String outFileName;
    public static int[] number = new int[1];
    public static void main(String[] args) throws Exception {
        if (args.length != 3) // we need three parameters
        {
            usageInstruction();
            return;
        }
        number[0] = 0;
        int geneNum = 0;
        treeFileName = args[0];
        geneSeqFileName = args[1];
        outFileName = args[2] + "\\output.txt";
        bio.parsimony.ReadString readString = new ReadString();
        readString.read1(treeFileName);
        int num = readString.num;
        readString.read2(treeFileName, num);
        PrintN print = new PrintN();
        geneNum = readString.read3(geneSeqFileName, num);
        int kMAX = (int) Math.floor(Math.log(geneNum) / Math.log(2));
        readString.network.Kmax = kMAX;
        for (int i = 0; i < kMAX; i++) {
            readString.network.list_forKmax.add(0);
        }
        System.out.println("leavesNum------" + num);
        System.out.println("geneNum----------" + geneNum);
        System.out.println("kMAX----------" + kMAX + '\n');
        System.out.println("root label-----" + readString.network.getRoot());
        System.out.println("total number of nodes:" + readString.nodes.size());
        output = new File(outFileName);
        outt = new PrintWriter(new FileOutputStream(output));
        Iterator print11 = readString.nodes.iterator();
        while (print11.hasNext()) {
            System.out.println(print11.next());
        }
        Iterator print1 = readString.edges.iterator();
        while (print1.hasNext()) {
            System.out.println(print1.next());
       }
        bio.parsimony.GetTreesFromNetwork getTreesFromNetwork3 = new GetTreesFromNetwork();
        readString.network.ParsimonyScore = getTreesFromNetwork3.getScore(readString.network, geneNum);
        bio.parsimony.Network newNetwork = new Network();
        long startTime = System.currentTimeMillis();

            SAP SAP = new SAP();
            newNetwork = SAP.getSA(readString.network, geneNum, 0, 1, num);
            long endTime = System.currentTimeMillis();

            outt.println("running time: " + (double) (endTime - startTime) / 1000 + "s");
            outt.println("number of iterations: " + SAP.getCount());

           System.out.println("network's edges:::::::::::::::");
            Iterator print3 = newNetwork.edges.iterator();
            while (print3.hasNext()) {
                System.out.println(print3.next());
            }

            outt.println("the parsimony score of the best network:" + newNetwork.ParsimonyScore);

            outt.println("the best network:");
            print.print(newNetwork, outt, number);

            outt.flush();
    }
    private static void usageInstruction() {
        System.out.println("\nargs: [speciesTreeFileName] [geneSeqsFileName] [outputFileName]");
        System.out.println("an example: testData\\speciesTree_4_1 testData\\S1 Result_Output");
    }}
