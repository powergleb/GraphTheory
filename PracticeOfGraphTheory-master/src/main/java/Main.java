import com.sun.xml.internal.ws.api.ha.StickyFeature;
import javafx.util.Pair;

import javax.swing.plaf.multi.MultiDesktopIconUI;
import java.io.IOException;
import java.util.*;

public class Main {

    public static String DIR = "D:\\jsons\\";

    public static void PrintGraph(Graph graph) {
        System.out.println();
        System.out.println("Граф ориентирован ? - " + graph.isOriented());
        System.out.println("Граф взвешан ? - " + graph.isWeighted());
        for (Map.Entry<String, Map<String, Integer>> vertex : graph.getVertexMap().entrySet()) {
            System.out.println("Вершина " + vertex.getKey());
            for (Map.Entry<String, Integer> edge : vertex.getValue().entrySet()) {
                System.out.println("Ребро из вершины " + vertex.getKey() + " в вершину "
                        + edge.getKey() + " с весом " + edge.getValue());
            }
        }

        System.out.println();
    }

    public static void TaskLa1(Graph graph, String strVertex) {
        int semiExodus = 0;
        for (Map.Entry<String, Map<String, Integer>> vertex : graph.getVertexMap().entrySet()) {
            if (vertex.getKey().equals(strVertex)) {
                for (Map.Entry<String, Integer> edge : vertex.getValue().entrySet()) {
                    semiExodus++;
                }
            }
        }

        for (Map.Entry<String, Map<String, Integer>> vertex : graph.getVertexMap().entrySet()) {
            if (!vertex.getKey().equals(strVertex)) {
                int counter = 0;
                for (Map.Entry<String, Integer> edge : vertex.getValue().entrySet()) {
                    counter++;
                }
                if (counter > semiExodus)
                    System.out.println(vertex.getKey());
            }
        }
    }

    public static void TaskLa2(Graph graph) {
        for (Map.Entry<String, Map<String, Integer>> vertex : graph.getVertexMap().entrySet()) {
            boolean isLoop = false;
            for (Map.Entry<String, Integer> edge : vertex.getValue().entrySet()) {
                if (vertex.getKey().equals(edge.getKey()))
                    isLoop = true;
            }
            if (isLoop)
                System.out.println(vertex.getKey());
        }
    }

    public static Graph TaskLb(Graph graph) throws IOException {
        Graph graphFull = new Graph(graph.isOriented(), graph.isWeighted());
        for (Map.Entry<String, Map<String, Integer>> vertex : graph.getVertexMap().entrySet()) {
            graphFull.AddVertex(vertex.getKey());
            for (Map.Entry<String, Map<String, Integer>> vertexTo : graph.getVertexMap().entrySet()) {
                graphFull.AddEdge(vertex.getKey(), vertexTo.getKey(), 1);
            }
        }

        Graph graphCompliment = Graph.copyGraph(graphFull);

        for (Map.Entry<String, Map<String, Integer>> vertex : graphFull.getVertexMap().entrySet()) {
            for (Map.Entry<String, Integer> edge : vertex.getValue().entrySet()) {
                for (Map.Entry<String, Map<String, Integer>> inVertex : graph.getVertexMap().entrySet()) {
                    for (Map.Entry<String, Integer> inEdge : inVertex.getValue().entrySet()) {
                        if (vertex.getKey().equals(inVertex.getKey()) && edge.getKey().equals(inEdge.getKey()))
                            graphCompliment.DeleteEdge(vertex.getKey(), edge.getKey());
                    }
                }
            }
        }

        graphCompliment.saveToFile(DIR);
        return graphCompliment;
    }

