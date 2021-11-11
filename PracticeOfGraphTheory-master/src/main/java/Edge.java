public class Edge implements Comparable<Edge> {
    String to;
    String from;
    Integer weight;

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public Integer getWeight() {
        return weight;
    }

    Edge(String From, String To, Integer Weight) {
        this.to = To;
        this.from = From;
        this.weight = Weight;
    }

    @Override
    public int compareTo(Edge edge) {
        if (weight != edge.weight) return  weight < edge.weight ? -1 : 1;
        return 0;
    }
}
