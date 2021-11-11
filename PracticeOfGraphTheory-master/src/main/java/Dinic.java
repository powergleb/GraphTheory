

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Dinic {
    Map<String, Double> dist;
    Map<String, Integer> ptr;
    Map<String, List<FlowEdge>> FlowGraph;
    Graph graph;

    public Dinic(Graph graph){
        this.graph = graph;
        this.dist = new HashMap<>();
        this.ptr = new HashMap<>();
        this.FlowGraph = graph.createGraphToDinic();
    }

    public double dinicAlg(String start, String second){
        Double flow = 0.0;
        for (; ; ){
            if (!BFS(start, second))
                break;
            for (String vertex : graph.VertexMap.keySet()){
                ptr.put(vertex, 0);
            }
            Double pushed = -1.0;
            while (pushed != 0){
                pushed = DFS(start, second, Double.MAX_VALUE);
                if (pushed == 0)
                    break;
                flow += pushed;
            }
        }
        return flow;
    }

    public boolean BFS(String start, String second){
        for (String vertex : this.graph.VertexMap.keySet()){
            dist.put(vertex, -1.0);
        }
        dist.put(start, 0.0);
        Map<Integer, String> helpMap = new HashMap<>();
        Integer helpMapSize = 0;
        helpMap.put(helpMapSize++, start);
        for (int i = 0; i < helpMapSize; i ++){
            String vertex = helpMap.get(i);
            for (FlowEdge edge : FlowGraph.get(vertex)){
                if (dist.get(edge.getTargetedVertex()) < 0 && edge.getFlow() < edge.getValue()){
                    dist.put(edge.getTargetedVertex(), dist.get(vertex) + 1);
                    helpMap.put(helpMapSize++, edge.getTargetedVertex());
                }
            }
        }
        return dist.get(second) >= 0;
    }

    public Double DFS(String start, String second, Double flow){
        if (start.equals(second))
            return flow;
        for (; ptr.get(start) < FlowGraph.get(start).size(); ptr.put(start, ptr.get(start) + 1)) {
            FlowEdge edge = FlowGraph.get(start).get(ptr.get(start));
            if (dist.get(edge.getTargetedVertex()) == dist.get(start) + 1 && edge.getFlow() < edge.getValue()) {
                Double dfsResult = DFS(edge.getTargetedVertex(), second, Math.min(flow,
                                                                                  edge.getValue() - edge.getFlow()));
                if (dfsResult > 0) {
                    edge.setFlow(edge.getFlow() + dfsResult);
                    FlowGraph.get(edge.getTargetedVertex())
                            .get(edge.getRevVertex())
                            .setFlow(FlowGraph.get(edge.getTargetedVertex()).get(edge.getRevVertex())
                            .getFlow() - dfsResult);
                    return dfsResult;
                }
            }
        }
        return 0.0;
    }
}
