package com.pluralsight;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final Scanner input = new Scanner(System.in);
    static ArrayList<Product> productList = new ArrayList<>();
    static ArrayList<CartItem> cart = new ArrayList<>();
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Welcome to Online store application");
        startMenu();
//        storeProductInArrayList();
//        filterProductsBy();
//        listAllProducts(null);
//        filterProductByName();

//        storeProduct(); // Method that ask for input and store it in product file

    }

    public static void startMenu () throws InterruptedException {
        productList.clear();
        storeProductInArrayList();

        System.out.println("\n====== Online Store ======");
        System.out.println("[1]. View all products");
        System.out.println("[2]. Search or filter products");
        if(!cart.isEmpty()){System.out.println("[3]. View cart items");}
        System.out.println("[4]. Add Product");
        System.out.println("[5]. Exit");
        System.out.print("Choose an option (1–5): ");
        int choice = input.nextInt();
        switch (choice) {
            case 1:
                listAllProducts(null);
                break;

            case 2:
                filterProductsBy();
                break;

            case 3:
                viewCartItems();
                break;

            case 4:
                storeProduct();
                break;

            case 5:
                System.out.println("Good Bye!");
                System.exit(0);
                break;

            default:
                System.out.println("Wrong choice!");
                Thread.sleep(2000);
                break;

        }
    }

    public static void filterProductsBy() {
        System.out.println("""
                [1]. Filter by name:
                [2]. Filter by price:
                [3]. Filter by department:
                [X]. Return main menu
                """);
        System.out.print("Choose an option (1–3, X): ");
        input.nextLine();
        String filterBy = input.nextLine();
        try {

            if (filterBy.equalsIgnoreCase("x")) {
                startMenu();
            }else {
                switch (Integer.parseInt(filterBy)) {
                    case 1:
                        filterProductByName();
                        break;
                    case 2:
                        filterProductByPrice();
                        break;
                    case 3:
                        filterProductByDepartment();
                        break;
                    default:
                        System.out.println("Invalid Input, you will be redirected back to the main menu");
                        Thread.sleep(2000);
                        startMenu();

                }
            }

        }catch (Exception e){
            System.out.println("Invalid Input");
            System.out.println(e.getMessage());
        }
    }

    public static void listAllProducts(ArrayList<Product> products ) throws InterruptedException {

        ArrayList<Product> productData;

        if (products == null) {
            productData = productList; // use the global list
        } else {
            productData = products; // use the list passed to the method
        }


        System.out.printf("%-10s %-30s %10s %-20s%n",
                "SKU", "Product Name", "Price", "Department");
        System.out.println("-----------------------------------------------------------------------");

        if(productData.isEmpty()) {
            System.out.println("Product are not available, at this time, please later");
        }

        for(Product productRow : productData) {

            try {
                System.out.printf("%-10s %-30s $%9.2f %-20s%n",
                        productRow.getSKU(),
                        productRow.getName(),
                        productRow.getPrice(),
                        productRow.getDepartment());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        if(!productData.isEmpty()) {
            addCartConfirmation();
        }

    }

    public static void addCartConfirmation() throws InterruptedException {
        System.out.println("\n========== CONFIRMATION ADD TO CART ==========");
        System.out.println("[1] Ready to add a product to your cart?");
        System.out.println("[0] Return to the main menu");
        System.out.print("Choose an option (0-1): ");


            try {
                int choice = input.nextInt(); input.nextLine();
                if (choice == 1) {
                    addProductToCart();
                }else {
                    startMenu();
                }
            }catch (Exception e){
                System.out.println("Invalid Input");
            }

    }

    public static void addProductToCart() throws InterruptedException {

        System.out.println("Please enter the product SKU number:");
        String productSKU = input.nextLine();

        for(Product productRow : productList) {
            try {
                if (productSKU.equalsIgnoreCase(productRow.getSKU())) {
                    System.out.printf("%nSelected: %s — $%.2f%n",
                            productRow.getName(),
                            productRow.getPrice());

                    System.out.println("How many would you like to add to your cart?");
                    System.out.print("Enter a quantity, or enter 0 to return to the main menu: ");
                    int quantity = input.nextInt();
                    input.nextLine();

                    if (quantity > 0) {
                        cart.add(new CartItem(productRow, quantity));
                        System.out.println("Product added successfully!");
                    }

                    if (!cart.isEmpty()) {
                        System.out.println("Number of items in cart: " + cart.size());
                    }

                    System.out.println("\nWhat would you like to do next?");
                    System.out.println("[1] Add another product");
                    System.out.println("[2] View your cart");
                    System.out.println("[0] Return to the main menu");
                    System.out.print("Choose an option (0-1): ");
                    int choice = input.nextInt();
                    input.nextLine();

                    if (choice == 1) {
                        addProductToCart();
                    } else if (choice == 2) {
                        viewCartItems();
                    } else {
                        startMenu();
                    }
                }
            }catch (Exception e){
                System.out.println("Invalid Input");
            }
        }
    }

    public static void viewCartItems() throws InterruptedException {
        System.out.println("\n================ YOUR CART ===========================================\n");
        System.out.printf("%-10s %-28s %5s %12s %12s%n",
                "SKU", "Product", "Qty", "Unit Price", "Line Total");
        System.out.println("--------------------------------------------------------------------------");

        double cartTotal = 0;
        int totalItems = 0;
        for (CartItem item : cart) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();
            double lineTotal = product.getPrice() * quantity;

            totalItems += quantity;
            cartTotal += lineTotal;

            System.out.printf("%-10s %-28s %4d $%10.2f $%10.2f%n",
                    product.getSKU(),
                    product.getName(),
                    quantity,
                    product.getPrice(),
                    lineTotal);
        }

        System.out.println("--------------------------------------------------------------------------");
        System.out.printf("Total items: %d%n", totalItems);
        System.out.printf("Cart total: $%.2f%n", cartTotal);

        System.out.println("============================== END OF CART ITEMS ===========================");
        checkoutOrRemoveCartItems();

    }

    public static void RemoveCartItems() throws InterruptedException {
        System.out.println("Enter the SKU number to remove product from your cart:");
        String productSKU = input.nextLine();
        boolean confirmation = false;
        for(CartItem item : cart) {
            if(productSKU.equalsIgnoreCase(item.getProduct().getSKU())) {
                cart.remove(item);
                confirmation = true;
            }
        }
        showProcessingAnimation();
        if(!confirmation) {
            System.out.println("Product not found!");

        }else {
            System.out.println("Product removed successfully!");
        }

        viewCartItems();
    }

    public static void checkoutOrRemoveCartItems() throws InterruptedException {
        System.out.println("Ready to checkout your cart");
        System.out.println("[1] to checkout");
        System.out.println("[2] to remove item from your cart");
        System.out.println("[0] Return to the main menu");
        System.out.print("Choose an option (0-2): ");
        Integer choice = input.nextInt();
        input.nextLine();
        switch (choice) {
            case 0:
                startMenu();
                break;
            case 1:
                showProcessingAnimation();
                System.out.println("Thank you for you payment");
                break;
            case 2:
                showProcessingAnimation();
                RemoveCartItems();
                break;
            default:
                System.out.println("Invalid input");
                System.out.println("You will be redirected back to the main menu");
                startMenu();

        }
    }

    public static void showProcessingAnimation() {
        char[] spinner = {'|', '/', '-', '\\'};

        for (int i = 0; i < 20; i++) {
            System.out.print("\rProcessing... " + spinner[i % spinner.length]);

            try {
                Thread.sleep(50); // pause for 50 milliseconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        System.out.println("\rProcessing complete!   ");
    }

    public static void filterProductByName() throws InterruptedException {
        ArrayList<Product> matches = new ArrayList<>();

        System.out.println("Please enter the product name:");
        String productName = input.nextLine();

        for (Product product : productList) {
            if (product.getName().toLowerCase().contains(productName.trim().toLowerCase())) {
                matches.add(product);
            }
        }

        listAllProducts(matches);
    }

    public static void filterProductByPrice() throws InterruptedException {
        ArrayList<Product> matches = new ArrayList<>();

        System.out.println("Please enter the start price:");
        Double price = input.nextDouble();
        System.out.println("Please enter the End price");
        Double endPrice = input.nextDouble();
        input.nextLine();

        for (Product product : productList) {
            if (product.getPrice() > price && product.getPrice() <= endPrice) {
                matches.add(product);
            }
        }

        listAllProducts(matches);
    }

    public static void filterProductByDepartment() throws InterruptedException {
        ArrayList<Product> matches = new ArrayList<>();

        System.out.println("Please enter the product department:");
        String productDepartment = input.nextLine();

        for (Product product : productList) {
            if (product.getDepartment().toLowerCase()
                    .contains(productDepartment.trim().toLowerCase())) {
                matches.add(product);
            }
        }

        listAllProducts(matches);
    }

    public static void storeProductInArrayList() {
        productList.clear();

        try {
            BufferedReader br = new BufferedReader(new FileReader("uploads/products.csv"));
            br.readLine();
            String line = "";

            while ((line = br.readLine()) != null) {
                String[] data = line.split("\\|", -1);
                if (data.length < 4) {
                    System.out.println("Skipping incomplete row: " + line);
                    continue;
                }

                try {
                    productList.add(new Product(
                            data[0].trim(),
                            data[1].trim(),
                            Double.parseDouble(data[2].trim()),
                            data[3].trim()
                    ));

                } catch (NumberFormatException e) {
                    System.out.println("Skipping row with an invalid price: " + line);
                }
            }

//            System.out.println(productList.size() + " products stored");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public static void storeProduct() {
        System.out.println("LOGIN PAGE");
        input.nextLine();
        System.out.println("Please enter username (admin):");
        String username = input.nextLine();
        System.out.println("Please enter password (admin):");
        String password = input.nextLine();
        if(username.equals("admin") && password.equals("admin")) {
            System.out.println("Login successful!");

            try {
                Product product = new Product();
                System.out.println("Please enter the product SKU number: ");
                product.setSKU(input.nextLine());
                System.out.println("Please enter the product name: ");
                product.setName(input.nextLine());
                System.out.println("Please enter the product price: ");
                product.setPrice(input.nextDouble()); input.nextLine(); // to clear remaining
                System.out.println("Which department: ");
                product.setDepartment(input.nextLine());

                try {
                    BufferedWriter bw = new BufferedWriter(new FileWriter("uploads/products.csv", true));
                    bw.write(product.getSKU() + "|" + product.getName() + "|" + product.getPrice() + "|" + product.getDepartment());
                    bw.newLine();
                    bw.close();

                    System.out.println("Product stored successfully!");
                    startMenu();
                }catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }else {
            System.out.println("Invalid username or password!");
            storeProduct();
        }




    }
}