    public static void Kruskal(Graph graph){
        List<Edge> edges = new LinkedList<>();
        int numOfEdges = 0;
        int numOfVertexes = graph.getVertexMap().size();
        for (Map.Entry<String, Map<String, Integer>> v : graph.getVertexMap().entrySet()){
            for (Map.Entry<String, Integer> e : v.getValue().entrySet()){
                Edge edge = new Edge(v.getKey(), e.getKey(), e.getValue());
                edges.add(edge);
                numOfEdges++;
            }
        }
        Collections.sort(edges);
        int index = 0;
        Map<String, Integer> set = new HashMap<>();
        for (Map.Entry<String, Map<String, Integer>> v : graph.getVertexMap().entrySet()){
            set.put(v.getKey(), index);
            index++;
        }
        List<Edge> mstEdges = new LinkedList<>();

        for (int i = 0; i < numOfEdges; i++){
            String from = edges.get(i).getFrom();
            String to = edges.get(i).getTo();
            int weight = edges.get(i).getWeight();
            if (!set.get(from).equals(set.get(to))){
                mstEdges.add(edges.get(i));
                Integer oldIndex = set.get(to);
                Integer newIndex = set.get(from);
                for (Map.Entry<String, Integer> iter : set.entrySet()){
                    if (iter.getValue().equals(oldIndex))
                        set.put(iter.getKey(), newIndex);
                }
            }
        }
        System.out.println("Minimum Spanning Tree");
        for (int i = 0; i < mstEdges.size(); i++){
            System.out.println("Из " + mstEdges.get(i).getFrom() + " в " + mstEdges.get(i).getTo() +
                                " с весом " + mstEdges.get(i).getWeight());
        }
        if (numOfVertexes - 1 == mstEdges.size())
            System.out.println("Получается построить дерево минимального остова");
        else
            System.out.println("Не получается построить дерево минимального остова");
    }

    public static void Task4a(Graph graph){
        Integer curMin = Integer.MAX_VALUE;
        String vertexName = "";
        for (Map.Entry<String, Map<String, Integer>> vertex : graph.getVertexMap().entrySet())
        {
            Integer cur = graph.Dijkstra(vertex.getKey());
            if (cur < curMin){
                curMin = cur;
                vertexName = vertex.getKey();
            }
        }
        System.out.println("Эта вершина - " + vertexName + " сумма путей равна " + curMin);
    }

