package bio.parsimony;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Random;

import static java.lang.Math.pow;

public class SAP {
    public static final int T = 100;// initial temperature

    public static final double delta = 0.98;// temperature drop rate
    private Network tempNetwork = new Network();
    private Network newNetwork = new Network();
    private Network bestNetwork = new Network();
    public int count = 0;

    public Network getSA(Network network, int geneNum, int orderTraverse, int ii, int k) throws Exception {
        File output_bestN;
        PrintWriter outt_bestN;

        double Tmin = T * pow(delta, 4 * k);
        double t = T;

        tempNetwork = (Network) network.clone();
        bestNetwork = (Network) network.clone();
        while (t > Tmin) {
            for (int i = 0; i < k; i++) {

                if (tempNetwork.k == 0) {
                    OneStepOfIteration oneStepOfIteration = new OneStepOfIteration();
                    newNetwork = oneStepOfIteration.next(tempNetwork, 0, geneNum, orderTraverse, ii);
                    count++;
                    System.out.println("iteration" + count + ",the parsimony score of the current network：" + newNetwork.ParsimonyScore);

                } else {
                    int operationId = getRandom(tempNetwork.k, tempNetwork.Kmax);
                    System.out.println("next operation id:" + operationId);
                    OneStepOfIteration oneStepOfIteration = new OneStepOfIteration();
                    newNetwork = oneStepOfIteration.next(tempNetwork, operationId, geneNum, orderTraverse, ii);
                    Iterator print13 = newNetwork.trees_String.iterator();
                    while (print13.hasNext()) {
                        System.out.println(print13.next());
                    }
                    count++;
                }
//if the new network does not meet the conditions, accept the new network with a certain probability
//1.k>Kmax，2.the score of the new network is higher than that of the old network，3.there are unused trees in new network，4.the new network is the same as the old one
                if ((newNetwork.k > newNetwork.Kmax) || (newNetwork.ParsimonyScore == tempNetwork.ParsimonyScore && newNetwork.mark == -1)) {

                } else if (newNetwork.ParsimonyScore > tempNetwork.ParsimonyScore || newNetwork.UselessTreeCount_forKmax > 0) {
                    int funTmp = tempNetwork.ParsimonyScore;
                    int funTmp_new = newNetwork.ParsimonyScore;
//System.out.println(funTmp + "----" + funTmp_new);
                    double p = 1 / (1 + Math.exp(-(double) (funTmp_new - funTmp) / t));
                    if (Math.random() < p) {
                        tempNetwork = (Network) newNetwork.clone();

                    }
                }
//accept the new network
                else if (newNetwork.ParsimonyScore < tempNetwork.ParsimonyScore && newNetwork.UselessTreeCount_forKmax == 0 && newNetwork.UselessTreeCount <= Math.pow(2, newNetwork.k - 1)) {
                    tempNetwork = (Network) newNetwork.clone();
                    if (bestNetwork.ParsimonyScore > newNetwork.ParsimonyScore)
                        bestNetwork = (Network) newNetwork.clone();
                    String pathname_bestN = "bestNetwork";
                    output_bestN = new File(pathname_bestN);
                    outt_bestN = new PrintWriter(new FileOutputStream(output_bestN));
                    outt_bestN.println("");
                    Iterator print3 = bestNetwork.edges.iterator();
                    while (print3.hasNext()) {
                        outt_bestN.println(print3.next());
                    }
                    outt_bestN.flush();
/*                        System.out.println("tempNetwork's edges:::::::::::::::");
                        Iterator print5 = tempNetwork.edges.iterator();
                        while (print5.hasNext()) {
                            System.out.println(print5.next());
                        }
                       */
                }


            }
            t = t * delta;
        }
        if (bestNetwork.ParsimonyScore > tempNetwork.ParsimonyScore) {
            return tempNetwork;

        }
        return bestNetwork;
    }

    public int getCount() {
        return count;
    }

    private static int getRandom(int k, int Kmax) {
        Random _random;
        _random = new Random();
        int a = _random.nextInt(100) + 1;
        if (k < Kmax) {
            if (a <= 15)
                return 0;
            if (a <= 45)
                return 3;
            if ( a <= 65)
                return 2;
            if ( a <= 85)
                return 4;
            else
                return 1;
        }
        if (k == Kmax) {
            if (a <= 25)
                return 3;
            if (a <= 50)
                return 2;
            if (a <= 75)
                return 4;
            else
                return 1;
        }
        return -1;
    }
}
