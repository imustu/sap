package bio.parsimony;

public abstract class NetworkOperation {
    protected Network _network;
    protected Edge _targetEdge;
    protected Edge _sourceEdge;
    protected Edge _destinationEdge;
    public void setParameters(Network network, Edge targetEdge, Edge sourceEdge, Edge destinationEdge)  {

/*        _network = (Network)network.clone();
if(targetEdge!=null)
        _targetEdge =(Edge) targetEdge.clone();
if(sourceEdge!=null)
        _sourceEdge =(Edge) sourceEdge.clone();
if(destinationEdge!=null)
        _destinationEdge =(Edge) destinationEdge.clone();*/
        //_network = (Network)network.clone();
        _network=network;
        _targetEdge = targetEdge;
        _sourceEdge = sourceEdge;
        _destinationEdge = destinationEdge;
    }
    abstract public Network performOperation() throws Exception;
}
