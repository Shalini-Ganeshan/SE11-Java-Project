package labs.pm.app.Shop;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Locale;
import labs.pm.data.Product;
import labs.pm.data.ProductManager;
import labs.pm.data.Rating;

public class ProductManagement {

    /**
     * Main method to run the product management application.
     * <p>
     * It creates multiple products, sets their details, adds reviews, and
     * prints the product information.
     * </p>
     *
     * @param args the command line arguments (not used in this version)
     */
    public static void main(String[] args) {
        // Create a new ProductManager instance with UK locale
        ProductManager pm = new ProductManager("en-GB");
//its random where the logger prints the error message as it has its own thread
        // Add products
       pm.parseProduct("F,101,Idli,0.99,1");
pm.parseProduct("F,102,Dosa,1.50,2");
pm.parseProduct("F,103,Sambar,1.29,4");
pm.parseProduct("F,104,Chutney,0.79,2");
pm.parseProduct("F,105,Rasam,1.00,4");

// Add reviews for each product
//pm.parseReview("101,5,Soft and fluffy idli, loved it!");
//pm.parseReview("101,4,Classic Tamil breakfast, so good!");
//pm.parseReview("101,5,Idli with chutney and sambar is perfect!");

pm.parseReview("102,5,Golden crispy dosa, amazing!");
pm.parseReview("102,3,Too oily but tastes okay.");
pm.parseReview("102,4,Masala dosa was really flavorful!");

pm.parseReview("103,5,Authentic Tamil sambar, delicious!");
pm.parseReview("103,4,Perfectly spiced, great with rice.");
pm.parseReview("103,3,A bit watery, but still good.");

pm.parseReview("104,4,Creamy coconut chutney, very tasty.");
pm.parseReview("104,5,Best chutney to go with dosa!");
pm.parseReview("104,3,Good chutney, but needed more salt.");

pm.parseReview("105,5,Tangy rasam, so refreshing!");
pm.parseReview("105,4,Perfect rasam for a cold day.");
pm.parseReview("105,5,Spicy and flavorful, loved it!");
// Tamilian Drinks
pm.parseProduct("D,201,Filter Coffee,1.50,5,2024-12-31");
pm.parseProduct("D,202,Jigarthanda,2.00,4,2024-11-15");
pm.parseProduct("D,203,Panakam,1.00,4,2024-10-20");
pm.parseProduct("D,204,Buttermilk,0.75,5,2024-09-10");
pm.parseProduct("D,205,Rose Milk,1.25,4,2024-12-01");

// Add reviews for each drink
pm.parseReview("201,5,Authentic South Indian filter coffee, strong and aromatic!");
pm.parseReview("201,4,Perfect start to the day, loved the taste.");
pm.parseReview("201,5,Brewed to perfection, just like at home!");

pm.parseReview("202,5,Refreshing and sweet, perfect Jigarthanda!");
pm.parseReview("202,4,Thick and creamy, enjoyed it.");
pm.parseReview("202,4,Good, but could have been colder.");

pm.parseReview("203,5,Traditional Panakam, sweet and spicy!");
pm.parseReview("203,4,Best drink for a hot day, very refreshing.");
pm.parseReview("203,3,Good, but a little too sweet for me.");

pm.parseReview("204,5,Cooling buttermilk, absolutely delicious!");
pm.parseReview("204,5,Perfectly spiced with curry leaves and green chili.");
pm.parseReview("204,4,Good buttermilk, refreshing and light.");

pm.parseReview("205,5,Rose milk with the perfect balance of sweetness and flavor!");
pm.parseReview("205,4,Chilled and refreshing, loved it.");
pm.parseReview("205,4,Good drink, but the rose flavor was slightly strong.");


        // Print product reports for all products
        pm.printProductReport(101);
        pm.printProductReport(102);
        pm.printProductReport(103);
        pm.printProductReport(104);
        pm.printProductReport(105);
           pm.printProductReport(201);
        pm.printProductReport(202);
        pm.printProductReport(203);
        pm.printProductReport(204);
        pm.printProductReport(205);

        // Optionally, print all products sorted by rating (descending)
        System.out.println("\nAll Products Sorted by Rating:");
        pm.printProducts(
            (product) -> true,  // No filter, just sort all products
            (p1, p2) -> p2.getRating().ordinal() - p1.getRating().ordinal()  // Sorting by rating
        );
    }
}
