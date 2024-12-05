/*
 * Copyright (C) 2024 ADMIN
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package labs.pm.data;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author ADMIN
 */
public class ProductManager {

    private Map<Product, List<Review>> products = new HashMap<>();
    private ResourceFormatter formatter;
    private ResourceBundle config;
    private static final Logger logger = Logger.getLogger(ProductManager.class.getName());
    private Path reportsFolder;
    private Path dataFolder ;
    private Path tempFolder ;

    // Moved the initialization of productFormat and reviewFormat here to ensure config is set before using it
    private MessageFormat foodFormat;
    private MessageFormat drinkFormat;
    private MessageFormat reviewFormat;
    private static Map<String, ResourceFormatter> formatters;

    static {
        // Initialize formatters map
        formatters = new HashMap<>();
        formatters.put("en-GB", new ResourceFormatter(Locale.UK));
        formatters.put("en-US", new ResourceFormatter(Locale.US));
        formatters.put("fr-FR", new ResourceFormatter(Locale.FRANCE));
        formatters.put("ru-RU", new ResourceFormatter(new Locale("ru", "RU")));
        formatters.put("zh-CN", new ResourceFormatter(Locale.SIMPLIFIED_CHINESE));
    }

    private Review[] reviews = new Review[5];

    public ProductManager(Locale locale) {
        this(locale.toLanguageTag());
    }

    public ProductManager(String langTag) {
        changeLocale(langTag);
        initializePaths();
    }

    private void changeLocale(String langTag) {
         this.config = ResourceBundle.getBundle("labs.pm.data.config", Locale.forLanguageTag(langTag));

    // Ensure config is loaded properly
    if (config == null) {
        logger.severe("Configuration file not found or couldn't be loaded.");
        throw new IllegalStateException("Configuration file is missing or invalid.");
    }
        this.formatter = formatters.getOrDefault(langTag, formatters.get("en-GB"));
        this.config = ResourceBundle.getBundle("labs.pm.data.config", Locale.forLanguageTag(langTag));
        this.foodFormat = new MessageFormat(config.getString("food.data.format"));
        this.drinkFormat = new MessageFormat(config.getString("drink.data.format"));
        this.reviewFormat = new MessageFormat(config.getString("review.data.format"));
    }

    private void initializePaths() {
        this.reportsFolder = Path.of(config.getString("reports.folder"));
        this.dataFolder = Path.of(config.getString("data.folder"));
        this.tempFolder = Path.of(config.getString("temp.folder"));
    }

    public Review parseReview(String text) {
        Review review=null;
        try {
            Object[] values = reviewFormat.parse(text);
            review=new Review( Rateable.convert(Integer.parseInt((String) values[0])),(String) values[1]);
        } catch (ParseException | NumberFormatException ex) {
            logger.log(Level.WARNING, "Error parsing review: " + text, ex);//ex ->is the trace of error
        }
        return review;
    }

    public static Set<String> getSupportedLocales() {
        return formatters.keySet();
    }

    private Product product;
    private Review review;

    public Product createProduct(int id, String name, BigDecimal valueOf, Rating rating, LocalDate of) {
        Product product = new Drink(id, name, valueOf, rating, of);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    //in order to know where it has gone wrong while trying to print an non available id product.
    public Product findProduct(int id) throws ProductManagerException {
        return products.keySet()
                .stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ProductManagerException("Product with id " + id + " not found"));
    }

    public Product reviewProduct(int id, Rating rating, String comments) {
        try {
            return reviewProduct(findProduct(id), rating, comments);
        } catch (ProductManagerException ex) {
            logger.log(Level.INFO, ex.getMessage(), ex);
        }
        return null;
    }

    public Product reviewProduct(Product product, Rating rating, String comments) {
        List<Review> reviews = products.get(product);
        reviews.add(new Review(rating, comments));

        // Recalculate the product's rating based on reviews
        product = product.applyRating(Rateable.convert(
                (int) Math.round(reviews.stream().mapToInt(r -> r.getRating().ordinal()).average().orElse(0))
        ));

        products.put(product, reviews);
        return product;
    }

    public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
        Product product = new Food(id, name, price, rating);
        products.putIfAbsent(product, new ArrayList<>());
        return product;
    }

    public void printProductReport(int id) {
        try {
            printProductReport(findProduct(id));
        } catch (ProductManagerException ex) {
            logger.log(Level.INFO, ex.getMessage(), ex);
        } catch (IOException ex) {
                 logger.log(Level.SEVERE, "Error printing product report:"+ex.getMessage(), ex);
        }
    }
