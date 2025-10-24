import java.util.*;

public class Main {
    public static void main(String[] args) {

        System.out.println("=== Демонстрация алгоритмов в одном классе ===\n");

        System.out.println("sdfsdfsdfsdfsdfsdf");


        // Массив для сортировок и поиска
        int[] arr = generateRandomArray(20, 0, 200);
        System.out.println("Исходный массив:");
        printArray(arr);

        // Merge Sort
        int[] mergeSorted = Arrays.copyOf(arr, arr.length);
        mergeSort(mergeSorted, 0, mergeSorted.length - 1);
        System.out.println("\nПосле Merge Sort:");
        printArray(mergeSorted);

        // Quick Sort
        int[] quickSorted = Arrays.copyOf(arr, arr.length);
        quickSort(quickSorted, 0, quickSorted.length - 1);
        System.out.println("\nПосле Quick Sort:");
        printArray(quickSorted);

        // Бинарный поиск (на отсортированном массиве)
        int target = quickSorted[quickSorted.length / 3];
        System.out.println("\nBinary search for " + target + ": index -> " + binarySearch(quickSorted, target));

        // LIS (Longest Increasing Subsequence) - O(n log n)
        System.out.println("\nLIS (length) for исходного массива: " + lisLength(arr));

        // 0/1 Knapsack (пример)
        int[] weights = {2, 3, 4, 5, 9};
        int[] values  = {3, 4, 8, 8, 10};
        int W = 20;
        System.out.println("\n0/1 Knapsack: best value for W=" + W + " -> " + knapsack01(values, weights, W));

        // Union-Find (Disjoint Set) пример
        System.out.println("\nUnion-Find demo:");
        unionFindDemo();

        // Sieve of Eratosthenes
        int n = 100;
        System.out.println("\nПростые числа до " + n + ":");
        List<Integer> primes = sieve(n);
        System.out.println(primes);

        // Directed weighted graph — Dijkstra
        System.out.println("\nDijkstra on random weighted graph:");
        WeightedGraph g = new WeightedGraph(7);
        g.addEdge(0, 1, 7);
        g.addEdge(0, 2, 9);
        g.addEdge(0, 5, 14);
        g.addEdge(1, 2, 10);
        g.addEdge(1, 3, 15);
        g.addEdge(2, 3, 11);
        g.addEdge(2, 5, 2);
        g.addEdge(3, 4, 6);
        g.addEdge(4, 5, 9);
        int[] dist = dijkstra(g, 0);
        System.out.println("Distances from 0: " + Arrays.toString(dist));

        // Topological sort demo (Kahn)
        System.out.println("\nTopological sort demo (Kahn):");
        DirectedGraph dg = new DirectedGraph(6);
        dg.addEdge(5, 2);
        dg.addEdge(5, 0);
        dg.addEdge(4, 0);
        dg.addEdge(4, 1);
        dg.addEdge(2, 3);
        dg.addEdge(3, 1);
        List<Integer> topo = topologicalSortKahn(dg);
        System.out.println(topo);

        System.out.println("\n=== Конец демонстрации ===");
    }

    // ========================= Утилиты =========================

    static int[] generateRandomArray(int n, int low, int high) {
        Random r = new Random();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = low + r.nextInt(high - low + 1);
        return a;
    }

