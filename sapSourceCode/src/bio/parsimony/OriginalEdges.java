package bio.parsimony;

public class OriginalEdges {
    Integer SourceEdgeNode1;
    Integer SourceEdgeNode2;
    Integer DestinationEdgeNode1;
    Integer DestinationEdgeNode2;
    public OriginalEdges(Integer SourceEdgeNode1,Integer SourceEdgeNode2,Integer DestinationEdgeNode1,Integer DestinationEdgeNode2){
        this.SourceEdgeNode1=SourceEdgeNode1;
        this.SourceEdgeNode2=SourceEdgeNode2;
        this.DestinationEdgeNode1=DestinationEdgeNode1;
        this.DestinationEdgeNode2=DestinationEdgeNode2;
    }
/*    public boolean equals(OriginalEdges oe1,OriginalEdges oe2){
        if(oe1.SourceEdgeNode1.equals(oe2.SourceEdgeNode1) && oe1.DestinationEdgeNode1.equals(oe2.DestinationEdgeNode1)
        && oe1.SourceEdgeNode2.equals(oe2.SourceEdgeNode2) && oe1.DestinationEdgeNode2.equals(oe2.DestinationEdgeNode2))
            return true;
        if(oe1.SourceEdgeNode1.equals(oe2.DestinationEdgeNode1) && oe1.SourceEdgeNode2.equals(oe2.DestinationEdgeNode2)
        && oe1.DestinationEdgeNode1.equals(oe2.SourceEdgeNode1) && oe1.DestinationEdgeNode2.equals(oe2.SourceEdgeNode2))
            return true;
        return false;
    }*/
public boolean equals(OriginalEdges oe2){
    if(SourceEdgeNode1.equals(oe2.SourceEdgeNode1) && DestinationEdgeNode1.equals(oe2.DestinationEdgeNode1)
            && SourceEdgeNode2.equals(oe2.SourceEdgeNode2) && DestinationEdgeNode2.equals(oe2.DestinationEdgeNode2))
        return true;
    if(SourceEdgeNode1.equals(oe2.DestinationEdgeNode1) && SourceEdgeNode2.equals(oe2.DestinationEdgeNode2)
            && DestinationEdgeNode1.equals(oe2.SourceEdgeNode1) && DestinationEdgeNode2.equals(oe2.SourceEdgeNode2))
        return true;
    return false;
}

    public String toString(){
        return "sourceEdge:("+SourceEdgeNode1+","+SourceEdgeNode2+")|destationEdge:("+DestinationEdgeNode1+","+DestinationEdgeNode2+")";
    }
}
