import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.xml.internal.dtm.ref.DTMAxisIterNodeList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class Graph {
    public Map<String, Map<String, Integer>> VertexMap;
    public boolean oriented;
    public boolean isWeighted;

    public boolean isWeighted() {
        return isWeighted;
    }

    public Map<String, Map<String, Integer>> getVertexMap() {
        return VertexMap;
    }

    public boolean isOriented() {
        return oriented;
    }

    public Graph() {

    }

    public Graph(boolean isOriented, boolean weighted) {
        this.VertexMap = new HashMap<>();
        this.oriented = isOriented;
        this.isWeighted = weighted;
    }

    public static Graph copyGraph(Graph graph) {
        Map<String, Map<String, Integer>> newVertMap = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> vertMap : graph.getVertexMap().entrySet()) {
            Map<String, Integer> newEdgeMap = new HashMap<>();
            String key = vertMap.getKey();
            for (Map.Entry<String, Integer> edgeMap : vertMap.getValue().entrySet()) {
                String keyEdgeMap = edgeMap.getKey();
                Integer valueEdgeMap = edgeMap.getValue();
                newEdgeMap.put(keyEdgeMap, valueEdgeMap);
            }
            newVertMap.put(key, newEdgeMap);
        }
        boolean isOriented = graph.isOriented();
        boolean weighted = graph.isWeighted();
        return new Graph(isOriented, weighted, newVertMap);
    }

    public Graph(Boolean Oriented, Boolean Weighted, Map<String, Map<String, Integer>> map) {
        this.oriented = Oriented;
        this.isWeighted = Weighted;
        this.VertexMap = map;
    }

    public static Graph createGraphFromFile(String adjacencyListDirectory) throws IOException {
        File file = new File(adjacencyListDirectory);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder input = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            input.append(line);
        }

        reader.close();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(input.toString(), new TypeReference<Graph>() {
        });
    }

    public void saveToFile(String dir) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> outMap = new HashMap<>();
        outMap.put("oriented", this.oriented);
        outMap.put("isWeighted", this.isWeighted);
        outMap.put("VertexMap", this.VertexMap);
        objectMapper.writeValue(Paths.get(dir + "out.json").toFile(), outMap);
    }

    public void AddVertex(String vertex) {
        Map<String, Integer> connections = new HashMap<String, Integer>();
        VertexMap.put(vertex, connections);
    }


    public void AddEdge(String from, String to, int weight) {
        for (Map.Entry<String, Map<String, Integer>> entry : VertexMap.entrySet()) {
            if (entry.getKey().equals(from)) {
                entry.getValue().put(to, weight);
                break;
            }
        }
    }

    public void DeleteEdge(String from, String to) {
        for (Map.Entry<String, Map<String, Integer>> entry : VertexMap.entrySet()) {
            if (entry.getKey().equals(from)) {
                Map<String, Integer> edges = entry.getValue();
                edges.remove(to);
                entry.setValue(edges);
                break;
            }
        }
    }

    public void DeleteVertex(String vertex) {
        for (Map.Entry<String, Map<String, Integer>> entry : VertexMap.entrySet()) {
            for (Map.Entry<String, Integer> edges : entry.getValue().entrySet()) {
                if (edges.getKey().equals(vertex)) {
                    DeleteEdge(entry.getKey(), edges.getKey());
                }
            }
        }
        VertexMap.remove(vertex);
    }


    public void BFS(String vertex) {
        Set<String> used = new HashSet<>();
        Map<String, Integer> ans = new HashMap<>();
        LinkedList<String> queue = new LinkedList<String>();
        used.add(vertex);
        queue.add(vertex);

        int length = 1;
        while (queue.size() != 0) {
            String v = queue.poll();
            for (Map.Entry<String, Integer> vert : VertexMap.get(v).entrySet()) {
                if (!used.contains(vert.getKey())) {
                    used.add(vert.getKey());
                    queue.add(vert.getKey());
                    ans.put(vert.getKey(), length);
                    length++;
                }
            }
        }

        for (Map.Entry<String, Integer> a : ans.entrySet()) {
            System.out.println("В вершину " + a.getKey() + " длина " + a.getValue());
        }
        System.out.println();
    }

    public void DFS(String vertex, Set<String> used, Stack stack) {
        used.add(vertex);

        Iterator<Map<String, Integer>> iterator = getVertexMap().values().iterator();


        for (Map.Entry<String, Map<String, Integer>> vert : getVertexMap().entrySet()) {
            if (vert.getKey().equals(vertex)) {
                for (Map.Entry<String, Integer> edges : vert.getValue().entrySet()) {
                    String v = edges.getKey();
                    if (!used.contains(v)) {
                        DFS(v, used, stack);
                    }
                }
            }
        }

        stack.push(vertex);
    }

    public void topologicalSort() {
        Set<String> used = new HashSet<>();
        Stack stack = new Stack();

        for (Map.Entry<String, Map<String, Integer>> vert : getVertexMap().entrySet()) {
            if (!used.contains(vert.getKey())) {
                DFS(vert.getKey(), used, stack);
            }
        }
        while (!stack.empty())
            System.out.print(stack.pop() + " ");
        System.out.println();
    }

    public Integer Dijkstra(String startVertex) {
        Map<String, Integer> d = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> vert : this.getVertexMap().entrySet()) {
            d.put(vert.getKey(), Integer.MAX_VALUE);
        }

        Map<String, Boolean> used = new HashMap<>();

        d.put(startVertex, 0);

        for (; ; ) {
            String v = "";
            for (String vertex : this.getVertexMap().keySet()) {
                if (!used.containsKey(vertex) && d.get(vertex) < Integer.MAX_VALUE
                        && (v.equals("") || d.get(v) > d.get(vertex))) {
                    v = vertex;
                }
            }
            if (v.equals("")) {
                break;
            }
            used.put(v, true);

            for (String vertex : this.getVertexMap().keySet()) {
                if (!used.containsKey(vertex) &&
                        this.getVertexMap().get(v).containsKey(vertex)) {
                    d.put(vertex, Math.min(d.get(vertex), d.get(v) +
                            this.getVertexMap().get(v).get(vertex)));
                }
            }

        }

        int ans = 0;

        for (Map.Entry<String, Integer> dist : d.entrySet()) {
            if (!dist.getValue().equals(Integer.MAX_VALUE)) {
//                System.out.println("Вершина " + dist.getKey() + " путь " + dist.getValue());
                ans += dist.getValue();
            }

        }

        return ans;
    }

    public void fordBellman(String u, String v) {
        if (!this.VertexMap.containsKey(u) || !this.VertexMap.containsKey(v)) {
            System.out.println("Введенных вершин не существует");
        } else {
            Map<String, Integer> distance = new HashMap<>();
            for (String vertex : this.VertexMap.keySet()) {
                distance.put(vertex, Integer.MAX_VALUE);
            }
            distance.put(u, 0);
            List<Edge> edges = new LinkedList<>();
            for (Map.Entry<String, Map<String, Integer>> vertex : this.VertexMap.entrySet()) {
                for (Map.Entry<String, Integer> e : vertex.getValue().entrySet()) {
                    Edge edge = new Edge(vertex.getKey(), e.getKey(), e.getValue());
                    edges.add(edge);
                }
            }

            for (int i = 0; i < this.VertexMap.size() - 1; i++) {
                for (int j = 0; j < edges.size(); j++) {
                    if (distance.get(edges.get(j).getFrom()) != Integer.MAX_VALUE &&
                            distance.get(edges.get(j).getTo()) > distance.get(edges.get(j).getFrom())
                                    + edges.get(j).getWeight()) {
                        distance.put(edges.get(j).getTo(), distance.get(edges.get(j).getFrom())
                                + edges.get(j).getWeight());
                    }
                }
            }
            System.out.println("Кратчайший путь из вершины " + u + " в вершину " + v);
            for (Map.Entry<String, Integer> d : distance.entrySet()) {
                if (d.getKey().equals(v)) {
                    System.out.println("Путь в вершину " + d.getKey() + " с весом " + d.getValue());
                    break;
                } else
                    System.out.println("Путь в вершину " + d.getKey() + " с весом " + d.getValue());
            }
        }
    }


    public Map<String, Map<String, Integer>> floyd() {
        Map<String, Map<String, Integer>> d = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> vertex : this.VertexMap.entrySet()) {
            Map<String, Integer> dHelp = new HashMap<>();
            for (Map.Entry<String, Integer> ver2 : vertex.getValue().entrySet()) {
                dHelp.put(ver2.getKey(), ver2.getValue());
            }
            for (Map.Entry<String, Map<String, Integer>> ver2 : this.VertexMap.entrySet()) {
                if (!dHelp.containsKey(ver2.getKey()) && !ver2.getKey().equals(vertex.getKey())) {
                    dHelp.put(ver2.getKey(), Integer.MAX_VALUE);
                } else if (!dHelp.containsKey(ver2.getKey()) && ver2.getKey().equals(vertex.getKey())) {
                    dHelp.put(vertex.getKey(), 0);
                }
            }
            d.put(vertex.getKey(), dHelp);
        }

        for (String k : this.VertexMap.keySet()) {
            for (String j : this.VertexMap.keySet()) {
                for (String i : this.VertexMap.keySet()) {
                    if (d.get(i).get(k) < Integer.MAX_VALUE && d.get(k).get(j) < Integer.MAX_VALUE) {
                        d.get(i).put(j, Math.min(d.get(i).get(j),
                                d.get(i).get(k) + d.get(k).get(j)));
                    }
                }
            }
        }
        for (String i : this.VertexMap.keySet()){
            for (String j : this.VertexMap.keySet()){
                for (String k : this.VertexMap.keySet()){
                    if (d.get(i).get(k) < Integer.MAX_VALUE && d.get(k).get(k) < 0
                                                            && d.get(k).get(j) < Integer.MAX_VALUE){
                        d.get(i).put(j, Integer.MIN_VALUE);
                        System.out.println("Есть циклы отрицательного веса");
                    }
                }
            }
        }
        return d;
    }

    public Map<String, List<FlowEdge>> createGraphToDinic(){
        Map<String, List<FlowEdge>> ans = new HashMap<>();
        for (String vertex : this.VertexMap.keySet()){
            ans.put(vertex, new ArrayList<>());
        }
        for (Map.Entry<String, Map<String, Integer>> vertex : this.VertexMap.entrySet()){
            for (Map.Entry<String, Integer> edge : vertex.getValue().entrySet()){
                ans.get(vertex.getKey()).add(new FlowEdge(edge.getKey(),
                                                          ans.get(edge.getKey()).size(),
                                                          edge.getValue().doubleValue()));
                ans.get(edge.getKey()).add(new FlowEdge(vertex.getKey(),
                                                        ans.get(vertex.getKey()).size() - 1,
                                                        0.0));
            }
        }
        return ans;
    }
}
