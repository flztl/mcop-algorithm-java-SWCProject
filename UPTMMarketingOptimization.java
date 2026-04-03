import java.util.*;

public class UPTMMarketingOptimization {

    // Cost Matrix
    static int[][] costMatrix = {
        {0, 15, 25, 35},
        {15, 0, 30, 28},
        {25, 30, 0, 20},
        {35, 28, 20, 0}
    };

    // Locations
    static String[] locations = {"UPTM", "City B", "City C", "City D"};

    // ================= GREEDY =================
    public static String greedyMCOP(int[][] dist) {
        int n = dist.length;
        boolean[] visited = new boolean[n];
        StringBuilder path = new StringBuilder();
        int totalCost = 0;
        int current = 0;

        visited[0] = true;
        path.append(locations[0]);

        for (int step = 0; step < n - 1; step++) {
            int nearest = -1;
            int minCost = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (!visited[i] && dist[current][i] < minCost) {
                    minCost = dist[current][i];
                    nearest = i;
                }
            }

            visited[nearest] = true;
            path.append(" -> ").append(locations[nearest]);
            totalCost += minCost;
            current = nearest;
        }

        totalCost += dist[current][0];
        path.append(" -> ").append(locations[0]);

        return "Greedy Route: " + path + " | Total Cost: " + totalCost;
    }

    // ================= DYNAMIC PROGRAMMING =================
    public static String dynamicProgrammingMCOP(int[][] dist) {
        int n = dist.length;
        int VISITED_ALL = (1 << n) - 1;
        int[][] memo = new int[n][1 << n];
        String[][] paths = new String[n][1 << n];

        for (int i = 0; i < n; i++)
            Arrays.fill(memo[i], -1);

        int cost = dynamicProgrammingMCOPHelper(0, 1, dist, memo, VISITED_ALL, paths);

        StringBuilder pathStr = new StringBuilder();
        int pos = 0;
        int mask = 1;
        pathStr.append(locations[pos]);

        while (mask != VISITED_ALL) {
            int next = Integer.parseInt(paths[pos][mask]);
            pathStr.append(" -> ").append(locations[next]);
            mask |= (1 << next);
            pos = next;
        }
        pathStr.append(" -> ").append(locations[0]);

        return "Dynamic Programming Route: " + pathStr + " | Total Cost: " + cost;
    }

    private static int dynamicProgrammingMCOPHelper(int pos, int mask, int[][] dist,
                                                   int[][] memo, int VISITED_ALL, String[][] paths) {

        if (mask == VISITED_ALL)
            return dist[pos][0];

        if (memo[pos][mask] != -1)
            return memo[pos][mask];

        int minCost = Integer.MAX_VALUE;

        for (int city = 0; city < dist.length; city++) {
            if ((mask & (1 << city)) == 0) {
                int newCost = dist[pos][city] +
                        dynamicProgrammingMCOPHelper(city, mask | (1 << city),
                                dist, memo, VISITED_ALL, paths);

                if (newCost < minCost) {
                    minCost = newCost;
                    paths[pos][mask] = String.valueOf(city);
                }
            }
        }

        return memo[pos][mask] = minCost;
    }

    // ================= BACKTRACKING =================
    static class Result {
        int minCost = Integer.MAX_VALUE;
        String bestPath = "";
    }

    public static String backtrackingMCOP(int[][] dist) {
        int n = dist.length;
        boolean[] visited = new boolean[n];
        visited[0] = true;

        StringBuilder path = new StringBuilder("UPTM");
        Result result = new Result();

        mcopBacktracking(0, dist, visited, n, 1, 0, path, result);

        return "Backtracking Route: " + result.bestPath + " | Total Cost: " + result.minCost;
    }

    private static void mcopBacktracking(int pos, int[][] dist, boolean[] visited,
                                         int n, int count, int cost, StringBuilder path, Result result) {

        if (count == n) {
            int totalCost = cost + dist[pos][0];
            String finalPath = path + " -> UPTM";

            if (totalCost < result.minCost) {
                result.minCost = totalCost;
                result.bestPath = finalPath;
            }
            return;
        }

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                visited[i] = true;

                int len = path.length();
                path.append(" -> ").append(locations[i]);

                mcopBacktracking(i, dist, visited, n, count + 1,
                        cost + dist[pos][i], path, result);

                visited[i] = false;
                path.setLength(len);
            }
        }
    }

    // ================= DIVIDE & CONQUER =================
    public static String divideAndConquerMCOP(int[][] dist) {
        int n = dist.length;
        boolean[] visited = new boolean[n];
        visited[0] = true;

        StringBuilder path = new StringBuilder(locations[0]);

        int cost = divideAndConquerHelper(0, visited, 0, dist, n, path);
        path.append(" -> ").append(locations[0]);

        return "Divide & Conquer Route: " + path + " | Total Cost: " + cost;
    }

    private static int divideAndConquerHelper(int pos, boolean[] visited,
                                              int cost, int[][] dist, int n, StringBuilder path) {

        if (allVisited(visited))
            return cost + dist[pos][0];

        int bestCity = -1;
        int bestCost = Integer.MAX_VALUE;

        for (int i = 0; i < n; i++) {
            if (!visited[i] && dist[pos][i] < bestCost) {
                bestCost = dist[pos][i];
                bestCity = i;
            }
        }

        visited[bestCity] = true;
        path.append(" -> ").append(locations[bestCity]);

        return divideAndConquerHelper(bestCity, visited,
                cost + bestCost, dist, n, path);
    }

    private static boolean allVisited(boolean[] visited) {
        for (boolean v : visited)
            if (!v) return false;
        return true;
    }

    // ================= SORTING =================
    public static String insertionSort(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;

            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
        return Arrays.toString(arr);
    }

    // ================= SEARCHING =================
    public static String binarySearch(int[] arr, int target) {
        int left = 0, right = arr.length - 1;

        while (left <= right) {
            int mid = (left + right) / 2;

            if (arr[mid] == target)
                return String.valueOf(mid);

            if (arr[mid] < target)
                left = mid + 1;
            else
                right = mid - 1;
        }
        return "-1";
    }

    // ================= MIN HEAP =================
    static class MinHeap {
        PriorityQueue<Integer> heap = new PriorityQueue<>();

        void insert(int val) {
            heap.add(val);
        }

        int extractMin() {
            return heap.poll();
        }
    }

    // ================= SPLAY TREE =================
    static class SplayTree {
        class Node {
            int data;
            Node left, right;

            Node(int d) { data = d; }
        }

        Node root;

        Node rightRotate(Node x) {
            Node y = x.left;
            x.left = y.right;
            y.right = x;
            return y;
        }

        Node leftRotate(Node x) {
            Node y = x.right;
            x.right = y.left;
            y.left = x;
            return y;
        }

        Node splay(Node root, int key) {
            if (root == null || root.data == key)
                return root;

            if (key < root.data) {
                if (root.left == null) return root;

                if (key < root.left.data) {
                    root.left.left = splay(root.left.left, key);
                    root = rightRotate(root);
                } else if (key > root.left.data) {
                    root.left.right = splay(root.left.right, key);
                    if (root.left.right != null)
                        root.left = leftRotate(root.left);
                }

                return (root.left == null) ? root : rightRotate(root);
            } else {
                if (root.right == null) return root;

                if (key > root.right.data) {
                    root.right.right = splay(root.right.right, key);
                    root = leftRotate(root);
                } else if (key < root.right.data) {
                    root.right.left = splay(root.right.left, key);
                    if (root.right.left != null)
                        root.right = rightRotate(root.right);
                }

                return (root.right == null) ? root : leftRotate(root);
            }
        }

        void insert(int key) {
            root = splay(root, key);

            if (root == null) {
                root = new Node(key);
                return;
            }

            Node newNode = new Node(key);

            if (key < root.data) {
                newNode.right = root;
                newNode.left = root.left;
                root.left = null;
            } else {
                newNode.left = root;
                newNode.right = root.right;
                root.right = null;
            }

            root = newNode;
        }

        boolean search(int key) {
            root = splay(root, key);
            return root != null && root.data == key;
        }
    }

    // ================= MAIN =================
    public static void main(String[] args) {

        System.out.println(greedyMCOP(costMatrix));
        System.out.println(dynamicProgrammingMCOP(costMatrix));
        System.out.println(backtrackingMCOP(costMatrix));
        System.out.println(divideAndConquerMCOP(costMatrix));

        int[] arr = {8, 3, 5, 1, 9, 2};
        insertionSort(arr);
        System.out.println("Sorted Array: " + Arrays.toString(arr));
        System.out.println("Binary Search (5 found at index): " + binarySearch(arr, 5));

        MinHeap heap = new MinHeap();
        heap.insert(10);
        heap.insert(3);
        heap.insert(15);
        System.out.println("Min-Heap Extract Min: " + heap.extractMin());

        SplayTree tree = new SplayTree();
        tree.insert(20);
        tree.insert(10);
        tree.insert(30);
        System.out.println("Splay Tree Search (10 found): " + tree.search(10));
    }
}