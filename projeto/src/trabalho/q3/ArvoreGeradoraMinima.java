package trabalho.q3;
    
public class ArvoreGeradoraMinima {

    public static void main(String[] args) {
        System.out.println("--- Questão 3: Grafo em Matriz de Adjacência ---");
        
        int[][] graph = new int[][]{
            { 0, 15, 10, 19,  0,  0,  0,  0,  0,  0}, // A
            {15,  0,  0,  7, 17,  0,  0,  0,  0,  0}, // B
            {10,  0,  0, 16,  0, 14,  0,  0,  0,  0}, // C
            {19,  7, 16,  0, 12,  6,  3,  0,  0,  0}, // D
            { 0, 17,  0, 12,  0,  0, 20, 13,  0,  0}, // E
            { 0,  0, 14,  6,  0,  0,  9,  0,  5,  0}, // F
            { 0,  0,  0,  3, 20,  9,  0,  4,  1, 11}, // G
            { 0,  0,  0,  0, 13,  0,  4,  0,  0,  2}, // H
            { 0,  0,  0,  0,  0,  5,  1,  0,  0, 18}, // I
            { 0,  0,  0,  0,  0,  0, 11,  2, 18,  0}  // J
        };
        
        primMST(graph);
    }

    public static void primMST(int[][] graph) {
        int V = graph.length;
        int[] parent = new int[V];
        int[] key = new int[V];
        boolean[] mstSet = new boolean[V];

        for (int i = 0; i < V; i++) {
            key[i] = Integer.MAX_VALUE;
            mstSet[i] = false;
        }

        key[0] = 0;
        parent[0] = -1;

        for (int count = 0; count < V - 1; count++) {
            int u = minKey(key, mstSet, V);
            mstSet[u] = true;
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mstSet[v] && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }
        printMST(parent, graph, V);
    }

    private static int minKey(int[] key, boolean[] mstSet, int V) {
        int min = Integer.MAX_VALUE, minIndex = -1;
        for (int v = 0; v < V; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                minIndex = v;
            }
        }
        return minIndex;
    }

    private static void printMST(int[] parent, int[][] graph, int V) {
        char[] map = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
        System.out.println("\n--- MST (Prim) ---");
        System.out.println("Arestas da Árvore Geradora Mínima:");
        
        int totalWeight = 0;
        for (int i = 1; i < V; i++) {
            int u = parent[i];
            int v = i;
            int weight = graph[u][v];
            totalWeight += weight;
            System.out.printf("%c - %c : %d\n", map[u], map[v], weight);
        }
        System.out.println("Peso total da MST = " + totalWeight);
    }
}