    static void printArray(int[] a) {
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + (i+1 < a.length ? ", " : ""));
        }
        System.out.println();
    }

    // ========================= Merge Sort =========================

    static void mergeSort(int[] a, int l, int r) {
        if (l >= r) return;
        int m = l + (r - l) / 2;
        mergeSort(a, l, m);
        mergeSort(a, m+1, r);
        merge(a, l, m, r);
    }

    static void merge(int[] a, int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;
        int[] L = new int[n1];
        int[] R = new int[n2];
        for (int i = 0; i < n1; i++) L[i] = a[l + i];
        for (int j = 0; j < n2; j++) R[j] = a[m + 1 + j];
        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) a[k++] = L[i++];
            else a[k++] = R[j++];
        }
        while (i < n1) a[k++] = L[i++];
        while (j < n2) a[k++] = R[j++];
    }

    // ========================= Quick Sort =========================

    static void quickSort(int[] a, int l, int r) {
        if (l >= r) return;
        int p = partition(a, l, r);
        quickSort(a, l, p - 1);
        quickSort(a, p + 1, r);
    }

    static int partition(int[] a, int l, int r) {
        int pivot = a[r];
        int i = l - 1;
        for (int j = l; j < r; j++) {
            if (a[j] < pivot) {
                i++;
                int tmp = a[i]; a[i] = a[j]; a[j] = tmp;
            }
        }
        int tmp = a[i+1]; a[i+1] = a[r]; a[r] = tmp;
        return i + 1;
    }

    // ========================= Binary Search =========================

    static int binarySearch(int[] a, int key) {
        int l = 0, r = a.length - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;
            if (a[m] == key) return m;
            else if (a[m] < key) l = m + 1;
            else r = m - 1;
        }
        return -1;
    }

    // ========================= LIS (n log n) =========================

    static int lisLength(int[] arr) {
        // patience sorting method
        if (arr.length == 0) return 0;
        int[] tail = new int[arr.length];
        int size = 0;
        for (int x : arr) {
            int i = Arrays.binarySearch(tail, 0, size, x);
            if (i < 0) i = -(i + 1);
            tail[i] = x;
            if (i == size) size++;
        }
        return size;
    }

    // ========================= 0/1 Knapsack =========================

    static int knapsack01(int[] val, int[] wt, int W) {
        int n = val.length;
        int[][] dp = new int[n+1][W+1];
        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= W; w++) {
                dp[i][w] = dp[i-1][w];
                if (w >= wt[i-1]) {
                    dp[i][w] = Math.max(dp[i][w], dp[i-1][w - wt[i-1]] + val[i-1]);
                }
            }
        }
        return dp[n][W];
    }

    // ========================= Union-Find (Disjoint Set) =========================

    static class UnionFind {
        int[] parent;
        int[] rank;
        public UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;
        }
        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }
        void union(int x, int y) {
            int rx = find(x), ry = find(y);
            if (rx == ry) return;
            if (rank[rx] < rank[ry]) parent[rx] = ry;
            else if (rank[ry] < rank[rx]) parent[ry] = rx;
            else { parent[ry] = rx; rank[rx]++; }
        }
    }

    static void unionFindDemo() {
        UnionFind uf = new UnionFind(10);
        uf.union(1, 2);
        uf.union(2, 5);
        uf.union(5, 6);
        uf.union(6, 7);
        uf.union(3, 8);
        uf.union(8, 9);
        System.out.println("Find(5) -> " + uf.find(5));
        System.out.println("Find(7) -> " + uf.find(7));
        System.out.println("Find(3) -> " + uf.find(3));
        System.out.println("Are 1 and 7 connected? " + (uf.find(1) == uf.find(7)));
        System.out.println("Are 4 and 9 connected? " + (uf.find(4) == uf.find(9)));
    }

    // ========================= Sieve of Eratosthenes =========================

    static List<Integer> sieve(int n) {
        boolean[] isPrime = new boolean[n+1];
        Arrays.fill(isPrime, true);
        isPrime[0] = false; if (n >= 1) isPrime[1] = false;
        for (int p = 2; p*p <= n; p++) {
            if (isPrime[p]) {
                for (int q = p*p; q <= n; q += p) isPrime[q] = false;
            }
        }
        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= n; i++) if (isPrime[i]) primes.add(i);
        return primes;
    }

    // ========================= Weighted Graph & Dijkstra =========================

    static class WeightedGraph {
        int n;
        List<List<Edge>> adj;
        WeightedGraph(int n) {
            this.n = n;
            adj = new ArrayList<>();
            for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        }
        void addEdge(int u, int v, int w) {
            adj.get(u).add(new Edge(v, w));
            adj.get(v).add(new Edge(u, w)); // если хотите направленный граф — уберите эту строку
        }
    }

    static class Edge {
        int to;
        int w;
        Edge(int t, int ww) { to = t; w = ww; }
    }

    static int[] dijkstra(WeightedGraph g, int src) {
        int n = g.n;
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE / 4);
        dist[src] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.add(new int[]{src, 0});
        boolean[] vis = new boolean[n];
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int u = cur[0];
            if (vis[u]) continue;
            vis[u] = true;
            for (Edge e : g.adj.get(u)) {
                if (dist[e.to] > dist[u] + e.w) {
                    dist[e.to] = dist[u] + e.w;
                    pq.add(new int[]{e.to, dist[e.to]});
                }
            }
        }
        return dist;
    }

    // ========================= Directed Graph & Topological Sort (Kahn) =========================

    static class DirectedGraph {
        int n;
        List<List<Integer>> adj;
        DirectedGraph(int n) {
            this.n = n;
            adj = new ArrayList<>();
            for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        }
        void addEdge(int u, int v) { adj.get(u).add(v); }
    }

    static List<Integer> topologicalSortKahn(DirectedGraph g) {
        int n = g.n;
        int[] indeg = new int[n];
        for (int u = 0; u < n; u++) {
            for (int v : g.adj.get(u)) indeg[v]++;
        }
        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) if (indeg[i] == 0) q.add(i);
        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            order.add(u);
            for (int v : g.adj.get(u)) {
                indeg[v]--;
                if (indeg[v] == 0) q.add(v);
            }
        }
        if (order.size() != n) {
            System.out.println("Graph has a cycle! Topological order not possible for all vertices.");
        }
        return order;
    }
}