
public class FlowEdge {
    public void setTargetedVertex(String targetedVertex) {
        this.targetedVertex = targetedVertex;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public void setFlow(Double flow) {
        this.flow = flow;
    }

    public void setRevVertex(Integer revVertex) {
        this.revVertex = revVertex;
    }

    public String getTargetedVertex() {
        return targetedVertex;
    }

    public Double getValue() {
        return value;
    }

    public Double getFlow() {
        return flow;
    }

    public Integer getRevVertex() {
        return revVertex;
    }

    public String targetedVertex;
    public Double value;
    public Double flow;
    public Integer revVertex;

    public FlowEdge(String targetedVertex, Integer revVertex, Double value){
        this.targetedVertex = targetedVertex;
        this.revVertex = revVertex;
        this.value = value;
        this.flow = 0.0;
    }
}
