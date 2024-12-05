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
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author ADMIN
 */
public class Food extends Product {

     Food(int id, String name, BigDecimal price, Rating rating) {
        super(id, name, price, rating);
    }

    @Override
    public Product applyRating(Rating newRating) {
       return new Food(getId(),getName(),getPrice(),newRating);
    }

    @Override
    public String toString() {
        return super.toString()+"Food{" + '}';
    }

    @Override
    public BigDecimal getDiscount() {
         LocalTime now=LocalTime.now();
   LocalTime startTime = LocalTime.of(17, 30);
    LocalTime endTime = LocalTime.of(18, 30);   
    return (now.isAfter(startTime) && now.isBefore(endTime)) ? super.getDiscount() : BigDecimal.ZERO;
    }
  
    
    
}
