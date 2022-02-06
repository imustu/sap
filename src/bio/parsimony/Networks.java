package bio.parsimony;

import java.util.ArrayList;
import java.util.List;

public class Networks {
    public List<Network> networkList = new ArrayList<Network>();
    public int label=0;
    public Networks addNetwork(Network network) throws Exception {
        //network.setNumber();
        Networks networks1=new Networks();
        networkList.add(network);
        label++;
        return networks1;
    }

    @Override
    public String toString() {
        return "网络个数"+Integer.toString(label);
    }
}
