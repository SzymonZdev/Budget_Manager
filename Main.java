package budget;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static final HashMap<String, HashMap<String, Double>> allProducts = new HashMap<>();

    private static final DecimalFormat dfZero = new DecimalFormat("0.00");

    private static final String pathName = "./purchases.txt";

    private static double balance;

    public static boolean goLive = true;

    static {
        allProducts.put("Food", new HashMap<>());
        allProducts.put("Clothes", new HashMap<>());
        allProducts.put("Entertainment", new HashMap<>());
        allProducts.put("Other", new HashMap<>());
    }

    public static void showMenu() {
        System.out.println();
        System.out.println("Choose your action:");
        System.out.println("1) Add income");
        System.out.println("2) Add purchase");
        System.out.println("3) Show list of purchases");
        System.out.println("4) Balance");
        System.out.println("5) Save");
        System.out.println("6) Load");
        System.out.println("7) Analyze (Sort)");
        System.out.println("0) Exit");
        System.out.println();
    }

    public static void showSortMenu() {
        System.out.println();
        System.out.println("How do you want to sort?");
        System.out.println("1) Sort all purchases");
        System.out.println("2) Sort by type");
        System.out.println("3) Sort certain type");
        System.out.println("4) Back");
        System.out.println();
        int choice = scanner.nextInt();
        switch (choice) {
            case 1 -> sortAllPurchases();
            case 2 -> sortCategories();
            case 3 -> {
                showCategories("don't show all");
                int category = scanner.nextInt();
                switch (category) {
                    case 1 -> sortByCategory("Food");
                    case 2 -> sortByCategory("Clothes");
                    case 3 -> sortByCategory("Entertainment");
                    case 4 -> sortByCategory("Other");
                }
            }
            default -> liveProgram();
        }
    }

    public static void sortByCategory(String category) {
        HashMap<String, Double> listOfProducts = listProducts(category);
        if (listOfProducts.isEmpty()) {
            System.out.println("\nThe purchase list is empty!");
        } else {
            List<Map.Entry<String, Double> > list = new LinkedList<>(listOfProducts.entrySet());
            list.sort(Map.Entry.comparingByValue());
            Collections.reverse(list);
            HashMap<String, Double> temp = new LinkedHashMap<>();
            for (Map.Entry<String, Double> aa : list) {
                temp.put(aa.getKey(), aa.getValue());
            }
            System.out.println("\nAll:");
            double total = 0.0;
            for (String key: temp.keySet()
            ) {
                if (key.equals("Income ")) {
                    continue;
                }
                System.out.println(key + "$" + dfZero.format(temp.get(key)));
                total += temp.get(key);
            }
            System.out.println("Total: $" + dfZero.format(total));
        }
        showSortMenu();
    }

    public static void sortAllPurchases() {
        if (isPurchasesEmpty()) {
            System.out.println("\nThe purchase list is empty!");
        } else {
            HashMap<String, Double> listOfProducts = listProducts();
            List<Map.Entry<String, Double> > list = new LinkedList<>(listOfProducts.entrySet());
            list.sort(Map.Entry.comparingByValue());
            Collections.reverse(list);
            HashMap<String, Double> temp = new LinkedHashMap<>();
            for (Map.Entry<String, Double> aa : list) {
                temp.put(aa.getKey(), aa.getValue());
            }
            System.out.println("\nAll:");
            double total = 0.0;
            for (String key: temp.keySet()
            ) {
                if (key.equals("Income ")) {
                    continue;
                }
                System.out.println(key + "$" + dfZero.format(temp.get(key)));
                total += temp.get(key);
            }
            System.out.println("Total: $" + dfZero.format(total));
        }
        showSortMenu();
    }
    public static void sortCategories() {
        HashMap<String, Double> listOfCategories = new HashMap<>();
        listOfCategories.put("Food", 0.0);
        listOfCategories.put("Clothes", 0.0);
        listOfCategories.put("Entertainment", 0.0);
        listOfCategories.put("Other", 0.0);

        for (String category: allProducts.keySet()
        ) {
            for (String product : allProducts.get(category).keySet()
            ) {
                if (!product.equals("Income ")) {
                    listOfCategories.put(category, listOfCategories.get(category) + allProducts.get(category).get(product));
                }
            }
        }

        List<Map.Entry<String, Double> > list = new LinkedList<>(listOfCategories.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);
        HashMap<String, Double> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        System.out.println("\nTypes:");
        double total = 0.0;
        for (String key: temp.keySet()
        ) {
            System.out.println(key + " - $" + dfZero.format(temp.get(key)));
            total += temp.get(key);
        }
        System.out.println("Total sum: $" + dfZero.format(total));

        showSortMenu();
    }

    public static HashMap<String, Double> listProducts() {
        HashMap<String, Double> listedProducts = new HashMap<>();
        for (String outerKey: allProducts.keySet()
        ) {
            if (!allProducts.get(outerKey).isEmpty()) {
                for (String innerKey: allProducts.get(outerKey).keySet()
                ) {
                    listedProducts.put(innerKey ,allProducts.get(outerKey).get(innerKey));
                }
            }
        }

        return listedProducts;
    }

    public static HashMap<String, Double> listProducts(String category) {
        HashMap<String, Double> listedProducts = new HashMap<>();

        if (!allProducts.get(category).isEmpty()) {
            for (String innerKey: allProducts.get(category).keySet()) {
                listedProducts.put(innerKey ,allProducts.get(category).get(innerKey));
            }
        }

        return listedProducts;
    }

    public static void showCategories(String flag) {
        System.out.println();
        System.out.println("Choose the type of purchase:");
        System.out.println("1) Food");
        System.out.println("2) Clothes");
        System.out.println("3) Entertainment");
        System.out.println("4) Other");
        if (flag.equals("show all")) {
            System.out.println("5) All");
            System.out.println("6) Back");
            System.out.println();
        } else {
            System.out.println("5) Back");
            System.out.println();
        }
    }

    public static void chooseCategory() {
        showCategories("show all");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1 -> showPurchasesAndIncome("Food");
            case 2 -> showPurchasesAndIncome("Clothes");
            case 3 -> showPurchasesAndIncome("Entertainment");
            case 4 -> showPurchasesAndIncome("Other");
            case 5 -> showPurchasesAndIncome();
            case 6 -> liveProgram();
        }
    }


    public static void showPurchasesAndIncome() {
        System.out.println();

        if (isPurchasesEmpty()) {
            System.out.println("The purchase list is empty!");
        } else {
            Double sum = 0.00;
            for (String category : allProducts.keySet()
            ) {
                for (String product : allProducts.get(category).keySet()) {
                    if (product.equals("Income ")) {
                        System.out.println("$" + dfZero.format(allProducts.get(category).get(product)));
                        continue;
                    }
                    sum += allProducts.get(category).get(product);
                    System.out.println(product + "$" + dfZero.format(allProducts.get(category).get(product)));
                }
            }
            System.out.println("Total sum: $" + dfZero.format(sum));
        }
        chooseCategory();
    }

    public static void showPurchasesAndIncome(String category) {
        System.out.println();
        if (isPurchasesEmpty()) {
            System.out.println("The purchase list is empty!");
        }

        double sum = 0.0;

        for (String product : allProducts.get(category).keySet()) {
            if (product.equals("Income ")) {
                System.out.println("$" + dfZero.format(allProducts.get(category).get(product)));
                continue;
            }
            sum += allProducts.get(category).get(product);
            System.out.println(product + "$" + dfZero.format(allProducts.get(category).get(product)));
        }
        System.out.println("Total sum: $" + dfZero.format(sum));

        chooseCategory();
    }


    public static void showBalance() {
        System.out.println("Balance: $" + dfZero.format(balance));
    }

    public static void addIncome() {
        System.out.println("Enter income:");
        double income = scanner.nextDouble();
        balance += income;
        allProducts.get("Other").put("Income " , income);
        System.out.println("Income was added!");
    }

    public static void addPurchase() {
        showCategories("don't show all");
        switch (scanner.nextInt()) {
            case 1 -> {
                addPurchaseForCategory("Food");
            }
            case 2 -> {
                addPurchaseForCategory("Clothes");
            }
            case 3 -> {
                addPurchaseForCategory("Entertainment");
            }
            case 4 -> {
                addPurchaseForCategory("Other");
            }
            default -> {
                liveProgram();
            }
        }


    }

    public static void addPurchaseForCategory(String category) {
        System.out.println("Enter purchase name:");
        scanner.nextLine();
        String purchase = scanner.nextLine() + " ";
        System.out.println("Enter its price:");
        double purchasePrice = scanner.nextDouble();
        savePurchase(category, purchase, purchasePrice);
        balance -= purchasePrice;

        addPurchase();
    }

    public static void savePurchases() throws IOException {
        FileWriter fileWriter = new FileWriter(pathName);

        StringBuilder purchases = new StringBuilder();

        if (isPurchasesEmpty()) {
            System.out.println("The purchase list is empty!");
        } else {
            for (String outerKey : allProducts.keySet()
            ) {
                if (allProducts.get(outerKey).isEmpty()) {
                    continue;
                }
                purchases.append(outerKey).append("\n");
                for (String purchase : allProducts.get(outerKey).keySet()) {
                    purchases.append(purchase).append("$").append(dfZero.format(allProducts.get(outerKey).get(purchase))).append("\n");
                }
            }
        }
        try {
            fileWriter.write(purchases.toString());
        } catch (IOException e) {
            e.getStackTrace();
        } finally {
            fileWriter.close();
        }
    }

    public static void loadPurchases() throws IOException {
        FileReader fileReader = new FileReader((pathName));
        Scanner scanner1 = new Scanner(fileReader);
        String category = "";

        while (scanner1.hasNext()) {
            String str = scanner1.nextLine();
            StringBuilder product = new StringBuilder();
            if (allProducts.containsKey(str)) {
                category = str;
            } else {
                String[] purchase = str.split("\\$");

                if (purchase.length == 2) {
                    double amount = Double.parseDouble(purchase[1]);
                    savePurchase(category, purchase[0], amount);
                    if (purchase[0].equals("Income ")) {
                        balance += amount;
                    } else {
                        balance -= amount;
                    }
                } else {
                    double amount = Double.parseDouble(purchase[purchase.length-1]);
                    for (int i = 0; i < purchase.length - 1; i++) {
                        product.append(purchase[i]);
                        if (i == 0) {
                            product.append("$");
                        }
                    }
                    savePurchase(category, product.toString(), amount);
                    balance -= amount;
                }
                continue;
            }
            str = scanner1.nextLine();
            String[] purchase = str.split("\\$");


            if (purchase.length == 2) {
                double amount = Double.parseDouble(purchase[1]);
                savePurchase(category, purchase[0], amount);
                if (purchase[0].equals("Income ")) {
                    balance += amount;
                } else {
                    balance -= amount;
                }
            } else {
                double amount = Double.parseDouble(purchase[purchase.length-1]);
                for (int i = 0; i < purchase.length - 1; i++) {
                    product.append(purchase[i]);
                    if (i == 0) {
                        product.append("$");
                    }
                }
                savePurchase(category, product.toString(), amount);
                balance -= amount;
            }
        }

        fileReader.close();
        System.out.println("Purchases were loaded!");
    }

    public static void savePurchase(String category, String product, Double price) {
        allProducts.get(category).put(product, price);
    }

    public static boolean isPurchasesEmpty() {
        for (String outerKey: allProducts.keySet()
        ) {
            if (!allProducts.get(outerKey).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static void liveProgram() {
        while (goLive) {
            showMenu();
            switch (scanner.nextInt()) {
                case 1 -> {
                    System.out.println();
                    addIncome();
                }
                case 2 -> {
                    addPurchase();
                }
                case 3 -> {
                    chooseCategory();
                }
                case 4 -> {
                    System.out.println();
                    showBalance();
                }
                case 5 -> {
                    System.out.println();
                    try {
                        savePurchases();
                    } catch (IOException e) {
                        e.getStackTrace();
                    }
                }

                case 6 -> {
                    System.out.println();
                    try {
                        loadPurchases();
                    } catch (IOException e) {
                        e.getStackTrace();
                    }
                }
                case 7 -> {
                    showSortMenu();
                }
                default -> {
                    System.out.println();
                    System.out.println("Bye!");
                    goLive = false;
                    System.out.println();
                }
            }
        }
    }



    public static void main(String[] args) {
        liveProgram();
    }
}