package trabalho.comum;

/**
 * Representa uma aresta ponderada e é comparável com base no peso.
 */
public class Edge implements Comparable<Edge> {
    public int u, v, w;

    public Edge(int u, int v, int w) {
        this.u = u;
        this.v = v;
        this.w = w;
    }

    @Override
    public int compareTo(Edge other) {
        return this.w - other.w;
    }
}