    public static void main(String[] args) throws IOException {

        Graph graph = new Graph(true, false);
        Graph clonedGraph = new Graph(false, false);

        Scanner scan = new Scanner(System.in);
        int x = 0;
        String s;

        System.out.println("1. Создать пустой граф");
        System.out.println("2. Считать граф из файла");
        System.out.println("3. Добавление вершины");
        System.out.println("4. Добавление ребра");
        System.out.println("5. Удаление вершины");
        System.out.println("6. Удаление ребра");
        System.out.println("7. Вывод графа в консоль");
        System.out.println("8. Клонирование графа");
        System.out.println("9. Вывод графа в файл");
        s = scan.next();
        boolean flag;
        try {
            x = Integer.parseInt(s);
            flag = true;
        } catch (NumberFormatException e) {
            System.out.println("Неверный ввод");
            flag = false;
        }
        while (flag && x > 0) {
            switch (x) {
                case 1:
                    System.out.println("Будет создан граф с названием graph");
                    System.out.println("Введите ориентированность графа: ");
                    System.out.println("1. Неоринетированный");
                    System.out.println("2. Ориентированный");
                    int or = 0;
                    String in;
                    in = scan.next();
                    try {
                        or = Integer.parseInt(in);
                    } catch (NumberFormatException e) {
                        System.out.println("Неверный ввод");
                    }
                    boolean oriented;
                    if (or == 1)
                        oriented = false;
                    else
                        oriented = true;
                    System.out.println("Введите взвешенность графа: ");
                    System.out.println("1. Взвешенный");
                    System.out.println("2. Невзвешенный");
                    int weight = 0;
                    in = scan.next();
                    try {
                        weight = Integer.parseInt(in);
                    } catch (NumberFormatException e) {
                        System.out.println("Неверный ввод");
                    }
                    boolean isWeighted;
                    if (weight == 1)
                        isWeighted = true;
                    else
                        isWeighted = false;
                    graph = new Graph(oriented, isWeighted);
                    break;
                case 2:
                    System.out.println("Введите название файла (он должен располагаться в директории " + DIR + "), " +
                            "будет записано в граф graph");
                    String fileName = scan.next();
                    graph = Graph.createGraphFromFile(DIR + fileName);
                    break;
                case 3:
                    System.out.println("Введите название вершины: ");
                    String vertexName = scan.next();
                    boolean alreadyExist = false;
                    for (Map.Entry<String, Map<String, Integer>> vertex : graph.getVertexMap().entrySet()) {
                        if (vertex.getKey().equals(vertexName))
                            alreadyExist = true;
                    }
                    if (alreadyExist)
                        System.out.println("Такая вершина уже существует");
                    else
                        graph.AddVertex(vertexName);
                    break;
                case 4:
                    System.out.println("Введите из какой вершины идет ребро: ");
                    String from = scan.next();
                    boolean fromVertexExist = false;
                    for (Map.Entry<String, Map<String, Integer>> vertex : graph.getVertexMap().entrySet()) {
                        if (vertex.getKey().equals(from))
                            fromVertexExist = true;
                    }
                    System.out.println("Введите в какую вершину идет ребро: ");
                    String to = scan.next();
                    boolean toVertexExist = false;
                    for (Map.Entry<String, Map<String, Integer>> vertex : graph.getVertexMap().entrySet()) {
                        if (vertex.getKey().equals(to))
                            toVertexExist = true;
                    }
                    int intWeight = 1;
                    if (graph.isWeighted) {
                        System.out.println("Введите вес: ");
                        String strWeight = scan.next();
                        intWeight = Integer.parseInt(strWeight);
                    }
                    if (fromVertexExist && toVertexExist && !graph.isOriented()) {
                        graph.AddEdge(from, to, intWeight);
                        graph.AddEdge(to, from, intWeight);
                    }
                    else if (fromVertexExist && toVertexExist && graph.isOriented())
                        graph.AddEdge(from, to, intWeight);
                    else
                        System.out.println("Неправильно введены вершины");
                    break;
                case 5:
                    System.out.println("Введите название вершины: ");
                    boolean delVertexExist = false;
                    String vertexDelName = scan.next();
                    for (Map.Entry<String, Map<String, Integer>> vertex : graph.getVertexMap().entrySet()) {
                        if (vertex.getKey().equals(vertexDelName))
                            delVertexExist = true;
                    }
                    if (delVertexExist)
                        graph.DeleteVertex(vertexDelName);
                    else
                        System.out.println("Такой вершины не существует");
                    break;
                case 6:
                    System.out.println("Введите из какой вершины идет ребро: ");
                    String delFrom = scan.next();
                    boolean delFromExist = false;
                    for (Map.Entry<String, Map<String, Integer>> vertex : graph.getVertexMap().entrySet()) {
                        if (vertex.getKey().equals(delFrom))
                            delFromExist = true;
                    }
                    System.out.println("Введите в какую вершину идет ребро: ");
                    String delTo = scan.next();
                    boolean delToExist = false;
                    for (Map.Entry<String, Map<String, Integer>> vertex : graph.getVertexMap().entrySet()) {
                        if (vertex.getKey().equals(delTo))
                            delToExist = true;
                    }
                    if (delFromExist && delToExist)
                        graph.DeleteEdge(delFrom, delTo);
                    else
                        System.out.println("Неправильно введены вершины");
                    break;
                case 7:
                    PrintGraph(graph);
                    break;
                case 8:
                    System.out.println("Клонирование графа graph в clonedGraph");
                    clonedGraph = Graph.copyGraph(graph);
                    PrintGraph(clonedGraph);
                    break;
                case 9:
                    System.out.println("Граф сохранен в файл out.json в директории " + DIR);
                    graph.saveToFile(DIR);
                    break;
                case 10:
                    System.out.println("Задание la (1)");
                    System.out.println("Введите вершину: ");
                    String strVertex = scan.next();
                    if (graph.getVertexMap().keySet().contains(strVertex))
                        TaskLa1(graph, strVertex);
                    else
                        System.out.println("Некорректно введена вершина");
                    break;
                case 11:
                    System.out.println("Задание la (2)");
                    TaskLa2(graph);
                    break;
                case 12:
                    PrintGraph(TaskLb(graph));
                    break;
                case 13:
                    graph.topologicalSort();
                    break;
                case 14:
                    System.out.println("Введите вершину:");
                    String v = scan.next();
                    if (graph.getVertexMap().keySet().contains(v))
                        graph.BFS(v);
                    else
                        System.out.println("Такой вершины нет");
                    break;
                case 15:
                    Kruskal(graph);
                    break;
                case 16:
                    Task4a(graph);
                    break;
                case 17:
                    System.out.println("Введите вершину u1:");
                    String u1 = scan.next();
                    System.out.println("Введите вершину u2:");
                    String u2 = scan.next();
                    System.out.println("Введите вершину v:");
                    String V = scan.next();
                    graph.fordBellman(u1, V);
                    graph.fordBellman(u2, V);
                    break;
                case 18:
                    System.out.println("Ввидет вершину: ");
                    String vFloyd = scan.next();
                    if (graph.getVertexMap().containsKey(vFloyd)) {
                        System.out.println("Ввидет N: ");
                        String nStr = scan.next();
                        Integer n = 0;
                        try {
                            n = Integer.parseInt(nStr);
                        } catch (NumberFormatException e) {
                            System.out.println("Введено некорректное число");
                        }
                        Map<String, Map<String, Integer>> d = new HashMap<>();
                        d = graph.floyd();
                        System.out.println(n + " - периферия для вершины " + vFloyd + " :");
                        for (Map.Entry<String, Map<String, Integer>> vertex : graph.getVertexMap().entrySet()) {
                            if (!vertex.getKey().equals(vFloyd)) {
                                if (d.get(vertex.getKey()).get(vFloyd) > n) {
                                    System.out.println(vertex.getKey());
                                }
                            }
                        }
                    }
                    else
                        System.out.println("Введена некорректная вершина");
                    break;
                case 19:
                    Dinic dinic = new Dinic(graph);
                    System.out.println("Введите исток:");
                    String start = scan.next();
                    boolean startFlag = true;
                    startFlag = graph.getVertexMap().containsKey(start);
                    if (!startFlag){
                        System.out.println("Введены некорректны данные для истока");
                    }
                    System.out.println("Введите сток:");
                    String second = scan.next();
                    Boolean secondFlag = true;
                    secondFlag = graph.getVertexMap().containsKey(second);
                    if (!secondFlag){
                        System.out.println("Введены некорректные данные для стока");
                    }
                    Double dinicAns;
                    if (startFlag && secondFlag){
                         dinicAns = dinic.dinicAlg(start, second);
                        System.out.println("Искомый поток равен = " + dinicAns.toString());
                    }
                    break;
            }

            System.out.println("1. Создать пустой граф");
            System.out.println("2. Считать граф из файла");
            System.out.println("3. Добавление вершины");
            System.out.println("4. Добавление ребра");
            System.out.println("5. Удаление вершины");
            System.out.println("6. Удаление ребра");
            System.out.println("7. Вывод графа в консоль");
            System.out.println("8. Клонирование графа");
            System.out.println("9. Вывод графа в файл");
            System.out.println("10. Task La(1)");
            System.out.println("11. Task La(2)");
            System.out.println("12. Task Lb");
            System.out.println("13. Task 2 (17)");
            System.out.println("14. Task 2 (26)");
            System.out.println("15. MST с помощью алгоритма Kruskal");
            System.out.println("16. Task 4 (6)");
            System.out.println("17. Task 4 (15)");
            System.out.println("18. Task 4 (7)");
            System.out.println("19. Потоковый алгоритм Диника");;
            s = scan.next();
            try {
                x = Integer.parseInt(s);
                flag = true;
            } catch (NumberFormatException e) {
                System.out.println("Неверный ввод");
                flag = false;
            }
        }

    }

}