// the error is printed correctly with the message and it doesnt affect the other products printing.

    public void printProductReport(Product product) throws IOException {
//        StringBuilder txt = new StringBuilder();

        List<Review> reviews = products.get(product);
        Path productFile = reportsFolder.resolve(MessageFormat.format(config.getString("report.file"), product.getId()));
        // Format the product details using MessageFormat
//        txt.append(formatter.formatProduct(product)).append('\n');
//
//        if (reviews.isEmpty()) {
//            txt.append(formatter.getText("no.reviews")).append('\n');
//        } else {
//            txt.append(reviews.stream().map(r -> formatter.formatReview(r) + '\n')
//                    .collect(Collectors.joining()));
//        }
//
//        System.out.println(txt);
          try (PrintWriter out = new PrintWriter(new OutputStreamWriter(Files.newOutputStream(productFile, StandardOpenOption.CREATE), "UTF-8"))) {
        out.append(formatter.formatProduct(product)).append(System.lineSeparator());

        if (reviews.isEmpty()) {
            out.append(formatter.getText("no.reviews")).append(System.lineSeparator());
        } else {
            out.append(reviews.stream().map(r -> formatter.formatReview(r) + System.lineSeparator())
                    .collect(Collectors.joining()));
        }
    }
}
    public void printProducts(Predicate<Product> filter, Comparator<Product> sorter) {
        StringBuilder txt = new StringBuilder();
        products.keySet().stream().sorted(sorter).filter(filter)
                .forEach(p -> txt.append(formatter.formatProduct(p) + '\n'));
        System.out.println(txt);
    }

    public void parseProduct(String text) {
        try {
            // Identify product type and parse accordingly
            Object[] values = null;
            if (text.startsWith("D")) { // Assuming 'D' is for Drink
                values = drinkFormat.parse(text);
                int id = Integer.parseInt((String) values[1]);
                String name = (String) values[2];
                BigDecimal price = BigDecimal.valueOf(Double.parseDouble((String) values[3]));
                Rating rating = Rateable.convert(Integer.parseInt((String) values[4]));
                LocalDate bestBefore = LocalDate.parse((String) values[5]);
                createProduct(id, name, price, rating, bestBefore); // Creating a Drink product
            } else if (text.startsWith("F")) { // Assuming 'F' is for Food
                values = foodFormat.parse(text);
                int id = Integer.parseInt((String) values[1]);
                String name = (String) values[2];
                BigDecimal price = BigDecimal.valueOf(Double.parseDouble((String) values[3]));
                Rating rating = Rateable.convert(Integer.parseInt((String) values[4]));
                createProduct(id, name, price, rating); // Creating a Food product
            }
        } catch (ParseException | NumberFormatException | DateTimeParseException ex) {
            logger.log(Level.WARNING, "Error parsing product: " + text, ex);
        }
    }

    public Map<String, String> getDiscounts() {
        return products.keySet()
                .stream()
                .collect(Collectors.groupingBy(
                        product -> product.getRating().getStars(),
                        Collectors.collectingAndThen(
                                Collectors.summingDouble(product -> product.getDiscount().doubleValue()),
                                discount -> formatter.moneyFormat.format(discount)
                        )
                ));
    }

    private static class ResourceFormatter {

        private Locale locale;
        private ResourceBundle resources;
        private DateTimeFormatter dateFormat;
        private NumberFormat moneyFormat;

        private ResourceFormatter(Locale locale) {
            this.locale = locale;
            try {
                resources = ResourceBundle.getBundle("labs.pm.data.resources", locale);
            } catch (java.util.MissingResourceException e) {
                System.err.println("Missing resource bundle for locale: " + locale);
                resources = ResourceBundle.getBundle("labs.pm.data.resources"); // fallback to default
            }
            dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
            moneyFormat = NumberFormat.getCurrencyInstance(locale);
        }

        private String formatProduct(Product product) {
            // Use a default message or format when bestBefore is null
            String bestBeforeText = (product.getBestBefore() != null)
                    ? dateFormat.format(product.getBestBefore())
                    : "No expiry date";

            return MessageFormat.format(resources.getString("product"),
                    product.getName(),
                    moneyFormat.format(product.getPrice()),
                    product.getRating().getStars(),
                    bestBeforeText);
        }

        private String formatReview(Review review) {
            return MessageFormat.format(resources.getString("review"), review.getRating().getStars(), review.getComment());
        }

        private String getText(String key) {
            return resources.getString(key);
        }
    }
}
