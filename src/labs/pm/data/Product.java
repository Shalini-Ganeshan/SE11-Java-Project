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

import java.math.BigDecimal;
import static java.math.RoundingMode.HALF_UP;
import java.time.LocalDate;
import java.util.Objects;
import static labs.pm.data.Rating.*;//static import 

/**
 * Represents a product in the system. Each product has an ID, name, price, and
 * a discount calculated based on a fixed discount rate.
 *
 * The discount is calculated using a static constant discount rate
 * ({@link DISCOUNT_RATE}).
 *
 * @author ADMIN
 */
public abstract class Product implements Rateable<Product> {

    private final int id;               // Product ID
    private final String name;          // Product name
    private final BigDecimal price;     // Product price
    private final Rating rating;
    private LocalDate bestBefore;
    // The constant discount rate for the product (10%)
    public static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.1);

    @Override
    public Rating getRating() {
        return rating;
    }

//     Product() {
//        this(0, "no name", BigDecimal.ZERO);//to prevent exception when calc dicount when price is null 
//    }
    Product(int id, String name, BigDecimal price) {
        this(id, name, price, NOT_RATED);//from import

    }

//    public abstract Product applyRating(Rating newRating); ->now it comes from interface. there is no need to implement a abstract method from interface if the class is abstact ,only concrete classes need to provide implementation.
//        return new Product(id, name, price, newRating);// must not use this here can also use this.id...
//constructor is invoked only once for an object.
    Product(int id, String name, BigDecimal price, Rating rating) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.rating = rating;
    }

    /**
     * Gets the unique identifier of the product.
     *
     * @return the product ID
     */
    public int getId() {
        return id;
    }

    //remove all setter methods & mark inst variables as final to make the product object immutable 
    /**
     * Gets the name of the product.
     *
     * @return the product name
     */
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.id;
        return hash;
    }

    public LocalDate getBestBefore() {
        return bestBefore;
    }

    /**
     * Gets the price of the product.
     *
     * @return the product price
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        //true even if one is food and the other is drink
//        if(obj instanceof Product)->no need to check null since if obj is null, it return false.
//false even if one is food and the other is drink
        if (obj != null && getClass() == obj.getClass()) {

            Product other = (Product) obj;
            return this.id == other.id;
        }
        return false;
    }

    /**
     * Calculates the discount based on the product's price. The discount is
     * calculated using the static {@link DISCOUNT_RATE}.
     *
     * @return the discount value as a {@link BigDecimal}
     * @throws NullPointerException if the price is not initialized
     */
    public BigDecimal getDiscount() {
        if (price == null) {
            throw new NullPointerException("Product price is not initialized");
        }
        return price.multiply(DISCOUNT_RATE).setScale(2, HALF_UP);
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    @Override
    public String toString() {//only for technical logging not for end-user consumption
        return "ProductManagement{id=" + id + ", name='" + name + "', price=" + price + "}";
    }

}
